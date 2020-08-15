// $Id: PreferenceTextField.java,v 1.10 2002/10/08 08:55:52 nicky Exp $
// Copyright (C) 2002 Beta Nine B.V.B.A.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package be.beta9.framework.swing;

import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.Toolkit;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import be.beta9.hylafax.Preferences;
import be.beta9.hylafax.HylaFaxSenderPreferences;

/** special JTextField prefence changes */
public class PreferenceTextField extends JTextField implements FocusListener,DocumentListener {
    
    private Preferences prefs;
    private boolean fieldChanged = false;
    private Document doc = new PlainDocument();
    
    public PreferenceTextField(String name,Preferences prefs,int columns){
        super(columns);
        this.setDocument(doc);
        // setDocument empties the field!
        this.setName(name);
        this.prefs = prefs;
        // sets the stuff that ALL PreferenceTextFields should have
        this.setDefaultProperties(doc);
        // load from preference if any
        setText(prefs.get(name));
    }
    
    public PreferenceTextField(String name,Preferences prefs, boolean isNumberField) {
        this.setDocument(new NumberDocument());
        this.setName(name);
        this.prefs = prefs;
        // sets the stuff that ALL PreferenceTextFields should have
        this.setDefaultProperties(this.getDocument());
    }
    
    public void setDefaultProperties(Document doc) {
        // we got to be able to enter something ;-)
        if (this.getColumns() == 0) this.setColumns(5);
        // default UI properties
        this.setMinimumSize(this.getPreferredSize());
        // event handling
        addFocusListener(this);
        doc.addDocumentListener(this);
        
    }
    
    public void focusGained(FocusEvent f) {
    }
    
    public void focusLost(FocusEvent f) {
        if(fieldChanged == true) {
            prefs.put(this.getName(), this.getText());
            fireActionPerformed();
            fieldChanged = false;
        }
    }
    
    public void insertUpdate(DocumentEvent e) {
        if (fieldChanged != true) {
            fieldChanged = true;
        }
    }
    public void removeUpdate(DocumentEvent e) {
        if (fieldChanged != true) {
            fieldChanged = true;
        }
    }
    public void changedUpdate(DocumentEvent e) {
    }
    
    // to validate a textfield (only numbers)
    protected class NumberDocument extends PlainDocument {
        private Toolkit toolkit = Toolkit.getDefaultToolkit();
        public void insertString(int offs,
                                 String str,
                                 AttributeSet a)
            throws BadLocationException {
            char[] source = str.toCharArray();
            char[] result = new char[source.length];
            int j = 0;
            
            for (int i = 0; i < result.length; i++) {
                if (Character.isDigit(source[i]))
                    result[j++] = source[i];
                else {
                    toolkit.beep();
                }
            }
            super.insertString(offs, new String(result, 0, j), a);
        }
    }
}
