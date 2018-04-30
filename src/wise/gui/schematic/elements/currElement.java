package wise.gui.schematic.elements;

import java.awt.*;
import java.awt.event.*;

public class currElement extends voltElement {
    public currElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);

    }

    // setup props of the element (is overwritten by childs)
    void resetShape () {
        props=new objectData(4,2,2,9);

        // objects are allowed on grid/2
        props.line[0] = new Rectangle(0,0,0,2);
        props.line[1] = new Rectangle(0,8,0,10);
        props.line[2] = new Rectangle(-1,1,0,2);
        props.line[3] = new Rectangle(1,1,0,2);

        props.circle[0] = new Rectangle(-2,2,4,4);
        props.circle[1] = new Rectangle(-2,4,4,4);

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

        unit = new String("Ampere");
        prim = new String("I");

        // all element properties
        props.paramName[0] = new String("AC value");
        props.paramValue[0] = new String("0");
        props.paramUnit[0] = unit;
        props.paramDef[0] = new String("AC");
        props.paramPredef[0] = null;
        props.paramName[1] = new String("transient type");
        props.paramValue[1] = new String("DC");
        props.paramUnit[1] = new String("");
        props.paramDef[1] = new String("type");
        props.paramPredef[1] = "*DC SIN PULSE PWL";
        props.paramName[2] = new String("DC value");
        props.paramValue[2] = new String("0");
        props.paramUnit[2] = unit;
        props.paramDef[2] = new String("DC");
        props.paramPredef[2] = null;
        // we reduce the parameter count properly
        props.paramcount=3;

        props.primitive = prim;

        props.valx=4;
        props.valy=5;

        type=new String("DC");
    }
}
