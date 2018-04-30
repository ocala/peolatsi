package wise.gui.schematic.elements;


import java.awt.*;
import java.awt.event.*;

public class ampElement extends electricElement
{
    public ampElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);
    }

    void resetShape () {
        props=new objectData(13,0,4,1);

        props.line[0] = new Rectangle(2,0,12,6);
        props.line[1] = new Rectangle(2,12,12,6);
        props.line[2] = new Rectangle(2,0,2,12);
        props.line[3] = new Rectangle(0,2,2,2);
        props.line[4] = new Rectangle(0,10,2,10);
        props.line[5] = new Rectangle(10,2,12,2);
        props.line[6] = new Rectangle(10,10,12,10);
        props.line[7] = new Rectangle(3,3,5,3);
        props.line[8] = new Rectangle(4,2,4,4);
        props.line[9] = new Rectangle(3,9,5,9);
        props.line[10] = new Rectangle(7,2,9,2);
        props.line[11] = new Rectangle(8,1,8,3);
        props.line[12] = new Rectangle(7,10,9,10);
        props.pinx[0] = 6;
        props.piny[0] = 1;
        props.pinx[1] = 6;
        props.piny[1] = 5;
        props.pinx[2] = 0;
        props.piny[2] = 1;
        props.pinx[3] = 0;
        props.piny[3] = 5;
        props.pinnamex[0] = 14;
        props.pinnamey[0] = 3;
        props.pinnamex[1] = 14;
        props.pinnamey[1] = 8;
        props.pinnamex[2] = -2;
        props.pinnamey[2] = 3;
        props.pinnamex[3] = -2;
        props.pinnamey[3] = 8;
        props.pinname[0] = "ncp";
        props.pinname[1] = "ncn";
        props.pinname[2] = "np";
        props.pinname[3] = "nn";

        // border is always on grid (x,y,w,h);
        props.border = new Rectangle(0,0,6,6);

        // all element properties
        props.paramName[0] = new String("Amplification");
        props.paramValue[0] = new String("1e9");
        props.paramUnit[0] = new String("Volts/Volts");
        props.paramPredef[0] = new String("!1e-13 1e12");

        props.primitive = new String("E");

        props.valx=4;
        props.valy=6;
    }
}
