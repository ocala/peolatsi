package co.edu.udistrital.VirtualLabs.jschematic;

import co.edu.udistrital.VirtualLabs.jschematic.comm.Base64;
import co.edu.udistrital.VirtualLabs.jschematic.comm.DorminSocketInterface;
import co.edu.udistrital.VirtualLabs.jschematic.comm.SelectionActionInputKey;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import wise.gui.schematic.schemBoard;
import java.awt.Dimension;
import wise.gui.schematic.connectionList;
import wise.gui.schematic.elements.electricElement;
import co.edu.udistrital.VirtualLabs.jschematic.messages.*;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Set;
import wise.gui.schematic.elements.TransferFunction;
import wise.gui.schematic.elements.gndElement;

/**
 * Esta clase representa el esquematico de captura de circuitos. Hereda directamente de la clase
 * schemBoard para obtener toda la funcionalidad de dicha clase y adicionalmente
 * sobreescribir los métodos necesarios de forma que se pueda infromar al 
 * BehaviourRecorder las acciones que el estudiante realiza sobre la interfaz *
 *
 * @version 0.0.1
 * @author Oscar
 * @see schemBoard
 */
public class VLSchematicBoard extends schemBoard {

    /**
     * Instancia unica de la clase que sera devuelta por el metodo getInstance
     * Representa un patron Singleton dado que una aplicación debe tener
     * solamente un VLSchematicBoard
     *
     * @see VLSchematicBoard
     * @see getInstance
     */
    int[] xPs = new int[]{35, 35, 28};
    int[] yPs = new int[]{20, 6, 13};
    Polygon p = new Polygon(xPs, yPs, xPs.length);
    int[] xPs2 = new int[]{90, 90, 97};
    int[] yPs2 = new int[]{20, 6, 13};
    Polygon p2 = new Polygon(xPs2, yPs2, xPs2.length);
    public static VLSchematicBoard instance;
    /**
     * paquete donde se encuentran las clases que permiten atender
     * los mensajes de respuesta del BR
     */
    public static final String DEFAULT_PACKAGE_FOR_IMPL = "co.edu.udistrital.VirtualLabs.jschematic.messages";
    private String boardMessage = "";
    private Color boardMessageColor = Color.RED;
    private String hintMessage = "";
    private Color hintMessageColor = Color.RED;
    private ArrayList<String> hints;
    private ListIterator<String> hintsIterator;
    private HashMap<String, Integer> elementCount = new HashMap<String, Integer>();
    private boolean lockingWidgets = false;
    private boolean groundCreated = false;

    /**
     * Constructor sin parametros utilizado para construir el objeto padre
     */
    private VLSchematicBoard() {
        super();
    }

    /**
     *
     * Contructor privado del VLSchematicBoard usado por el metodo singleton para crear
     * la instancia que sera usada en la aplicación
     *
     * addMouseListener, addMouseMotionListener y addKeyListener son asignados desde
     * esta funcion a THIS de manera que los lsitener que tengan acciones relevantes
     * seran sobreescritos en esta clase.
     *
     * @param width
     * @param height
     * @param grid
     * @param preload
     */
    private VLSchematicBoard(int width, int height, int grid, String preload) {
        super(width, height, grid, preload);
    }

    /**
     * Metodo singleton para obtener la instancia de la clase VLSchematicBoard
     * que funciona en la ventana de captura.
     *
     * @param width
     * @param height
     * @param grid
     * @param preload
     * @return VLSchematicBoard Instancia Singleton usada en la ventana de captura
     */
    public static VLSchematicBoard getInstance(int width, int height, int grid, String preload) {
        synchronized (VLSchematicBoard.class) {
            if (instance == null) {
                instance = new VLSchematicBoard(width, height, grid, preload);
            }
            return instance;
        }
    }

    public static VLSchematicBoard getInstance() {
        synchronized (VLSchematicBoard.class) {
            return instance;
        }
    }

    @Override
    /**
     * 
     *
     */
    public void deleteSelected() {
        String str = null;
        if ((selectedElement != null) && (selectedIndex != -1)) {
            //sendMessage("InterfaceAction", selectedElement.props.getPrimitive() + "_" + selectedIndex, "ElementDeleted", "-1");
        } else {
            // sendMessage("InterfaceAction", connections.getselectedNet().name + "_" + connections.getSelectedSegment(), "ConnectionDeleted", "-1");
        }
        /*
        Integer old = elementCount.get(selectedElement.getClass().getName());
        if(old != null){
        elementCount.put(onHold.getClass().getName(), old  - 1 );
        }
         *
         * 
         */
        super.deleteSelected();
    }

    @Override
    public void editSelected() {
        super.editSelected();
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public String getSelectedElementName() {
        if (selectedElement != null) {
            return selectedElement.props.getPrimitive() + "_" + getSelectedIndex();
        }
        return null;
    }

    public String getSelectedElementPrimitive() {
        if (selectedElement != null) {
            return selectedElement.getPrimitive();
        }
        return null;
    }

    @Override
    public void rotateSelected() {
        if (selectedElement != null) {
            selectedElement.rotate90();
            selectionRect = selectedElement.elementArea;
            //("InterfaceAction", getSelectedElementName(), "ElementRotated", selectedElement.getRotation() + "");
        }
        repaint();
    }

    /**
     * If there is an element selected, we move it!
     *
     */
    @Override
    public void moveSelected() {
        if (selectedElement != null) {
            selectedElement.setShadow(true);
            onHold = selectedElement;
            oldx = selectedElement.xpos;
            oldy = selectedElement.ypos;
            //sendMessage("InterfaceAction",getSelectedElementName(),"ElementMoved", "");
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int i;
        int x = ((e.getX() + (myGrid >> 1)) / myGrid) * myGrid;
        int y = ((e.getY() + (myGrid >> 1)) / myGrid) * myGrid;
        int mod = e.getModifiers();

        //System.out.println(e.getX()+"/"+e.getY()+" "+e.paramString());
        if (onHold != null) {
            //if (((mod&e.BUTTON3_MASK)!=0) || (e.isShiftDown())) {
            if (e.isShiftDown()) {
                onHold.rotate90();
                try {
                    String selection = getSelectedElementPrimitive() + "_" + onHold.getCountInClass();
                    HashMap<String, String> externalProps = new HashMap<String, String>();
                    externalProps.put(InterfaceAction.ELEMENT_CREATED_HOLD_FIELD, Base64.encodeObject(onHold, Base64.GZIP));
                    DorminSocketInterface.holdObjects.put(
                            new SelectionActionInputKey(selection, InterfaceAction.ELEMENT_CREATED, "-1"), externalProps);
                } catch (IOException ex) {
                    Logger.getLogger(VLSchematicBoard.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Elemento Rotado
            } else {
                if (selectedIndex < 0) {
                    //nuevo elemento
                    try {
                        if (!(groundCreated && onHold instanceof gndElement)) {
                            this.addElement(onHold, true, true);
                            HashMap<String, String> externalProps = new HashMap<String, String>();
                            externalProps.put(InterfaceAction.ELEMENT_CREATED_HOLD_FIELD, Base64.encodeObject(onHold, Base64.GZIP));
                            String selection = getSelectedElementPrimitive() + "_" + onHold.getCountInClass();
                            sendMessage(InterfaceAction.MESSAGE_TYPE, selection, InterfaceAction.ELEMENT_CREATED, "-1", externalProps);
                        } else {
                        }
                        onHold = null;
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                } else {
                    //mover elemento finaliado
                    onHold.setShadow(false);
                    onHold.checkConnection(connections);
                    try {
                        String selection = getSelectedElementPrimitive() + "_" + onHold.getCountInClass();
                        HashMap<String, String> externalProps = new HashMap<String, String>();
                        externalProps.put(InterfaceAction.ELEMENT_CREATED_HOLD_FIELD, Base64.encodeObject(onHold, Base64.GZIP));
                        DorminSocketInterface.holdObjects.put(
                                new SelectionActionInputKey(selection, InterfaceAction.ELEMENT_CREATED, "-1"), externalProps);
                    } catch (IOException ex) {
                        Logger.getLogger(VLSchematicBoard.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    selectedElement = onHold;
                    selectionRect = selectedElement.elementArea;
                    onHold = null;
                    //sendMessage("InterfaceAction",getSelectedElementName(),"ElementMoved","["+selectedElement.xpos + "," + selectedElement.ypos+"]");
                    //sendMessage("InterfaceAction", getSelectedElementName(), "ElementMoved", selectedElement.toString());
                }
            }
            repaint();
        } else if (e.getClickCount() > 1) {
            checkHintsIterators(e);
            String netName = connections.selectFullNetAt(x, y);
            if (netName != null) {
                //seleccion de net completa
                selectionRect = null;
                //sendMessage("InterfaceAction", netName, "FullNetSelected", "-1");
            } else {
                for (i = 1; i <= elCount; i++) {
                    selectionRect = components[i].sendStrikeNotify(e.getX() - 1, e.getY() - 1);
                    // got one, we are finished!
                    if (!(lockingWidgets && (components[i] instanceof TransferFunction))) {
                        if (components[i].showParams(e.getX() - 1, e.getY() - 1,
                                (Frame) this.getParent().getParent().getParent())) {
                            break;
                        }
                    }

                }
            }
        } else {

            checkHintsIterators(e);

            String netName = connections.selectNetAt(x, y);
            if (netName != null) {
                selectionRect = null;
                selectedElement = null;
                //("InterfaceAction", netName, "PartialNetSelected", "[" + x + "," + y + "]");
                //seleccion de net iun dividual
            } else {
                //seleccion de elemento
                selectedElement = null;
                for (i = 1; i <= elCount; i++) {
                    selectionRect = components[i].sendStrikeNotify(e.getX() - 1, e.getY() - 1);
                    // got one, we are finished!
                    if (selectionRect != null) {
                        selectedIndex = i;
                        selectedElement = components[i];
                        break;
                    }
                }
                if (selectedElement != null) {
                    //("InterfaceAction", getSelectedElementName(), "ElementSelected", selectedElement.toString());
                }
            }
        }
        repaint();
    }

    /**
     * Used to draw a new net segment interactive.
     * <p>
     * Set up end point for a new segment drawn online
     * within the schematic board. It adds the segment to
     * the connectivity list and updates the element pins.
     *
     * @param e The mouse event data.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        int i;
        if ((!notValid) && (currX >= 0)) {
            String netName;

            if ((startX != currX) || (startY != currY)) {
                //TODO: NEw conenction
                // add the net to the connection structure
                connections.setComponents(components);
                netName = connections.add(startX, startY, currX, currY);
                if (netName == null || !connections.isNotifyCreation()) {
                    if (connections.isLastNetCreated() && netName != null) {
                        HashMap<String, String> externalProps = new HashMap<String, String>();
                        try {
                            externalProps.put(InterfaceAction.NET_CREATED_CONN_FIELD, Base64.encodeObject(connections, Base64.GZIP));
                            DorminSocketInterface.holdObjects.put(
                                    new SelectionActionInputKey(netName, InterfaceAction.NET_CREATED, "-1"), externalProps);
                        } catch (IOException ex) {
                            Logger.getLogger(VLSchematicBoard.class.getName()).log(Level.SEVERE, null, ex);
                        }


                    } else {
                        System.out.println("Ouch! Something got wrong...");
                    }
                } else {
                    try {
                        HashMap<String, String> externalProps = new HashMap<String, String>();
                        externalProps.put(InterfaceAction.NET_CREATED_CONN_FIELD, Base64.encodeObject(connections, Base64.GZIP));
                        sendMessage(InterfaceAction.MESSAGE_TYPE, netName, InterfaceAction.NET_CREATED, "-1", externalProps);
                    } catch (IOException ex) {
                        Logger.getLogger(VLSchematicBoard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // we need to update the component connections
                for (i = 1; i <= elCount; i++) {
                    components[i].checkConnection(connections);
                }
            }
        }
        startX = -1;
        currX = -1;
        selectionRect = null;
        repaint();
    }

    public static void sendMessage(String msgType, String selection, String action, String input) {
        DorminSocketInterface.instace.setPackageForMsgImpl(DEFAULT_PACKAGE_FOR_IMPL);
        DorminSocketInterface.sendMessage(msgType, selection, action, input);
    }

    public void sendMessage(String msgType, String selection, String action, String input, HashMap<String, String> externalProps) {
        DorminSocketInterface.instace.setPackageForMsgImpl(DEFAULT_PACKAGE_FOR_IMPL);
        DorminSocketInterface.sendMessage(msgType, selection, action, input, externalProps);
    }

    public void setGrid(int grid) {
        myGrid = grid;
        for (int i = 1; i <= elCount; i++) {
            components[i].setGrid(grid);
        }

    }

    public int getGrid() {
        return this.myGrid;
    }

    public void zoomIn() {
        setGrid(getGrid() * 2);
        connections.zoomIn();
    }

    public void zoomOut() {
        if (getGrid() > 4) {
            setGrid(getGrid() / 2);
            connections.zoomOut();
        }
    }

    /**
     * Some containers need this to lay out this canvas.
     */
    @Override
    public Dimension getMinimumSize() {
        return mySize;
    }

    /**
     * Some containers need this to lay out this canvas.
     */
    @Override
    public Dimension getMaximumSize() {
        return new Dimension(800, 600);
    }

    /**
     * Some containers need this to lay out this canvas.
     */
    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    public void clearBoard() {
        // max. 30 nets with 50 segments each
        connections = new connectionList(myGrid, 30, 50);
        components = new electricElement[30];
        elCount = 0;
        onHold = null;
        selectedIndex = 0;
        selectionRect = null;
        boardMessage = "";
        hintMessage = "";
        elementCount.clear();
        groundCreated = false;
        repaint();
    }

    public void clearBoard(boolean cleanCountByClass) {
        if (cleanCountByClass) {
            clearBoard();
        } else {
            Set<Entry<String, Integer>> entrySet = elementCount.entrySet();
            clearBoard();
        }
    }

    public void addElement(electricElement onHold, boolean notifyTutor, boolean incrementBoardCount) {
        //DorminSocketInterface.enableSend = notifyTutor;
        if (incrementBoardCount) {
            Integer old = elementCount.get(onHold.getClass().getName());
            if (old == null) {
                old = 0;
            }
            elementCount.put(onHold.getClass().getName(), old + 1);
            onHold.setCountInClass(old + 1);

        }
        onHold.setShadow(false);
        onHold.checkConnection(connections);
        components[++elCount] = onHold;
        if (onHold instanceof gndElement) {
            groundCreated = true;
        }
        selectedIndex = elCount;
        selectedElement = onHold;
        selectionRect = selectedElement.elementArea;


        onHold = null;
        repaint();
    }

    public void setConnections(connectionList c, boolean notifyTutor) {
        connections = c;
        //DorminSocketInterface.enableSend = notifyTutor;
        for (int i = 1; i <= elCount; i++) {
            components[i].checkConnection(connections);
        }
    }

    public String getElementName(electricElement aThis) {
        String primitive = aThis.props.getPrimitive();
        String name = "";
        boolean found = false;
        int i = 0;
        for (i = 1; i <= elCount; i++) {
            if (components[i].equals(aThis)) {
                found = true;
                break;
            }
        }
        if (found) {
            name = primitive + "_" + i;
            return name;
        }
        Logger.getLogger(VLSchematicBoard.class.getName()).log(Level.INFO, "Tried to get an elementName and was not found");
        return null;
    }

    public void setBoardMessage(String msg, Color boardMC) {
        boardMessage = msg;
        boardMessageColor = boardMC;
    }

    public void setCorrectBoardMessage(String msg) {
        Color c = Color.GREEN;
        setBoardMessage(msg, c);
        hintMessage = "";
        repaint();
    }

    public void setIncorrectBoardMessage(String msg) {
        Color c = Color.RED;
        setBoardMessage(msg, c);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (boardMessage != null && !boardMessage.equals("")) {
            g.setColor(boardMessageColor);
            g.setFont(new Font("Arial Bold", Font.BOLD, 14));
            g.drawString(boardMessage, 40, 40);
        }
        if (hintMessage != null && !hintMessage.equals("")) {
            g.setColor(hintMessageColor);
            g.setFont(new Font("Arial Bold", Font.BOLD, 14));
            g.drawString(hintMessage, 40, 60);
        }
        g.setFont(new Font("Arial Bold", Font.BOLD, 14));
        g.setColor(Color.YELLOW);
        g.drawString("Hint:", 40, 20);
        g.fillPolygon(p);
        g.fillPolygon(p2);
        g.drawLine(0, 70, 6600, 70);
    }

    public void setBoardHintMessage(String tMsg, Color hmc) {
        hintMessage = tMsg;
        hintMessageColor = hmc;
    }

    public void setSuccessMessage(String tMsg) {
        setBoardHintMessage(tMsg, Color.GREEN);
    }

    public void setBuggyMessage(String tMsg) {
        setBoardHintMessage(tMsg, Color.RED);
    }

    public void setSelectedElement(electricElement object) {
        selectedElement = object;
        if (selectedElement == null) {
            selectionRect = null;
        } else {
            selectionRect = selectedElement.elementArea;
        }
    }

    public int getElementAt(int xpos, int ypos) {
        for (int i = 0; i < components.length; i++) {
            if (components[i] != null && components[i].xpos == xpos && components[i].ypos == ypos) {
                return i;
            }
        }
        return -1;
    }

    public boolean replaceElement(electricElement ele) {
        int indexToReplace = getElementAt(ele.xpos, ele.ypos);
        if (indexToReplace < 0) {
            return false;
        }
        components[indexToReplace] = ele;
        repaint();
        return true;
    }

    /**
     * Used to update an element on hold (before it is correctly placed by the user)
     *
     * @param e The mouse event data.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        int x = (e.getX() + (myGrid >> 1)) / myGrid;
        int y = (e.getY() + (myGrid >> 1)) / myGrid;
        //System.out.println("M: "+x+"/"+y);
        if (onHold != null) {
            onHold.setLocation(x, y);
            repaint();
        }
        if (p.contains(e.getX(), e.getY()) || p2.contains(e.getX(), e.getY())) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }

    }

    public void setHintsList(ArrayList<String> hints) {
        this.hints = hints;
        hintsIterator = this.hints.listIterator();
    }

    private void checkHintsIterators(MouseEvent e) {
        if (hintsIterator != null && p.contains(e.getX(), e.getY())) {
            previousHint();
        } else if (hintsIterator != null && p2.contains(e.getX(), e.getY())) {
            nextHint();
        }
    }

    public void nextHint() {
        if (hintsIterator.hasNext()) {
            hintMessage = hintsIterator.next();
            hintMessageColor = Color.YELLOW;
        }
    }

    public void previousHint() {
        if (hintsIterator.hasPrevious()) {
            hintMessage = hintsIterator.previous();
            hintMessageColor = Color.YELLOW;
        }
    }

    public void setLockingWidgets(boolean b) {
        lockingWidgets = b;
    }
}
