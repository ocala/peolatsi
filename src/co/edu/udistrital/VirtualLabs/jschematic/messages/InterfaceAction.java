package co.edu.udistrital.VirtualLabs.jschematic.messages;

import co.edu.udistrital.VirtualLabs.jschematic.VLSchematicBoard;
import co.edu.udistrital.VirtualLabs.jschematic.comm.AbstractInterfaceAction;
import co.edu.udistrital.VirtualLabs.jschematic.comm.Base64;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;
import wise.gui.schematic.connectionList;
import wise.gui.schematic.elements.electricElement;

/**
 *
 * @author Oscar E. Cala W.
 */
public class InterfaceAction extends AbstractInterfaceAction {

    public static final String ELEMENT_CREATED = "ElementCreated";
    public static String NET_CREATED = "NetCreated";
    public static String NET_CREATED_CONN_FIELD = "connectionsList";
    public static String MESSAGE_TYPE = "InterfaceAction";
    public static String ELEMENT_CREATED_HOLD_FIELD = "onHold";
    public static String ELEMENT_EDITED = "ElementEdited";
    public static String DONE_SELECTION = "done";
    public static String SIMULATION_PANEL = "SimulationPanel";
    public static String HINT_SELECTION = "hint";
    public static String SIMULATION_LAUNCHED = "SimulationLauched";
    public static String MENU_SELECTED = "MenuSelected";
    protected electricElement onHold;
    private connectionList connections;

    @Override
    public void dispatch() {
        if (this.getAction().equalsIgnoreCase(InterfaceAction.ELEMENT_CREATED) && onHold != null) {
            if(VLSchematicBoard.getInstance().getElementAt(onHold.xpos, onHold.ypos) < 0){
                VLSchematicBoard.getInstance().addElement(onHold, false, true);
            }else{
                VLSchematicBoard.getInstance().replaceElement(onHold);
            }            
            VLSchematicBoard.getInstance().setSelectedElement(null);
            VLSchematicBoard.getInstance().repaint();
        } else if (this.getAction().equalsIgnoreCase(InterfaceAction.NET_CREATED) && connections != null ) {
            VLSchematicBoard.getInstance().setConnections(connections, false);
        } else if (this.getAction().equalsIgnoreCase(InterfaceAction.ELEMENT_EDITED) && onHold != null ) {
            VLSchematicBoard.getInstance().replaceElement(onHold);
        }
    }

    @Override
    protected void extractToolProperties() {
        try {
            if (this.getAction().equalsIgnoreCase(InterfaceAction.ELEMENT_CREATED)) {
                Element holdField = properties.getChild(InterfaceAction.ELEMENT_CREATED_HOLD_FIELD);
                if (holdField != null) {
                    String onHoldString = holdField.getChild("value").getTextNormalize();
                    onHold = (electricElement) Base64.decodeToObject(onHoldString, Base64.GZIP, null);
                }
            } else if (this.getAction().equalsIgnoreCase(InterfaceAction.NET_CREATED)) {
                Element connListField = properties.getChild(InterfaceAction.NET_CREATED_CONN_FIELD);
                if (connListField != null) {
                    String serObjString = connListField.getChild("value").getTextNormalize();
                    connections = (connectionList) Base64.decodeToObject(serObjString, Base64.GZIP, null);
                }
            } else if (this.getAction().equalsIgnoreCase(InterfaceAction.ELEMENT_EDITED)) {
                Element holdField = properties.getChild(InterfaceAction.ELEMENT_CREATED_HOLD_FIELD);
                if (holdField != null) {
                    String onHoldString = holdField.getChild("value").getTextNormalize();
                    onHold = (electricElement) Base64.decodeToObject(onHoldString, Base64.GZIP, null);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InterfaceAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InterfaceAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean isInterfaceActionMessage(String action) {
        boolean result = false;
        result = action.equalsIgnoreCase(ELEMENT_CREATED)
                || action.equalsIgnoreCase(ELEMENT_EDITED)
                || action.endsWith(SIMULATION_LAUNCHED)
                || action.equalsIgnoreCase(NET_CREATED);
        return result;
    }
}
