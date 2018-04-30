package co.edu.udistrital.VirtualLabs.jschematic.comm;

/**
 *
 * @author Oscar Eduardo Cala W
 */
abstract public class AbstractSuccessMessage extends DorminMessage {

    private String tMsg = "";

    @Override
    protected void extractDorminProperties() {
        settMsg(properties.getChild("SuccessMsg").getTextNormalize());
    }

    /**
     * @return the tMsg
     */
    public String gettMsg() {
        return tMsg;
    }

    /**
     * @param tMsg the tMsg to set
     */
    public void settMsg(String tMsg) {
        this.tMsg = tMsg;
    }
}