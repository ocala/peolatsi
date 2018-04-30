package wise.gui.schematic.elements;
import wise.spice.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class mosElement extends electricElement {
    spiceModels model;

    public mosElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);
    }

    // setup props of the element (is overwritten by childs)
    void resetShape () {
        props=new objectData(14,0,4,3);

        // objects are allowed on grid/2
        props.line[0] = new Rectangle(0,0,0,3);
        props.line[1] = new Rectangle(-3,3,0,3);
        props.line[2] = new Rectangle(-3,9,0,9);
        props.line[3] = new Rectangle(0,9,0,12);
        props.line[4] = new Rectangle(-4,3,-4,9);
        props.line[5] = new Rectangle(-3,3,-3,4);
        props.line[6] = new Rectangle(-3,5,-3,7);
        props.line[7] = new Rectangle(-3,8,-3,9);
        props.line[8] = new Rectangle(-6,6,-4,6);
        props.line[9] = new Rectangle(-3,10,-2,10);
        props.line[10] = new Rectangle(-3,11,-2,11);
        props.line[11] = new Rectangle(-3,12,-2,12);
        props.line[12] = new Rectangle(-3,10,-3,11);
        props.line[13] = new Rectangle(-2,11,-2,12);

        // pins are always on grid
        props.pinx[0] = 0;
        props.piny[0] = 0;
        props.pinx[1] = -3;
        props.piny[1] = 3;
        props.pinx[2] = 0;
        props.piny[2] = 6;
        props.pinx[3] = 0;
        props.piny[3] = 3;

        // pins names are always on grid/4
        props.pinnamex[0] = 2;
        props.pinnamey[0] = 2;
        props.pinnamex[1] = -6;
        props.pinnamey[1] = 7;
        props.pinnamex[2] = 2;
        props.pinnamey[2] = 10;
        props.pinnamex[3] = 2;
        props.pinnamey[3] = 4;
        props.pinname[0] = "ns";
        props.pinname[1] = "ng";
        props.pinname[2] = "nd";
        props.pinname[3] = "nb";

        // border is always on grid (x,y,w,h);
        props.border = new Rectangle(-3,0,3,6);

        model = new spiceModels();
        String list = model.modelList("M");
        StringTokenizer st = new StringTokenizer(list.substring(1));

        // all element properties
        props.paramName[0] = new String("Model");
        props.paramValue[0] = st.nextToken();
        props.paramUnit[0] = new String("");
        props.paramPredef[0] = list;
        props.paramDef[0] = new String("");
        props.paramName[1] = new String("Width");
        props.paramValue[1] = new String("1");
        props.paramUnit[1] = new String("um");
        props.paramPredef[1] = null;
        props.paramDef[1] = new String("");
        props.paramName[2] = new String("Length");
        props.paramValue[2] = new String("1");
        props.paramUnit[2] = new String("um");
        props.paramPredef[2] = null;
        props.paramDef[2] = new String("");

        props.primitive = new String("M");

        props.valx=4;
        props.valy=5;
    }

    /**
      * Generates a parameter string for display.
      *
      * @return the parameter string
      */
    public String genParam() {
        return props.paramValue[0]+" ("+props.paramValue[1]+"u/"+props.paramValue[2]+"u)";
    }

    /**
      * Gives a SPICE line for a given instance number.
      * Overwritten version.
      *
      * @param inst number of this instance.
      * @return a SPICE conform netlist line
      *
      */
    public String toString (int inst)
    {   int i;
        String s = new String(props.primitive+inst+" ");
        for(i=0;i<props.pincount;i++) {
            if (netConnects[i]!=null)
                s = s + netConnects[i].toString() + " ";
            else
                s = s + "0 ";
        }
        s = s + props.paramValue[0] + " ";
        if (!props.paramValue[1].equals(""))
            s = s + "W=" + props.paramValue[1] + "u ";
        if (!props.paramValue[2].equals(""))
            s = s + "L=" + props.paramValue[2] + "u ";
        return s;
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
