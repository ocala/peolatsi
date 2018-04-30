package wise.gui.schematic.elements;

import java.awt.*;
import java.awt.event.*;

public class probeElement extends electricElement {
    String pname;

    public probeElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);
    }

    // setup props of the element (is overwritten by childs)
    void resetShape () {
        props=new objectData(7,0,1,0);

        // objects are allowed on grid/2
        props.line[0] = new Rectangle(0,0,1,-1);
        props.line[1] = new Rectangle(1,-1,4,-1);
        props.line[2] = new Rectangle(1,-4,4,-4);
        props.line[3] = new Rectangle(1,-1,1,-4);
        props.line[4] = new Rectangle(4,-1,4,-4);
        props.line[5] = new Rectangle(2,-2,2,-3);
        props.line[6] = new Rectangle(2,-2,3,-3);

        // pins are always on grid
        props.pinx[0] = 0;
        props.piny[0] = 0;

        // pins names are always on grid/4
        props.pinnamex[0] = 3;
        props.pinnamey[0] = -3;

        // border is always on grid (x,y,w,h);
        props.border = new Rectangle(0,-2,2,2);

        props.primitive = new String("!");

        props.valx=4;
        props.valy=5;

        pname = null;
    }

    void setNode(String pinname) {
        int i;
        pname = pinname;
        for(i=0;i<4;i++) {
            props.rotate90();
            if (props.rotation==0)
                if (pname!=null)
                    props.line[5] = new Rectangle(3,-2,2,-3);
                else
                    props.line[5] = new Rectangle(2,-2,2,-3);
            }
    }

    public String toString (int inst) 
    {   int i;
        String s = new String(".print ** ");
        if (pname!=null)
            s = s + " i("+ pname + ") ";
        else
            if (netConnects[0]!=null)
                s = s + " v("+ netConnects[0].toString() + ") ";
            else
                s = s + " v(0) ";
        return s;
    }

}
