// $Id: SwingTools.java,v 1.4 2002/10/08 09:37:57 sven Exp $
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

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * SwingTools is a collection of Swing utilities.
 */
public class SwingTools {
    
    /**
     * Center a component in reference to another,
     * if the reference is null or not visible the screen is used as reference
     */
    public static void centerComponent(Component component, Component reference) {
        Dimension componentSize = component.getSize();
        Dimension referenceSize;
        Point referencePosition;
        if (reference != null && reference.isShowing()) {
            referenceSize = reference.getSize();
            referencePosition = reference.getLocationOnScreen();
        } else {
            referenceSize = Toolkit.getDefaultToolkit().getScreenSize();
            referencePosition = new Point(0, 0);
        }
        component.setBounds(referencePosition.x
                                + Math.abs(referenceSize.width - componentSize.width) / 2,
                            referencePosition.y
                                + Math.abs(referenceSize.height - componentSize.height) / 2,
                            componentSize.width,
                            componentSize.height);
    }
    
    public static void showWaitCursor(Component component) {
        showPredefinedCursor(component, Cursor.WAIT_CURSOR);
    }
    
    public static void showPredefinedCursor(Component component, int cursor) {
        component.setCursor(Cursor.getPredefinedCursor(cursor));
    }
    
    public static void showDefaultCursor(Component component) {
        showPredefinedCursor(component, Cursor.DEFAULT_CURSOR);
    }
    
    /**
     * Shows an ordinary messageDialog with an error message
     */
    public static void showErrorMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent,
                                      message,
                                      title,
                                      JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Shows an ordinary messageDialog with an information message
     */
    public static void showMessage(Component parent, String title, Object message) {
        JOptionPane.showMessageDialog(parent,
                                      message,
                                      title,
                                      JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows an ordinary optionDialog with a plain message
     * containing an InputPanel which asks for a password.
     * Returns password if OK was clicked, else returns null.
     */
    public static String askForPassword(Component parent,
                                        String title,
                                        String question,
                                        String okLabel,
                                        String cancelLabel) {
        InputPanel panel = new InputPanel(question);
        int result = JOptionPane.showOptionDialog(parent,
                                                  panel,
                                                  title,
                                                  JOptionPane.YES_NO_OPTION,
                                                  JOptionPane.PLAIN_MESSAGE,
                                                  null,
                                                  new String[] { okLabel, cancelLabel },
                                                  okLabel);
        if (result == 0) {
            return panel.passwordField.getText();
        } else {
            return null;
        }
    }
    
    private static class InputPanel extends JPanel {
        
        JPasswordField passwordField;
        
        InputPanel(String labelText) {
            super(new BorderLayout(10, 10));
            JLabel label = new JLabel(labelText);
            passwordField = new JPasswordField();
            add(label, BorderLayout.NORTH);
            add(passwordField, BorderLayout.CENTER);
        }
        
    }
    
    public static void main(String[] args) {
        System.out.println(askForPassword(null,
                                          "Password",
                                          "Enter your password",
                                          "Login",
                                          "Cancel"));
    }
    
}

