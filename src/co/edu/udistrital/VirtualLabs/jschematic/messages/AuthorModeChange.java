/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udistrital.VirtualLabs.jschematic.messages;

import co.edu.udistrital.VirtualLabs.jschematic.VLJschematic;
import co.edu.udistrital.VirtualLabs.jschematic.VLSchematicBoard;
import co.edu.udistrital.VirtualLabs.jschematic.comm.AbstractAuthorModeChange;
import co.edu.udistrital.VirtualLabs.jschematic.comm.DorminSocketInterface;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public class AuthorModeChange extends AbstractAuthorModeChange {

    public static final String TEST_TUTOR_MODE = "Test Tutor";
    public static final String DEMONSTRATE_MODE = "Demonstrate";

    @Override
    public void dispatch() {
        if (newMode.equalsIgnoreCase(TEST_TUTOR_MODE)) {
            setTestTutorMode();
        } else if (newMode.equalsIgnoreCase(DEMONSTRATE_MODE)) {
            DorminSocketInterface.instace.setDispatchNextCorrectAction(false);
            VLSchematicBoard.getInstance().setLockingWidgets(false);
        }
    }

    @Override
    protected void extractToolProperties() {
    }

    public static void setTestTutorMode() {
        DorminSocketInterface.instace.setDispatchNextCorrectAction(true);
        VLSchematicBoard.getInstance().setLockingWidgets(true);
    }
}
