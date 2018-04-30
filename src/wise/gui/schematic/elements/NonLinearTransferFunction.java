/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wise.gui.schematic.elements;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public class NonLinearTransferFunction extends TransferFunction{
    
    public NonLinearTransferFunction(int x, int y, int gr, int r){
        super(x,y,gr,r);
    }  
    
    @Override
    public void resetShape(){
        props=new objectData(6,0,2,8);
        setProperties();
        props.paramName[3] = "Input Offset";
        props.paramValue[3] = "0";
        props.paramUnit[3] = "Units";
        props.paramPredef[3] = null;
        props.paramDef[3] = "";
        
        props.paramName[4] = "Out Lower Limit";
        props.paramValue[4] = "-10";
        props.paramUnit[4] = "Units";
        props.paramPredef[4] = null;
        props.paramDef[4] = "";
        
        props.paramName[5] = "Out Upper Limit";
        props.paramValue[5] = "10";
        props.paramUnit[5] = "Units";
        props.paramPredef[5] = null;
        props.paramDef[5] = "";
        
        props.paramName[6] = "Dead Time";
        props.paramValue[6] = "1e-3";
        props.paramUnit[6] = "Seconds";
        props.paramPredef[6] = null;
        props.paramDef[6] = "";
        
        props.paramName[7] = "Dead Zone";
        props.paramValue[7] = "1";
        props.paramUnit[7] = "Volts";
        props.paramPredef[7] = null;
        props.paramDef[7] = "";
        
        props.primitive = "A_NLTF";
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
        String inputNet =  (netConnects[0] != null) ? netConnects[0].toString() : " 0 ";
        String outputNet = (netConnects[1] != null) ? netConnects[1].toString() : " 0 ";
        
        String tfDef = props.primitive + inst + " " ;         
        
        String deadZoneName = "X_dead_" + props.primitive + inst;
        String deadOut = deadZoneName + "_out";
        String deadZone = deadZoneName + " " + inputNet + " " + deadOut + " deadZone VT=" +props.paramValue[7] + " \n";
        
        
 /**       String limiterNameIn = props.primitive + inst + "_limiterIn";
        String limiterModelNameIn = limiterNameIn + "_model";
        String limiterInOutput = limiterNameIn + "_output";
        String limiterIn = limiterNameIn + " "+ deadOut +" " + limiterInOutput + " "+ limiterModelNameIn +" \n";        
        limiterIn = limiterIn + ".model "+ limiterModelNameIn +" limit(in_offset="+ props.paramValue[3] +" gain=1 out_lower_limit="+ props.paramValue[4] 
                + " out_upper_limit="+ props.paramValue[5] +" fraction=FALSE)\n";        
    */          
        String tfModelName = props.primitive + inst + "_model";   
        String tempNet = tfModelName + "_out";
        tfDef = tfDef + " " + deadOut + " " + tempNet + " " + tfModelName + " \n";
        tfDef += ".model " + tfModelName + " s_xfer( gain="+ props.paramValue[0] +" num_coeff=["+ props.paramValue[1]+ "] den_coeff=["+props.paramValue[2]+"] int_ic=[0 0])\n";
        
        String limiterName = props.primitive + inst + "_limiter";
        String limiterModelName = limiterName + "_model";
        String limiterOutput = limiterName + "_output";
        String limiter = limiterName + " "+ tempNet +" " + limiterOutput + " "+ limiterModelName +" \n";        
        limiter = limiter + ".model "+ limiterModelName +" limit(in_offset="+ props.paramValue[3] +" gain=1 out_lower_limit="+ props.paramValue[4] 
                + " out_upper_limit="+ props.paramValue[5] +" fraction=FALSE)\n";        
        
        String amp2Name = props.primitive + inst + "_outBuffer2";
        String amp2NameModel = amp2Name + "_model";
        String amp2Output = amp2Name + "_output";
        String amp2 = amp2Name + " " + limiterOutput + " " + amp2Output + " " + amp2NameModel + "\n";
        amp2 = amp2 + ".model " + amp2NameModel + " gain(gain=1)\n";
                
        String delayLineName = "T_" + props.primitive + inst + "_line";
        String delayOut = delayLineName + "_output";
        String delayLine = delayLineName + " " + amp2Output + " 0 " +  delayOut + " 0 z0=1k td =" + props.paramValue[6] + "\n";
        
        String loadName = "R_" + props.primitive + inst + "_rLoad";        
        String ampName = props.primitive + inst + "_outBuffer";
        String ampNameModel = ampName + "_model";
        String ampNameInput = delayOut;        
        String ampAndLoad = loadName + " " + delayOut + " 0 1k\n" + 
                ampName + " " + ampNameInput + " " + outputNet + " " + ampNameModel + "\n";
        ampAndLoad += ".model " + ampNameModel + " gain(gain=1)";            
                
        return deadZone /*+ limiterIn*/ + tfDef + limiter + amp2 + delayLine + ampAndLoad;
    }    
}
