package wise.app;

// Source File Name:   jschematic.java
//
//  Author/Copyright: Wolfgang Scherr
//
//  $Id: jschematic.java,v 1.6 2002/10/09 19:38:21 hoidain Exp $
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

import wise.spice.spiceReader;
import wise.gui.schematic.schemBoard;
import wise.gui.panel.acPanel;
import wise.gui.panel.viewPanel;
import wise.gui.panel.plotPanel;
import wise.gui.panel.dcPanel;
import wise.gui.panel.tranPanel;
import java.applet.*;
// graphics stuff e.g. Button
import java.awt.*;
// e.g. ActionListener
import java.awt.event.*;
// e.g. URLConnection, URL
import java.net.*;
// e.g. PrintStream
import java.io.*;


/**
  *  This applet handles the menu and the different "results"
  *  for the schematic entry: elements, probes , analyses via separate forms
  *                           TODO: parameter (sweeps), more comments
  *  It further launches simulations and keeps plot data in memory
  *  for later viewing with ptplot.
  */
public class jschematic extends Applet
    implements Runnable, WindowListener
{

    // main window handles
    protected Panel schemPan;
    protected TextField statField;

    // menu handles
    protected MenuBar schemMenu;
    protected Menu compMenu, editMenu, simMenu, viewMenu;
    protected MenuItem addComp[] = new MenuItem[100];
    protected MenuItem rotEl, moveEl, delEl, changeEl, closeWin;
    protected MenuItem doAC, doDC, doTRAN;
    protected MenuItem showCode, checkIt, showSpice;
    // menu handles - only visible within application mode!
    protected Menu fileMenu;
    protected MenuItem loadCode,saveCode,exitApp;

    // a flag if we run as application
    // OCala - the protected keyord its neccesary for extending the class
    /**
     * 
     */
    protected boolean application = false;

    // this is the schematic board instantiation
    protected schemBoard schem;

    // this is the netlist and the plot data
    protected String netlist;
    protected String plot;

    // The startup button in the html page
    protected Button openWise;

    // we define a action listener class on the fly and
    // call in there a method of this class - this allows
    // later very easily to move this part somewhere outside
    // including the work done by the method call
    protected class myActionListener implements ActionListener, Serializable {
        public void actionPerformed(ActionEvent e) {
            menuAction(e);
        }
    }
    // The instance of the listener class - points to method "menuAction" in this class
    // - we use it also for the button...
   protected  myActionListener listener;

    // the frame used - integrating in the html page is not possible as some
    // browsers doesn't show the menu....
    protected Frame frame=null;

    // the own thread
    protected Thread me;
    // reaction speed, error msg.
    protected int speed;
    protected String ErrorMessage;

    // this one keeps the SPICE simulation results
    protected spiceReader spiceData;

    // let the parent class do what has to be done, we leave the
    // constructor alone
    public jschematic()
    {
        super();
    }

    // ====== APPLET BROWSER API START ======
    
    /** This is called by any master (e.g. a browser) to init the applet.
      * (there we set up the gui)
      */
    @Override
    public void init()
    {   listener = new myActionListener();

        // give some startup message (mostly not visible within browsers)

        System.out.println("WISE SchemBoard (c) 2002 by Wolfgang Scherr, 'scherr@net4you.at'.");

        // this is a trick to find the frame of the applet -
        // we need this to add a menu there
        //Object f = getParent ();
        //while (! (f instanceof Frame))
        //	f = ((Component) f).getParent ();
        //frame = (Frame) f; 
        // Some browser versions (e.g. iexplorer) doesn't show the menu...
        // we use a real frame and comment out above stuff!

        if (application) {
            // if we are not within a browser, we have to create our own frame!
            createFrame();
            return;
        }
        openWise = new Button("Start WISE");
        openWise.addActionListener(listener);
        this.add(openWise);

        this.setBackground(Color.black);
        this.setForeground(Color.red);

        // just some info output for debugging...
        System.out.println(getCodeBase());
        System.out.println(getCodeBase().getHost());
        System.out.println(getCodeBase().getProtocol());
    }

    /** This class is called by a master to get some information about this applet.
      */
    @Override
    public String getAppletInfo()
    {
        return "WISE SchemBoard (c) 2002 by Wolfgang Scherr, 'scherr@net4you.at'.";
    }

    /** With this the master tells the applet to start computations (here not needed).
      * (e.g. when the browser is deiconized, etc.)
      */
    @Override
    public void start()
    {
    }

    /** With this the master tells the applet to stop computations (here not needed).
      * (e.g. when the browser is iconized, etc.)
      */
    @Override
    public void stop()
    {
    }

    /** After init, this method is called by the master to activate the applet thread.
      */
    public void run()
    {
        while(me != null) 
        {
            try
            {
                Thread.sleep(speed);
            }
            catch(InterruptedException ex) {}
            repaint();
        }

    }

    // ====== APPLET BROWSER API END ======


    // INTERNAL METHODS 

    /** This is called by any master (e.g. a browser) to init the applet.
      * (there we set up the gui)
      */
    public void createFrame()
    {   int i;

        // we don't go further if a window already exists!
        if (frame != null) {
            frame.show();
            return;
        }

        // we read the parameter PRELOAD of the applet call - if it is valid,
        // we load the netlist and plot list from there
        String preload;
        if (application) {
            preload = new String("");
        } else {
            preload = getParameter("PRELOAD");
            if (preload!=null) {
                // we have an absolute path but no protocol/server - add it!
                if (preload.startsWith("/")) {
                    preload = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + preload;
                }
                // we have a relative path
                if (!preload.startsWith("http:")) {
                    preload = getCodeBase() + preload;
                }
            }
        }

        frame = new Frame("WISE simulation window");
        frame.addWindowListener(this);

        // setup menu, submenus etc. the 'true' when creating a menu
        // allows to tear off the complete menu bar later
        // the menuitems get also the previously defined listener which
        // is called when the user selects it
        schemMenu = new MenuBar();
        if (application) {
            fileMenu = new Menu("File",false);
            schemMenu.add(fileMenu);
            loadCode = new MenuItem("Load code");
            loadCode.addActionListener(listener);
            fileMenu.add(loadCode);
            saveCode = new MenuItem("Save code");
            saveCode.addActionListener(listener);
            fileMenu.add(saveCode);
            fileMenu.add(new MenuItem());
            exitApp = new MenuItem("Exit");
            exitApp.addActionListener(listener);
            fileMenu.add(exitApp);
        }

        editMenu = new Menu("Edit",false);
        schemMenu.add(editMenu);
        compMenu = new Menu("Components",true);
        schemMenu.add(compMenu);
        simMenu = new Menu("Simulate",true);
        schemMenu.add(simMenu);
        viewMenu = new Menu("View",false);
        schemMenu.add(viewMenu);

        changeEl = new MenuItem("edit properties");
        changeEl.addActionListener(listener);
        editMenu.add(changeEl);
        moveEl = new MenuItem("move element");
        moveEl.addActionListener(listener);
        editMenu.add(moveEl);
        rotEl = new MenuItem("rotate element");
        rotEl.addActionListener(listener);
        editMenu.add(rotEl);
        editMenu.add(new MenuItem());
        delEl = new MenuItem("delete element");
        delEl.addActionListener(listener);
        editMenu.add(delEl);
        editMenu.add(new MenuItem());
        closeWin = new MenuItem("close schematic");
        closeWin.addActionListener(listener);
        editMenu.add(closeWin);

        addComp[0] = new MenuItem("!-VProbe");
        compMenu.add(addComp[0]);
        addComp[1] = new MenuItem("/-IProbe");
        compMenu.add(addComp[1]);
        addComp[2] = new MenuItem("*-Ground");
        compMenu.add(addComp[2]);
        compMenu.add(new MenuItem());
        addComp[3] = new MenuItem("R-Resistor");
        compMenu.add(addComp[3]);
        addComp[4] = new MenuItem("C-Capacitor");
        compMenu.add(addComp[4]);
        addComp[5] = new MenuItem("L-Inductor");
        compMenu.add(addComp[5]);
        addComp[6] = new MenuItem("M-MOS Transistor");
        compMenu.add(addComp[6]);
        addComp[7] = new MenuItem("Q-Bipolar Transistor");
        compMenu.add(addComp[7]);
        addComp[8] = new MenuItem("J-JFET(ransistor)");
        compMenu.add(addComp[8]);
        addComp[9] = new MenuItem("D-Bipolar Diode");
        compMenu.add(addComp[9]);
        addComp[10] = new MenuItem("E-Ideal V-Amplifier");
        compMenu.add(addComp[10]);
        compMenu.add(new MenuItem());
        addComp[11] = new MenuItem("V-Voltage Source");
        compMenu.add(addComp[11]);
        addComp[12] = new MenuItem("I-Current Source");
        compMenu.add(addComp[12]);
        for(i=0;i<13;i++) {
            addComp[i].addActionListener(listener);
        }

        doAC = new MenuItem("AC");
        doAC.addActionListener(listener);
        simMenu.add(doAC);
        doDC = new MenuItem("DC");
        doDC.addActionListener(listener);
        simMenu.add(doDC);
        doTRAN = new MenuItem("TRAN");
        doTRAN.addActionListener(listener);
        simMenu.add(doTRAN);

        checkIt = new MenuItem("VIEW waveform results");
        checkIt.setEnabled(false);
        checkIt.addActionListener(listener);
        viewMenu.add(checkIt);
        showSpice = new MenuItem("VIEW SPICE input data");
        showSpice.addActionListener(listener);
        viewMenu.add(showSpice);
        showCode = new MenuItem("VIEW preloadable code");
        showCode.addActionListener(listener);
        viewMenu.add(showCode);

        // this sets up the rest of the applet
        // - the schematic class
        // for debugging - TODO: replace later
        schemPan = new Panel();
        //schemPan.add(schem = new schemBoard(35,25,10,preload));
        schemPan.add(schem = new schemBoard(150,100,10,preload));
        // - a status bar
        statField = new TextField("Status: --");
        statField.setEditable(false);

        // this constraints class is needed to add data to
        // a containter managed by a gridbag layout manager
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        
        // and this manager is set here
        frame.setLayout(new GridBagLayout());

        // now set the elements, one on top of the other
        ScrollPane sp = new ScrollPane();
        sp.add(schemPan);
        frame.add(sp,c);
        //frame.add(schemPan,c);
        c.weighty = 0.0;
        frame.add(statField,c);
        // and tie the menu to our frame, then pack everything to
        // optimal size
        frame.setMenuBar(schemMenu);
        //frame.pack();
        frame.setSize(400,300);

        frame.show();

        statField.setText("Status: ok");
    }

    /** handles the menu actions, we may separate this later....
     * @param event
     * @return
     */
    public boolean menuAction(ActionEvent event) {
        // this is the command string
        String cmd = new String(event.getActionCommand());
        int i;

        // this is only possible in an application 
        if (cmd.equals("Save code")) {
            FileDialog fd = new FileDialog(frame,"Save schematic code",
                                           FileDialog.SAVE);
            //fd.setFilenameFilter(new schemFilter());
            //fd.setDirectory("/home/wolfi/neu/circuits");
            fd.show();
            if ((fd.getFile()==null) || (fd.getFile().equals(""))) {
                return false;
            }
            String file = fd.getDirectory()+fd.getFile();
            String data = schem.getGraData();
            if (!file.endsWith(".gra")) {
                file += ".gra";
            }
            try {
                DataOutputStream out =  new DataOutputStream(
                                            new FileOutputStream(file));
                out.writeBytes(data);
                out.close();
            } catch (IOException e) {
                statField.setText("Status: save error - no permissions or no space left");
                return false;
            }
            statField.setText("Status: saving finished successfully");
            return true;
        }
        if (cmd.equals("Load code")) {
            FileDialog fd = new FileDialog(frame,"Load schematic code",
                                           FileDialog.LOAD);
            //fd.setFilenameFilter(new schemFilter());
            //fd.setDirectory("/home/wolfi/neu/circuits");
            fd.show();
            boolean ok = schem.load("file:"+fd.getDirectory()+fd.getFile());
            schem.repaint();
            if (ok) {
                statField.setText("Status: loading finished successfully");
            }
            else {
                statField.setText("Status: load error - missing/wrong file");
            }
            return true;
        }
        if (cmd.equals("Exit")) {
            System.exit(0);
        }
        // this is only possible in an applet (just the button visible)
        if (schem==null) {
            if (cmd.equals("Start WISE")) {
                createFrame();
            }
            return true;
        }
        // this is always possible
        if (cmd.equals("delete element")) {
            schem.deleteSelected();
            return true;
        }
        if (cmd.equals("edit properties")) {
            schem.editSelected();
            return true;
        }
        if (cmd.equals("move element")) {
            schem.moveSelected();
            return true;
        }
        if (cmd.equals("rotate element")) {
            schem.rotateSelected();
            return true;
        }
        if (cmd.equals("close schematic")) {
            frame.hide();
            frame.dispose();
            schem=null;
            frame=null;
            if (application) {
                System.exit(0);
            }
            return true;
        }
        if (cmd.equals("VIEW preloadable code")) {
            viewPanel vp;
            vp = new viewPanel(frame,schem.getGraData());
            vp.show();
            return true;
        } 
        if (cmd.equals("VIEW SPICE input data")) {
            viewPanel vp;
            vp = new viewPanel(frame,schem.getNetlist()+schem.getProbes());
            vp.show();
            return true;
        } 
        if (cmd.equals("VIEW waveform results")) {
            plotPanel pp;
            pp = new plotPanel(frame,spiceData.toString(),spiceData.xVals,spiceData.yVals);
            pp.show();
    
            return true;
        }
        // ok, this is weird - TODO: cleanup the structure...

        // we check if an element needs to inserted - otherwise just
        // analysis commands are left.
        if (!schem.addElement(cmd)) {
            // panels used here
            acPanel acp;
            dcPanel dcp;
            tranPanel trp;
    
            // resulting line and flag if simulation should be started
            String analysis = null;
            boolean doIt = false;
            int iter;
    
            // generate netlist here (grepping elements)!
            netlist = schem.getNetlist();
            // generate plot lines here (grepping I/U measure elements)!
            plot = schem.getProbes();
            // TODO: fetch all parameter sweeps here!
    
            if (cmd.equals("AC")) {
                acp = new acPanel(frame,netlist);
                if (acp.noSource) {
                    statField.setText("Status: AC run without input source is useless!");
                    return false;
                } 
                acp.show();
                if (acp.dataValid) {
                    analysis = acp.toString();
                    statField.setText("Status: SPICE line: '"+analysis+"'");
                } else {
                    return false;
                }
            } else
            if (cmd.equals("DC")) {
                dcp = new dcPanel(frame,netlist);
                if (dcp.noSource) {
                    statField.setText("Status: DC run without sweep source is not possible!");
                    return false;
                } 
                dcp.show();
                if (dcp.dataValid) {
                    analysis = dcp.toString();
                    statField.setText("Status: SPICE line: '"+analysis+"'");
                } else {
                    return false;
                }
            } else
            if (cmd.equals("TRAN")) {
                trp = new tranPanel(frame,netlist);
                trp.show();
                if (trp.dataValid) {
                    analysis = trp.toString();
                    statField.setText("Status: SPICE line: '"+analysis+"'");
                } else {
                    return false;
                }
            } else {
                //  menu item not covered - give a hint in status line
                statField.setText("Status: MENU - "+cmd);
                return false;
            }
    
            // update plot statements for the analysis
            String specificplot = new String(plot);
            for (iter=0; iter<specificplot.length(); iter++) {
                int pos = specificplot.indexOf("**");
                if (pos>0) {
                    specificplot = new String(specificplot.substring(0,pos)+cmd+
                                              specificplot.substring(pos+2,specificplot.length()));
                }
            }
    
            // start simulation if needed
            if (true) { // just for easy disabling for now - TODO: parameter sweep loop and data collection
                //statField.setText("Status: trying iteration "+(iter++)+" ...");
                URL u=null;
                String line;
    
                // remove enable from VIEW waveform button not to confuse the user with old data
                checkIt.setEnabled(false);

                // some stuff in here is deprecated - TODO: will be corrected later....
                // TODO: add application setup for simulation
                try {
                    String sim_server=null;
                    if (application) {
                        sim_server = System.getProperty("jschematic.sim_server");
                    } else {
                        sim_server = getParameter("SIM_SERVER");
                    }
                    // we can't simulate without a given server
                    if (sim_server==null) { 
                        statField.setText("Status: no server - simulation aborted!");
                        return false;
                    }

                    // we have an absolute path but no protocol/server - add it!
                    if (sim_server.startsWith("/")) {
                        sim_server = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + sim_server;
                    }
                    // we have a relative path
                    if (!sim_server.startsWith("http:")) {
                        sim_server = getCodeBase() + sim_server;
                    }

                    System.out.println("Accessing: "+sim_server);
                    u = new URL(sim_server);
                    URLConnection c = u.openConnection();
                    // we 'simulate' the simulation if a results text file is given!
                    if (sim_server.endsWith(".txt")) { 
                        statField.setText("Status: only demo data file - predefined simulation!");
                        // wait 2 seconds to allow the status to be viewed (and its more realistic :-))
                        Thread.sleep(2000);
                    }
                    else {
                        //u = new URL(getCodeBase().getProtocol()+"://"+getCodeBase().getHost()+
                        //            "/cgi-bin/cgitest.cgi");
                        c.setDoOutput(true);
                        // open a stream and sent POST data to CGI
                        PrintWriter out = new PrintWriter(c.getOutputStream());
                        out.print(netlist);
                        out.print(analysis);
                        out.print(specificplot);
                        out.close();
                        statField.setText("Status: simulation launched, waiting....");
                    }

                    // now wait for the return data of the CGI
                    //DataInputStream in = new DataInputStream(c.getInputStream());
                    BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));                   
                    statField.setText("Status: simulation launched, retrieving data....");
                    repaint();
                    spiceData = new spiceReader(in);
                    in.close();
        
                    // we got it!
                    if (sim_server.endsWith(".txt")) {
                        statField.setText("Status: demo data file loaded!");
                    }
                    else {
                        statField.setText("Status: simulation finished!");
                    }
                    checkIt.setEnabled(true);
                } catch (MalformedURLException mue) {
                    statField.setText("Status: Illegal URL - "+mue+" - '"+u+"'");
                    return false;
                } catch (IOException ioe) {
                    statField.setText("Status: IO error - "+ioe+" - '"+u+"'");
                    return false;
                } catch (Exception e) {
                    statField.setText("Status: Error - "+e);
                    return false;
                }
            }
    
            return true;
        }
        return false;
    }

    // INTERFACE METHODS (for the frame listener)

    // must be here to be an window listener (this interface is implemented in this class)
    // we want only know if it gets closed, the others are to satisfy the interface

    /** With this we detect that the window manager tries to close the window.
      * We dispose our frame here.
     * @param e
      */
    public void windowClosing(WindowEvent e) {
        frame.dispose();
        schem=null;
        frame=null;
        if (application) {
            System.exit(0);
        }
    }
    /** With this we detect that the window was opened (not used here).
     * @param e
      */
    public void windowOpened(WindowEvent e) {
    }
    /** With this we detect that the window was closed (not used here).
     * @param e
     */
    public void windowClosed(WindowEvent e) {
        schem=null;
        frame=null;
    }
    /** With this we detect that the window was deiconified (not used here).
     * @param e
     */
    public void windowDeiconified(WindowEvent e) {
    }
    /** With this we detect that the window was iconified (not used here).
     * @param e
     */
    public void windowIconified(WindowEvent e) {
    }
    /** With this we detect that the window was deactivated (not used here).
     * @param e
     */
    public void windowDeactivated(WindowEvent e) {
    }
    /** With this we detect that the window was activated (not used here).
     * @param e
     */
    public void windowActivated(WindowEvent e) {
    }

    /** With this we allow starting the applet as an application, too.
        THIS IS CURRENTLY NOT FULLY TESTED/SUPPORTED WITH SIMULATION!
     * @param argv
     */
    public static void main(String argv[]) {
        jschematic me = new jschematic();
        me.application=true;
        me.init();
        me.run();
    }
}