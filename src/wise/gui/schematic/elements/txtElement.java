package wise.gui.schematic.elements;

import java.awt.*;
import java.awt.event.*;

public class txtElement extends electricElement {
    public txtElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);
    }

    // setup props of the element (is overwritten by childs)
    void resetShape () {
        props=new objectData(0,0,0,1);

        props.primitive = new String("*");

        props.paramName[0] = new String("text");
        props.paramValue[0] = new String("note");
        props.paramUnit[0] = new String("ASCII");
        props.paramPredef[0] = new String("");

        props.border = new Rectangle(-1,-1,2,2);

        props.valx=4;
        props.valy=5;
    }

    public String toString (int inst) 
    {   
        String s = new String(props.primitive+inst+" ");
        s = s + props.paramValue[0];
        return s;
    }

    /** 
     * Can be called to allow the elements to draw them themselves.
     * It draws the shapes in green, connected pins blue and unconnected
     * pins in red. Additionally the net names of the pins (if available)
     * are printed.
     *
     * @param g Graphics context to draw to
     *
     */
    public void paint(Graphics g) {
        int i;

        g.setFont(new Font("TimesRoman",0,grid*2));
        if (!shadow) 
            g.setColor(Color.white);
        else
            g.setColor(Color.lightGray);
        g.drawString(props.paramValue[0],xpos*grid,ypos*grid);
    }
}
