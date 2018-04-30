package wise.gui.schematic.elements;

import co.edu.udistrital.VirtualLabs.jschematic.VLSchematicBoard;
import co.edu.udistrital.VirtualLabs.jschematic.comm.Base64;
import co.edu.udistrital.VirtualLabs.jschematic.comm.DorminSocketInterface;
import co.edu.udistrital.VirtualLabs.jschematic.messages.InterfaceAction;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import wise.gui.schematic.connectionList;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.*;

/**
 * Base class for all electric elements.
 * It keeps the data of shape and parameter (values). It has also the
 * neccessary methods to generate a line for the netlist and the data
 * for a preload file. Every "real" electrical element inherits this
 * class and overwrites at least the resetShape method to setup an own
 * parameter/shape data set.
 *
 */
public class electricElement implements ActionListener, ItemListener, Serializable {

    public objectData props;
    protected int grid = 5;
    protected int grid2 = 2;
    protected int grid4 = 1;
    // The absolute position (grid) of the element X/Y
    public int xpos = 0;
    public int ypos = 0;
    // The used area of the element (pixel values)
    public Rectangle elementArea;
    // The connectivity of the pins (net names)
    protected String netConnects[];
    // Flag for shadowed drawing
    protected boolean shadow = false;
    // The dialog for the device parameters
    transient protected Dialog d;
    // The dialogs frame
    transient protected Frame df;
    // The text fields of the dialog
    protected TextField tf[];
    private String creationCommand;
    private int countInClass;

    /**
     * Sets up an electric element at a specific position and with
     * a specific rotation on a given grid.
     *
     * @param x initial position of the element (grid units)
     * @param y initial position of the element (grid units)
     * @param r   initial rotation of the element
     * @param gr  grid to be used
     *
     */
    public electricElement(int x, int y, int gr, int r) {
        int i;

        // reset basic props
        this.resetShape();

        // setup grid
        setGrid(gr);

        // setup position
        setLocation(x, y);

        // setup rotation
        setRotation(r);

        netConnects = new String[props.pincount];
    }

    /**
     * 
     * @param r number of times the element is rotated 90 degrees
     */
    public void setRotation(int r) {
        for (int i = 0; i < r; i++) {
            this.rotate90();
        }
    }

    /**
     * Update the position of the element.
     *
     * @param x position of the element (grid units)
     * @param y position of the element (grid units)
     *
     */
    public void setLocation(int x, int y) {
        xpos = x;
        ypos = y;

        // recalculate current element area in graphics context
        elementArea = props.selectionBox(xpos, ypos, grid);
    }

    /**
     * Set shadow mode of the element.
     * This mode may be used when the element is about to be placed
     * and is therefore not fully instanciated.
     *
     * @param val set to true if element is partially placed
     *
     */
    public void setShadow(boolean val) {
        shadow = val;
    }

    /**
     * Set rotation of the element.
     * Each call rotates by 90 degrees, we rotate the props data
     * directly, this makes redraw very fast.
     *
     */
    public void rotate90() {
        props.rotate90();

        // recalculate current element area in graphics context
        elementArea = props.selectionBox(xpos, ypos, grid);
    }

    /**
     * Set up element properties.
     * This method is usually overwritten by every new child, the default
     * may not be useful at all.
     *
     */
    void resetShape() {

        props = new objectData(6, 0, 2, 3);

        // objects are allowed on grid/2
        props.line[0] = new Rectangle(0, 0, 0, 2);
        props.line[1] = new Rectangle(-1, 2, 1, 2);
        props.line[2] = new Rectangle(-1, 2, -1, 8);
        props.line[3] = new Rectangle(1, 2, 1, 8);
        props.line[4] = new Rectangle(-1, 8, 1, 8);
        props.line[5] = new Rectangle(0, 8, 0, 10);

        // pins are always on grid
        props.pinx[0] = 0;
        props.piny[0] = 0;
        props.pinx[1] = 0;
        props.piny[1] = 5;

        // pins names are always on grid/4
        props.pinnamex[0] = 2;
        props.pinnamey[0] = 2;
        props.pinnamex[1] = 2;
        props.pinnamey[1] = 8;
        props.pinname[0] = "np";
        props.pinname[1] = "nn";

        // border is always on grid (x,y,w,h);
        props.border = new Rectangle(-1, 0, 2, 5);

        // all element properties
        props.paramName[0] = "value";
        props.paramValue[0] = "1";
        props.paramUnit[0] = "Ohms";
        props.paramPredef[0] = "!1e-3 1e12";
        props.paramName[1] = "*Rth1";
        props.paramValue[1] = "";
        props.paramUnit[1] = "Ohms/deg";
        props.paramPredef[1] = null;
        props.paramName[2] = "*Rth2";
        props.paramValue[2] = "";
        props.paramUnit[2] = "Ohms/deg";
        props.paramPredef[2] = null;

        props.primitive = "R";

        props.valx = 4;
        props.valy = 5;
    }

    /**
     * Check if the given position hits this element. (TODO)
     *
     * @param x position to be checked (absolute position)
     * @param y position to be checked (absolute position)
     * @return null if position is not within element, otherwise return
     *         its element area
     *
     */
    public Rectangle sendStrikeNotify(int x, int y) {
        // we don't compare the very border
        Rectangle target = new Rectangle(elementArea.x + sign(elementArea.x),
                elementArea.y + sign(elementArea.y),
                elementArea.width - 2 * sign(elementArea.width),
                elementArea.height - 2 * sign(elementArea.height));
        //System.out.println("I am :"+elementArea);
        if (target.contains(x, y)) {
            return elementArea;
        }
        return (null);
    }

    /**
     * Check if the given rectangle hits this element. (TODO)
     *
     * @param r rectangle to be checked (absolute position)
     * @return null if position is not within element, otherwise return
     *         its element area
     *
     */
    public Rectangle sendStrikeNotify(Rectangle r) {
        // we don't compare the very border
        Rectangle target = new Rectangle(elementArea.x + sign(elementArea.x),
                elementArea.y + sign(elementArea.y),
                elementArea.width - 2 * sign(elementArea.width),
                elementArea.height - 2 * sign(elementArea.height));
        //System.out.println("I am :"+elementArea);
        //if (target.contains(r)) return (elementArea);
        return (null);
    }

    /**
     * Show parameter menu of the element if it is hit by the coordinates.
     *
     * @param y position to be checked (absolute position)
     * @param x position to be checked (absolute position)
     * @param f the parent frame
     * @return true if position is within element, otherwise false
     *
     */
    public boolean showParams(int x, int y, Frame f) {

        if (sendStrikeNotify(x, y) != null) {
            return openParams(f);
        }
        return false;
    }

    /**
     * Open parameter menu of the element
     * May be overwritten by a more sofisticated method.
     *
     * @param f the parent frame
     * @return true if open is successful
     *
     */
    public boolean openParams(Frame f) {
        int i, j;
        tf = new TextField[props.paramcount];
        Button ok, cancel;

        if (props.paramcount == 0) {
            return true;
        }

        GridBagConstraints c = new GridBagConstraints();
        d = new Dialog(f, props.primitive + "-data", true);
        df = f;
        d.setLayout(new GridBagLayout());
        for (i = 0; i < props.paramcount; i++) {
            c.gridwidth = 1;
            d.add(new Label(props.paramName[i]), c);
            if ((props.paramPredef[i] != null)
                    && (props.paramPredef[i].startsWith("*"))) {
                Choice choice = new Choice();
                choice.addItemListener(this);
                c.gridwidth = GridBagConstraints.REMAINDER;
                d.add(choice, c);
                StringTokenizer st = new StringTokenizer(props.paramPredef[i].substring(1));
                j = 0;
                while (st.hasMoreTokens()) {
                    String s = st.nextToken();
                    choice.add(s);
                    if (s.equals(props.paramValue[i])) {
                        choice.select(j);
                    } else {
                        j++;
                    }
                }
            } else {
                d.add(tf[i] = new TextField(props.paramValue[i], 10), c);
                tf[i].addActionListener(this);
                c.gridwidth = GridBagConstraints.REMAINDER;
                d.add(new Label("[" + props.paramUnit[i] + "]"), c);
            }
        }
        c.gridwidth = 1;
        d.add(ok = new Button("OK"));
        ok.addActionListener(this);
        c.gridwidth = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        d.add(cancel = new Button("Cancel"));
        cancel.addActionListener(this);
        d.pack();
        d.setLocation((xpos + 5) * grid, ypos * grid);
        d.show();
        return true;
    }

    /** 
     * This method is called when a choice in the parameter form
     * is changed (to do further updates).
     *
     *  
     * @param choice the Choice object which was changed
     *
     */
    public void updateChoice(Choice choice) {
    }

    /**
     * Processes the dialog buttons.
     *
     * @param e an AWT event
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        int i;

        if (cmd.equals("OK")) {
            try {
                /* TODO: do the predef checking and warn on mismatch */
                for (i = 0; i < props.paramcount; i++) {
                    if ((props.paramPredef[i] == null) || (!props.paramPredef[i].startsWith("*"))) {
                        props.paramValue[i] = tf[i].getText();
                    }
                }
                int si = VLSchematicBoard.instance.getSelectedIndex();
                HashMap<String, String> externalProps = new HashMap<String, String>();
                externalProps.put(InterfaceAction.ELEMENT_CREATED_HOLD_FIELD, Base64.encodeObject(this, Base64.GZIP));
                //sendMessage("InterfaceAction", this.props.primitive, "ElementEdited", );
                DorminSocketInterface.instace.setPackageForMsgImpl(VLSchematicBoard.DEFAULT_PACKAGE_FOR_IMPL);
                DorminSocketInterface.sendMessage(InterfaceAction.MESSAGE_TYPE, this.props.primitive + "_" + this.countInClass, InterfaceAction.ELEMENT_EDITED, this.toString(si, false, true), externalProps);
                d.hide();
            } catch (IOException ex) {
                Logger.getLogger(electricElement.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (cmd.equals("Cancel")) {
            d.hide();
        } else {
            //TextArea t = (TextArea) e.getSource();
            /* TODO: remove unneccessary spaces etc. and check if value(s) is/are numerical */
        }
    }

    /**
     * Processes any choice changes
     *
     * @param e an AWT event
     */
    public void itemStateChanged(ItemEvent e) {
        // we forward the change to a specific method for handling
        updateChoice((Choice) e.getSource());
    }

    /**
     * Try to connect element at given position to a given net.
     *
     * @deprecated Use checkConnection instead of this method.
     *
     * @param x,y position to be connected (grid position)
     * @param net name of the net (null if unconnect element)
     * @return true if position is on a pin and net (or null) is captured
     *
     */
     boolean connectPin(int x, int y, String net) {
        int i;

        for (i = 0; i < props.pincount; i++) {
            if ((xpos * grid + props.pinx[i] * grid == x)
                    && (ypos * grid + props.piny[i] * grid == y)) {
                netConnects[i] = net;
                //System.out.println("Connect to :"+elementArea);
                return (true);
            }
        }
        return (false);
    }

    /**
     * Try to retrive instance + pin name of given position.
     * As Spice3 supports only voltage source current probing,
     * we don't use this for now...
     *
     * @param x,y position to be connected (grid position)
     * @return the components pin name or null
     *
     */
    String elementPin(int x, int y, int inst) {
        int i;
        String s = props.primitive + inst;

        for (i = 0; i < props.pincount; i++) {
            if ((xpos * grid + props.pinx[i] * grid == x)
                    && (ypos * grid + props.piny[i] * grid == y)) {
                return s + "," + props.pinname[i];
            }
        }
        return (null);
    }

    /**
     * Try to connect element to the given network (in connectionList class).
     *
     * @param conns a given network (of several nets)
     *
     */
    public void checkConnection(connectionList conns) {
        int i;
        for (i = 0; i < props.pincount; i++) {
            netConnects[i] = conns.getNetAt(xpos * grid + props.pinx[i] * grid, ypos * grid + props.piny[i] * grid);
            //System.out.println("Pin "+i+" connects to :"+netConnects[i]);
        }
    }

    /**
     * Generates a parameter string for display.
     * May be overwritten by a more sofisticated version.
     *
     * @return the parameter string
     */
    public String genParam() {
        if (props.paramcount > 0) {
            return props.paramValue[0];
        }
        return "";
    }

    /** 
     * Can be called to allow the elements to draw them themselves.
     * It draws the shapes in green, connected pins blue and unconnected
     * pins in red. Additionally the net names of the pins (if available)
     * are printed.
     *
     * @param g Graphics context to draw to
     *
     */
    public void paint(Graphics g) {
        int i;

        g.setFont(new Font("TimesRoman", 0, grid));
        if (!shadow) {
            g.setColor(Color.blue);
            for (i = 0; i < props.pincount; i++) {
                if (netConnects[i] != null) {
                    g.drawString(netConnects[i].toString(), xpos * grid + props.pinnamex[i] * grid2 - grid2,
                            ypos * grid + props.pinnamey[i] * grid2 + grid2);
                }
            }
            g.setColor(Color.green);
            g.drawString(getNameAndCount() + genParam(), xpos * grid + props.valx * grid2 - grid2,
                    ypos * grid + props.valy * grid2 + grid2);
        } else {
            g.setColor(Color.lightGray);
        }
        for (i = 0; i < props.linecount; i++) {
            g.drawLine(xpos * grid + props.line[i].x * grid2, ypos * grid + props.line[i].y * grid2,
                    xpos * grid + props.line[i].width * grid2, ypos * grid + props.line[i].height * grid2);
        }
        for (i = 0; i < props.arcscount; i++) {
            g.drawArc(xpos * grid + (int)props.arc[i].x * grid2, ypos * grid + (int)props.arc[i].y * grid2,
                    (int)props.arc[i].width * grid2, (int)props.arc[i].height * grid2, (int)props.arc[i].start, (int)props.arc[i].extent);
        }
        for (i = 0; i < props.circlecount; i++) {
            g.drawOval(xpos * grid + props.circle[i].x * grid2, ypos * grid + props.circle[i].y * grid2,
                    props.circle[i].width * grid2, props.circle[i].height * grid2);
        }
        if (!shadow) {
            for (i = 0; i < props.pincount; i++) {
                if (netConnects[i] == null) {
                    g.setColor(Color.red);
                    g.drawRect(xpos * grid + props.pinx[i] * grid - grid4, ypos * grid + props.piny[i] * grid - grid4, grid2, grid2);
                } else {
                    g.setColor(Color.green);
                    g.fillRect(xpos * grid + props.pinx[i] * grid - grid4, ypos * grid + props.piny[i] * grid - grid4, grid2, grid2);
                }
            }
        }
    }

    /** 
     * Reads a GRAPHICAL line to set up element.
     * May be overwritten by a more sofisticated version.
     *
     * @param s 
     * @return true if read in was successful
     *
     */
    public boolean fromString(String s) {
        int i;
        StringTokenizer st = new StringTokenizer(s);
        String x, y, g, r, v = "";

        if (!st.hasMoreTokens()) {
            return false;
        }
        // not this type of element
        if (!st.nextToken().equals("*")) {
            return false;
        }

        // uuuups..
        if (!st.hasMoreTokens()) {
            return false;
        }
        // not this type of element
        if (!st.nextToken().equals(props.primitive)) {
            return false;
        }

        //System.out.println("Reading data for "+props.primitive);
        // uuuups..
        if (!st.hasMoreTokens()) {
            return false;
        }
        x = st.nextToken();
        // uuuups..
        if (!st.hasMoreTokens()) {
            return false;
        }
        y = st.nextToken();

        if (!st.hasMoreTokens()) {
            return false;
        }
        r = st.nextToken();
        i = Integer.valueOf(r).intValue();
        if ((i > 3) || (i < 0)) {
            return false;
        }

        // uuuups..
        if (!st.hasMoreTokens()) {
            return false;
        }
        g = st.nextToken();
        // last check before modifications happen
        if (Integer.valueOf(g).intValue() != grid) {
            return false;
        }


        while (i-- != 0) {
            props.rotate90();
        }
        setLocation(Integer.valueOf(x).intValue(),
                Integer.valueOf(y).intValue());

        if (!st.hasMoreTokens()) {
            return false;
        }
        v = st.nextToken();
        for (i = 0; i < props.paramcount; i++) {
            // System.out.println("1: "+v);
            // uuuups..
            if (v.equals(";")) {
                return false;
            }

            if (!st.hasMoreTokens()) {
                return false;
            }
            v = st.nextToken();
            //System.out.println("2: "+v);

            if (!(v.equals(",")
                    || v.equals(";"))) {   // got a value, use it
                props.paramValue[i] = v;
                if (!st.hasMoreTokens()) {
                    return false;
                }
                v = st.nextToken(); // read next as it is no sep.
            } else {                // separator, value is empty
                props.paramValue[i] = "";
            }
        }
        //System.out.println("done.");
        return true;
    }

    /** 
     * Gives a GRAPHICAL line for this element.
     * We just use '(', ')' and ' ' as separator.
     * May be overwritten by a more sofisticated version.
     *
     * @return a graphical definition line
     *
     */
    @Override
    public String toString() {
        int i;
        String s = "* " + props.primitive + " ";
        s = s + xpos + " ";
        s = s + ypos + " ";
        s = s + props.rotation + " ";
        s = s + grid + " ";
        for (i = 0; i < props.paramcount; i++) {
            s = s + ", " + props.paramValue[i] + " ";
        }
        s = s + ";";
        return s;
    }

    /**
     * Gives a SPICE line for a given instance number.
     * May be overwritten by a more sofisticated version.
     *
     * @param inst number of this instance.
     * @return a SPICE conform netlist line
     *
     */
    public String toString(int inst, boolean sendSelectedIndex, boolean avoidConnections) {
        int i;
        String s;
        if (sendSelectedIndex) {
            s = props.primitive + inst + " ";
        } else {
            s = "";
        }
        s = s + getPropsAsString(avoidConnections);
        return s;
    }

    public String getPropsAsString(boolean avoidConnections) {
        String s = "";
        if (!avoidConnections) {
            s = getConnectionsString();
        }
        String r = getParamsString();
        return s + r;
    }

    public String getPropsAsString() {
        String s = "";
        s = getConnectionsString();
        String r = getParamsString();
        return s + r;
    }

    /** 
     * Gives a SPICE line for a given instance number.
     * May be overwritten by a more sofisticated version.
     *
     * @param inst number of this instance.
     * @return a SPICE conform netlist line
     *
     */
    public String toString(int inst) {
        String s = props.primitive + inst + " ";
        s = s + getPropsAsString();
        return s;
    }

    /**
     * Private method for the sign of an integer.
     */
    private int sign(int i) {
        if (i >= 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public int getRotation() {
        return this.props.getRotation();
    }

    public void setGrid(int gr) {
        grid = gr;
        grid2 = gr >> 1;
        grid4 = gr >> 2;
        elementArea = props.selectionBox(xpos, ypos, grid);
    }

    public void setCreationCommand(String el) {
        this.creationCommand = el;
    }

    public String getCreationCommand() {
        return creationCommand;
    }

    public String[] getNetConnections() {
        return netConnects;
    }

    public int getPinAt(int x, int y) {
        int pinx = 0;
        int piny = 0;
        for (int i = 0; i < props.pincount; i++) {
            pinx = xpos * grid + props.pinx[i] * grid;
            piny = ypos * grid + props.piny[i] * grid;
            if (x == pinx && y == piny) {
                return i;
            }
        }
        return -1;
    }

    public void setCountInClass(int i) {
        countInClass = i;
    }

    public Integer getCountInClass() {
        return countInClass;
    }

    public String getPrimitive() {
        return props.primitive;
    }

    private String getConnectionsString() {
        int i;
        String s = "";
        for (i = 0; i < props.pincount; i++) {
            if (netConnects[i] != null) {
                s = s + netConnects[i].toString() + " ";
            } else {
                s = s + "0 ";
            }
        }
        return s;
    }

    private String getParamsString() {
        int i;
        String s = "";
        for (i = 0; i < props.paramcount; i++) {
            if (props.paramValue[i].equals("")) {
                break;
            }
            if (props.paramDef[i] != null) {
                s = s + props.paramDef[i] + " ";
            }
            s = s + props.paramValue[i] + " ";
        }
        return s;
    }

    protected String getNameAndCount() {
        return props.primitive+ this.countInClass + " ";
    }
}
