package wise.app;


import co.edu.udistrital.VirtualLabs.jschematic.*;
import edu.cmu.pact.BehaviorRecorder.Controller.CTAT_Launcher;
import java.awt.EventQueue;

/**
 * Esta clase es la clase principal de ejecución y esta encargada de crear los
 * objetos que representan la aplicación WISE y CTAT e iniciar su ejecución
 * @author Familia
 */
public class TutorLabAuthoring {

    /**
     * Esta funcion lanza la aplicación WISE y la aplicación CTAT.
     * Esta funcion se encarga de lanzar una instancia de CTAT configurada
     * propiamente para que se ejecute sobre el puerto requerido, comunicandose
     * en el formato requerido, y con la configuraciòn sincrona requerida.
     *
     * Existe el formato 0, 1, y 2 , el 0 es dormin, el 1, ex xml dormin y el 2
     * es el formato OLI DataSHop que es el mas avanzado y tiene especificacion
     * con esquema xml.
     *
     * @todo Debe compararse la configuracion con el tutoring service.
     * @param args
     */
    public static void main(String args[]) {
        
        EventQueue.invokeLater(
            new Runnable() {
                public void run() {

                    String args[] = {/*"-traceLevel", "100", /*"-debugCodes", "  ",*/ "-spClientHost",
                        "localhost", "-spServerPort", "1502", "-spMsgFormat", "1", "-spEOM", "0",
                        "-spUseSingleSocket", "true"};
                    CTAT_Launcher.main(args);
                }
            });

            EventQueue.invokeLater(
            new Runnable() {
                public void run() {
                    VLJschematic me = new VLJschematic();
                    VLSchematicBoard.sendMessage("", "", "", "");
                }
            });
    }
}