package wise.gui.schematic.elements;


import java.awt.*;
import java.awt.event.*;

public class capElement extends electricElement
{
    public capElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);
    }

    void resetShape () {
        props=new objectData(4,0,2,1);

        props.line[0] = new Rectangle(0,0,0,4);
        props.line[1] = new Rectangle(-2,4,2,4);
        props.line[2] = new Rectangle(-2,6,2,6);
        props.line[3] = new Rectangle(0,6,0,10);
        props.pinx[0] = 0;
        props.piny[0] = 0;
        props.pinx[1] = 0;
        props.piny[1] = 5;
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
        props.paramValue[0] = new String("1e-9");
        props.paramUnit[0] = new String("Farad");
        props.paramPredef[0] = new String("!1e-13 5e-2");

        props.primitive = new String("C");

        props.valx=4;
        props.valy=5;
    }
}
