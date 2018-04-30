/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.udistrital.VirtualLabs.jschematic.comm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Oscar Eduardo Cala W
 */
public class SelectionActionInputKey {

    private String selection;
    private String action;
    private String input;

    public SelectionActionInputKey(String selection, String action, String input) {
        this.selection = selection.trim();
        this.action = action.trim();
        this.input = input.trim();
    }

    @Override
    public String toString() {
        return selection + "," + action + "," + input;
    }

    /**
     * @return the selection
     */
    public String getSelection() {
        return selection;
    }

    /**
     * @param selection the selection to set
     */
    public void setSelection(String selection) {
        this.selection = selection;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the input
     */
    public String getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public int hashCode() {
        try {
            
            String dig = hash((selection + action + input));
            //return dig.hashCode();
            return (selection + action + input).hashCode();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SelectionActionInputKey.class.getName()).log(Level.SEVERE, null, ex);
            return (selection + action + input).hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SelectionActionInputKey other = (SelectionActionInputKey) obj;
        if ((this.selection == null) ? (other.selection != null) : !this.selection.equals(other.selection)) {
            return false;
        }
        if ((this.action == null) ? (other.action != null) : !this.action.equals(other.action)) {
            return false;
        }
        if ((this.input == null) ? (other.input != null) : !this.input.equals(other.input)) {
            return false;
        }
        return true;
    }
    private static final char[] HEXADECIMAL = {'0', '1', '2', '3',
        '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public String hash(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(stringToHash.getBytes());
        StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            int low = (int) (bytes[i] & 0x0f);
            int high = (int) ((bytes[i] & 0xf0) >> 4);
            sb.append(HEXADECIMAL[high]);
            sb.append(HEXADECIMAL[low]);
        }
        return sb.toString();
    }
}
