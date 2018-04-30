package wise.gui.schematic.elements;
import wise.spice.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class bjtElement extends electricElement {
    spiceModels model;

    public bjtElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);
    }

    // setup props of the element (is overwritten by childs)
    void resetShape () {
        props=new objectData(10,0,3,1);

        // objects are allowed on grid/2
        props.line[0] = new Rectangle(0,0,0,2);
        props.line[1] = new Rectangle(-4,6,0,2);
        props.line[2] = new Rectangle(-4,6,0,10);
        props.line[3] = new Rectangle(0,10,0,12);
        props.line[4] = new Rectangle(-4,2,-4,10);
        props.line[5] = new Rectangle(-6,6,-4,6);
        props.line[6] = new Rectangle(-3,10,-2,10);
        props.line[7] = new Rectangle(-3,11,-2,11);
        props.line[8] = new Rectangle(-3,12,-2,12);
        props.line[9] = new Rectangle(-3,10,-3,12);

        // pins are always on grid
        props.pinx[0] = 0;
        props.piny[0] = 0;
        props.pinx[1] = -3;
        props.piny[1] = 3;
        props.pinx[2] = 0;
        props.piny[2] = 6;

        // pins names are always on grid/4
        props.pinnamex[0] = 2;
        props.pinnamey[0] = 2;
        props.pinnamex[1] = -6;
        props.pinnamey[1] = 7;
        props.pinnamex[2] = 2;
        props.pinnamey[2] = 10;
        props.pinname[0] = "nc";
        props.pinname[1] = "nb";
        props.pinname[2] = "ne";

        // border is always on grid (x,y,w,h);
        props.border = new Rectangle(-3,0,3,6);

        model = new spiceModels();
        String list = model.modelList("Q");
        StringTokenizer st = new StringTokenizer(list.substring(1));

        // all element properties
        props.paramName[0] = new String("Model");
        props.paramValue[0] = st.nextToken();
        props.paramUnit[0] = new String("");
        props.paramPredef[0] = list;

        props.primitive = new String("Q");

        props.valx=4;
        props.valy=5;
    }

    /** 
      * This method is called when a choice in the parameter form
      * is changed (to do further updates).
      *
      * @param chouce the Choice object which was changed
      *
      */
    public void updateChoice (Choice choice) 
    {   
        props.paramValue[0] = choice.getItem(choice.getSelectedIndex());
    }
}
