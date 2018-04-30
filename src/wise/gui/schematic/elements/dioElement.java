package wise.gui.schematic.elements;
import wise.spice.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class dioElement extends electricElement {
    spiceModels model;

    public dioElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);
    }

    // setup props of the element (is overwritten by childs)
    void resetShape () {
        props=new objectData(6,0,2,1);

        // objects are allowed on grid/2
        props.line[0] = new Rectangle(0,0,0,4);
        props.line[1] = new Rectangle(-1,4,1,4);
        props.line[2] = new Rectangle(0,6,-1,4);
        props.line[3] = new Rectangle(0,6,1,4);
        props.line[4] = new Rectangle(-1,6,1,6);
        props.line[5] = new Rectangle(0,6,0,10);

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

        model = new spiceModels();
        String list = model.modelList("D");
        StringTokenizer st = new StringTokenizer(list.substring(1));

        // all element properties
        props.paramName[0] = new String("Model");
        props.paramValue[0] = st.nextToken();
        props.paramUnit[0] = new String("");
        props.paramPredef[0] = list;

        props.primitive = new String("D");

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
