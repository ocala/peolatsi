/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wise.gui.panel;

import java.awt.Dialog;
import java.awt.Frame;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public class simulationDialog extends Dialog {

    public boolean dataValid;
    public boolean noSource;
    boolean grounded = false;
    boolean probes = false;

    public simulationDialog(Frame f, String title) {
        super(f, title);
    }

    protected void parseNetlist(String netlist) {
        StringTokenizer st = new StringTokenizer(netlist, "\n");
        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            if (line.startsWith("V")) {
                String[] parts = line.split(" ");
                if (!grounded && !parts[1].equals("0") && parts[2].equals("0") && parts[3].equals("DC") && parts[4].equals("0")) {
                    grounded = true;
                    //break;
                }
            } else if (!probes && line.startsWith(".print")) {
                probes = true;
                //break;
            }
        }
        if (!grounded) {
            JOptionPane.showMessageDialog(this, "GND Element must be present. Before trying any simulation place the GND element.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (!probes) {
            JOptionPane.showMessageDialog(this, "At least one probe must exist in order to get some data as output.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void show() {
        if (!grounded || !probes) {
            return ;
        }
        super.show();
    }

    public void resetValidations(){
        grounded = false;
        probes = false;
        noSource = true;
        dataValid = false;        
    }
}
