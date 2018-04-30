package wise.gui.panel;
// Source File Name:   plotPanel.java
//
//  Author: Wolfgang Scherr
//
//  $Id: plotPanel.java,v 1.1.1.1 2002/09/03 19:30:49 hoidain Exp $
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

// the ptolemy plotter PTPLOT
import ptolemy.plot.*;

//
// class to setup a VIEW pane
//
public class plotPanel extends Dialog implements ActionListener, WindowListener
{
    EditablePlot plotArea;
    TextArea data;
    Button cancel;

    public plotPanel(Frame f, String results, Vector xVal, Vector yVal)
    {
        // let the original class do everything needed...
        super(f, "WISE - SIMULATION RESULTS");

        plotArea = new EditablePlot();
        plotArea.setButtons(true);
        plotArea.setMarksStyle("none");
        plotArea.setImpulses(false);
        plotArea.setTitle("Simulation Data");
        plotArea.setMarksStyle("various");

        plotArea.addEditListener(new EditListener() {
            public void editDataModified(EditablePlot source, int dataset) {
                
            }
        });
        
        //plotArea.setYRange(-4,4);
        //plotArea.setXRange(0,100);
        //plotArea.addYTick("-PI", -Math.PI);
        //plotArea.addYTick("-PI/2", -Math.PI/2);
        //plotArea.addYTick("0",0);
        //plotArea.addYTick("PI/2", Math.PI/2);
        //plotArea.addYTick("PI", Math.PI);

        for (int j=0; j < xVal.size(); j++) {
            Vector xv = (Vector)xVal.elementAt(j);
            Vector yv = (Vector)yVal.elementAt(j);

            plotArea.setXLabel((String)xv.firstElement());

            if (((String)xv.firstElement()).startsWith("freq")) plotArea.setXLog(true);
            plotArea.addLegend(j,(String)yv.firstElement());

            boolean first = true;
            for (int i=1; i < xv.size(); i++) {
                plotArea.addPoint(j,((Double)xv.elementAt(i)).doubleValue(),
                        ((Double)yv.elementAt(i)).doubleValue(), !first);
                first = false;
            }
        }

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
        add(plotArea,c);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(data = new TextArea(10,85),c);
        data.append(results);
        //data.setEditable(false);
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        add(cancel = new Button("cancel"),c);
        cancel.addActionListener(this);

        // give the stuff an optimal size
        pack();
    }

    // this method is needed as this class is an action listener
    //  (this interface is implemented in this class)
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("cancel")) {
            dispose();
         } else {
            System.out.println("cmd '"+cmd+"' not known.");
         }
    }

    // this generates a dummy return - all classes have it!
    // the main class collects everything for simulation by calling this
    @Override
    public String toString() {
        return null;
    }

    // must be here to be an window listener (this interface is implemented in this class)
    // we want only know if it gets closed, the others are to satisfy the interface
    public void windowClosing(WindowEvent e) {
        dispose();
    }
    public void windowOpened(WindowEvent e) {
    }
    public void windowClosed(WindowEvent e) {
    }
    public void windowDeiconified(WindowEvent e) {
    }
    public void windowIconified(WindowEvent e) {
    }
    public void windowDeactivated(WindowEvent e) {
    }
    public void windowActivated(WindowEvent e) {
    }
}
