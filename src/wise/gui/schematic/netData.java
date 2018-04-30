package wise.gui.schematic;

import java.awt.*;
import java.io.Serializable;

/** 
 * This class keeps the segment information for a specific net.
 *
 * @author Wolfgang Scherr
 * @version $Id: netData.java,v 1.1.1.1 2002/09/03 19:31:06 hoidain Exp $
 * @since JDK1.1
 */
public class netData implements Serializable{
    /**
      * The name of the network.
      */
    public String name;
    /**
      * Amount of segments in this network.
      */
    public int segmentcount;
    /**
      * List of segments in Rectangle format.
      */
    public Rectangle segment[];

    int maxsegs;

    /** 
     * Initializes network data.
     *
     * @param segs amount of max. allowed segments
     * @param nam  name of network
     *
     */
    public netData (int segs, String nam) {
        int i;
        segment = new Rectangle[segs];
        segmentcount = 0;
        maxsegs = segs;
        name = nam;
    }

    /** 
     * Initializes network data with a given dataset (creates a copy).
     *
     * @param n    an other netData set
     *
     */
    public netData (netData n) {
        int i;
        maxsegs = n.maxsegs;
        segment = new Rectangle[maxsegs];
        segmentcount = n.segmentcount;
        name = n.name;
        for(i=0;i<maxsegs;i++) {
            segment[i]=n.segment[i];
        }
    }

    /** 
     * Adds a segment.
     *
     * @param seg  new segment in Rectangle class
     *
     */
    public void add (Rectangle seg) {
        segment[segmentcount++] = seg;
    }
    
    /** 
     * Adds segments from a given dataset.
     *
     * @param n    an other netData set
     *
     */
    public void add (netData n) {
        int i;
        for(i=0;i<n.segmentcount;i++) {
            segment[segmentcount++]=n.segment[i];
        }
    }


    /** 
     * Returns the net name and the amount of used segments.
     *
     */
    public String toString () {
        return name + " (#segs=" + segmentcount + ")";
    }
}
