package wise.gui.schematic.elements;

import java.awt.*;

public class resElement extends electricElement {
    public resElement (int x, int y, int gr, int r) {
        super(x,y,gr,r);
    }

    // setup props of the element (is overwritten by childs)
    @Override
    void resetShape () {
        props=new objectData(6,0,2,4);

        // objects are allowed on grid/2
        props.line[0] = new Rectangle(0,0,0,2);
        props.line[1] = new Rectangle(-1,2,1,2);
        props.line[2] = new Rectangle(-1,2,-1,8);
        props.line[3] = new Rectangle(1,2,1,8);
        props.line[4] = new Rectangle(-1,8,1,8);
        props.line[5] = new Rectangle(0,8,0,10);

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

        // all element properties
        props.paramName[0] = "value";
        props.paramValue[0] = "1e3";
        props.paramUnit[0] = "Ohms";
        props.paramPredef[0] = "!1e-3 1e12";
        props.paramDef[0] = "";
        props.paramName[1] = "*Rth1";
        props.paramValue[1] = "";
        props.paramUnit[1] = "Ohms/deg";
        props.paramPredef[1] = null;
        props.paramDef[1] = "";
        props.paramName[2] = "*Rth2";
        props.paramValue[2] = "";
        props.paramUnit[2] = "Ohms/deg";
        props.paramPredef[2] = null;
        props.paramDef[2] = "";
        props.paramName[3] = "*alternate TEMP";
        props.paramValue[3] = "27";
        props.paramUnit[3] = "deg";
        props.paramPredef[3] = null;
        props.paramDef[3] = "";


        props.primitive = "R";

        props.valx=4;
        props.valy=5;
    }

    /**
      * Generates a parameter string for display.
      *
      * @return the parameter string
      */
    @Override
    public String genParam() {
        if (props.paramValue[3].equals(""))
            return props.paramValue[0];
        else 
            return props.paramValue[0]/*+"(@"+props.paramValue[3]+")"*/;
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
        String s = props.primitive + inst + " ";
        for(i=0;i<props.pincount;i++) {
            if (netConnects[i]!=null)
                s = s + netConnects[i].toString() + " ";
            else
                s = s + "0 ";
        }
        if ( (props.paramValue[1].equals("")) &&
             (props.paramValue[2].equals("")) ) {
            s = s + props.paramValue[0];
        } else {
            s = s + "RMOD_"+inst+" L="+ props.paramValue[0];
            if (props.paramValue[3].equals("")) s=s+"\n";
            else s=s+" TEMP="+props.paramValue[3]+"\n";
            s = s + ".model RMOD_"+inst+" R RSH=1 DEFW=1 ";
            if (!props.paramValue[1].equals(""))
                s = s + "TC1="+props.paramValue[1]+" ";
            if (!props.paramValue[2].equals(""))
                s = s + "TC2="+props.paramValue[2]+" ";
        }
        return s;
    }


}
