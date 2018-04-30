/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udistrital.VirtualLabs.jschematic;

import co.edu.udistrital.VirtualLabs.jschematic.comm.DorminSocketInterface;
import co.edu.udistrital.VirtualLabs.jschematic.messages.InterfaceAction;
import edu.cmu.old_pact.dormin.MessageObject;
import edu.cmu.pact.BehaviorRecorder.Controller.BR_Controller;
import edu.cmu.pact.SocketProxy.SocketToolProxy;
import wise.app.jschematic;



import wise.spice.spiceReader;
import wise.gui.panel.acPanel;
import wise.gui.panel.viewPanel;
import wise.gui.panel.plotPanel;
import wise.gui.panel.dcPanel;
import wise.gui.panel.tranPanel;
// graphics stuff e.g. Button
import java.awt.*;
// e.g. ActionListener
import java.awt.event.*;
// e.g. URLConnection, URL
import java.net.*;
// e.g. PrintStream
import java.io.*;
import java.util.Vector;
import javax.swing.JOptionPane;
import pact.DorminWidgets.UniversalToolProxy;

/**
 *
 * Esta clase representa la extensión de la aplicación WISE. Mantiene
 * las funcionalidades iniciales del software y realiza extensiones para proveer
 * notificación de los eventos realizados en la interfaz gráfica hacia el tutor
 * e implmenenta funcionalidades adicionales para mejorar el proceso de resolución
 * deun problema.
 *
 * @author Oscar
 */
public class VLJschematic extends jschematic {

    private MenuItem zoomIn;
    private MenuItem zoomOut;
    private VLSchematicBoard schem2;
    private MenuItem stopNotificacions;

    /**
     *
     * Crea el objeto padre, configura la instancia como una instancia de
     * aplicacion, inicializa y ejecuta.
     *
     * @since 0.0.1     
     * @see jschematic
     */
    public VLJschematic() {
        //crea la clase padre Applet
        super();
        //Habilita las funcionalidades de Aplicacion standalone
        this.setApplication(true);
        // LLama a la funcion Init de la clase padre
        init();

        run();
    }

    @Override
    /**
     * This is called by any master (e.g. a browser) to init the applet.
     * (there we set up the gui)
     *
     * Here is readed the preload file path.
     *
     * All listeners aare assigned in this method.
     *
     * WindowListener is this.
     *
     * Action performed - capable elements are routed to the menuAction  Method
     * 
     */
    public void createFrame() {
        int i;

        // we don't go further if a window already exists!
        if (frame != null) {
            frame.show();
            return;
        }

        // we read the parameter PRELOAD of the applet call - if it is valid,
        // we load the netlist and plot list from there
        String preload;
        if (application) {
            /**
             * @todo implement preload for application
             */
            preload = System.getProperty("jschematic.preloadURL");//"file:///C:/test.gra";
        } else {
            preload = getParameter("PRELOAD");
            if (preload != null) {
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
            fileMenu = new Menu("File", false);
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

        editMenu = new Menu("Edit", false);
        schemMenu.add(editMenu);
        compMenu = new Menu("Components", true);
        schemMenu.add(compMenu);
        simMenu = new Menu("Simulate", true);
        schemMenu.add(simMenu);
        viewMenu = new Menu("View", false);
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
        zoomIn = new MenuItem("Zoom In");
        zoomIn.addActionListener(listener);
        editMenu.add(zoomIn);
        zoomOut = new MenuItem("Zoom Out");
        zoomOut.addActionListener(listener);
        editMenu.add(zoomOut);
        stopNotificacions = new MenuItem("Stop Notifications");
        stopNotificacions.addActionListener(listener);
        editMenu.add(stopNotificacions);

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
        compMenu.add(new MenuItem());
        addComp[13] = new MenuItem("S-Switch");
        compMenu.add(addComp[13]);
        addComp[14] = new MenuItem("Transfer Function");
        compMenu.add(addComp[14]);
        addComp[15] = new MenuItem("Non-linear Transfer Function");
        compMenu.add(addComp[15]);
        for (i = 0; i < addComp.length; i++) {
            if (addComp[i] != null) {
                addComp[i].addActionListener(listener);
            }
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
        showCode = new MenuItem("Send DONE Signal");
        showCode.addActionListener(listener);
        viewMenu.add(showCode);
        MenuItem reqHint = new MenuItem("Request Hint");
        reqHint.addActionListener(listener);
        viewMenu.add(reqHint);


        // this sets up the rest of the applet
        // - the schematic class
        // for debugging - TODO: replace later
        schemPan = new Panel();
        schem = VLSchematicBoard.getInstance(250, 200, 4, preload);
        schem2 = (VLSchematicBoard) schem;
        schemPan.add(schem);
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
        frame.add(sp, c);
        //frame.add(schemPan,c);
        c.weighty = 0.0;
        frame.add(statField, c);
        // and tie the menu to our frame, then pack everything to
        // optimal size
        frame.setMenuBar(schemMenu);
        //frame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) screenSize.getWidth() / 2 , (int) screenSize.getHeight() - 100);

        frame.show();

        statField.setText("Status: ok");
        schem2.zoomIn();
        schem2.zoomIn();
        schem2.repaint();


    }

    /**
     * TODO: support java property for preload command in standalone version.
     * @return the preload Param that was passed in the applet or in the command line.
     */
    public String getPreloadParam() {
        String preload;
        if (application) {
            preload = "";
        } else {
            preload = getParameter("PRELOAD");
            if (preload != null) {
                // we have an absolute path but no protocol/server - add it!
                if (preload.startsWith("/")) {
                    preload = getCodeBase().getProtocol()
                            + "://" + getCodeBase().getHost() + preload;
                }
                // we have a relative path
                if (!preload.startsWith("http:")) {
                    preload = getCodeBase() + preload;
                }
            }
        }
        return preload;
    }

    /**
     *
     * @param isStandAloneApp whether init application as applet or desktop
     */
    protected final void setApplication(boolean isStandAloneApp) {
        application = isStandAloneApp;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
    }

    /** handles the menu actions, we may separate this later....
     * @param event
     * @return
     */
    @Override
    public boolean menuAction(ActionEvent event) {
        // this is the command string

        String cmd = event.getActionCommand();
        int i;

        // this is only possible in an application
        if (cmd.equals("Save code")) {
            FileDialog fd = new FileDialog(frame, "Save schematic code",
                    FileDialog.SAVE);
            //fd.setFilenameFilter(new schemFilter());
            //fd.setDirectory("/home/wolfi/neu/circuits");
            fd.show();
            if ((fd.getFile() == null) || (fd.getFile().equals(""))) {
                return false;
            }
            String file = fd.getDirectory() + fd.getFile();
            String data = schem2.getGraData();
            if (!file.endsWith(".gra")) {
                file += ".gra";
            }
            try {
                DataOutputStream out = new DataOutputStream(
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
            FileDialog fd = new FileDialog(frame, "Load schematic code",
                    FileDialog.LOAD);

            //fd.setFilenameFilter(new schemFilter());
            //fd.setDirectory("/home/wolfi/neu/circuits");
            fd.show();
            String file = fd.getFile();
            if (file == null) {
                return false;
            }
            boolean ok = schem2.load("file:" + fd.getDirectory() + file);
            schem2.repaint();
            if (ok) {
                statField.setText("Status: loading finished successfully");
            } else {
                statField.setText("Status: load error - missing/wrong file");
            }
            return true;
        }
        if (cmd.equals("Exit")) {
            System.exit(0);
        }
        // this is only possible in an applet (just the button visible)
        if (schem == null) {
            if (cmd.equals("Start WISE")) {
                createFrame();
            }
            return true;
        }
        // this is always possible
        if (cmd.equals("delete element")) {
            schem2.deleteSelected();
            return true;
        }
        if (cmd.equals("edit properties")) {
            schem2.editSelected();
            return true;
        }
        if (cmd.equals("move element")) {
            schem2.moveSelected();
            return true;
        }
        if (cmd.equals("rotate element")) {
            schem2.rotateSelected();
            return true;
        }
        if (cmd.equals("close schematic")) {
            frame.hide();
            frame.dispose();
            schem = null;
            frame = null;
            if (application) {
                System.exit(0);
            }
            return true;
        }
        //

        if (cmd.equals("Zoom In")) {
            schem2.zoomIn();
            schem2.repaint();
            return true;
        }
        if (cmd.equals("Zoom Out")) {
            schem2.zoomOut();
            schem2.repaint();

            return true;
        }

        if (cmd.equals("Stop Notifications") || cmd.equals("Start Notifications")) {
            DorminSocketInterface.enableSend = !DorminSocketInterface.enableSend;
            if (!DorminSocketInterface.enableSend) {
                stopNotificacions.setLabel("Start Notifications");
            } else {
                stopNotificacions.setLabel("Stop Notifications");
            }
            return true;
        }

        if (cmd.equals("Send DONE Signal")) {
            DorminSocketInterface.sendMessage(InterfaceAction.MESSAGE_TYPE, InterfaceAction.DONE_SELECTION, InterfaceAction.MENU_SELECTED, "-1");
            return true;
        }

        if (cmd.equals("Request Hint")) {
            DorminSocketInterface.sendMessage(InterfaceAction.MESSAGE_TYPE, InterfaceAction.HINT_SELECTION, InterfaceAction.MENU_SELECTED, "-1");
            return true;
        }
        if (cmd.equals("VIEW preloadable code")) {
            viewPanel vp;
            vp = new viewPanel(frame, schem2.getGraData());
            vp.show();
            return true;
        }
        if (cmd.equals("VIEW SPICE input data")) {
            viewPanel vp;
            vp = new viewPanel(frame, schem2.getNetlist() + schem2.getProbes());
            vp.show();
            return true;
        }
        if (cmd.equals("VIEW waveform results")) {
            plotPanel pp;
            pp = new plotPanel(frame, spiceData.toString(), spiceData.xVals, spiceData.yVals);
            pp.show();

            return true;
        }
        // ok, this is weird - TODO: cleanup the structure...

        // we check if an element needs to inserted - otherwise just
        // analysis commands are left.
        if (!schem2.addElement(cmd)) {
            // panels used here
            acPanel acp;
            dcPanel dcp;
            tranPanel trp;

            // resulting line and flag if simulation should be started
            String analysis = null;
            boolean doIt = false;
            int iter;

            // generate netlist here (grepping elements)!
            netlist = schem2.getNetlist();
            // generate plot lines here (grepping I/U measure elements)!
            plot = schem2.getProbes();
            // TODO: fetch all parameter sweeps here!
            String netlist2 = netlist + plot;
            if (cmd.equals("AC")) {
                acp = acPanel.getInstance(frame, netlist2);
                if (acp.noSource) {
                    statField.setText("Status: AC run without input source is useless!");
                    JOptionPane.showMessageDialog(frame, statField.getText(), "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                acp.show();
                if (acp.dataValid) {
                    analysis = acp.toString();
                    statField.setText("Status: SPICE line: '" + analysis + "'");
                } else {
                    statField.setText("Status: Not valid data for the simulation.");
                    //JOptionPane.showMessageDialog(frame,statField.getText(),"Error",JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                acp.resetValidations();
            } else if (cmd.equals("DC")) {
                dcp = dcPanel.getInstance(frame, netlist2);
                if (dcp.noSource) {
                    statField.setText("Status: DC run without sweep source is not possible!");
                    JOptionPane.showMessageDialog(frame, statField.getText(), "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                dcp.show();
                if (dcp.dataValid) {
                    analysis = dcp.toString();
                    statField.setText("Status: SPICE line: '" + analysis + "'");
                } else {
                    statField.setText("Status: Not valid data for the simulation.");
                    //JOptionPane.showMessageDialog(frame,statField.getText(),"Error",JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                dcp.resetValidations();
            } else if (cmd.equals("TRAN")) {
                trp = tranPanel.getInstance(frame, netlist2);
                trp.show();
                if (trp.dataValid) {
                    analysis = trp.toString();
                    statField.setText("Status: SPICE line: '" + analysis + "'");
                } else {
                    statField.setText("Status: Not valid data for the simulation.");
                    //JOptionPane.showMessageDialog(frame,statField.getText(),"Error",JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                trp.resetValidations();
            } else {
                //  menu item not covered - give a hint in status line
                statField.setText("Status: MENU - " + cmd + " action not supported.");
                return false;
            }
            // update plot statements for the analysis
            String specificplot = plot;
            for (iter = 0; iter < specificplot.length(); iter++) {
                int pos = specificplot.indexOf("**");
                if (pos > 0) {
                    specificplot = specificplot.substring(0, pos) + cmd + specificplot.substring(pos + 2, specificplot.length());
                }
            }

            // start simulation if needed
            if (true) { // just for easy disabling for now - TODO: parameter sweep loop and data collection
                //statField.setText("Status: trying iteration "+(iter++)+" ...");
                URL u = null;
                String line;
                URLConnection c = null;

                // remove enable from VIEW waveform button not to confuse the user with old data
                checkIt.setEnabled(false);

                // some stuff in here is deprecated - TODO: will be corrected later....
                // TODO: add application setup for simulation
                try {
                    String sim_server = null;
                    if (application) {
                        sim_server = System.getProperty("jschematic.sim_server");
                    } else {
                        sim_server = getParameter("SIM_SERVER");
                    }
                    // we can't simulate without a given server
                    if (sim_server == null) {
                        statField.setText("Status: no server - simulation aborted!");
                        return false;
                    }

                    // we have an absolute path but no protocol/server - add it!
                    if (sim_server.startsWith("/")) {
                        // This will fail in application MODE
                        sim_server = getCodeBase().getProtocol() + "://" + getCodeBase().getHost() + sim_server;
                    }
                    // we have a relative path
                    if (!sim_server.startsWith("http:")) {
                        // This will fail in application MODE
                        sim_server = getCodeBase() + sim_server;
                    }

                    System.out.println("Accessing: " + sim_server);

                    String proxyHost = System.getProperty("http.proxyHost");
                    
                    
                    u = new URL(sim_server);
                    if (proxyHost != null && !"".equals(proxyHost)) {
                        String port = System.getProperty("http.proxyPort");
                        int proxyPort = 3128;
                        try{
                         proxyPort = Integer.valueOf(proxyPort);
                        }catch(Exception e){
                            
                        }
                        SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
                        
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);                    
                        c = u.openConnection(proxy);
                    } else {
                        c = u.openConnection();
                    }
                    // we 'simulate' the simulation if a results text file is given!
                    if (sim_server.endsWith(".txt")) {
                        //DorminSocketInterface.sendMessage(InterfaceAction.MESSAGE_TYPE, InterfaceAction.SIMULATION_PANEL, "DemoDataLoaded", sim_server);
                        statField.setText("Status: only demo data file - predefined simulation!");
                        // wait 2 seconds to allow the status to be viewed (and its more realistic :-))
                        Thread.sleep(2000);
                    } else {
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
                        DorminSocketInterface.sendMessage(InterfaceAction.MESSAGE_TYPE, InterfaceAction.SIMULATION_PANEL, cmd + "_" + InterfaceAction.SIMULATION_LAUNCHED, analysis);
                    }
                    // now wait for the return data of the CGI
                    BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    statField.setText("Status: simulation Done, retrieving data from server....");
                    repaint();
                    while (!in.ready()) {
                        Thread.sleep(10);
                    }
                    statField.setText("Status: Data ready, Parsing data....");
                    spiceData = new spiceReader(in);
                    in.close();

                    // we got it!
                    if (sim_server.endsWith(".txt")) {
                        statField.setText("Status: demo data file loaded!");
                    } else {
                        statField.setText("Status: simulation finished!");
                    }
                    //JOptionPane.showMessageDialog(null,spiceData.getResult2());
                    checkIt.setEnabled(true);
                } catch (MalformedURLException mue) {
                    statField.setText("Status: Illegal URL - " + mue + " - '" + u + "'");
                    return false;
                } catch (IOException ioe) {
                    statField.setText("Status: IO error - " + ioe + " - '" + u + "'");
                    return false;
                } catch (Exception e) {
                    statField.setText("Status: Error - " + e);
                    return false;
                }
            }

            return true;
        }
        return false;
    }
}
