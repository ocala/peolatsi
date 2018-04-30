package wise.gui.panel;
// Source File Name:   viewPanel.java
//
//  Author: Wolfgang Scherr
//
//  $Id: viewPanel.java,v 1.2 2002/09/15 18:01:29 hoidain Exp $
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
import java.lang.*;
// StringTokenizer
import java.util.*;

// the ptolemy plotter PTPLOT
import ptolemy.plot.*;

/** class to setup a VIEW pane -
    It is responsible for showing wave data results graphically to the user. It needs PTPLOT
    as wave viewing class.
  */
public class viewPanel extends Dialog implements ActionListener, WindowListener
{
    TextArea data;
    Button cancel;

    public viewPanel(Frame f, String results)
    {
        // let the original class do everything needed...
        super(f, "WISE - CIRCUIT DATA");

        // needed for GridBagLayout
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        setLayout(new GridBagLayout());

        // this dialog grabs the input focus entirely
        setModal(true);

        // but we want to know when he wants to get rid of it (indirect close)...
        addWindowListener(this);

        // SETUP GUI (a list of labels and user entry facilities)
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(data = new TextArea(10,85),c);
        data.append(results);
        data.setEditable(false);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(cancel = new Button("cancel"),c);
        cancel.addActionListener(this);

        // give the stuff an optimal size
        pack();
    }

    /** This method is needed as this class is an action listener.
      * (this interface is implemented in this class)
      *
      * @param e       the user event to handle
      */
    public void actionPerformed(ActionEvent e) {
        String cmd = new String(e.getActionCommand());
        if (cmd.equals("cancel")) {
            dispose();
         } else {
            System.out.println("cmd '"+cmd+"' not known.");
         }
    }


    /** this generates a dummy return - all classes have it!
        the main class collects everything for simulation by calling this
      */
    public String toString() {
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
    /** just to satisfy window listener interface
      */
    }
    public void windowDeactivated(WindowEvent e) {
    }
    /** just to satisfy window listener interface
      */
    public void windowActivated(WindowEvent e) {
    }
}
