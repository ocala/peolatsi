package wise.gui.schematic.elements;

import java.awt.*;
import java.awt.event.*;

public class iprobeElement extends electricElement {
    public iprobeElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);
    }

    // setup props of the element (is overwritten by childs)
    void resetShape () {
        props=new objectData(7,0,2,0);

        // objects are allowed on grid/2
        props.line[0] = new Rectangle(0,0,1,0);
        props.line[1] = new Rectangle(1,-2,1,2);
        props.line[2] = new Rectangle(1,-2,3,-2);
        props.line[3] = new Rectangle(1,2,3,2);
        props.line[4] = new Rectangle(3,-2,3,2);
        props.line[5] = new Rectangle(3,0,4,0);
        props.line[6] = new Rectangle(2,-1,2,1);

        // pins are always on grid
        props.pinx[0] = 0;
        props.piny[0] = 0;
        props.pinx[1] = 2;
        props.piny[1] = 0;
        props.pinname[0] = "np";
        props.pinname[1] = "nn";

        // pins names are always on grid/4
        props.pinnamex[0] = -1;
        props.pinnamey[0] = 2;
        props.pinnamex[1] = 5;
        props.pinnamey[1] = 2;

        // border is always on grid (x,y,w,h);
        props.border = new Rectangle(0,-1,2,2);

        props.primitive = new String("/");

        props.valx=4;
        props.valy=5;
    }

    public String toString (int inst) 
    {   int i;
        String inets;
        if  ((netConnects[0]!=null) && (netConnects[1]!=null)) {
            inets = new String(netConnects[0].toString()+"_"+netConnects[1].toString());
        } else {
            inets = new String("probe_"+inst);
        }
        String s = new String(".print ** i(V"+inets+")\nV"+inets+" ");
        for(i=0;i<props.pincount;i++) {
            if (netConnects[i]!=null)
                s = s + netConnects[i].toString() + " ";
            else
                s = s + "0 ";
        }
        s = s + "AC 0 DC 0";
        return s;
    }

}
