/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udistrital.VirtualLabs.jschematic.comm;

/**
 *
 * @author Oscar E. Cala W.
 */
public abstract class AbstractInterfaceAction extends DorminMessage {

    protected String selection;
    protected  String action;
    protected  String input;

    @Override
    protected void extractDorminProperties() {
        setSelection(properties.getChild("Selection").getChild("value").getTextNormalize());
        setAction(properties.getChild("Action").getChild("value").getTextNormalize());
        setInput(properties.getChild("Input").getChild("value").getTextNormalize());
    }

    /**
     * @return the selection
     */
    public String getSelection() {
        return selection;
    }

    /**
     * @param selection the selection to set
     */
    public void setSelection(String selection) {
        this.selection = selection;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the input
     */
    public String getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    public void setInput(String input) {
        this.input = input;
    }

}
