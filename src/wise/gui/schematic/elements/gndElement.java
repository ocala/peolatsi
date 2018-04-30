package wise.gui.schematic.elements;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;

public class gndElement extends electricElement {

    public gndElement(int x, int y, int gr, int r) {
        super(x, y, gr, r);
    }

    // setup props of the element (is overwritten by childs)
    void resetShape() {
        props = new objectData(5, 0, 1, 0);

        // objects are allowed on grid/2
        props.line[0] = new Rectangle(0, 0, 0, 2);
        props.line[1] = new Rectangle(-1, 2, 1, 2);
        props.line[2] = new Rectangle(-1, 3, 1, 3);
        props.line[3] = new Rectangle(-1, 2, -1, 3);
        props.line[4] = new Rectangle(1, 2, 1, 3);

        // pins are always on grid
        props.pinx[0] = 0;
        props.piny[0] = 0;

        // pins names are always on grid/4
        props.pinnamex[0] = 2;
        props.pinnamey[0] = 2;

        // border is always on grid (x,y,w,h);
        props.border = new Rectangle(-1, 0, 2, 2);

        props.primitive = new String("V");

        props.valx = 4;
        props.valy = 5;
    }

    @Override
    public String toString(int inst) {
        int i;
        String s = props.primitive + inst + " ";
        if (netConnects[0] == null) {
            JOptionPane.showMessageDialog(null,"A GND Element has not been conneted to a net. Connect it o remove it from the circuit before trying a simulation.","Eror",JOptionPane.ERROR_MESSAGE);
            return "";
        }
        s = s + netConnects[0].toString() + " ";
        s = s + "0 DC 0";
        return s;
    }

    @Override
    public String getPrimitive() {
        return "GND";
    }
    
    @Override
    protected String getNameAndCount() {
        return "GND ";
    }
}
