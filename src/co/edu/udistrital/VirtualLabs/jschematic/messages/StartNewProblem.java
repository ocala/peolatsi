/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udistrital.VirtualLabs.jschematic.messages;

import co.edu.udistrital.VirtualLabs.jschematic.VLSchematicBoard;
import co.edu.udistrital.VirtualLabs.jschematic.comm.AbstractStartNewProblem;

/**
 *
 * @author Oscar E. Cala W.
 */
public class StartNewProblem extends AbstractStartNewProblem {


    @Override
    public void dispatch() {
        VLSchematicBoard.getInstance().clearBoard();
        StartStateCreated.setReceived(false);
    }


    @Override
    protected void extractToolProperties() {
        
    }

    
}
