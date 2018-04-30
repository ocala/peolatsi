package wise.spice;

// Source File Name:   spiceReader.java
//
//  Author: Wolfgang Scherr
//
//  $Id: spiceReader.java,v 1.4 2003/10/12 12:11:44 hoidain Exp $
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
// this applet handles the menu and the different "results"
// from the schematic class: elements, probes (plot), analysis
//                           via separate forms, parameter (sweeps)
// it further launches simulations and keeps plot data in memory
// e.g. StringTokenizer
import java.util.*;
// e.g. PrintStream
import java.io.*;

// for Math stuff
import javax.swing.JOptionPane;

public class spiceReader implements Serializable {

    int waveCount = 0;
    String results = "";
    private String results2 = "";
    public Vector xVals = null;
    public Vector yVals = null;
    public Vector yiVals = null;
    // just for now...
    String xlabel, ylabel;
    private boolean waveReaded = false;

    public spiceReader(BufferedReader in) {
        String line;
        int i;
        boolean complex;
        Double xVal = null, yVal = null, yVali = null;

        xVals = new Vector();
        yVals = new Vector();
        yiVals = new Vector();



        line = getLine(in);
        results += line + "\n";

        //System.out.println("Data read: " + line);

        while (line != null) {
            while (line != null) {
                if (line.startsWith("Index")) {
                    break;
                }
                line = getLine(in);
                results += line + "\n";
            }

            // sorry, folks
            if (line == null) {
                //System.out.println("returnin cuz line is null line 71");
                if(!waveReaded){
                    JOptionPane.showMessageDialog(null, "An error has ocurred reading the simulation Results. Check your circuit and try again.");
                }else{
                    JOptionPane.showMessageDialog(null, "Simulation has Finished. Data is ready in the Plot Window.");
                }
                return;
            }

            // read labels
            StringTokenizer st = new StringTokenizer(line);
            if (st.hasMoreTokens()) {
                st.nextToken();  // ignore the index
            } else {
                JOptionPane.showMessageDialog(null, " Incorrect Format. It is expected to be a \"Index\" for the data.");
                return;
            }
            if (st.hasMoreTokens()) {
                xlabel = st.nextToken();
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Format. First Axis Label not found.");
                return;
            }
            if (xlabel.startsWith("freq")) {
                complex = true;
            } else {
                complex = false;
            }
            if (st.hasMoreTokens()) {
                ylabel = st.nextToken();
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Format. Y-Axis Label not found.");
                return;
            }
            //System.out.println("Label founds.");
            //System.out.println(xlabel + " " + ylabel);

            line = getLine(in); // this is a --- line
            results += line + "\n";
            line = getLine(in); // First line of Data

            Vector xv = new Vector();
            Vector yv = new Vector();
            Vector yiv = new Vector();

            xv.addElement(xlabel); // first element is the label
            if (complex) {
                yv.addElement(ylabel + " dB");
            } else {
                yv.addElement(ylabel);
            }
            yiv.addElement(ylabel + " phi");

            // read all values, stop if we reached the end of a table
            i = 0;
            while (line != null) {
                if (line.length() < 2) {
                    //System.out.println("Incorrect Format. Lines should have a lenght greater that 1.");
                    break;
                }
                String cleanLine = line.replace(',', ' ');            //for lines with imaginary parts, remove the colon
                StringTokenizer tok = new StringTokenizer(cleanLine);

                if (tok.hasMoreTokens()) {
                    tok.nextToken();  // ignore the index
                } else {
                    //System.out.println("Format Icorrect. Expencting the first token of a line data to be the index.");
                    break;
                }

                if (tok.hasMoreTokens()) {
                    xVal = new Double(tok.nextToken());
                } else {
                    //System.out.println("Format Incorrect. Extracting the x-axis data for this line has failed.");
                    break;
                }

           /*
                NGSPICE RETURNS FREQUENCY DATA AS REAL NUMBERS ONLY. NOT IMAGINARY PART THERE EXIST.
                if (complex) {//for signals with imaginary parts
                    if (tok.hasMoreTokens()) {
                        tok.nextToken(); // ignore imaginary frequency part
                    } else {
                        //System.out.println("Format Incorrect. X-axis Signal is complex but imaginary parts does'nt exist.");
                        break;
                    }
                } */
                
                if (tok.hasMoreTokens()) {
                    yVal = new Double(tok.nextToken());
                } else {
                    //System.out.println("Format Incorrect. Extracting the y-axis data for this line has failed.");
                    break;
                }
                
                
                if (complex) {
                    if (tok.hasMoreTokens()) {
                        yVali = new Double(tok.nextToken());
                    } else {
                        //System.out.println("Breaking because there are not more tokens at line 153");
                        break;
                    }
                }

                xv.addElement(xVal);
                
                if (complex) {
                    yv.addElement(new Double(20 * Math.log(
                            Math.sqrt(yVal.doubleValue() * yVal.doubleValue()
                            + yVali.doubleValue() * yVali.doubleValue())) / Math.log(10)));
                    if (yVal.doubleValue() < 0) {
                        yiv.addElement(new Double(180 * Math.atan(yVali.doubleValue() / yVal.doubleValue())
                                / Math.PI + 180));
                    } else {
                        yiv.addElement(new Double(180 * Math.atan(yVali.doubleValue() / yVal.doubleValue())
                                / Math.PI));
                    }
                    results += xVal + "\t" + yVal + "\t" + yVali + "\n";
                } else {
                    yv.addElement(yVal);
                    results += xVal + "\t" + yVal + "\n";
                }

                i++;
                line = getLine(in);
            }

            xVals.addElement(xv);
            yVals.addElement(yv);
            waveCount++;

            if (complex) {
                xVals.addElement(xv);
                yVals.addElement(yiv);
                waveCount++;
                results += "(" + i + " complex values)\n";
            } else {
                results += "(" + i + " values)\n";
            }
            //System.out.println("Wave processed, " + i + " values.");
            waveReaded = true;
        }
        //System.out.println("reading loop breaked or finished");
    }

    // internal line reader function
    final String getLine(BufferedReader in) {
        try {
            //boolean waiting = true;
            //while (waiting) {
            if (in.ready()) {
                String l = in.readLine();
                results2 += l + "\n";
                //System.out.println(l);
                return l;
            } else {
                //System.out.println("InputStream is not Ready. Its possible the stream is empty.");
                // waiting = true;
                // Thread.sleep(100);
                return null;
            }
            //}
        } catch (IOException e) {
            //System.out.println("IOException Reading de InputStream:" + e.getMessage());
            return null;
        }
        /*catch (InterruptedException e1) {
        //System.out.println("InterruptedException Waiting for InputStream:" + e1.getMessage());
        return null;
        }*/
        //return null;
    }

    // returns the data previously read (or null)
    @Override
    public String toString() {
        return results;
    }

    public String getResult2() {
        return results2;
    }

    /**
     * @return the results2
     */
    public String getResults2() {
        return results2;
    }

    /**
     * @param results2 the results2 to set
     */
    public void setResults2(String results2) {
        this.results2 = results2;
    }
}
