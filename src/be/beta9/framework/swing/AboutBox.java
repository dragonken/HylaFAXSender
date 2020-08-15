// $Id: AboutBox.java,v 1.4 2002/10/08 10:18:52 sven Exp $
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
import java.awt.event.*;
import java.util.*;

/**
 * AboutBox is a frame with an icon, a text and an OK button.
 */
public class AboutBox extends JFrame {

    private JLabel iconLabel;
    private JLabel titleLabel;

    /**
     * Creates an AboutBox with a given title, message and icon and centers it over the parentcomponent.
     */
    public AboutBox(Component parent, String title, String message, Icon icon) {
        super();
        addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent event) {
                        close();
                    }
                });
        addKeyListener(new KeyAdapter() {
                    public void keyPressed(KeyEvent event) {
                        if (event.getKeyCode() == KeyEvent.VK_ESCAPE
                            ||
                            event.getKeyCode() == KeyEvent.VK_ENTER) {
                            close();
                        }
                    }
                });

        JPanel contentsPanel = new JPanel(new ExtendedGridBagLayout());
        contentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        iconLabel = new JLabel(icon, JLabel.CENTER);
        titleLabel = new JLabel(title, JLabel.CENTER);

        Font font = titleLabel.getFont();
        titleLabel.setFont(font.deriveFont(16.0f).deriveFont(Font.BOLD));

        contentsPanel.add(iconLabel, "gridwidth=REM;insets=5,5,5,5");
        contentsPanel.add(titleLabel, "gridwidth=REM;insets=5,5,5,5");

        font = font.deriveFont(10.0f);
        StringTokenizer tokens = new StringTokenizer(message, "\n");
        while (tokens.hasMoreElements()) {
            String text = tokens.nextToken();
            JLabel messageLabel = new JLabel(text, JLabel.CENTER);
            messageLabel.setFont(font);
            contentsPanel.add(messageLabel, "gridwidth=REM");
        }

        getContentPane().add(contentsPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        SwingTools.centerComponent(this, parent);
    }

    /**
     * Sets the visibility to false and disposes the frame.
     */
    public void close() {
        setVisible(false);
        dispose();
    }

    public static void main(String[] args) throws Exception {
        Icon icon = new ImageIcon(AboutBox.class.getResource("/images/hylafaxsender.gif"));
        AboutBox aboutBox = new AboutBox(null,
                                         "About Box",
                                         "Version 1.1 (September 2002)\n \nCopyright 2002 Beta Nine B.V.B.A.\nAll Rights Reserved.",
                                         icon);
    }

}


