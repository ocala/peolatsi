/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udistrital.VirtualLabs.jschematic.comm;

/**
 *
 * @author Oscar E. Cala W.
 */
public abstract class AbstractStartStateCreated extends DorminMessage {

    private String startStateName;

    protected void extractDorminProperties(){
        setStartStateName(properties.getChild("StartStateName").getTextNormalize());
    }

    /**
     * @return the startStateName
     */
    public String getStartStateName() {
        return startStateName;
    }

    /**
     * @param startStateName the startStateName to set
     */
    public void setStartStateName(String startStateName) {
        this.startStateName = startStateName;
    }
    
}
