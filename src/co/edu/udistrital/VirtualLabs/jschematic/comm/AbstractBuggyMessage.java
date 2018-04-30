package co.edu.udistrital.VirtualLabs.jschematic.comm;

/**
 *
 * @author Oscar E. Cala W.
 */
public abstract class AbstractBuggyMessage extends DorminMessage {
    private String tMsg = "";
    private String selection = "";

    @Override
    protected void extractDorminProperties() {
        settMsg(properties.getChild("BuggyMsg").getTextNormalize());
        //setSelection(properties.getChild("Selection").getChild("value").getTextNormalize());
    }

    /**
     * @return the mText
     */
    public String gettMsg() {
        return tMsg;
    }

    /**
     * @param mText the mText to set
     */
    public void settMsg(String mText) {
        this.tMsg = mText;
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
}
