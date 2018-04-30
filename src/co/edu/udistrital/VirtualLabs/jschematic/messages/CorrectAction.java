package co.edu.udistrital.VirtualLabs.jschematic.messages;

import co.edu.udistrital.VirtualLabs.jschematic.VLSchematicBoard;
import co.edu.udistrital.VirtualLabs.jschematic.comm.AbstractCorrectAction;
import co.edu.udistrital.VirtualLabs.jschematic.comm.Base64;
import co.edu.udistrital.VirtualLabs.jschematic.comm.DorminSocketInterface;
import co.edu.udistrital.VirtualLabs.jschematic.comm.SelectionActionInputKey;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import wise.gui.schematic.connectionList;
import wise.gui.schematic.elements.electricElement;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public class CorrectAction extends AbstractCorrectAction {

    protected electricElement onHold;
    protected connectionList connections;
    public static final String[] successMessages = {"La ultima acción ejecutada fue correcta.",
                                                    "Acción realizada correctamente.",
                                                    "La acción es correcta. Continue trabajando."};

    protected static int lastMessage = 0;


    @Override
    public void _dispatch() {
        VLSchematicBoard.getInstance().setCorrectBoardMessage(getRandomSuccessMessage());
        if (this.getAction().equalsIgnoreCase(InterfaceAction.ELEMENT_CREATED) && onHold != null) {            
            if(VLSchematicBoard.getInstance().getElementAt(onHold.xpos, onHold.ypos) < 0){
                VLSchematicBoard.getInstance().addElement(onHold, false, true);
            }else{
                VLSchematicBoard.getInstance().replaceElement(onHold);
            }
            VLSchematicBoard.getInstance().setSelectedElement(null);
            VLSchematicBoard.getInstance().repaint();
        } else if (this.getAction().equalsIgnoreCase(InterfaceAction.NET_CREATED) && connections != null) {
            VLSchematicBoard.getInstance().setConnections(connections, false);
        } else if (this.getAction().equalsIgnoreCase(InterfaceAction.ELEMENT_EDITED) && onHold != null) {
            VLSchematicBoard.getInstance().replaceElement(onHold);
        } 
    }

    @Override
    protected void extractToolProperties() {
        try {
            //Todo: Existe un problema cuando se demuestra en dos ramas diferentes el mismo elemento creado
            //como resultado se obtiene solamente el seguimiento/replay del ultimo demostrado, dado que la
            //clave S,A,I, se reemplaza
            if (this.getAction().equalsIgnoreCase(InterfaceAction.ELEMENT_CREATED)) {
                HashMap<String, String> externalProps = DorminSocketInterface.holdObjects.get(new SelectionActionInputKey(selection, action, input));
                if (externalProps != null) {
                    String onHoldString = externalProps.get(InterfaceAction.ELEMENT_CREATED_HOLD_FIELD);
                    onHold = (electricElement) Base64.decodeToObject(onHoldString, Base64.GZIP, null);
                }
            } // El seguimiento/replay de las redes funciona porque la clave, selectin, action,input se reempalza por la que ya existe con el estado total de las conexiones.
              //Sin embargo, existe el caso en que si se elimina una red creada en un estado K cuyo nombre de red tambien se ha creado en un paso menor K,
              // El ultimo segmento no puede reproducirse dado que corresponde a link eliminado en el BR
            else if (this.getAction().equalsIgnoreCase(InterfaceAction.NET_CREATED)) {
                HashMap<String, String> externalProps = DorminSocketInterface.holdObjects.get(new SelectionActionInputKey(selection, action, input));
                if (externalProps != null) {
                    String connListString = externalProps.get(InterfaceAction.NET_CREATED_CONN_FIELD);
                    connections = (connectionList) Base64.decodeToObject(connListString, Base64.GZIP, null);
                }
            } else if (this.getAction().equalsIgnoreCase(InterfaceAction.ELEMENT_EDITED)) {
                HashMap<String, String> externalProps = DorminSocketInterface.holdObjects.get(new SelectionActionInputKey(selection, action, input));
                if (externalProps != null) {
                    String onHoldString = externalProps.get(InterfaceAction.ELEMENT_CREATED_HOLD_FIELD);
                    onHold = (electricElement) Base64.decodeToObject(onHoldString, Base64.GZIP, null);
                }
            }


        } catch (IOException ex) {
            Logger.getLogger(CorrectAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CorrectAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getRandomSuccessMessage() {
        int num = 0;
        while(num == lastMessage){
            num = Math.abs((int)((Math.random()-(1e-10)) * 3));
        }
        lastMessage = num;
        return successMessages[num];
    }
}
