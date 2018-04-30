package wise.spice;

import java.io.Serializable;

public class spiceModels implements Serializable {
    int modelCount;
    String name[];
    String model[];
    
    public spiceModels() {
        //model[0]=".MODEL genericNPN NPN BF 20 RB 100 TF .1NS CJC 2PF";
        modelCount=7;
        name = new String[modelCount];
        model = new String[modelCount];
        name[0]="Q_BC548C";
        model[0]=".MODEL Q_BC548C NPN (IS = 4.679E-14 NF = 1.01 ISE = 2.642E-15 NE = 1.581 BF = 458.7 IKF = 0.1371 VAF = 52.64 NR = 1.019 ISC = 2.337E-14 NC = 1.164 BR = 11.57 IKR = 0.1144 VAR = 364.5 RB = 1 IRB = 1E-06 RBM = 1 RE = 0.2598 RC = 1 XTB = 0 EG = 1.11 XTI = 3 CJE = 1.229E-11 VJE = 0.5591 MJE = 0.3385 TF = 4.689E-10 XTF = 160 VTF = 2.828 ITF = 0.8842 PTF = 0 CJC = 4.42E-12 VJC = 0.1994 MJC = 0.2782 XCJC = 0.6193 TR = 1E-32 CJS = 0 VJS = 0.75 MJS = 0.333 FC = 0.7936)";
        name[1]="D_BAS16";
        model[1]=".MODEL D_BAS16 D (IS  =  2.7838E-09 N   =  1.8703 RS  =  1.3548 EG  =  1.0637 XTI =  1.5000 CJO =  0.6000E-12 VJ  =  0.2000 M   =  0.1000)";
        name[2]="Q_2N2901";
        model[2]=".MODEL Q_2N2901 NPN (BF=95 BR=38 IS=8.E-14 IKF=3.E-3)";
        name[3]="Q_BC107";
        model[3]=".MODEL Q_BC107 NPN (BF=150 BR=71 IS=5.1E-15 VAF=278 TF=1.2E-8)";
        name[4]="M_generic_NMOS";
        model[4]=".MODEL M_generic_NMOS NMOS (LEVEL=3 VTO=1 GAMMA=0.6 CGSO=2E-10 CGDO=6E-10 CGBO=2E-10 CJ=0.0002 CJSW=4E-10 TOX=2E-8 XJ=3E-7 UO=500 THETA=0.4)";
        name[5]="M_generic_PMOS";
        model[5]=".MODEL M_generic_PMOS PMOS (LEVEL=3 VTO=-1 CGSO=6E-10 CGDO=6E-10 CGBO=6E-10 CJ=0.0002 CJSW=4E-10 TOX=2E-8 TPG=-1 XJ=3E-7 UO=150 THETA=0.4)";
     /*   name[6] = "deadZone";
        model[6] = ".subckt deadZone input output VT=1\n"
                + "R1 input n4 0\nS1 n4 n10 n4 n1 SWMOD_1 OFF\n.model SWMOD_1 SW (VT={VT} VH=0.1 RON=0.01 ROFF=1e9)\n"
                + "V2 n0 0 DC 0\n" + "V4 n2 0 DC 0\n" + "V5 n1 0 DC 0\n" +"R6 n10 n0 1e6\n"
                + "E7 n3 n2 n4 n2 -1\nS8 n3 n13 n3 n2 SWMOD_8 OFF\n"
                + ".model SWMOD_8 SW (VT={VT} VH=0.1 RON=0.01 ROFF=1e9)\nR9 n13 n2 1e6\n"
                + "E10 n8 n5 n7 n6 1\nR11 n6 n5 2e3\nR12 n7 n12 1e3\nR13 n6 n9 1e3\n"
                + "V14 n9 0 DC 0\nR15 n11 n7 1e3\nE17 n11 n0 n10 n0 -1\nE18 n12 n2 n13 n2 1\n"
                + "E19 n15 n14 n8 n14 -2 \nV20 n14 0 DC 0\nR2 n15 output 0\n.ends";*/
        name[6] = "deadZone";
        model[6] = ".subckt deadZone input output VT=1\n"
                + "E1 output 0 vol='V(input) > {VT} ? V(input):V(input) < -{VT} ? v(input) : 0'\n"
                + ".ends";
        
        //System.out.println("No. models available: "+modelCount);
    }

    public String modelList(String type) {
        int i;
        String s="*";
        for(i=0;i<modelCount;i++) {
            //System.out.println(name[i]);
            if (name[i].startsWith(type)) s = s + name[i] + " ";
        }
        return s;
    }

    @Override
    public String toString() {
        int i;
        String s="";
        for(i=0;i<modelCount;i++) {
            //System.out.println(model[i]);
            s = s + model[i] + "\n";
        }
        return s;
    }
}
