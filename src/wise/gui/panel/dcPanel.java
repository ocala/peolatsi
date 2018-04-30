package wise.gui.panel;
// Source File Name:   dcPanel.java
//
//  Author: Wolfgang Scherr
//
//  $Id: dcPanel.java,v 1.3 2002/10/20 19:25:53 hoidain Exp $
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

/** class to setup an DC pane
  */
public class dcPanel extends simulationDialog implements ActionListener, WindowListener
{
    private static dcPanel instance;

    TextField sval, eval, cnt, temp, status;
    Choice src;
    Button simulate, cancel;

    // PUBLIC DATA AND METHODS START HERE

   

    /** Opens a DC simulation panel.
      *
      * @param f       Parent frame
      * @param netlist netlist to extract all AC sources
      */
    public dcPanel(Frame f, String netlist)
    {
        // let the original class do everything needed...
        super(f, "WISE - DC SIMULATION");

        // needed for GridBagLayout
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        setLayout(new GridBagLayout());

        // this dialog grabs the input focus entirely
        setModal(true);
        dataValid = false;
        noSource = true;

        // but we want to know when he wants to get rid of it (indirect close)...
        addWindowListener(this);

        // SETUP GUI (a list of labels and user entry facilities)
        c.gridwidth = 1;
        add(new Label("Sweep Source"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(src = new Choice(),c);
        // take the netlist line by line apart and check for every source
        parseNetlist(netlist);
        // have no source, makes no sense to continue!
        //if (noSource) return;

        c.gridwidth = 1;
        add(new Label("Start Value [V/A]"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(sval = new TextField(15),c);
        sval.setText("-1");

        c.gridwidth = 1;
        add(new Label("End Value [V/A]"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(eval = new TextField(15),c);
        eval.setText("5");

        c.gridwidth = 1;
        add(new Label("Increment Value [V/A]"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(cnt = new TextField(15),c);
        cnt.setText("0.1");

        c.gridwidth = 1;
        add(new Label("Temperature(s)"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(temp = new TextField(15),c);
        temp.setText("27");

        c.gridwidth = 1;
        add(simulate = new Button("simulate DC"),c);
        simulate.addActionListener(this);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(cancel = new Button("cancel"),c);
        cancel.addActionListener(this);

        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(status = new TextField(30),c);
        status.setEditable(false);
        status.setText("Setup DC simulation...");

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
        if (cmd.equals("simulate DC")) {
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
                status.setText("Start value wrong!");
                return;
            }
            try {
                ev = Double.valueOf(eval.getText()).doubleValue();
            } catch (NumberFormatException ne) {
                status.setText("End value wrong!");
                return;
            }
            try {
                c = Double.valueOf(cnt.getText()).doubleValue();
            } catch (NumberFormatException ne) {
                status.setText("Increment val. wrong!");
                return;
            }
            if ((ev<sv) || (ev-sv)<(2*c)) {
                status.setText("End val. (not much) bigger than start!");
                return;
            }
            if (((ev-sv)/c)>100) {
                status.setText("Increment results in too much (100) points!");
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
            line = ".DC " + src.getSelectedItem() + " " + sval.getText() + " " + eval.getText() + " " + " " + cnt.getText() + "\n";
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

    public static dcPanel getInstance(Frame f, String netlist){
        if(instance == null){
            instance = new dcPanel(f,netlist);
        }else{
            instance.parseNetlist(netlist);
        }
        return instance;
    }

    @Override
    protected void parseNetlist(String netlist) {
        super.parseNetlist(netlist);
        StringTokenizer st = new StringTokenizer(netlist,"\n");
        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            if (line.startsWith("V") || line.startsWith("I")) {
                if (line.indexOf("AC")>0) {
                    // get the first element of the line (the instance name)
                    StringTokenizer sst = new StringTokenizer(line);
                    if (sst.hasMoreTokens()) {
                        // gotcha! add it to the choice button
                        src.add(sst.nextToken());
                        noSource = false;
                    }
                }
            }
        }
    }
}
