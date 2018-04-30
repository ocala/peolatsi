/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udistrital.VirtualLabs.jschematic.messages;

import co.edu.udistrital.VirtualLabs.jschematic.VLSchematicBoard;
import co.edu.udistrital.VirtualLabs.jschematic.comm.AbstractShowHintsMessage;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public class ShowHintsMessage extends AbstractShowHintsMessage {

    @Override
    public void dispatch() {
        VLSchematicBoard.getInstance().setHintsList(hints);
        VLSchematicBoard.getInstance().nextHint();
        VLSchematicBoard.getInstance().repaint();
    }



    @Override
    protected void extractToolProperties() {
    }

}
