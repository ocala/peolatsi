/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udistrital.VirtualLabs.jschematic.comm;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public abstract class AbstractAuthorModeChange extends DorminMessage{

    protected String oldMode;
    protected String newMode;


    @Override
    protected void extractDorminProperties() {
        oldMode = properties.getChild("oldMode").getTextNormalize();
        newMode = properties.getChild("newMode").getTextNormalize();
    }
}
