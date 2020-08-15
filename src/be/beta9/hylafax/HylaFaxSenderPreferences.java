// $Id: HylaFaxSenderPreferences.java,v 1.14 2002/10/08 14:44:59 nicky Exp $
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

package be.beta9.hylafax;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import gnu.hylafax.*;
import be.beta9.framework.swing.*;

public class HylaFaxSenderPreferences extends JFrame {
    
    private HylaFaxSender hylaFaxSender;
    
    private PreferenceTextField hostField,portField, accountField, passwordField, emailField, resolutionField;
    private JComboBox notificationComboBox, resolutionComboBox, paperFormatComboBox;
    private JRadioButton standardPropertiesRadioButton, allPropertiesRadioButton;
    private JButton connectButton; private JLabel serverStatusLabel;
    
    private boolean preferenceChanged = false;
    private Preferences prefs;
    
    public HylaFaxSenderPreferences(HylaFaxSender hylaFaxSender) {
        super("HylaFAXSender Preferences");
        this.hylaFaxSender = hylaFaxSender;
        this.prefs = hylaFaxSender.getPreferences();
        getContentPane().add(getContents());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    public JPanel getContents() {
        JPanel preferencePanel = new JPanel(new BorderLayout());
        preferencePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JTabbedPane tabbedPane = new JTabbedPane();
        // first tab
        JPanel serverPreferencePanel = new JPanel(new ExtendedGridBagLayout());
        serverPreferencePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        serverPreferencePanel.add(new JLabel("Server Hostname:"), "anchor=W;insets=0,0,0,10;weighty=0.0");
        serverPreferencePanel.add(hostField = new PreferenceTextField("hostname", this.prefs,20), "anchor=W;insets=5,0,5,0");
        hostField.setToolTipText("Enter the hostname of the HylaFAX server");
        
        serverPreferencePanel.add(new JLabel("Port:"), "anchor=W;insets=0,10,0,10");
        serverPreferencePanel.add(portField = new PreferenceTextField("port", this.prefs,5), "gridwidth=REM;anchor=W;insets=5,0,5,0");
        portField.setToolTipText("Enter the portnumber of the HylaFAX service");
        
        serverPreferencePanel.add(new JLabel("Server Account:"), "anchor=W;insets=0,0,0,10;weighty=0.0");
        serverPreferencePanel.add(accountField = new PreferenceTextField("account", this.prefs,20), "anchor=NW;insets=5,0,5,0");
        accountField.setToolTipText("Enter the user account on the server");
        
        serverPreferencePanel.add(new JLabel("Email:"), "anchor=W;insets=0,10,0,10");
        serverPreferencePanel.add(emailField = new PreferenceTextField("email", this.prefs,20), "gridwidth=REM;anchor=W;insets=5,0,5,0");
        emailField.setToolTipText("Enter the email address to send the confirmation email to");
        emailField.addActionListener(new GenericActionListener(this, "propagateEmail"));
        
        //serverPreferencePanel.add(new JLabel("Password:"),"anchor=E;insets=0,0,0,10");
        //serverPreferencePanel.add(passwordField = new PreferenceTextField("password"), "gridwidth=REM;anchor=W;insets=2,0,2,0");
        
        // to get it so stick SOUTH
        serverPreferencePanel.add(new JLabel("Server Status:"), "anchor=W;insets=5,0,5,10");
        serverStatusLabel = new JLabel("No servers found");
        serverStatusLabel.setBorder(BorderFactory.createEtchedBorder());
        serverPreferencePanel.add(serverStatusLabel, "gridwidth=2;anchor=W;insets=5,0,5,0;fill=H");
        connectButton = new JButton("Test Server Settings");
        connectButton.addActionListener(new GenericActionListener(this, "checkServerSettings"));
        serverPreferencePanel.add(connectButton, "gridwidth=REM;anchor=E;insets=5,10,5,0;;weighty=0.0");
        
        // second tab
        JPanel jobPreferencePanel = new JPanel(new ExtendedGridBagLayout());
        jobPreferencePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jobPreferencePanel.add(new JLabel("Notify when:"), "anchor=W;insets=0,0,0,10;weighty=0.0");
        notificationComboBox = new JComboBox();
        notificationComboBox.addItem(Job.NOTIFY_DONE.toUpperCase());
        notificationComboBox.addItem(Job.NOTIFY_NONE.toUpperCase());
        notificationComboBox.addItem(Job.NOTIFY_REQUEUED.toUpperCase());
        notificationComboBox.setSelectedItem(prefs.get("notify").toUpperCase());
        notificationComboBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String notify = (String) notificationComboBox.getSelectedItem();
                        prefs.put("notify", notify.toLowerCase());
                    }
        });
        jobPreferencePanel.add(notificationComboBox,"gridwidth=REM;anchor=W;insets=5,0,5,0");
        jobPreferencePanel.add(new JLabel("Send Resolution:"), "anchor=W;insets=0,0,0,10;weighty=0.0");
        resolutionComboBox = new JComboBox();
        resolutionComboBox.addItem("LOW");
        resolutionComboBox.addItem("HIGH");
        resolutionComboBox.setSelectedItem(prefs.get("resolution").toUpperCase());
        resolutionComboBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String resolution = (String) resolutionComboBox.getSelectedItem();
                        prefs.put("resolution", resolution.toLowerCase());
                    }
        });
        jobPreferencePanel.add(resolutionComboBox,"gridwidth=REM;anchor=W;insets=5,0,5,0");
        jobPreferencePanel.add(new JLabel("Paper Size:"), "anchor=W;insets=0,0,0,10;weighty=0.0");
        paperFormatComboBox = new JComboBox();
        Hashtable pagesizes = Job.pagesizes; // there is no getPagesizes !?
        for (Enumeration e = pagesizes.keys(); e.hasMoreElements(); ) {
            paperFormatComboBox.addItem(e.nextElement().toString().toUpperCase());
        }
        paperFormatComboBox.setSelectedItem(prefs.get("pagesize").toUpperCase());
        paperFormatComboBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String size = (String) paperFormatComboBox.getSelectedItem();
                        prefs.put("pagesize", size.toLowerCase());
                    }
        });
        jobPreferencePanel.add(paperFormatComboBox,"gridwidth=REM;anchor=W;insets=5,0,5,0");
        
        tabbedPane.add("Server Settings", serverPreferencePanel);
        tabbedPane.add("Job Options", jobPreferencePanel);
        preferencePanel.add(tabbedPane);
        // return toplevel panel
        return preferencePanel;
    }
    
    public void savePreference(String key, String value) {
        prefs.put(key,value);
    }
    
    public void checkServerSettings() {
        HylaFAXClient c= new HylaFAXClient();
        try{
            c.open(hostField.getText());
            serverStatusLabel.setText("OK - Server " + hostField.getText() + " found");
            c.quit();
        }catch(Exception e){
            serverStatusLabel.setText("ERROR - Server " + hostField.getText() + " NOT found!");
        }
    }
    
    public void propagateEmail() {
        hylaFaxSender.setEmail(emailField.getText());
    }
    
}



