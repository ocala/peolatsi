/*
 * TutorTemplate.java
 *
 * Created on April 27, 2011, 5:33 PM
 * @autor Oscar E. Cala W. ocala@udistrital.edu.co
 */
package example;


import co.edu.udistrital.VirtualLabs.jschematic.VLJschematic;
import co.edu.udistrital.VirtualLabs.jschematic.VLSchematicBoard;
import co.edu.udistrital.VirtualLabs.jschematic.messages.AuthorModeChange;
import edu.cmu.pact.BehaviorRecorder.Controller.CTAT_Launcher;
import edu.cmu.pact.SocketProxy.SocketToolProxy;
import edu.cmu.pact.SocketProxy.VLJToolProxy;
import java.awt.EventQueue;
import pact.DorminWidgets.UniversalToolProxy;

/**
 * A template for Java-language student interfaces. For use with TutorTemplate.form and NetBeans v6.x.
 * @author  Oscar E. Cala W.
 */
public class NewVirtualLabsTutorTemplate extends javax.swing.JPanel {

    private boolean showBR = true;
    
    /** Creates new form TutorTemplate */
    public NewVirtualLabsTutorTemplate(boolean showbr) {
         showBR = showbr;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cTAT_Options1 = new edu.cmu.pact.BehaviorRecorder.Controller.CTAT_Options();
        dorminComboBox1 = new pact.DorminWidgets.DorminComboBox();
        jLabel11 = new javax.swing.JLabel();
        dorminComboBox2 = new pact.DorminWidgets.DorminComboBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        dorminTextField3 = new pact.DorminWidgets.DorminTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        dorminTextField6 = new pact.DorminWidgets.DorminTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        dorminLabel1 = new pact.DorminWidgets.DorminLabel();
        dorminTextField1 = new pact.DorminWidgets.DorminTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        dorminTextField2 = new pact.DorminWidgets.DorminTextField();
        dorminLabel4 = new pact.DorminWidgets.DorminLabel();
        jPanel4 = new javax.swing.JPanel();
        dorminTextField7 = new pact.DorminWidgets.DorminTextField();
        jLabel2 = new javax.swing.JLabel();
        dorminTextField8 = new pact.DorminWidgets.DorminTextField();
        jLabel14 = new javax.swing.JLabel();
        dorminTextField9 = new pact.DorminWidgets.DorminTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        dorminTextField4 = new pact.DorminWidgets.DorminTextField();
        dorminTextField5 = new pact.DorminWidgets.DorminTextField();
        horizontalLine1 = new pact.DorminWidgets.HorizontalLine();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        dorminTextField10 = new pact.DorminWidgets.DorminTextField();
        jLabel17 = new javax.swing.JLabel();
        dorminTextField11 = new pact.DorminWidgets.DorminTextField();
        jLabel18 = new javax.swing.JLabel();
        dorminTextField12 = new pact.DorminWidgets.DorminTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        dorminTextField13 = new pact.DorminWidgets.DorminTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        dorminTextField14 = new pact.DorminWidgets.DorminTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        dorminTextField15 = new pact.DorminWidgets.DorminTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        dorminTextField16 = new pact.DorminWidgets.DorminTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        dorminTextField21 = new pact.DorminWidgets.DorminTextField();
        jLabel38 = new javax.swing.JLabel();
        dorminTextField22 = new pact.DorminWidgets.DorminTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        dorminTextField18 = new pact.DorminWidgets.DorminTextField();
        horizontalLine2 = new pact.DorminWidgets.HorizontalLine();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        dorminTextField19 = new pact.DorminWidgets.DorminTextField();
        dorminTextField20 = new pact.DorminWidgets.DorminTextField();
        dorminTextField23 = new pact.DorminWidgets.DorminTextField();
        dorminTextField24 = new pact.DorminWidgets.DorminTextField();
        jLabel34 = new javax.swing.JLabel();
        dorminLabel2 = new pact.DorminWidgets.DorminLabel();

        cTAT_Options1.setShowBehaviorRecorder(showBR);

        setLayout(null);

        dorminComboBox1.setToolTipText("Seleccione el Orden del Sistema");
        dorminComboBox1.setValues("Seleccione una opción,Primer Orden,Segundo Orden,Orden Superior");
        dorminComboBox1.addStudentActionListener(new pact.DorminWidgets.event.StudentActionListener() {
            public void studentActionPerformed(pact.DorminWidgets.event.StudentActionEvent evt) {
                dorminComboBox1StudentActionPerformed(evt);
            }
        });
        add(dorminComboBox1);
        dorminComboBox1.setBounds(240, 10, 150, 20);

        jLabel11.setText("Tipo de Sistema");
        add(jLabel11);
        jLabel11.setBounds(20, 10, 110, 20);

        dorminComboBox2.setValues("Seleccione una opción,Lineal,Semi-Lineal");
        dorminComboBox2.addStudentActionListener(new pact.DorminWidgets.event.StudentActionListener() {
            public void studentActionPerformed(pact.DorminWidgets.event.StudentActionEvent evt) {
                dorminComboBox2StudentActionPerformed(evt);
            }
        });
        add(dorminComboBox2);
        dorminComboBox2.setBounds(240, 50, 150, 20);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Sistema de Primer Orden"));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(dorminTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, 60, 20));

        jLabel3.setText("Tiempo para alcanzar del 50% de la Salida");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 210, 40));

        jLabel8.setText("Valor correspondiente al 50% de la Salida");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 210, 40));
        jPanel3.add(dorminTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 60, -1));

        jLabel9.setText("Voltios");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 90, 70, 24));

        jLabel10.setText("Segundos");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, 70, 24));

        dorminLabel1.setText("Voltaje de Excitación de la Planta");
        jPanel3.add(dorminLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 190, 20));
        jPanel3.add(dorminTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 60, -1));

        jLabel1.setText("Voltios");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, 50, 20));

        jLabel12.setText("Voltios");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 50, 20));
        jPanel3.add(dorminTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 60, -1));

        dorminLabel4.setText("Salida en estado estable");
        jPanel3.add(dorminLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 140, 20));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 420, 120));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("No linealidades fuertes"));

        jLabel2.setText("Voltios");

        jLabel14.setText("Segundos");

        jLabel16.setText("Voltios");

        jLabel7.setText("Saturación");

        jLabel13.setText("Tiempo muerto");

        jLabel15.setText("Zona Muerta");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dorminTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(dorminTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dorminTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(dorminTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dorminTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dorminTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)))
        );

        jPanel4.setVisible(false);

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 420, 130));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Funcion de Transferencia"));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setText("G(s) =");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 51, 40, 20));
        jPanel2.add(dorminTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 29, 30));
        jPanel2.add(dorminTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 30, 30));
        jPanel2.add(horizontalLine1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 100, 10));

        jLabel6.setText("S");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 77, 12, 20));

        jLabel5.setText("1     +  ");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 74, 40, 20));

        jPanel2.setVisible(true);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 310, 180, 100));

        jPanel1.setVisible(false);

        add(jPanel1);
        jPanel1.setBounds(0, 90, 440, 420);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Sistema de Segundo Orden"));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("No linealidades fuertes"));

        jLabel17.setText("Voltios");

        jLabel18.setText("Segundos");

        jLabel19.setText("Voltios");

        jLabel20.setText("Saturación");

        jLabel21.setText("Tiempo muerto");

        jLabel22.setText("Zona Muerta");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dorminTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dorminTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dorminTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(dorminTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(dorminTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22)
                    .addComponent(dorminTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel6.setVisible(false);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Caracterización del Sistema"));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(dorminTextField13, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 50, 20));

        jLabel23.setText("Tiempo de Subida");
        jPanel7.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 64, 140, 20));

        jLabel24.setText("Tiempo Pico");
        jPanel7.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 64, 110, 20));
        jPanel7.add(dorminTextField14, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 90, 50, -1));

        jLabel25.setText("Sec");
        jPanel7.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, 50, 24));

        jLabel26.setText("Voltios");
        jPanel7.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 160, 50, 30));
        jPanel7.add(dorminTextField15, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 60, -1));

        jLabel27.setText("Voltios");
        jPanel7.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 50, 20));

        jLabel28.setText("Sec");
        jPanel7.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 40, 20));
        jPanel7.add(dorminTextField16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 50, -1));

        jLabel35.setText("Overshoot (Mp)");
        jPanel7.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 150, 20));

        jLabel36.setText("Tiempo Establecimiento");
        jPanel7.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, 150, 20));

        jLabel37.setText("Voltaje Alimentación");
        jPanel7.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 130, 20));
        jPanel7.add(dorminTextField21, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 50, -1));

        jLabel38.setText("Sec");
        jPanel7.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 50, 24));
        jPanel7.add(dorminTextField22, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, 50, -1));

        jLabel39.setText("Voltios");
        jPanel7.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, 50, 30));

        jLabel40.setText("Salida en estado Estable");
        jPanel7.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 140, 20));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Funcion de Transferencia"));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel29.setText("G(s) =");
        jPanel8.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 51, 40, 20));
        jPanel8.add(dorminTextField18, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 70, 30, 30));
        jPanel8.add(horizontalLine2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 240, 10));

        jLabel30.setText("S");
        jPanel8.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, 10, 30));

        jLabel31.setText("1     +    2 ");
        jPanel8.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 70, 30));

        jLabel32.setText("+");
        jPanel8.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 74, 40, 20));
        jPanel8.add(dorminTextField19, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 30, 30));
        jPanel8.add(dorminTextField20, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 30, 30));
        jPanel8.add(dorminTextField23, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 40, 40));
        jPanel8.add(dorminTextField24, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 29, 30));

        jPanel2.setVisible(true);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel5.setVisible(false);

        add(jPanel5);
        jPanel5.setBounds(0, 90, 440, 490);

        jLabel34.setText("Orden del sistema");
        add(jLabel34);
        jLabel34.setBounds(20, 50, 100, 20);
        add(dorminLabel2);
        dorminLabel2.setBounds(150, -90, 58, 14);
    }// </editor-fold>//GEN-END:initComponents

    private void dorminComboBox1StudentActionPerformed(pact.DorminWidgets.event.StudentActionEvent evt) {//GEN-FIRST:event_dorminComboBox1StudentActionPerformed
        // TODO add your handling code here:
        if(dorminComboBox1.getValue().equals("Primer Orden")){
            jPanel1.setVisible(true);
            jPanel5.setVisible(false);
        }else if(dorminComboBox1.getValue().equals("Segundo Orden")){
            jPanel5.setVisible(true);
            jPanel1.setVisible(false);
        }
        else{
            jPanel1.setVisible(false);
            jPanel5.setVisible(false);
        }
    }//GEN-LAST:event_dorminComboBox1StudentActionPerformed

    private void dorminComboBox2StudentActionPerformed(pact.DorminWidgets.event.StudentActionEvent evt) {//GEN-FIRST:event_dorminComboBox2StudentActionPerformed
        
        if(dorminComboBox2.getValue().equals("Semi-Lineal")){
            jPanel4.setVisible(true);
            jPanel6.setVisible(true);
        }else{
            jPanel4.setVisible(false);
            jPanel6.setVisible(false);
        }       
    }//GEN-LAST:event_dorminComboBox2StudentActionPerformed

    public static void main(final String[] argv) {

        EventQueue.invokeLater(
                new Runnable() {

                    public void run() {
                        String argc[] = {"-spClientHost",
                            "localhost", "-spServerPort", "1502", "-spMsgFormat", "1", "-spEOM", "0",
                            "-spUseSingleSocket", "true"};
                        
                        
                        String brdURL = System.getProperty("brdURL");
                        boolean sb = true;
                        if (brdURL != null) {
                            sb = false;
                        }
                        
                        NewVirtualLabsTutorTemplate t = new NewVirtualLabsTutorTemplate(sb);
                        
                        CTAT_Launcher laun = new CTAT_Launcher(argc);
                        //se crea un UTP y se inicializa
                        //se crea un SkP y se inicia lo que reemplaza el UTP por un SkTP

                        SocketToolProxy sktp = (SocketToolProxy) laun.getController().getUniversalToolProxy();                        
                        laun.launch(t);
                        VLJToolProxy utp2 = new VLJToolProxy(sktp);
                        utp2.init(laun.getController());                        
                        laun.getController().setUniversalToolProxy(utp2);
                        
                        if (brdURL != null) {
                            laun.getController().openBRFromURL(brdURL);
                            AuthorModeChange.setTestTutorMode();
                        }
                    }
                });

        EventQueue.invokeLater(
                new Runnable() {
                    public void run() {
                        VLJschematic me = new VLJschematic();
                        VLSchematicBoard.sendMessage("", "", "", "");
                    }
                });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private edu.cmu.pact.BehaviorRecorder.Controller.CTAT_Options cTAT_Options1;
    private pact.DorminWidgets.DorminComboBox dorminComboBox1;
    private pact.DorminWidgets.DorminComboBox dorminComboBox2;
    private pact.DorminWidgets.DorminLabel dorminLabel1;
    private pact.DorminWidgets.DorminLabel dorminLabel2;
    private pact.DorminWidgets.DorminLabel dorminLabel4;
    private pact.DorminWidgets.DorminTextField dorminTextField1;
    private pact.DorminWidgets.DorminTextField dorminTextField10;
    private pact.DorminWidgets.DorminTextField dorminTextField11;
    private pact.DorminWidgets.DorminTextField dorminTextField12;
    private pact.DorminWidgets.DorminTextField dorminTextField13;
    private pact.DorminWidgets.DorminTextField dorminTextField14;
    private pact.DorminWidgets.DorminTextField dorminTextField15;
    private pact.DorminWidgets.DorminTextField dorminTextField16;
    private pact.DorminWidgets.DorminTextField dorminTextField18;
    private pact.DorminWidgets.DorminTextField dorminTextField19;
    private pact.DorminWidgets.DorminTextField dorminTextField2;
    private pact.DorminWidgets.DorminTextField dorminTextField20;
    private pact.DorminWidgets.DorminTextField dorminTextField21;
    private pact.DorminWidgets.DorminTextField dorminTextField22;
    private pact.DorminWidgets.DorminTextField dorminTextField23;
    private pact.DorminWidgets.DorminTextField dorminTextField24;
    private pact.DorminWidgets.DorminTextField dorminTextField3;
    private pact.DorminWidgets.DorminTextField dorminTextField4;
    private pact.DorminWidgets.DorminTextField dorminTextField5;
    private pact.DorminWidgets.DorminTextField dorminTextField6;
    private pact.DorminWidgets.DorminTextField dorminTextField7;
    private pact.DorminWidgets.DorminTextField dorminTextField8;
    private pact.DorminWidgets.DorminTextField dorminTextField9;
    private pact.DorminWidgets.HorizontalLine horizontalLine1;
    private pact.DorminWidgets.HorizontalLine horizontalLine2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    // End of variables declaration//GEN-END:variables
}
