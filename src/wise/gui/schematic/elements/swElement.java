package wise.gui.schematic.elements;
import java.awt.*;

public class swElement extends electricElement {
    public swElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);
    }

    // setup props of the element (is overwritten by childs)
    @Override
    void resetShape () {
        props=new objectData(6,0,4,5);

        // objects are allowed on grid/2
        props.line[0] = new Rectangle(0,0,0,2);
        props.line[1] = new Rectangle(-1,2,0,2);
        props.line[2] = new Rectangle(-2,2,0,8);
        props.line[3] = new Rectangle(-1,5,-4,5);
        props.line[4] = new Rectangle(-4,3,-4,7);
        props.line[5] = new Rectangle(0,8,0,10);

        // pins are always on grid
        props.pinx[0] = 0;
        props.piny[0] = 0;
        props.pinx[1] = 0;
        props.piny[1] = 5;
        props.pinx[2] = -2;
        props.piny[2] = 1;
        props.pinx[3] = -2;
        props.piny[3] = 4;

        // pins names are always on grid/4
        props.pinnamex[0] = 2;
        props.pinnamey[0] = 2;
        props.pinnamex[1] = 2;
        props.pinnamey[1] = 8;
        props.pinnamex[2] = -6;
        props.pinnamey[2] = 3;
        props.pinnamex[3] = -6;
        props.pinnamey[3] = 6;
        props.pinname[0] = "np";
        props.pinname[1] = "nn";
        props.pinname[2] = "cp";
        props.pinname[3] = "cn";

        // border is always on grid (x,y,w,h);
        props.border = new Rectangle(-2,0,2,5);

        // all element properties
        props.paramName[0] = new String("init state");
        props.paramValue[0] = new String("OFF");
        props.paramUnit[0] = new String("");
        props.paramPredef[0] = new String("*OFF ON");
        props.paramDef[0] = new String("");
        props.paramName[1] = new String("threshold voltage");
        props.paramValue[1] = new String("1");
        props.paramUnit[1] = new String("Volts");
        props.paramPredef[1] = new String("!0 1e12");
        props.paramDef[1] = new String("");
        props.paramName[2] = new String("hysteresis");
        props.paramValue[2] = new String("0.1");
        props.paramUnit[2] = new String("Volts");
        props.paramPredef[2] = new String("!0 1e12");
        props.paramDef[2] = new String("");
        props.paramName[3] = new String("on resistance");
        props.paramValue[3] = new String("0.01");
        props.paramUnit[3] = new String("Ohms");
        props.paramPredef[3] = new String("!0.001 1e12");
        props.paramDef[3] = new String("");
        props.paramName[4] = new String("off resistance");
        props.paramValue[4] = new String("1e9");
        props.paramUnit[4] = new String("Ohms");
        props.paramPredef[4] = new String("!0.001 1e12");
        props.paramDef[4] = new String("");


        props.primitive = new String("S");

        props.valx=4;
        props.valy=5;
    }

    /**
      * Generates a parameter string for display.
      *
      * @return the parameter string
      */
    public String genParam() {
        return props.paramValue[0];
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
        s = s + "SWMOD_"+inst+" "+props.paramValue[0]+"\n";
        s = s + ".model SWMOD_"+inst+" SW (";
        if (!props.paramValue[1].equals(""))
            s = s + "VT="+props.paramValue[1]+" ";
        if (!props.paramValue[2].equals(""))
            s = s + "VH="+props.paramValue[2]+" ";
        if (!props.paramValue[3].equals(""))
            s = s + "RON="+props.paramValue[3]+" ";
        if (!props.paramValue[4].equals(""))
            s = s + "ROFF="+props.paramValue[4]+")";
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
