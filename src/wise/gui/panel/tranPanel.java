package wise.gui.panel;
// Source File Name:   tranPanel.java
//
//  Author: Wolfgang Scherr
//
//  $Id: tranPanel.java,v 1.4 2002/10/20 19:25:53 hoidain Exp $
//
//     Webbased Interactive Simulation Environment
//                             __     __
//               ||    // ||  // \\ ||
//               ||//|//  ||  \\__  ||_
//               |// |/   ||     \\ ||
//               |/  |    || \\__// ||___
//
//  Fachhochschule TECHNIKUM english semester project
//
//  Semester: 4ebb
//

// GUI stuff
import java.awt.*;
// event listener stuff
import java.awt.event.*;
// Integer/Double
// StringTokenizer
import java.util.*;

/** class to setup an TRANSIENT pane
  */
public class tranPanel extends simulationDialog implements ActionListener, WindowListener
{

    private static tranPanel instance;

    TextField sval, eval, temp, status;
    Choice scl;
    Button simulate, cancel;

    // PUBLIC DATA AND METHODS START HERE

    /** Opens a TRAN simulation panel.
      *
      * @param f       Parent frame
      * @param netlist netlist to extract all AC sources
      */
    public tranPanel(Frame f, String netlist)
    {
        // let the original class do everything needed...
        super(f, "WISE - TRAN SIMULATION");

        // needed for GridBagLayout
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        setLayout(new GridBagLayout());

        // this dialog grabs the input focus entirely
        setModal(true);
        dataValid = false;

        // but we want to know when he wants to get rid of it (indirect close)...
        addWindowListener(this);

         // take the netlist line by line apart and check for every source
        parseNetlist(netlist);

        // SETUP GUI (a list of labels and user entry facilities)
        c.gridwidth = 1;
        add(new Label("Step Time [s]"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(sval = new TextField(15),c);
        sval.setText("1e-5");

        c.gridwidth = 1;
        add(new Label("Stop Time [s]"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(eval = new TextField(15),c);
        eval.setText("1e-3");

        c.gridwidth = 1;
        add(new Label("Initial setup"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(scl = new Choice(),c);
        scl.add("OP");
        scl.add("UIC");

        c.gridwidth = 1;
        add(new Label("Temperature(s)"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(temp = new TextField(15),c);
        temp.setText("27");

        c.gridwidth = 1;
        add(simulate = new Button("simulate TRAN"),c);
        simulate.addActionListener(this);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(cancel = new Button("cancel"),c);
        cancel.addActionListener(this);

        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(status = new TextField(30),c);
        status.setEditable(false);
        status.setText("Setup TRANSIENT simulation...");

        // give the stuff an optimal size
        pack();
    }

    /** This method is needed as this class is an action listener.
      * (this interface is implemented in this class)
      *
      * @param e       the user event to handle
      */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("simulate TRAN")) {
            double ev, sv, c;
            // check numeric data first and cancel on error
            try {
                int num=0;
                double test=0;
                StringTokenizer st = new StringTokenizer(temp.getText());
                while (st.hasMoreTokens()) {
                    test = Double.valueOf(st.nextToken()).doubleValue();
                    if ((test>199) && (test<-199)) {
                        status.setText("Temperature value(s) out of bounds!");
                        return;
                    }
                    num++;
                }
                if (num==0) {
                    status.setText("No temperature value given!");
                    return;
                }
            } catch (NumberFormatException ne) {
                status.setText("Temperature value(s) wrong!");
                return;
            }
            try {
                sv = Double.valueOf(sval.getText()).doubleValue();
            } catch (NumberFormatException ne) {
                status.setText("Step time wrong!");
                return;
            }
            try {
                ev = Double.valueOf(eval.getText()).doubleValue();
            } catch (NumberFormatException ne) {
                status.setText("End time wrong!");
                return;
            }
            if (ev>100) {
                status.setText("End value too big!");
                return;
            }
            if ((ev<1e-9) || (sv<1e-9)) {
                status.setText("Step/end value too small!");
                return;
            }
            if ((ev/sv)>1e4)  {
                status.setText("Possibly too much  resulting steps!");
                return;
            }
            if ((ev/sv)<10)  {
                status.setText("Step (not much) smaller end time!");
                return;
            }
            if (ev>100) {
                status.setText("End value bigger than 100s!");
                return;
            }
            dataValid = true;
            dispose();
         } else
        if (cmd.equals("cancel")) {
            dispose();
         } else {
            System.out.println("cmd '"+cmd+"' not known.");
         }
    }

    /** This generates the needed line for simulation.
      * The main class collects everything for simulation by calling this.
      */
    @Override
    public String toString() {
        String line;

        if (dataValid) {
            line = ".TRAN " + sval.getText() + " " + eval.getText() + " 0 " + sval.getText();
            if (scl.getSelectedItem().equals("UIC"))
                line += "UIC\n";
            else
                line += "\n";
            line += ".options TEMP="+temp.getText()+"\n";
            return line;
        } else
            return null;
    }

    /** This generates the needed line for temperature variation.
      * The main class collects additional sweep data by calling this.
      */
    public String tempSweep() {
        String line;

        if (dataValid) {
            line = "T " + temp.getText();
            return line;
        } else
            return null;
    }

    /** we want to know if the panel gets closed (window listener interface)
      */
    public void windowClosing(WindowEvent e) {
        dispose();
    }
    /** just to satisfy window listener interface
      */
    public void windowOpened(WindowEvent e) {
    }
    /** just to satisfy window listener interface
      */
    public void windowClosed(WindowEvent e) {
    }
    /** just to satisfy window listener interface
      */
    public void windowDeiconified(WindowEvent e) {
    }
    /** just to satisfy window listener interface
      */
    public void windowIconified(WindowEvent e) {
    }
    /** just to satisfy window listener interface
      */
    public void windowDeactivated(WindowEvent e) {
    }
    /** just to satisfy window listener interface
      */
    public void windowActivated(WindowEvent e) {
    }


    public static tranPanel getInstance(Frame f, String netlist){
        if(instance == null){
            instance = new tranPanel(f,netlist);
        }else{
            instance.parseNetlist(netlist);
        }
        return instance;
    }
}
