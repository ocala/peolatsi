/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wise.gui.schematic.elements;

import java.awt.Rectangle;
import java.util.StringTokenizer;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public class TransferFunction extends electricElement {

    public TransferFunction(int x, int y, int gr, int r){
        super(x,y,gr,r);
    }

    @Override
    public void resetShape(){
        props=new objectData(6,0,2,3);
        setProperties();
    }
    
    public void setProperties(){
         // objects are allowed on grid/2
        props.line[0] = new Rectangle(-4,-2,4,-2);
        props.line[1] = new Rectangle(-4,2,4,2);
        props.line[2] = new Rectangle(-4,-2,-4,2);
        props.line[3] = new Rectangle(4,-2,4,2);
        props.line[4] = new Rectangle(-8,0,-4,0);
        props.line[5] = new Rectangle(4,0,8,0);
        
        
        //Arcs are allowed on grid/2
        //props.arc[0] = new Arc2D.Float(-1,-1,1,1,90,360,Arc2D.OPEN);
        

        // pins are always on grid
        props.pinx[0] = -4;
        props.piny[0] = 0;
        props.pinx[1] = 4;
        props.piny[1] = 0;

        // pins names are always on grid/4
        props.pinnamex[0] = -8;
        props.pinnamey[0] = -2;
        props.pinnamex[1] = 8;
        props.pinnamey[1] = -2;
        props.pinname[0] = "Input";
        props.pinname[1] = "Output";

         // all element properties
        props.paramName[0] = "gain";
        props.paramValue[0] = "1";
        props.paramUnit[0] = "K";
        props.paramPredef[0] = null;
        props.paramDef[0] = "";

        props.paramName[1] = "num_coeff";
        props.paramValue[1] = "0 1";
        props.paramUnit[1] = "A*S^n + B*S^n-1 + ... + X*S^1 + Y";
        props.paramPredef[1] = null;
        props.paramDef[1] = "";

        props.paramName[2] = "den_coeff";
        props.paramValue[2] = "1e-3 1";
        props.paramUnit[2] = "A*S^n + B*S^n-1 + ... + X*S^1 + Y";
        props.paramPredef[2] = null;
        props.paramDef[2] = "";
        
        // border is always on grid (x,y,w,h);
        props.border = new Rectangle(-4,-2,8,4);

        props.primitive = "A";

        props.valx=-1;
        props.valy=0;
    }
    
    @Override
    public String genParam(){
        return "G(s)";
    }

    /**
     * Gives a SPICE line for a given instance number.
     * May be overwritten by a more sofisticated version.
     *
     * @param inst number of this instance.
     * @return a SPICE conform netlist line
     *
     */
    @Override
    public String toString(int inst) {
        String s = props.primitive + inst + " ";
        int i=0;
        for (i = 0; i < props.pincount; i++) {
            if (netConnects[i] != null) {
                s = s + netConnects[i].toString() + " ";
            } else {
                s = s + "0 ";
            }
        }
        String modelName = "TF_A"+inst;
        s = s + " " + modelName + " \n";
        s = s + ".model " + modelName + " s_xfer( gain="+ props.paramValue[0] +" num_coeff=["+ props.paramValue[1]+ "] den_coeff=["+props.paramValue[2]+"] int_ic=[0 0])";
        return s;
    }
    
    /** 
     * Reads a GRAPHICAL line to set up element.
     * May be overwritten by a more sofisticated version.
     *
     * @param s 
     * @return true if read in was successful
     *
     */
    @Override
    public boolean fromString(String s) {
        int i;
        StringTokenizer st = new StringTokenizer(s);
        String x, y, g, r, v = "";

        if (!st.hasMoreTokens()) {
            return false;
        }
        // not this type of element
        if (!st.nextToken().equals("*")) {
            return false;
        }

        // uuuups..
        if (!st.hasMoreTokens()) {
            return false;
        }
        // not this type of element
        if (!st.nextToken().equals(props.primitive)) {
            return false;
        }

        //System.out.println("Reading data for "+props.primitive);
        // uuuups..
        if (!st.hasMoreTokens()) {
            return false;
        }
        x = st.nextToken();
        // uuuups..
        if (!st.hasMoreTokens()) {
            return false;
        }
        y = st.nextToken();

        if (!st.hasMoreTokens()) {
            return false;
        }
        r = st.nextToken();
        i = Integer.valueOf(r).intValue();
        if ((i > 3) || (i < 0)) {
            return false;
        }

        // uuuups..
        if (!st.hasMoreTokens()) {
            return false;
        }
        g = st.nextToken();
        // last check before modifications happen
        if (Integer.valueOf(g).intValue() != grid) {
            return false;
        }


        while (i-- != 0) {
            props.rotate90();
        }
        setLocation(Integer.valueOf(x).intValue(),
                Integer.valueOf(y).intValue());

        if (!st.hasMoreTokens()) {
            return false;
        }
        v = st.nextToken(); // The first Colon!
        for (i = 0; i < props.paramcount; i++) {            
            if (!st.hasMoreTokens()) {
                return false;
            }
            v = st.nextToken(",;"); //return the parameter
            if (!(v.equals(",")
                    || v.equals(";"))) {   // got a value, use it
                props.paramValue[i] = v.trim();
                /*if (!st.hasMoreTokens()) {
                    return false;
                }*/
                //v = st.nextToken(); // read next as it is no sep.
            } else {                // separator, value is empty
                props.paramValue[i] = "";
            }
        }
        //System.out.println("done.");
        return true;
    }
    
    protected String getNameAndCount() {
        return "";
    }


}
