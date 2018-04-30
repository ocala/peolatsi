package wise.gui.panel;
// Source File Name:   acPanel.java
//
//  Author/Copyright: Wolfgang Scherr
//
//  $Id: acPanel.java,v 1.3 2002/10/20 19:25:53 hoidain Exp $
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

/*
   ------------------------------------------------------------------------

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

   ------------------------------------------------------------------------
*/

// GUI stuff
import java.awt.*;
// event listener stuff
import java.awt.event.*;
// Integer/Double
// StringTokenizer
import java.util.*;

/** class to setup an AC pane
  */
public class acPanel extends simulationDialog implements ActionListener, WindowListener
{

    TextField sfreq, efreq, cnt, temp, status;
    Choice scl;
    Choice src;
    Button simulate, cancel;

    // PUBLIC DATA AND METHODS START HERE

    
    private static acPanel instance;

    /** Opens a AC simulation panel.
      *
      * @param f       Parent frame
      * @param netlist netlist to extract all AC sources
      */
    public acPanel(Frame f, String netlist)
    {
        // let the original class do everything needed...
        super(f, "WISE - AC SIMULATION");

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

        // take the netlist line by line apart and check for every source
        parseNetlist(netlist);
        // has no source, makes no sense to continue!
        //if (noSource) return;

        // SETUP GUI (a list of labels and user entry facilities)
        c.gridwidth = 1;
        add(new Label("Start Frequency [HZ]"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(sfreq = new TextField(15),c);
        sfreq.setText("1");

        c.gridwidth = 1;
        add(new Label("End Frequency [HZ]"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(efreq = new TextField(15),c);
        efreq.setText("100e6");

        c.gridwidth = 1;
        add(new Label("Scaling"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(scl = new Choice(),c);
        scl.add("DECADE");
        scl.add("OCTAVE");
        scl.add("LINEAR");

        c.gridwidth = 1;
        add(new Label("Points/Scale"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(cnt = new TextField(15),c);
        cnt.setText("10");

        c.gridwidth = 1;
        add(new Label("Temperature(s)"),c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(temp = new TextField(15),c);
        temp.setText("27");

        c.gridwidth = 1;
        add(simulate = new Button("simulate AC"),c);
        simulate.addActionListener(this);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(cancel = new Button("cancel"),c);
        cancel.addActionListener(this);

        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(status = new TextField(30),c);
        status.setEditable(false);
        status.setText("Setup AC simulation...");

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
        if (cmd.equals("simulate AC")) {
            double ef, sf;
            int c;
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
                sf = Double.valueOf(sfreq.getText()).doubleValue();
            } catch (NumberFormatException ne) {
                status.setText("Start frequency wrong!");
                return;
            }
            try {
                ef = Double.valueOf(efreq.getText()).doubleValue();
            } catch (NumberFormatException ne) {
                status.setText("End frequency wrong!");
                return;
            }
            if ((ef<=0) || (sf<=0)) {
                status.setText("Start/End freq. negative or zero!");
                return;
            }
            if ((ef<sf) || (ef-sf)<10) {
                status.setText("End freq. (not much) bigger than start!");
                return;
            }
            try {
                c = Integer.valueOf(cnt.getText()).intValue();
            } catch (NumberFormatException ne) {
                status.setText("Points/Scale wrong!");
                return;
            }
            if ((c<2) || (c>50)) {
                status.setText("Points/Scale bigger/less than 50/2!");
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
            line = ".AC " + scl.getSelectedItem().substring(0, 3) + " " + cnt.getText() + " " + sfreq.getText() + " " + efreq.getText() + "\n";
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

    public static acPanel getInstance(Frame f, String netlist){
        if(instance == null){
            instance = new acPanel(f,netlist);
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
                if (line.indexOf("AC")>0)  {
                    // get the first element of the line (the instance name)
                    StringTokenizer sst = new StringTokenizer(line);
                    if (sst.hasMoreTokens()) {
                        // gotcha!
                        noSource = false;
                    }
                }
            }
        }
    }
}
