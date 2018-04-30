/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.udistrital.VirtualLabs.jschematic.comm;

import org.jdom.Document;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public abstract class AbstractCorrectAction extends AbstractInterfaceAction {
    
    protected static boolean trackNextFeedback = true;

    @Override
    public final void init(Document doc){
        msg = doc;
        if(trackNextFeedback){
            parseMessage();
        }
    }

    public static void setTrackNextFeedback(boolean track) {
        trackNextFeedback = track;
    }

    public final void dispatch() {
        if(trackNextFeedback){
            _dispatch();
        }else{
            doNotDispatch();
            trackNextFeedback = true;
        }
    }

    abstract public void _dispatch();

    public final void doNotDispatch(){}
    
}
