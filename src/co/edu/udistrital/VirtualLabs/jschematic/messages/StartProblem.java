package co.edu.udistrital.VirtualLabs.jschematic.messages;

import co.edu.udistrital.VirtualLabs.jschematic.VLSchematicBoard;
import co.edu.udistrital.VirtualLabs.jschematic.comm.*;

/**
 *
 * @author Oscar E. Cala W.
 */
public class StartProblem extends AbstractStartProblem{

    public static boolean waitingForStartStateEndMessage = false;

    @Override
    public void dispatch() {
        if (!waitingForStartStateEndMessage) {
            VLSchematicBoard.getInstance().clearBoard();
            waitingForStartStateEndMessage = true;
        }
    }
    
    @Override
    protected void extractToolProperties() {
        
    }

    public static void setStartStateEndMessageReceived(){
        waitingForStartStateEndMessage = false;
    }

}
