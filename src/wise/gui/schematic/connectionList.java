package wise.gui.schematic;

import co.edu.udistrital.VirtualLabs.jschematic.messages.StartStateCreated;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import wise.gui.schematic.elements.electricElement;
import java.util.StringTokenizer;

/** 
 * This is one of the important informations for a schematic -
 * the connectivity list.
 * <p>
 * It sets up the datastructures for keeping segment-wise the
 * wires of the schematic including the graphical information.<br>
 * The segments are always drawn in the horizontal direction first,
 * then in vertical direction. On the both ends knots are added.
 * <p>
 * Examples:
 * <p><code>
 * "x1/y1 o----+  x2/y2 o                                x2/y2 o"<br>
 * "           |        |            x1/y1 o----o x2/y2        |"<br>
 * "     x2/y2 o        +----o x1/y1                     x1/y1 o"<br>
 * </code>
 * 
 * @author Wolfgang Scherr
 * @version $Id: connectionList.java,v 1.2 2002/09/15 18:01:29 hoidain Exp $
 * @since JDK1.1
 */
public class connectionList implements Serializable {

    netData nets[];    // the array of available networks
    int grid = 5;
    int grid2 = 2;
    int grid4 = 1;
    private int netCount;
    private int segsize;
    private int selectedSegment;
    private int selectedNet;
    /**
     *
     */
    protected boolean xAxisFirst = true;
    private int zoom = 1;
    transient private electricElement[] components;
    private boolean fromString = false;
    private boolean lastNetCreated = false;
    private boolean notifyCreation = false;

    /** 
     * Build new connectivity list for a schematic.
     *
     * @param gr gridsize in pixel
     * @param size amount of nets
     * @param subsize allowed amount of segments per net
     *
     */
    public connectionList(int gr, int size, int subsize) {
        // amount of segments allowed per net
        segsize = subsize;

        // setup grid
        grid = gr;
        grid2 = gr >> 1;
        grid4 = gr >> 2;

        // no net for now....
        netCount = 0;
        selectedNet = -1;
        selectedSegment = -1;


        // setup network list
        nets = new netData[size];
    }

    /** 
     * Adds a new segment.
     * It is joined to an existing net if:
     * <ul>
     * <li>the start/end point of this segment hits any other segment path
     * <li>the start/endpoints of other segments hit this segment path
     * </ul>
     * It may happen that two or more existing nets are connected together,
     * the first one which is found is reference for every other found.
     *
     * @param x1
     * @param y1 the given start coordinates (pixel)
     * @param x2
     * @param y2 the given end coordinates (pixel)
     * @return      name of the net created or joined
     *
     */
    public String add(int x1, int y1, int x2, int y2) {
        int i[];
        x1 = (int) ((1 / (double) zoom) * x1);
        y1 = (int) ((1 / (double) zoom) * y1);
        x2 = (int) ((1 / (double) zoom) * x2);
        y2 = (int) ((1 / (double) zoom) * y2);
        // basic shape
        Rectangle shape = new Rectangle(x1, y1, x2, y2);

        i = checkJoinNet(x1, y1, x2, y2);
        //System.out.println("Found "+i[0]+" connections.");

        selectedNet = -1;
        selectedSegment = -1;

        if (i[0] == 0) {
            boolean acceptCreation = false;
            String name = "";
            // we setup a network with a maximum of segsize segments
            nets[netCount] = new netData(segsize, "n" + netCount);
            nets[netCount].add(shape);
            name = nets[netCount].name;
            netCount++;
            acceptCreation = isBindedToPin(x1, y1, x2, y2);            
            if (acceptCreation) {
                lastNetCreated = true;
                notifyCreation = true;
                return name;
            } else {
                nets[netCount] = null;
                netCount--;
                lastNetCreated = false;
                notifyCreation = false;
                return null;
            }
        } else if (i[0] == 1) {
            // we add the segment to the found net
            nets[i[1]].add(shape);
            lastNetCreated = true;
            if(!StartStateCreated.isReceived()){
                notifyCreation = true;
            }else{
                notifyCreation = false;
            }
            
            String name = nets[i[1]].name;
            if (isBindedToPin(x1,y1,x2, y2)) {                
               // return nets[i[1]].name;
                return name;
            } else {
                return name;
            }
        } else if (i[0] > 1) {
            int j, k, l;
            // we add the segment to the found net
            nets[i[1]].add(shape);
            // we have to join net(s)
            for (j = 2; j <= i[0]; j++) {
                nets[i[1]].add(nets[i[j]]);
                //System.out.println(j+": Joining net "+i[j]+" to "+i[1]);
            }
            // now resort nets and remove joined ones
            j = 2;
            k = 1;
            for (l = 1; l < netCount; l++) {
                ////System.out.println(j+" "+k+" "+l+" "+i[j]+" "+i[0]);
                if ((j <= i[0]) && (i[j] == l)) {
                    //System.out.println("Remove joined net "+i[j]+" ("+j+")");
                    j++;
                } else {
                    nets[l].name = "n" + k;
                    nets[k++] = nets[l];
                }
            }
            //System.out.println("Reduced "+netCount+" nets to "+k);
            netCount = k;
            lastNetCreated = true;
            notifyCreation = false;
            return nets[i[1]].name;
            //return null;
        }
        // if we reach this, there is something mysterious happening...
        return null;
    }

    /** 
     * Checks if given path joins to the existing wires.
     * It checks if:
     * <ul>
     * <li>the start/end point of this segment hits any other segment path
     * <li>the start/endpoints of other segments hit this segment path
     * </ul>
     * We suppose not to have more than 20 matches....
     *
     * @param x1,y1 the given start coordinate (pixel)
     * @param x2,y2 the given end coordinate (pixel)
     * @return      indices of the nets to join (idx=0 is # of hits)
     *
     */
    public int[] checkJoinNet(int x1, int y1, int x2, int y2) {
        int i, j, idx = 1;
        int arr[] = new int[21]; // idx=0 gives the number of hits

        //System.out.println("Check path: "+x1+"/"+y1+" "+x2+"/"+y2);
        for (i = 0; i < netCount; i++) {
            boolean found = false;
            netData n = nets[i];
            //System.out.println("Check net "+n.name);
            for (j = 0; j < n.segmentcount; j++) {
                int nx1 = n.segment[j].x;
                int ny1 = n.segment[j].y;
                int nx2 = n.segment[j].width;
                int ny2 = n.segment[j].height;
                //System.out.println(" - "+nx1+"/"+ny1+" "+nx2+"/"+ny2);
                // check if stored endpoints connect to given path
                // match horizontal path left->right and right->left
                found |= hMatch(nx1, ny1, x1, x2, y1);
                found |= hMatch(nx2, ny2, x1, x2, y1);
                if (found) {
                    break;
                }
                // match vertical path top->down and bottom->up
                found |= vMatch(nx1, ny1, x2, y1, y2);
                found |= vMatch(nx2, ny2, x2, y1, y2);
                if (found) {
                    break;
                }
                // check if stored pathes connect to given endpoints
                // match horizontal path left->right and right->left
                found |= hMatch(x1, y1, nx1, nx2, ny1);
                found |= hMatch(x2, y2, nx1, nx2, ny1);
                if (found) {
                    break;
                }
                // match vertical path top->down and bottom->up
                found |= vMatch(x1, y1, nx2, ny1, ny2);
                found |= vMatch(x2, y2, nx2, ny1, ny2);
                if (found) {
                    break;
                }
            }
            if (found) {
                //System.out.println(" Gotcha! - adding index "+i);
                arr[idx++] = i;
            }
        }
        arr[0] = idx - 1;
        return arr;
    }

    /** 
     * Checks if given coordinate hits an existing wire.
     *
     * @param x
     * @param y the given coordinate (pixel)
     * @return    net name (or null if not hit)
     *
     */
    public String getNetAt(int x, int y) {
        int i = getIdxAt(x, y);
        if (i >= 0) {
            return nets[i].name;
        }
        return null;
    }

    /** 
     * Select a net segment at a given coordinate
     *
     * @param x,y the given coordinate (pixel)
     * @return    net name (or null if not hit)
     *
     */
    public String selectNetAt(int x, int y) {
        selectedNet = getIdxAt(x, y);

        if (selectedNet >= 0) {
            return nets[selectedNet].name;
        }

        return null;
    }

    /** 
     * Select a whole net at a given coordinate
     *
     * @param x,y the given coordinate (pixel)
     * @return    net name (or null if not hit)
     *
     */
    public String selectFullNetAt(int x, int y) {

        selectedNet = getIdxAt(x, y);
        selectedSegment = -1;

        if (selectedNet >= 0) {
            return nets[selectedNet].name;
        }

        return null;
    }

    /** 
     * Delete a selection
     *
     * @param x,y the given coordinate (pixel)
     * @return    net name (or null if not hit)
     *
     */
    public void deleteSelection() {
        int i;
        if (selectedNet >= 0) {
            netData n = nets[selectedNet];
            //System.out.println("Delete net "+n.name+" segment "+selectedSegment+"/"+selectedNet+" of nets "+netCount);
            for (i = selectedNet; i < netCount; i++) {
                nets[i] = nets[i + 1];
                if(nets[i] != null){
                    nets[i].name = "n" + i;
                }
            }
            netCount--;
            if (selectedSegment != -1) { // partial deletion, join other segments again
                int not_this = selectedSegment;
                for (i = 0; i < n.segmentcount; i++) {
                    //System.out.println(" "+i+" "+not_this);
                    if (i != not_this) {
                        add(n.segment[i].x, n.segment[i].y, n.segment[i].width, n.segment[i].height, false);
                    }
                }
            }
        }
    }

    public netData getselectedNet() {
        if (selectedNet >= 0) {
            netData n = nets[selectedNet];

            return n;
        }
        return null;
    }

    public int getSelectedSegment() {
        return this.selectedSegment;
    }

    /** 
     * Checks if given coordinate hits an existing wire.
     *
     * @param x,y the given coordinate (pixel)
     * @return    net index (or -1 if not hit)
     *
     */
    private int getIdxAt(int x, int y) {
        x = (int) ((1 / (double) zoom) * x);
        y = (int) ((1 / (double) zoom) * y);

        int i, j;

        //System.out.println("Check coord: "+x+"/"+y);
        for (i = 0; i < netCount; i++) {
            netData n = nets[i];
            //System.out.println("Check net "+n.name);
            for (j = 0; j < n.segmentcount; j++) {
                int nx1 = n.segment[j].x;
                int ny1 = n.segment[j].y;
                int nx2 = n.segment[j].width;
                int ny2 = n.segment[j].height;
                //System.out.println(" - "+nx1+"/"+ny1+" "+nx2+"/"+ny2);
                // check if stored pathes connect to given points
                // match horizontal path left->right and right->left
                if (hMatch(x, y, nx1, nx2, ny1)) {
                    selectedSegment = j;
                    return i;
                }
                // match vertical path top->down and bottom->up
                if (vMatch(x, y, nx2, ny1, ny2)) {
                    selectedSegment = j;
                    return i;
                }
            }
        }
        return -1;
    }

    /** 
     * Creates GRAPHICS line.
     *
     */
    @Override
    public String toString() {
        String s = "";
        int i, j;

        for (i = 0; i < netCount; i++) {
            netData n = nets[i];
            s = s + "* # " + nets[i].name;
            for (j = 0; j < n.segmentcount; j++) {
                int nx1 = n.segment[j].x;
                int ny1 = n.segment[j].y;
                int nx2 = n.segment[j].width;
                int ny2 = n.segment[j].height;
                s = s + " , " + nx1 + " " + ny1 + " " + nx2 + " " + ny2;
            }
            s = s + " ;\n";
        }

        return s;
    }

    /** 
     * Setup connectivity list from GRAPHICS line.
     *
     */
    public boolean fromString(String s) {
        int i;
        StringTokenizer st = new StringTokenizer(s);
        String name, x1, y1, x2, y2, k;

        if (!st.hasMoreTokens()) {
            return false;
        }
        // not this type of element
        if (!st.nextToken().equals("*")) {
            return false;
        }

        if (!st.hasMoreTokens()) {
            return false;
        }
        // not this type of element
        if (!st.nextToken().equals("#")) {
            return false;
        }

        // uuuups..
        if (!st.hasMoreTokens()) {
            return false;
        }
        name = st.nextToken();

        //System.out.println("Reading network: "+name);
        // uuuups..
        if (!st.hasMoreTokens()) {
            return false;
        }
        // last check before modifications happen
        if (!st.nextToken().equals(",")) {
            return false;
        }

        while (true) {
            // uuuups..
            if (!st.hasMoreTokens()) {
                return false;
            }
            x1 = st.nextToken();
            if (x1.equals(";")) {
                return true;
            }
            if (x1.equals(",") && st.hasMoreTokens()) {
                x1 = st.nextToken();
            }
            if (!st.hasMoreTokens()) {
                return false;
            }
            y1 = st.nextToken();
            if (!st.hasMoreTokens()) {
                return false;
            }
            x2 = st.nextToken();
            if (!st.hasMoreTokens()) {
                return false;
            }
            y2 = st.nextToken();
            fromString = true;
            add(Integer.valueOf(x1).intValue(),
                    Integer.valueOf(y1).intValue(),
                    Integer.valueOf(x2).intValue(),
                    Integer.valueOf(y2).intValue());
            fromString = false;
        }
    }

    /** 
     * Can be called to allow the connections to draw them themselves.
     * It draws the lines in blue, with knots at every segment end.
     *
     * @param g Graphics context to draw to
     *
     */
    public void paint(Graphics g) {
        int i, j;
        g.setFont(new Font("TimesRoman", 0, grid));
        for (i = 0; i < netCount; i++) {
            netData n = nets[i];
            for (j = 0; j < n.segmentcount; j++) {
                int nx1 = n.segment[j].x * zoom;
                int ny1 = n.segment[j].y * zoom;
                int nx2 = n.segment[j].width * zoom;
                int ny2 = n.segment[j].height * zoom;
                if ((i == selectedNet) && (j == selectedSegment)) {
                    g.setColor(Color.yellow);
                } else if (i == selectedNet) {
                    if (selectedSegment < 0) {
                        g.setColor(Color.yellow);
                    } else {
                        g.setColor(Color.blue);
                    }
                } else {
                    g.setColor(Color.blue);
                }
                if (xAxisFirst) {
                    g.drawLine(nx1, ny1, nx2, ny1);
                    g.drawLine(nx2, ny1, nx2, ny2);
                } else {
                    g.drawLine(nx1, ny1, nx1, ny2);
                    g.drawLine(nx1, ny2, nx2, ny2);
                }
                g.fillRect(nx1 - grid4, ny1 - grid4, grid2, grid2);
                g.fillRect(nx2 - grid4, ny2 - grid4, grid2, grid2);
            }
        }
    }

    // match horizontal path left->right and right->left
    boolean hMatch(int x, int y, int a1, int a2, int b) {
        return (y == b) && (((a1 < a2) && (a1 <= x) && (a2 >= x))
                || ((a1 > a2) && (a1 >= x) && (a2 <= x)));
    }

    // match vertical path top->down and bottom->up
    boolean vMatch(int x, int y, int a, int b1, int b2) {
        return (x == a) && (((b1 < b2) && (b1 <= y) && (b2 >= y))
                || ((b1 > b2) && (b1 >= y) && (b2 <= y)));
    }

    public void zoomIn() {
        zoom *= 2;
    }

    public void zoomOut() {
        zoom /= 2;
    }

    public int getZoom() {
        return zoom;
    }

    private void add(int x1, int y1, int x2, int y2, boolean useZoom) {
        if (!useZoom) {
            x1 = zoom * x1;
            y1 = zoom * y1;
            x2 = zoom * x2;
            y2 = zoom * y2;
            add(x1, y1, x2, y2);
        } else {
            add(x1, y1, x2, y2);
        }
    }

    public void deleteAllNets() {
        nets = new netData[50];
    }

    public void setComponents(electricElement[] components) {
        this.components = components;
    }

    private boolean isBindedToPin(int x1,int y1, int x2, int y2) {
        if(fromString){
            return true;
        }
        int pin = 0;
        if(!(components instanceof electricElement[]) ) return false;
        for (int i = 0 ; i < components.length; i ++) {
            electricElement com = components[i];
            if(com == null) continue;
            pin = com.getPinAt(x1, y1);
            if (pin >= 0) {
                return true;
            }
            pin = com.getPinAt(x2, y2);
            if (pin >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the lastNetCreated
     */
    public boolean isLastNetCreated() {
        return lastNetCreated;
    }

    /**
     * @param lastNetCreated the lastNetCreated to set
     */
    public void setLastNetCreated(boolean lastNetCreated) {
        this.lastNetCreated = lastNetCreated;
    }

    /**
     * @return the notifyCreation
     */
    public boolean isNotifyCreation() {
        return notifyCreation;
    }

    /**
     * @param notifyCreation the notifyCreation to set
     */
    public void setNotifyCreation(boolean notifyCreation) {
        this.notifyCreation = notifyCreation;
    }
}
