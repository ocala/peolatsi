/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udistrital.VirtualLabs.jschematic.messages;

import co.edu.udistrital.VirtualLabs.jschematic.VLSchematicBoard;
import co.edu.udistrital.VirtualLabs.jschematic.comm.AbstractInCorrectAction;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public class InCorrectAction extends AbstractInCorrectAction{

    public static final String[] notSuccessMessages = {"La acción realizada es incorrecta.",
                                                    "La ultima acción que ejecuto es incorrecta.",
                                                    "La acción realizada es incorrecta. Revise el procedimiento y vuelv a intentarlo."};
    protected static int lastMessage = 0;
   
    @Override
    public void dispatch() {
        VLSchematicBoard.getInstance().setIncorrectBoardMessage(getRandomSuccessMessage());
        VLSchematicBoard.getInstance().setSuccessMessage(null);
    }

    @Override
    protected void extractToolProperties() {
                
    }
    
     private String getRandomSuccessMessage() {
        int num = 0;
        while(num == lastMessage){
            num = Math.abs((int)((Math.random()-(1e-10)) * 3));
        }
        lastMessage = num;
        return notSuccessMessages[num];
    }

}
