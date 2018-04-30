package wise.gui.schematic.elements;

import java.awt.*;
import java.awt.event.*;

public class indElement extends electricElement {
    public indElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);
    }

    // setup props of the element (is overwritten by childs)
    void resetShape () {
        props=new objectData(12,0,2,1);

        // objects are allowed on grid/2
        props.line[0] = new Rectangle(0,0,0,2);
        props.line[1] = new Rectangle(-1,2,1,2);
        props.line[2] = new Rectangle(-1,2,-1,8);
        props.line[3] = new Rectangle(1,2,1,8);
        props.line[4] = new Rectangle(-1,8,1,8);
        props.line[5] = new Rectangle(0,8,0,10);
        props.line[6] = new Rectangle(-1,2,1,3);
        props.line[7] = new Rectangle(-1,4,1,3);
        props.line[8] = new Rectangle(-1,4,1,5);
        props.line[9] = new Rectangle(-1,6,1,5);
        props.line[10] = new Rectangle(-1,6,1,7);
        props.line[11] = new Rectangle(-1,8,1,7);

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
        props.border = new Rectangle(-1,0,2,5);

        // all element properties
        props.paramName[0] = new String("value");
        props.paramValue[0] = new String("1e-6");
        props.paramUnit[0] = new String("Henry");
        props.paramPredef[0] = new String("!1e-12 1");

        props.primitive = new String("L");

        props.valx=4;
        props.valy=5;
    }
}
