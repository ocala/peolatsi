/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udistrital.VirtualLabs.jschematic.messages;

import co.edu.udistrital.VirtualLabs.jschematic.VLSchematicBoard;
import co.edu.udistrital.VirtualLabs.jschematic.comm.AbstractSuccessMessage;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public class SuccessMessage extends AbstractSuccessMessage {

    @Override
    public void dispatch() {
        VLSchematicBoard.getInstance().setSuccessMessage(gettMsg());
    }

    @Override
    protected void extractToolProperties() {
        
    }

}
