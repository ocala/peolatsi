/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udistrital.VirtualLabs.jschematic.messages;

import co.edu.udistrital.VirtualLabs.jschematic.comm.AbstractStartStateEnd;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public class StartStateEnd extends AbstractStartStateEnd {

    @Override
    public void dispatch() {
        StartProblem.setStartStateEndMessageReceived();
        StartStateCreated.setReceived(true);
    }

    @Override
    protected void extractToolProperties() {
    }
}
