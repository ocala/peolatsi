/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udistrital.VirtualLabs.jschematic.comm;


import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * Esta clase crea instancias de objectos que heredan la clase DorminMessage.
 * Esta clase es utilizada cuando se reciben mensajes del behaviour recorder
 * para crear una representacion en memoria del objeto recibido. Las instancias
 * creadas desde esta clase tienen el conocimiento de como actualziar la interfaz
 * grafica de usuario cuando un mensaje es recibido. La clase funciona por refleccion
 * de manera que las clases usadas para crear las instancias son identificadas a partir del
 * del tipo de mensaje recibido. Las clases deben llevar el mismo nombre que el tipo de mensaje
 * y deben estar en el paquete especificado por el atributo packagedotpath perteneciente
 * a esta clase.
 *
 * @author Administrador
 */
public class DorminMessageFactory {

    private static DorminMessageFactory instance = new DorminMessageFactory();
    private String packageDotPath;
    private DorminMessage ream;

    private DorminMessageFactory(){
        
    }

    public static DorminMessageFactory getInstance(String packageDotPath){
        instance.setPackageDotPath(packageDotPath);
        return instance;
    }

    public ReadableDorminMessage getMessageFromXML(String rawXML){
        String msg = rawXML;
        DorminMessage msgD = null;
        String className = "";
        try {
            SAXBuilder builder = new SAXBuilder(false);
            Document doc = builder.build(new StringReader(msg));
            Element root = doc.getRootElement();
            List properties = root.getChildren("properties");
            Element propElement = (Element) properties.iterator().next();
            Element msgType = (Element) propElement.getChildren("MessageType").iterator().next();
            String msgTypeS = msgType.getTextNormalize();
            if(msgTypeS.equals("") || msgTypeS == null){
                return null;
            }
            System.out.println("Recibido msj del tipo:" +msgTypeS);
            if(!this.packageDotPath.endsWith(".")){
                packageDotPath += ".";
            }
            className = this.packageDotPath + msgTypeS;
            Class clazz = Class.forName(className);
            msgD = (DorminMessage) clazz.newInstance();
            msgD.init(doc);
        }catch (JDOMException ex) {
            Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch(InstantiationException ie){
            System.out.println(ie.getMessage());
        } catch(ClassNotFoundException cnfe){
            System.out.println("Clase No soportada Aun: " + className);            
        }catch(NoClassDefFoundError ce){
            System.out.println("Clase No encontrada: " + className);
        }
        catch(IllegalAccessException iae){
            System.out.println(iae.getMessage());
        }
        return msgD;
    }

    private void setPackageDotPath(String packageDotPath) {
        this.packageDotPath = packageDotPath;
    }

    public String getPackageDotPath(){
        return packageDotPath;
    }
}
