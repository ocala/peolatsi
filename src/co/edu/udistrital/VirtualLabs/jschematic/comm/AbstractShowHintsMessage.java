/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udistrital.VirtualLabs.jschematic.comm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public abstract class AbstractShowHintsMessage extends DorminMessage {

    protected ArrayList<String> hints = new ArrayList<String>();


    @Override
    protected void extractDorminProperties() {
        List listEl = properties.getChild("HintsMessage").getChildren("value");
        Iterator it = listEl.iterator();
        while(it.hasNext()){
            Element hint = (Element) it.next();
            String hintString = hint.getTextNormalize();
            hints.add(hintString);
        }
    }
}
