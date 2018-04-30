package wise.gui.schematic.elements;

import java.awt.*;

public class voltElement extends electricElement {
    String type;
    String unit;
    String prim;

    public voltElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);
    }

    // setup props of the element (is overwritten by childs)
    @Override
    void resetShape () {
        props=new objectData(6,1,2,9);

        // objects are allowed on grid/2
        props.line[0] = new Rectangle(0,0,0,3);
        props.line[1] = new Rectangle(0,7,0,10);
        props.line[2] = new Rectangle(-1,4,0,6);
        props.line[3] = new Rectangle(1,4,0,6);
        props.line[4] = new Rectangle(1,0,1,2);
        props.line[5] = new Rectangle(0,1,2,1);

        props.circle[0] = new Rectangle(-2,3,4,4);

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

        unit = "Volt";
        prim = "V";

        // all element properties
        props.paramName[0] = "AC value";
        props.paramValue[0] = "0";
        props.paramUnit[0] = unit;
        props.paramDef[0] = "AC";
        props.paramPredef[0] = null;
        props.paramName[1] = "transient type";
        props.paramValue[1] = "DC";
        props.paramUnit[1] = "";
        props.paramDef[1] = "type";
        props.paramPredef[1] = "*DC SIN PULSE PWL";
        props.paramName[2] = "DC value";
        props.paramValue[2] = "0";
        props.paramUnit[2] = unit;
        props.paramDef[2] = "DC";
        props.paramPredef[2] = null;
        // we reduce the parameter count properly
        props.paramcount=3;

        props.primitive = prim;

        props.valx=4;
        props.valy=5;

        type="DC";
    }

    /** 
      * This method is called when a choice in the parameter form
      * is changed (to do further updates).
      *
      * @param chouce the Choice object which was changed
      *
      */
    @Override
    public void updateChoice (Choice choice) {
        props.paramValue[1] = choice.getItem(choice.getSelectedIndex());
        //System.out.println("Choice is now: "+type);
        d.dispose();
        setProps();
        openParams(df);
    }

    /** 
      * This method is called to update the form entries.
      *
      */
    public void setProps () {
        type = props.paramValue[1];
        if (type.equals("DC")) {
            props.paramName[2] = ("DC value");
            props.paramValue[2] = ("0");
            props.paramUnit[2] = unit;
            props.paramDef[2] = ("DC");
            props.paramPredef[2] = null;
            // we set the parameter count properly
            props.paramcount=3;
        } else if (type.equals("PWL")) {
            props.paramName[2] = ("time/value pairs");
            props.paramValue[2] = ("0 0 1e-3 0 1.001e-3 1 1 1");
            props.paramUnit[2] = ("Seconds/"+unit);
            props.paramDef[2] = ("");
            props.paramPredef[2] = null;
            // we set the parameter count properly
            props.paramcount=3;
        } else if (type.equals("SIN")) {
            props.paramName[2] = ("frequency");
            props.paramValue[2] = ("1e3");
            props.paramUnit[2] = ("1/Seconds");
            props.paramDef[2] = ("");
            props.paramPredef[2] = ("!1e-6 10e9");
            props.paramName[3] = ("amplitude");
            props.paramValue[3] = ("1");
            props.paramUnit[3] = unit;
            props.paramDef[3] = ("");
            props.paramPredef[3] = ("!0 1e3");
            props.paramName[4] = ("offset");
            props.paramValue[4] = ("0");
            props.paramUnit[4] = unit;
            props.paramDef[4] = ("");
            props.paramPredef[4] = null;
            props.paramName[5] = ("delay");
            props.paramValue[5] = ("0");
            props.paramUnit[5] = ("Seconds");
            props.paramDef[5] = ("");
            props.paramPredef[5] = ("!0 1");
            props.paramName[6] = ("damping");
            props.paramValue[6] = ("0");
            props.paramUnit[6] = ("1/Seconds");
            props.paramDef[6] = ("");
            props.paramPredef[6] = ("!1e-6 1e6");
            // we set the parameter count properly
            props.paramcount=7;
        } else if (type.equals("PULSE")) {
            props.paramName[2] = ("low value");
            props.paramValue[2] = ("0");
            props.paramUnit[2] = unit;
            props.paramDef[2] = ("");
            props.paramPredef[2] = null;
            props.paramName[3] = ("high value");
            props.paramValue[3] = ("1");
            props.paramUnit[3] = unit;
            props.paramDef[3] = ("");
            props.paramPredef[3] = null;
            props.paramName[4] = ("period");
            props.paramValue[4] = ("1e-3");
            props.paramUnit[4] = ("Seconds");
            props.paramDef[4] = ("");
            props.paramPredef[4] = ("!1e-10 10");
            props.paramName[5] = ("delay");
            props.paramValue[5] = ("0");
            props.paramUnit[5] = ("Seconds");
            props.paramDef[5] = ("");
            props.paramPredef[5] = ("!0 1");
            props.paramName[6] = ("pulse width");
            props.paramValue[6] = ("0.5e-3");
            props.paramUnit[6] = ("Seconds");
            props.paramDef[6] = ("");
            props.paramPredef[6] = ("!1e-10 10");
            props.paramName[7] = ("risetime");
            props.paramValue[7] = ("1e-9");
            props.paramUnit[7] = ("Seconds");
            props.paramDef[7] = ("");
            props.paramPredef[7] = ("!1e-13 10");
            props.paramName[8] = ("falltime");
            props.paramValue[8] = ("1e-9");
            props.paramUnit[8] = ("Seconds");
            props.paramDef[8] = ("");
            props.paramPredef[8] = ("!1e-13 10");
            // we set the parameter count properly
            props.paramcount=9;
        }
    }

    /**
      * Generates a parameter string for display.
      *
      * @return the parameter string
      */
    @Override
    public String genParam() {
        if (type.equals("SIN"))
            return "AC:"+props.paramValue[0]+",SIN("+props.paramValue[3]+"/"+
                                                     props.paramValue[2]+")";
        if (type.equals("PULSE"))
            return "AC:"+props.paramValue[0]+",PULSE("+props.paramValue[2]+"/"+
                                                     props.paramValue[3]+"/"+
                                                     props.paramValue[4]+")";
        if (type.equals("PWL"))
            if (props.paramValue[2].length()>15) 
                return "AC:"+props.paramValue[0]+",PWL("+props.paramValue[2].substring(0,15)+"...)";
            else
                return "AC:"+props.paramValue[0]+",PWL("+props.paramValue[2]+")";
        return "AC:"+props.paramValue[0]+",DC("+props.paramValue[2]+")";
    }

    /** 
      * Gives a SPICE line for a given instance number.
      * Overwritten version.
      *
      * @param inst number of this instance.
      * @return a SPICE conform netlist line
      *
      */
    @Override
    public String toString (int inst) 
    {   int i;
        String s = (props.primitive+inst+" ");
        for(i=0;i<props.pincount;i++) {
            if (netConnects[i]!=null)
                s = s + netConnects[i].toString() + " ";
            else
                s = s + "0 ";
        }
        if (type.equals("SIN"))
            s = s + "AC "+props.paramValue[0]+" SIN("+props.paramValue[4]+" "+
                                                     props.paramValue[3]+" "+
                                                     props.paramValue[2]+" "+
                                                     props.paramValue[5]+" "+
                                                     props.paramValue[6]+")";
        else if (type.equals("PULSE"))
            s = s + "AC "+props.paramValue[0]+" PULSE("+props.paramValue[2]+" "+
                                                     props.paramValue[3]+" "+
                                                     props.paramValue[5]+" "+
                                                     props.paramValue[7]+" "+
                                                     props.paramValue[8]+" "+
                                                     props.paramValue[6]+" "+
                                                     props.paramValue[4]+")";
        else if (type.equals("PWL"))
            s = s + "AC "+props.paramValue[0]+" PWL("+props.paramValue[2]+")";
        else s = s + "AC "+props.paramValue[0]+" DC "+props.paramValue[2];
        return s;
    }

}
