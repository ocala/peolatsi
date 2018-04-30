package wise.gui.schematic.elements;

import java.awt.*;
import java.io.Serializable;
import java.awt.geom.Arc2D;

/**
  * Setup the data for any electrical element.
  */
public class objectData implements Serializable {
    /**
      * the boundary of the element
      */
    public Rectangle border;

    /**
      * number of lines for the shape
      */
    public int linecount;
    /**
      * lines in a rectangle structure
      */
    public Rectangle line[];
    /**
      * number of circles for the shape
      */
    public int circlecount;
    /**
      * circles in a rectangle structure
      */
    public Rectangle circle[];

    /**
      * number pins of the element
      */
    public int pincount;
    /**
      * pin positions
      */
    public int pinx[], piny[];
    /**
      * pin internal names
      */
    public String pinname[];
    /**
      * pin name positions
      */
    public int pinnamex[], pinnamey[];

    /**
      * defines the element parameter count
      */
    public int paramcount;
    /**
      * defines the element parameter names, starts with '*' if it is optional
      */
    public String paramName[];
    /**
      * defines the element parameter values
      */
    public String paramValue[];
    /**
      * defines the element parameter name for SPICE
      */
    public String paramDef[];
    /**
      * defines the element parameter units
      */
    public String paramUnit[];
    /**
      * defines the element parameter predefined values when starting
      * with a '*' or a max/min value when starting with '!' or null
      * if nothing is given (several values separated with blanks)
      */
    public String paramPredef[];
    /**
      * position of the parameter value label
      */
    public int valx, valy;

    /**
      * defines the net connectivity of every pin
      */
    String netConnects[];

    /**
      * primitive element name for SPICE
      */
    String primitive;

    /**
      * keeps the rotation count
      */
    int rotation;
    /**
     * Arcs in a Arcs2D.Float Structure
     */
    public Arc2D.Float arc[];
    
    /**
     * number of arcs in the shape
     */
    int arcscount;

    /**
      * initializes datasets for lines, circles, pins and parameters
      *
      * @param lines amount of lines
      * @param lines amount of circles
      * @param lines amount of pins
      * @param lines amount of parameters
      */
    public objectData (int lines, int circ, int pins, int params) {
        border = null;

        linecount = lines;
        line = new Rectangle[lines];

        circlecount = circ;
        circle = new Rectangle[circ];

        pincount = pins;
        pinx = new int[pins];
        piny = new int[pins];
        pinname = new String[pins];
        pinnamex = new int[pins];
        pinnamey = new int[pins];

        paramcount = params;
        paramName = new String[params];
        paramValue = new String[params];
        paramUnit = new String[params];
        paramPredef = new String[params];
        paramDef = new String[params];

        rotation = 0;
        primitive = null;
    }
    
    public objectData(int lines, int circ, int pins, int params, int arcs) {
        this( lines, circ, pins, params) ;
        arcscount = arcs;
        arc = new Arc2D.Float[arcs];
    }

    /**
      * Rotate all coordinates by 90 degrees.
      */
    public void rotate90() {
        int i,x;

        //System.out.println(border);
        // quater turns are easy...
        x = -border.x;
        border.x = border.y;
        border.y = x;
        x = -border.width;     
        border.width = border.height;
        border.height = x;
        //System.out.println(border);
        
        for (i=0; i<linecount; i++) {
            x = -line[i].x;
            line[i].x = line[i].y;
            line[i].y = x;
            x = -line[i].width;
            line[i].width = line[i].height;
            line[i].height = x;
        }
        for (i=0; i<circlecount; i++) {
            x = -circle[i].x;
            circle[i].x = circle[i].y;
            circle[i].y = x;
            x = -circle[i].width;
            circle[i].width = circle[i].height;
            circle[i].height = x;
        }
        for (i=0; i<arcscount; i++) {
            float xf = -arc[i].x;
            arc[i].x = arc[i].y;
            arc[i].y = xf;
            xf = -arc[i].width;
            arc[i].width = arc[i].height;
            arc[i].height = xf;
        }        
        for (i=0; i<pincount; i++) {
            x = -pinx[i];
            pinx[i]=piny[i];
            piny[i]=x;
            x = -pinnamex[i];
            pinnamex[i]=pinnamey[i];
            pinnamey[i]=x;
        }
        x = -valx;
        valx=valy;
        valy=x;

        // keep rotation level, more than 3 is a full turn
        rotation++;
        if (rotation==4) rotation=0;
    }

    /**
      * Return a drawable rechtangle with given grid and position
      */
    public Rectangle selectionBox(int x, int y, int grid) {
        // we need this handling as negative width/height in rectangles
        // is not allowed!
        Rectangle r = new Rectangle(border);
        if (border.width<0) {
            r.width=-r.width;
            r.x-=r.width;
        }
        if (border.height<0) {
            r.height=-r.height;
            r.y-=r.height;
        }
        //System.out.println(r.x+" "+r.y+" "+r.width+" "+r.height);
        return new Rectangle((x+r.x)*grid,
                             (y+r.y)*grid,
                             (r.width)*grid,
                             (r.height)*grid);
    }

    public String getPrimitive(){
        return this.primitive;
    }

    public int getRotation(){
        return this.rotation;
    }
}
