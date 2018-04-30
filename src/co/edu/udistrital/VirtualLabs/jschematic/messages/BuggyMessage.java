/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udistrital.VirtualLabs.jschematic.messages;

import co.edu.udistrital.VirtualLabs.jschematic.VLSchematicBoard;
import co.edu.udistrital.VirtualLabs.jschematic.comm.AbstractBuggyMessage;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public class BuggyMessage extends AbstractBuggyMessage {

    @Override
    public void dispatch() {
       VLSchematicBoard.getInstance().setBuggyMessage(gettMsg());
    }

    @Override
    protected void extractToolProperties() {
        
    }
}
