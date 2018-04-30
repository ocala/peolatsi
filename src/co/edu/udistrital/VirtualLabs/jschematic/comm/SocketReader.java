/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udistrital.VirtualLabs.jschematic.comm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * Clase que tiene la responsabilidad de leer los mensajes recibidos desde el tutor
 * y crear las representaciones necesarias de cada mensaje para ejecutar de manera concisa
 * la logica de negocio asociada a cada mensaje.
 *
 * Representa el Cliente en un patron Strategy. Utiliza la fabrica DorminMessageFactory para
 * crear objetos que encapsulan un algoritmo bajo la misma interfaz.
 *
 * @author Oscar E. Cala W.
 */
public class SocketReader extends Thread {

    private Socket inSock = null;
    private BufferedReader inStream;
    private JFrame logFrame;
    private JTextArea logArea;
    private String packageForMessagesImpl;
    private ReadableDorminMessage dm;
    private DorminMessageFactory fac;

    /**
     * Construct a socketReader thread for the given socket number.
     */
    private SocketReader() {
        super();
    }

    private void init() {
        logFrame = new JFrame("Mensajes Recibidos.");
        logArea = new JTextArea();
        logFrame.getContentPane().add(new JScrollPane(logArea));
        logFrame.setSize(200, 200);
    }

    public SocketReader(Socket inSock,String packageForMsgImpl) {
        this.init();
        this.inSock = inSock;
        packageForMessagesImpl = packageForMsgImpl;
    }

    @Override
    public void run() {
        BufferedReader rdr;
        int eom = 0;
        boolean run = true;
        try {
            System.out.println("Launching the listener in bidirectional socket");
            InputStreamReader isr = new InputStreamReader(inSock.getInputStream());
            inStream = new BufferedReader(isr);
            logFrame.setVisible(true);
            while (run) {
                System.out.println("sp .. reading..");
                String msg = "";
                if (eom >= 0) {
                    //System.out.println("readEOM called");

                    //msg = SocketProxy.readToEom(inStream, eom);
                    rdr = inStream;
                    StringWriter result = new StringWriter(4096);

                    int c;
                    c = rdr.read();
                    while ((0 <= c) && (c != eom)) {
                        //++count;
                        /*if (c != 0) {
                        //System.out.print((char) c);
                        } else {
                        //System.out.println("null");
                        }
                        if (c == 13) {
                        //System.out.println("CR return is found at offset " + count);
                        }*/
                        result.write(c);
                        c = rdr.read();
                    }
                    msg = result.toString();
                    logArea.append("\n" + msg);
                    if (msg.length()> 1) {
                        parseMessage(msg);
                    }
                }
                //System.out.println("****** " + msg + " *******");
                //msg = inStream.readLine();
                //System.out.println("\nSocketProxyTest.listener received:\n" + msg);
                if (msg.length() < 1) {
                    run = false;
                }
            }
            System.out.println("the bidirectional lister is closed.");
            inStream.close();
        } catch (Exception ex) {
            Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocurrio una excepcion y se cerro el listener reader.");
        }
    }

    private void parseMessage(String msg) {
        try {
            fac = DorminMessageFactory.getInstance(getPackageForMessagesImpl()); 
            dm = fac.getMessageFromXML(msg);
            System.out.println("mensaje obtenido...");
            if (dm != null) {
                dm.dispatch();
            } else {
                System.out.println("No se ha ejecutado oepraciones al recibir el mensaje");
            }
        } catch (Exception ex) {
            Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the packageForMessagesImpl
     */
    public String getPackageForMessagesImpl() {
        return packageForMessagesImpl;
    }

    /**
     * @param packageForMessagesImpl the packageForMessagesImpl to set
     */
    public void setPackageForMessagesImpl(String packageForMessagesImpl) {
        this.packageForMessagesImpl = packageForMessagesImpl;
    }
}
