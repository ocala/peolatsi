package co.edu.udistrital.VirtualLabs.jschematic.comm;


/**
 *
 * @author Oscar E. Cala W.
 */
public abstract class AbstractStartProblem extends DorminMessage {

    private String problemName;

    public static String testString(){
        String m = "<message>"
                + "<verb>NotePropertySet</verb>"
                + "<properties>"
                + "<MessageType>StartProblem</MessageType>"
                + "<ProblemName>pol</ProblemName>"
                + "<transaction_id>T1ff2e0e8:12f1d183f03:-7ff3</transaction_id>"
                + "</properties></message>";
        return m;
    }

    @Override
    protected void extractDorminProperties() {
        setProblemName(properties.getChild("ProblemName").getTextNormalize());
    }

    /**
     * @return the problemName
     */
    public String getProblemName() {
        return problemName;
    }

    /**
     * @param problemName the problemName to set
     */
    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }
}