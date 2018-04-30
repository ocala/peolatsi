package wise.gui.schematic;

import co.edu.udistrital.VirtualLabs.jschematic.comm.DorminSocketInterface;
import wise.spice.*;
import wise.gui.schematic.elements.*;
import java.awt.*;
import java.net.*;
import java.io.*;
// e.g. ActionListener
import java.awt.event.*;

/** 
 * This is the schematic board, containing electricElement classes and
 * their connections.
 * <p>
 * It inherits the Canvas type - so it can be used as every other AWT element.
 *
 * @author Wolfgang Scherr
 * @version $Id: schemBoard.java,v 1.6 2002/10/09 20:30:42 hoidain Exp $
 * @since JDK1.1
 */
public class schemBoard extends Canvas implements MouseListener, MouseMotionListener, KeyListener {   // some setup info, like size and basic grid

    protected Dimension mySize;
    protected int myHeight;
    protected int myWidth;
    protected int myGrid;
    // used for mouse movement
    protected int startX = -1, startY;
    protected int currX = -1, currY;
    protected boolean notValid = false;
    // used for backup of moved element
    protected int oldx, oldy;
    // the rectangle of the selected element
    protected Rectangle selectionRect = null;
    // the element info
    protected electricElement components[];
    protected int elCount = 0;
    protected electricElement onHold;
    protected int selectedIndex = 0;
    protected electricElement selectedElement;
    // the connectivity info
    /**
     *
     */
    protected connectionList connections;
    // this are the available models
    protected spiceModels models;
    private boolean xAxisFirst = true;

    public schemBoard() {
    }

    //private SocketReader listener;
    /** 
     * Setup a schematic board containing elements and connections.
     * A specific grid and a specific size must be given. Additionally
     * a preload file containing graphical data may be given.
     * <p>
     * The current connectivity may contain 30 nets with 50 segments each.
     *
     * @param width width (grid counts)
     * @param height height (grid counts)
     * @param grid the grid in pixel everything relates on
     * @param preload a URL where to find preload data (or null)
     */
    public schemBoard(int width, int height, int grid, String preload) {
        super();

        // setup size and grid
        mySize = new Dimension(width * grid + 1, height * grid + 1);
        myHeight = height;
        myWidth = width;
        myGrid = grid;

        // max. 30 nets with 50 segments each
        connections = new connectionList(grid, 30, 50);
        components = new electricElement[30];

        this.setBackground(Color.black);
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.addMouseMotionListener(this);

        this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        load(preload);
    }

    /** 
     * Load a schematic board file containing elements and connections.
     *
     * @param preload a URL where to find preload data (or null)
     * @return 
     */
    public boolean load(String preload) {
        int i;

        // mouse selection, wiring, movement values
        onHold = null;
        startX = -1;
        currX = -1;
        selectionRect = null;

        URL u = null;
        if ((preload != null)
                && (!preload.equals(""))) {
            try {
                String line = new String();
                // setup preload URL for graphical netlist
                if (!preload.endsWith(".gra")) {
                    preload += ".gra";
                }
                u = new URL(preload);
                System.out.println("Read preload data from: " + u + "\n");
                // we assign a input stream to a reader which can be used as line reader
                DataInputStream in = new DataInputStream(u.openStream());
                myGrid = Integer.valueOf(in.readLine().substring(2)).intValue();
                connections = new connectionList(myGrid, 30, 50);
                components = new electricElement[30];
                elCount = 0;
                while (in.available() > 0) {
                    //line = in.readUTF()+"\n";
                    // this is deprecated, but readUTF makes troubles....
                    line = in.readLine();
                    //System.out.println(line);
                    if (line.startsWith("* #")) {
                        connections.fromString(line);
                    } else {
                        int e = elCount;
                        if (line.startsWith("* !")) {
                            components[++elCount] = (electricElement) new probeElement(0, 0, myGrid, 0);
                        }
                        if (line.startsWith("* /")) {
                            components[++elCount] = (electricElement) new iprobeElement(0, 0, myGrid, 0);
                        } else if (line.startsWith("* V")) {
                            components[++elCount] = (electricElement) new voltElement(0, 0, myGrid, 0);
                            if (!components[elCount].fromString(line)) {
                                components[elCount].props.paramcount = 7;
                                if (!components[elCount].fromString(line)) {
                                    components[elCount].props.paramcount = 9;
                                    if (!components[elCount].fromString(line)) {
                                        components[elCount] = null;
                                    }
                                }
                            }
                            if (components[elCount] != null) {
                                ((voltElement) components[elCount]).setProps();
                            } else {
                                components[elCount] = (electricElement) new gndElement(0, 0, myGrid, 0);
                            }
                        } else if (line.startsWith("* I")) {
                            components[++elCount] = (electricElement) new currElement(0, 0, myGrid, 0);
                            if (!components[elCount].fromString(line)) {
                                components[elCount].props.paramcount = 7;
                                if (!components[elCount].fromString(line)) {
                                    components[elCount].props.paramcount = 9;
                                }
                            }
                            ((currElement) components[elCount]).setProps();
                        } else if (line.startsWith("* C")) {
                            components[++elCount] = (electricElement) new capElement(0, 0, myGrid, 0);
                        } else if (line.startsWith("* L")) {
                            components[++elCount] = (electricElement) new indElement(0, 0, myGrid, 0);
                        } else if (line.startsWith("* D")) {
                            components[++elCount] = (electricElement) new dioElement(0, 0, myGrid, 0);
                        } else if (line.startsWith("* Q")) {
                            components[++elCount] = (electricElement) new bjtElement(0, 0, myGrid, 0);
                        } else if (line.startsWith("* M")) {
                            components[++elCount] = (electricElement) new mosElement(0, 0, myGrid, 0);
                        } else if (line.startsWith("* R")) {
                            components[++elCount] = (electricElement) new resElement(0, 0, myGrid, 0);
                        } else if (line.startsWith("* E")) {
                            components[++elCount] = (electricElement) new ampElement(0, 0, myGrid, 0);
                        } else if (line.startsWith("* S")) {
                            components[++elCount] = (electricElement) new swElement(0, 0, myGrid, 0);
                        } else if (line.startsWith("* A")) {
                            if (line.startsWith("* A_NLTF")) {
                                components[++elCount] = (electricElement) new NonLinearTransferFunction(0, 0, myGrid, 0);
                            } else {
                                components[++elCount] = (electricElement) new TransferFunction(0, 0, myGrid, 0);
                            }
                        }
                        if (e == elCount) {
                            System.out.println("Can't decode: " + line);
                        } else if (!components[elCount].fromString(line)) {
                            components[elCount--] = null;
                        }
                    }
                }
                in.close();
                // TODO For preloading and loading operations We disable the
                // TODO tutor notification process
                // we need to update the component connections
                for (i = 1; i <= elCount; i++) {
                    components[i].checkConnection(connections);
                }

            } catch (MalformedURLException mue) {
                System.out.println("Preload illegal URL - " + mue + " - '" + u + "'");
                return false;
            } catch (IOException ioe) {
                System.out.println("Preload IO error - " + ioe + " - '" + u + "'");
                return false;
            } catch (Exception e) {
                System.out.println("Preload error - ");
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * We replace the update method to do double-buffering.
     * The new method creates a image, and calls update on this.
     * Then the image is painted in one step without flashing.
     *
     * @param g the graphics context
     */
    @Override
    public void update(Graphics g) {
        //System.out.println("update: "+g);
        Graphics gg;
        Image img = this.createImage(this.size().width, this.size().height);
        gg = img.getGraphics();
        super.update(gg); // calling update from canvas
        g.drawImage(img, 0, 0, this);
    }

    /**
     * A 'standard' paint method (called by the canvas' update).
     *
     * @param g the graphics context
     */
    @Override
    public void paint(Graphics g) {
        int i, j;

        //System.out.println("paint: "+g);

        if (currX >= 0) {
            if (notValid) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.lightGray);
            }

            if (true) {
                g.drawLine(startX, startY, currX, currY);
                //g.drawLine(currX, startY, currX, currY);
            }
            g.setPaintMode();
        }
        if (onHold != null) {
            onHold.paint(g);
        }
        g.setColor(Color.lightGray);
        for (i = 1; i < myWidth; i++) {
            for (j = 1; j < myHeight; j++) {
                g.drawLine(i * myGrid, j * myGrid, i * myGrid, j * myGrid);
            }
        }
        connections.paint(g);
        g.setColor(Color.yellow);
        if (selectionRect != null) {
            g.drawRect(selectionRect.x, selectionRect.y, selectionRect.width, selectionRect.height);
        }
        for (i = 1; i <= elCount; i++) {
            components[i].paint(g);
        }
    }

    /**
     * This method notifies the schem board that the user wants a new element.
     * The element is created and hold in a special variable. Additionally it is 'shadowed'
     * so the user can see easily the difference to an already placed element.
     *
     * @param  el a string that describes the element to add (conforms to menu item)
     * @return
     * @RETURN true (element added) or false (element does not exist)
     */
    public boolean addElement(String el) {
        int i;
        //System.out.println("Add element: "+el);

        if (el.equals("*-Ground")) {
            onHold = (electricElement) new gndElement(1, 1, myGrid, 0);
        } else if (el.equals("/-IProbe")) {
            onHold = (electricElement) new iprobeElement(1, 1, myGrid, 0);
        } else if (el.equals("!-VProbe")) {
            onHold = (electricElement) new probeElement(1, 1, myGrid, 0);
        } else if (el.equals("R-Resistor")) {
            onHold = (electricElement) new resElement(1, 1, myGrid, 0);
        } else if (el.equals("C-Capacitor")) {
            onHold = (electricElement) new capElement(1, 1, myGrid, 0);
        } else if (el.equals("L-Inductor")) {
            onHold = (electricElement) new indElement(1, 1, myGrid, 0);
        } else if (el.equals("D-Bipolar Diode")) {
            onHold = (electricElement) new dioElement(1, 1, myGrid, 0);
        } else if (el.equals("M-MOS Transistor")) {
            onHold = (electricElement) new mosElement(1, 1, myGrid, 0);
        } else if (el.equals("Q-Bipolar Transistor")) {
            onHold = (electricElement) new bjtElement(1, 1, myGrid, 0);
        } else if (el.equals("E-Ideal V-Amplifier")) {
            onHold = (electricElement) new ampElement(1, 1, myGrid, 0);
        } else if (el.equals("S-Switch")) {
            onHold = (electricElement) new swElement(1, 1, myGrid, 0);
        } else if (el.equals("V-Voltage Source")) {
            onHold = (electricElement) new voltElement(1, 1, myGrid, 0);
        } else if (el.equals("I-Current Source")) {
            onHold = (electricElement) new currElement(1, 1, myGrid, 0);
        } else if (el.equals("Transfer Function")) {
            onHold = (electricElement) new TransferFunction(1, 1, myGrid, 0);
        } else if (el.equals("Non-linear Transfer Function")) {
            onHold = (electricElement) new NonLinearTransferFunction(1, 1, myGrid, 0);
        } else {
            onHold = null;
            repaint();
            return false;
        }

        if (onHold != null) {
            selectedIndex = -1;
            onHold.setShadow(true);
            onHold.setCreationCommand(el);
            startX = -1;
            currX = -1;
            selectionRect = null;
            repaint();
        }

        //System.out.println("Hold: "+onHold);

        return true;
    }

    /**
     * If there is an element selected, we rotate it!
     *
     */
    public void rotateSelected() {
        if (selectedElement != null) {
            selectedElement.rotate90();
            selectionRect = selectedElement.elementArea;

        }
        repaint();
    }

    /**
     * If there is an element selected, we move it!
     *
     */
    public void moveSelected() {
        if (selectedElement != null) {
            selectedElement.setShadow(true);
            onHold = selectedElement;
            oldx = selectedElement.xpos;
            oldy = selectedElement.ypos;
        }
        repaint();
    }

    /**
     * If there is an element selected, we edit it!
     *
     */
    public void editSelected() {
        if (selectedElement != null) {
            selectedElement.openParams((Frame) this.getParent().getParent().getParent());
        }
        repaint();
    }

    /**
     * If there is an element selected, we delete it!
     *
     */
    public void deleteSelected() {
        int i;

        if ((selectedElement != null) && (selectedIndex != -1)) {
            for (i = selectedIndex; i < elCount; i++) {
                components[i] = components[i + 1];
            }
            elCount--;
            selectedElement = null;
            selectionRect = null;
            selectedIndex = -1;
        } else {
            connections.deleteSelection();
            // we need to update the component connections
            for (i = 1; i <= elCount; i++) {
                components[i].checkConnection(connections);
            }
        }
        repaint();
    }

    /**
     * A method to get a SPICE netlist out of the current schematic.
     * It greps through all elements which creates themselves the proper line for SPICE.
     *
     * @return
     * @RETURN the netlist within a string (including CRs)
     */
    public String getNetlist() {
        int i;
        models = new spiceModels();
        String nl = models.toString();

        for (i = 1; i <= elCount; i++) {
            String c = components[i].toString(i);
            if (!c.startsWith(".")) {
                nl = nl + c + "\n";
            }
        }
        return nl;
    }

    /**
     * A method to get a GRAPHICS data out of the current schematic.
     * It greps through all elements which creates themselves the proper line for the GRA structure.
     *
     * @return
     * @RETURN the graphics data within a string (including CRs)
     */
    public String getGraData() {
        int i;
        String nl = "* " + myGrid + "\n";

        nl = nl + connections.toString();
        for (i = 1; i <= elCount; i++) {
            String x = components[i].toString();
            nl = nl + x + "\n";
        }
        return nl;
    }

    /**
     * A method to get SPICE print cards for the probes out of the current schematic.
     * It greps through all elements which creates themselves the proper line for SPICE.
     *
     * @return
     * @RETURN the netlist within a string (including CRs)
     */
    public String getProbes() {
        int i;
        String nl = "";

        for (i = 1; i <= elCount; i++) {
            String c = components[i].toString(i);
            if (c.startsWith(".")) {
                nl = nl + c + "\n";
            }
        }
        return nl;
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
        return new Dimension(2048, 1024);
    }

    /**
     * Some containers need this to lay out this canvas.
     */
    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    /**
     * Used to select elements or nets.
     * <p>
     * A single click selects a net. Double click selects an element or
     * a net segment.
     *
     * @param e The mouse event data.
     */
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
            } else {
                if (selectedIndex < 0) {
                    onHold.setShadow(false);
                    onHold.checkConnection(connections);
                    components[++elCount] = onHold;
                    selectedIndex = elCount;
                    selectedElement = onHold;
                    selectionRect = selectedElement.elementArea;
                    onHold = null;
                } else {
                    onHold.setShadow(false);
                    onHold.checkConnection(connections);
                    selectedElement = onHold;
                    selectionRect = selectedElement.elementArea;
                    onHold = null;
                }
            }
            repaint();
        } else if (e.getClickCount() > 1) {
            if (connections.selectFullNetAt(x, y) != null) {
                selectionRect = null;
            } else {
                for (i = 1; i <= elCount; i++) {
                    selectionRect = components[i].sendStrikeNotify(e.getX() - 1, e.getY() - 1);
                    // got one, we are finished!
                    if (components[i].showParams(e.getX() - 1, e.getY() - 1, (Frame) this.getParent().getParent().getParent())) {
                        break;
                    }
                }
            }
        } else {
            if (connections.selectNetAt(x, y) != null) {
                selectionRect = null;
                selectedElement = null;
            } else {
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
            }
        }

        repaint();
    }

    /**
     * Used to draw a new net segment interactive.
     * <p>
     * Set up starting point for a new segment drawn online
     * within the schematic board.
     *
     * @param e The mouse event data.
     */
    public void mousePressed(MouseEvent e) {
        int x = ((e.getX() + (myGrid >> 1)) / myGrid) * myGrid;
        int y = ((e.getY() + (myGrid >> 1)) / myGrid) * myGrid;

        //System.out.println("P: "+x+"/"+y);

        startX = x;
        startY = y;
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
    public void mouseReleased(MouseEvent e) {
        int i;

        if ((!notValid) && (currX >= 0)) {
            String netName;

            if ((startX != currX) || (startY != currY)) {
                // add the net to the connection structure
                netName = connections.add(startX, startY, currX, currY);
                if (netName == null) {
                    System.out.println("Ouch! Something got wrong...");
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

    /**
     * Needed to satisfy the MouseListener interface - not needed here.
     *
     * @param e The mouse event data.
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Needed to satisfy the MouseListener interface - not needed here.
     *
     * @param e The mouse event data.
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Used to draw a new net segment interactive.
     * <p>
     * Updates the endpoint for the online drawing
     * within the schematic board. It checks the validity
     * of the line (crossing elements is not allowed).
     *
     * @param e The mouse event data.
     */
    public void mouseDragged(MouseEvent e) {
        int i;
        int incr;
        int x = ((e.getX() + (myGrid >> 1)) / myGrid) * myGrid;
        int y = ((e.getY() + (myGrid >> 1)) / myGrid) * myGrid;
        Rectangle obstacle;

        // System.out.println("D: "+x+"/"+y);

        currX = x;
        currY = y;

        selectionRect = null;
        incr = (startX < currX) ? myGrid : -myGrid;
        notValid = false;
        for (x = startX; x != currX + incr; x += incr) {
            for (i = 1; i <= elCount; i++) {
                obstacle = components[i].sendStrikeNotify(x, startY);
                //System.out.println("Component: "+i+" - "+selectionRect);
                if (obstacle != null) {
                    notValid = true;
                    selectionRect = obstacle;
                    break;
                }
            }
            if (notValid == true) {
                break;
            }
        }
        incr = (startY < currY) ? myGrid : -myGrid;
        for (y = startY; y != currY + incr; y += incr) {
            for (i = 1; i <= elCount; i++) {
                obstacle = components[i].sendStrikeNotify(currX, y);
                //System.out.println("Component: "+i+" - "+selectionRect);
                if (obstacle != null) {
                    notValid = true;
                    selectionRect = obstacle;
                    break;
                }
            }
            if (notValid == true) {
                break;
            }
        }
        repaint();
    }

    /**
     * Used to update an element on hold (before it is correctly placed by the user)
     *
     * @param e The mouse event data.
     */
    public void mouseMoved(MouseEvent e) {
        int x = (e.getX() + (myGrid >> 1)) / myGrid;
        int y = (e.getY() + (myGrid >> 1)) / myGrid;
        //System.out.println("M: "+x+"/"+y);
        if (onHold != null) {
            onHold.setLocation(x, y);
            repaint();
        }
    }

    /**
     * The part of the key listener interface used to watch key typing.
     *
     * @param e The key event data.
     */
    public void keyTyped(KeyEvent e) {
        char k = e.getKeyChar();
        //System.out.println(e.getKeyText(e.getKeyCode())+"/"+e.getKeyChar()+"/"+e.getKeyCode()+"/"+e.toString());
        if (k == KeyEvent.VK_ESCAPE) { // cancel movement
            if ((selectedElement != null) && (selectedIndex != -1) && (onHold != null)) {
                onHold.setLocation(oldx, oldy);
                onHold.setShadow(false);
                onHold = null;
                selectedIndex = -1;
                selectedElement = null;
                selectionRect = null;
            }
        } else if (k == KeyEvent.VK_DELETE) { // delete
            deleteSelected();
            onHold = null;
        } else if ((k == 'r')
                || (k == KeyEvent.VK_R)) { // rotation
            if (onHold != null) {
                onHold.rotate90();
            } else {
                rotateSelected();
            }
        } else if ((k == 'm')
                || (k == KeyEvent.VK_M)) { // move
            if (onHold == null) {
                moveSelected();
            }
        } else {
        }
        repaint();
    }

    /**
     * Needed to satisfy the keyListener interface - not needed here.
     *
     * @param e The key event data.
     */
    public void keyPressed(KeyEvent e) {
    }

    /**
     * Needed to satisfy the keyListener interface - not needed here.
     *
     * @param e The key event data.
     */
    public void keyReleased(KeyEvent e) {
    }
}
