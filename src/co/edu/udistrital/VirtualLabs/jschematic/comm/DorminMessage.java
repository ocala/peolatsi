/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udistrital.VirtualLabs.jschematic.comm;

import org.jdom.Document;
import org.jdom.Element;

/**
 *
 * @author Administrador
 */
public abstract class DorminMessage implements ReadableDorminMessage {

    protected Document msg;
    protected Element root;
    protected Element properties;

    public void init(Document doc){
        msg = doc;
        parseMessage();
    }

    abstract public void dispatch();

    protected void parseMessage(){
        root = msg.getRootElement();
        properties = root.getChild("properties");
        extractDorminProperties();
        extractToolProperties();
    }

    protected abstract void extractDorminProperties();

    protected abstract void extractToolProperties();
}
