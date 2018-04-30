/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udistrital.VirtualLabs.jschematic.comm;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Esta clase permite el envio de mensajes desde la interfaz hasta el Behaviour recorder
 * Tiene la responsabilidad de crear el socket bidireccional que se usara para
 * realizar las operaciones de lectura y escritura. Es decir para enviar y recibir datos.
 *
 * La clase demuestra un patron Singleton mediante el cual permite ofrecer un metodo
 * que puede ser usado por clases externas para realizar envio de mensajes de frorma consisa y facil.
 *
 *
 * Esta clase mantiene una instancia del Lector de Sockets, el cual se encarga de recibir
 * los mensajes enviados por el Behaviour Recorder y hacer los llamados a las clases que
 * permiten tomar acciones sobre la interfaz grafica de usuario en respuesta a los
 * mensajes recibidos desde el sistema tutor.
 *
 * @author Oscar E. Cala
 */
public class DorminSocketInterface {

    public static DorminSocketInterface instace = new DorminSocketInterface();


    /**
     * Permite decidir si las llamadas a sendMessage informaran al tutor
     * esta variable es usada para evitar ciclos de notificacion desde la herramienta
     * al tutor.
     *
     * TODO: Implementar la funcionalidad en el metodo sendMessage
     */
    public static boolean enableSend = true;

    /**
     *  Representa el socket de salida para el envio de información al BR
     */
    private Socket outSock;
    /**
     * Este atributo permite envolver el Stream de salida que se usa para
     * conectarse con el BR
     */
    private PrintWriter outStream;

    /*
     * Este atributo representa la conexión de entrada para leer los datos
     * que son enviados por el BR.
     */
    private Socket inSock;
    /**
     * Este atributo permite hacer uso de la clase SocketReader para permitir
     * lecturas de información que provienen desdeel BR
     * @see SocketReader
     */
    private SocketReader listener;
    /**
     * Contiene el nombre del host donde se ejecuta el servicio de tutoring
     * en el host referenciado debe existir una instancia de CTAT configurada
     * propiamente para leer y escribir mensajes en el puerto indicado
     *
     *
     * @see port
     */
    private String host = "localhost";
    /**
     * Numero de puerto por el cual la instancia de CTAT en el anfitrion
     * referenciado por host, esta escuchando.
     *
     * @see host
     */
    private int port = 1502;


    /**
     * Paquete en el cual se almacenan las clases que atenderan la logica
     * de negocio requerida cuando se reciba un mensajes.
     */
    private String packageForMsgImpl;

    /**
     * @todo. Este constructor privado permite la creacion de un socket que referencie
     * un host y un puerto diferentes de localhost
     *
     */
    private static int transactionID = 0;

    public static HashMap<SelectionActionInputKey,HashMap<String,String>> holdObjects = new HashMap<SelectionActionInputKey, HashMap<String,String>>();

    private boolean dispatchNextCorrectAction = false;


    private DorminSocketInterface() {
        try {
            //getOutputStream(host, port);
        } catch (Exception ex) {
            Logger.getLogger(DorminSocketInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * Este metodo permite especificar de manera estatica el host y el puerto
     * donde se encuentra la instancia de CTAT a la cual se le informaran
     * las acciones de interfaz.
     *
     * @param host
     * @param port
     */
    public static void setCTATInstanceHost(String host, int port) {
        instace.setHost(host);
        instace.setPort(port);
    }

    /**
     * Metodo utilitario que permite la creacion de un mensaje desde una clase
     * externa y el envio a un host y a un puerto definido previamente en
     * la instancia
     * @param msgType
     * @param selection
     * @param action
     * @param input
     */
    public static void sendMessage(String msgType, String selection, String action, String input) {
        String str = buildHeader(msgType,selection,action,input);
        str +=  buildFooter(msgType,selection,action,input);
        instace.sendString(str);
    }

    protected static String buildFooter(String msgType, String selection, String action, String input){
        transactionID++;
        String str = ""
              //  + "<transaction_id>"+transactionID+"</transaction_id>"
                + "</properties>"
                + "</message>";
        return str;
    }
    
    protected static String buildHeader(String msgType, String selection, String action, String input){
        String str = "<message><verb>NotePropertySet</verb>"
                + "<properties>"
                + "<MessageType>" + msgType + "</MessageType>"
                + "<Selection><value>" + selection + "</value></Selection>"
                + "<Action><value>" + action + "</value></Action>"
                + "<Input><value>" + input + "</value></Input>";
        return str;
    }

    public static void sendMessage(String msgType, String selection, String action, String input, HashMap<String, String> externalProps) {
        String str = buildHeader(msgType,selection,action,input);
        str +=  buildCustomProps(msgType, selection, action, input,externalProps);
        str +=  buildFooter(msgType,selection,action,input);
        instace.sendString(str);
        if(enableSend){
           holdObjects.put(new SelectionActionInputKey(selection,action,input),externalProps);
        }
    }

    public static String buildCustomProps(String msgType, String selection, String action, String input, HashMap<String, String> externalProps){
        //String str = "<myCustomProp><value>customValue</value></myCustomProp>";
        String str = "";
        Set<Entry<String,String>> set = externalProps.entrySet();
        Formatter f = new Formatter();
       for(Entry<String,String> ent : set ){
           str =  f.format("<%1$s><value>%2$s</value></%1$s>", ent.getKey(),ent.getValue()).out().toString();           
       }
       return str;
    }

    /**
     * Este metodo envia una cadena por el socket para que sea recibida por
     * el BR.
     * @param str
     */
    public void sendString(String str) {
        if(!enableSend){
            System.out.println("String NOT sent Enable Notificacions First -->" + str);
            return ;
        }
        try {
            System.out.println("String To send-->" + str);
            PrintWriter out = getOutputStream(host, port);
            if (out == null) {
                return;
            }
            out.println(str);
            out.write(0);
            out.flush();
            AbstractCorrectAction.setTrackNextFeedback(dispatchNextCorrectAction);            
            resetOutputStream(false);
        } catch (Exception e) {
            Logger.getLogger(DorminSocketInterface.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     *
     * Este metodo permite re establecer el strem de salida cuando sea necesario
     *
     * @param closeUnconditionally
     * @throws IOException
     */
    private void resetOutputStream(boolean closeUnconditionally) throws IOException {
        if (!closeUnconditionally) {
            return;
        }
    }

    /**
     *
     * Logica que maneja un patron singleton para la creaciòn del socket
     * una ves el socket se ha creado, nunca mas vuelve a crearse
     * por lo cual no puede cambiarse el host ni el puerto. 
     *
     * @param host
     * @param clientPort
     * @return
     * @throws Exception
     */
    private PrintWriter getOutputStream(String host, int clientPort) throws Exception {
        if (outSock == null) {
            InetAddress addr = InetAddress.getLocalHost();
            if (host != null && host.length() > 0) {
                addr = InetAddress.getByName(host);
            }
            //TODO: This creation should be fixed when  a host o port change occurs.
            outSock = new Socket(addr, clientPort);
            outStream = null;
            if (true) {
                inSock = outSock;
                listener = new SocketReader(inSock, getPackageForMsgImpl());
                listener.start();
            }
            if (outStream == null) {
                outStream = new PrintWriter(outSock.getOutputStream(), false);
            }
        }
        return outStream;
    }

    /**
     *
     * Permite obtener el host al cual el socket referencia.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     * @todo Since the socket is a inderect singleton, we need to destroy de socket
     * and call the getOutputStream method again.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Permite obtener el puerto al que el socket referencia.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     * @todo Since the socket is a inderect singleton, we need to destroy de socket
     * and call the getOutputStream method again.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the packageForMsgImpl
     */
    public String getPackageForMsgImpl() {
        return packageForMsgImpl;
    }

    /**
     * @param packageForMsgImpl the packageForMsgImpl to set
     */
    public void setPackageForMsgImpl(String packageForMsgImpl) {
        this.packageForMsgImpl = packageForMsgImpl;
    }

    /**
     * @return the dispatchNextCorrectAction
     */
    public boolean isDispatchNextCorrectAction() {
        return dispatchNextCorrectAction;
    }

    /**
     * @param dispatchNextCorrectAction the dispatchNextCorrectAction to set
     */
    public void setDispatchNextCorrectAction(boolean dispatchNextCorrectAction) {
        this.dispatchNextCorrectAction = dispatchNextCorrectAction;
    }
}
