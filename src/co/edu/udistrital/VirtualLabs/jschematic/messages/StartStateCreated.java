/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udistrital.VirtualLabs.jschematic.messages;

import co.edu.udistrital.VirtualLabs.jschematic.VLSchematicBoard;
import co.edu.udistrital.VirtualLabs.jschematic.comm.AbstractStartStateCreated;

/**
 *
 * @author Oscar E. Cala W.
 */
public class StartStateCreated extends AbstractStartStateCreated {

    private static boolean received = false;

    /**
     * @return the received
     */
    public static boolean isReceived() {
        return received;
    }

    /**
     * @param aReceived the received to set
     */
    public static void setReceived(boolean aReceived) {
        received = aReceived;
    }
    
    @Override
    public void dispatch() {
        //@todo This message should lock all the Widgets with a start value.
        //The idea is to keep lock the initial values in the widget across the
        //problem resolution
        received = true;
    }

    @Override
    protected void extractToolProperties() {
        
    }
    

}
