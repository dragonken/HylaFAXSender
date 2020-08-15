// $Id: HylaFaxSender.java,v 1.19 2002/10/08 14:11:05 sven Exp $
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

import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import be.beta9.framework.swing.*;
import com.apple.mrj.*;
import gnu.hylafax.*;

/** Main Program */
public class HylaFaxSender extends JFrame
    implements MRJAboutHandler, MRJQuitHandler, MRJPrefsHandler {
    
    private final static String[] DOC_TYPES = new String[] { ".pdf", ".ps", ".tiff", ".txt" };
    private static boolean macOS = System.getProperty("mrj.version") != null;
    private static Icon icon;
    
    private JTextField numberField, fileNameField, emailField;
    private JButton fileChooserButton, exitButton, resetButton, faxButton;
    private String password, passwordUser;
    private Preferences preferences = new Preferences("hylafaxsender");
    
    public static Icon getIcon() {
        if (icon == null) {
            icon = new ImageIcon(
                HylaFaxSender.class.getResource("/images/hylafaxsender.gif"));
        }
        return icon;
    }
    
    public HylaFaxSender() {
        super("HylaFAXSender");
        preferences.putIfAbsent("port", "4559");
        preferences.putIfAbsent("notify", "done");
        preferences.putIfAbsent("pagesize", "a4");
        preferences.putIfAbsent("resolution", "high");
        getContentPane().add(getContents());
        setJMenuBar(createMenuBar());
        if (macOS) MRJApplicationUtils.registerAboutHandler(this);
        if (macOS) MRJApplicationUtils.registerQuitHandler(this);
        if (macOS) MRJApplicationUtils.registerPrefsHandler(this);
        if (preferences.get("hostname") == null) preferences();
    }
    
    public JPanel getContents() {
        JPanel panel = new JPanel(new ExtendedGridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Fax Number:"), "anchor=E;insets=0,0,0,10");
        panel.add(numberField = new JTextField(16), "gridwidth=REM;anchor=W;insets=2,0,2,0");
        numberField.setToolTipText("Enter the fax number of the receiver");
        
        panel.add(new JLabel("Document Filename:"), "anchor=E;insets=0,0,0,10");
        panel.add(fileNameField = new JTextField(32), "insets=2,0,2,0;fill=H");
        fileNameField.setToolTipText("Enter the filename of the document to fax");
        panel.add(fileChooserButton = new JButton("Choose"), "gridwidth=REM;insets=2,0,2,0");
        fileChooserButton.setToolTipText("Select the document to fax");
        fileChooserButton.addActionListener(new GenericActionListener(this, "choose"));
        
        panel.add(new JLabel("User Email:"), "anchor=E;insets=0,0,0,10");
        panel.add(emailField = new JTextField(16), "gridwidth=REM;anchor=W;insets=2,0,2,0");
        emailField.setToolTipText("Enter the email address to send the confirmation email to");
        emailField.setText(preferences.get("email"));
        
        ButtonPanel buttonPanel = new ButtonPanel();
        exitButton = buttonPanel.newButton("Quit");
        resetButton = buttonPanel.newButton("Reset");
        faxButton = buttonPanel.newButton("Send Fax");
        exitButton.addActionListener(new GenericActionListener(this, "quit"));
        resetButton.addActionListener(new GenericActionListener(this, "reset"));
        faxButton.addActionListener(new GenericActionListener(this, "fax"));
        panel.add(buttonPanel, "gridwidth=REM;insets=10,0,0,0");
        return panel;
    }
    
    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        JMenu fileMenu = new JMenu("File");
        JMenuItem selectDocumentMenuItem = new JMenuItem("Select Document to Fax...");
        selectDocumentMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, mask));
        selectDocumentMenuItem.addActionListener(new GenericActionListener(this, "choose"));
        fileMenu.add(selectDocumentMenuItem);
        if (!macOS) {
            fileMenu.addSeparator();
            JMenuItem quitMenuItem = new JMenuItem("Exit");
            quitMenuItem.addActionListener(new GenericActionListener(this, "quit"));
            fileMenu.add(quitMenuItem);
        }
        menuBar.add(fileMenu);
        if (!macOS) {
            JMenu editMenu = new JMenu("Edit");
            JMenuItem prefsMenuItem = new JMenuItem("Preferences...");
            prefsMenuItem.addActionListener(new GenericActionListener(this, "preferences"));
            editMenu.add(prefsMenuItem);
            menuBar.add(editMenu);
        }
        JMenu helpMenu = new JMenu("Help");
        if (!macOS) {
            JMenuItem aboutMenuItem = new JMenuItem("About HylaFAXSender...");
            aboutMenuItem.addActionListener(new GenericActionListener(this, "about"));
            helpMenu.add(aboutMenuItem);
            helpMenu.addSeparator();
        }
        JMenuItem helpMenuItem = new JMenuItem("HylaFAXSender Help");
        helpMenuItem.addActionListener(new GenericActionListener(this, "help"));
        helpMenu.add(helpMenuItem);
        menuBar.add(helpMenu);
        return menuBar;
    }
    
    public void reset() {
        numberField.setText(null);
        fileNameField.setText(null);
        emailField.setText(preferences.get("email"));
    }
    
    public void quit() {
        System.exit(0);
    }
    
    public void setEmail(String email) {
        emailField.setText(email);
    }
    
    public void help() {
        SwingTools.showMessage(this,
                               "HylaFAXSender Help",
                               "Help is currently unavailable");
    }
    
    public void fax() {
        String number = numberField.getText();
        String filename = fileNameField.getText();
        String email = emailField.getText();
        if (number == null || "".equals(number)) {
            SwingTools.showErrorMessage(this, "Error", "No receiver specified");
            return;
        }
        if (filename == null || "".equals(filename)) {
            SwingTools.showErrorMessage(this, "Error", "No document specified");
            return;
        }
        if (!preferences.get("notify").equals("none") && (email == null || "".equals(email))) {
            SwingTools.showErrorMessage(this, "Error", "No email specified");
            return;
        }
        // System.out.println("Sending " + filename + " to " + number + " on behalf of " + email);
        doSendHylaFax(number, filename, email);
    }
    
    public void choose() {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String n = name.toLowerCase();
                for (int i = 0; i < DOC_TYPES.length; i++) {
                    if (n.endsWith(DOC_TYPES[i]))
                        return true;
                }
                return false;
            }
        };
        if (macOS) {
            FileDialog dialog = new FileDialog(this, "Select Document to Fax");
            dialog.setFilenameFilter(filter);
            dialog.setVisible(true);
            if (dialog.getFile() != null) {
                File file = new File(dialog.getDirectory(), dialog.getFile());
                fileNameField.setText(file.getAbsolutePath());
            }
        } else {
            JFileChooser dialog = new JFileChooser();
            dialog.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        public boolean accept(File file) {
                            String n = file.getName().toLowerCase();
                            for (int i = 0; i < DOC_TYPES.length; i++) {
                                if (n.endsWith(DOC_TYPES[i]))
                                    return true;
                            }
                            return file.isDirectory();
                        }
                        public String getDescription() {
                            return "Select Document to Fax";
                        }
                    });
            if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = dialog.getSelectedFile();
                fileNameField.setText(file.getAbsolutePath());
            }
        }
    }
    
    public void doSendHylaFax(String destination, String filename, String user) {
        String host= preferences.get("hostname"); // -h
        if (host == null || "".equals(host)) {
            SwingTools.showErrorMessage(this, "Error", "No hostname specified");
            return;
        }
        String port = preferences.get("port");
        if (port == null || "".equals(port)) {
            SwingTools.showErrorMessage(this, "Error", "No portnumber specified");
            return;
        }
        String from= preferences.get("account"); // -f
        if (from == null || "".equals(from)) {
            SwingTools.showErrorMessage(this, "Error", "No account specified");
            return;
        }
        String notify = preferences.get("notify");
        String notifyaddr= preferences.get("email"); // -f
        if (notify != "none") {
            if ((notifyaddr == null || "".equals(notifyaddr))) {
                SwingTools.showErrorMessage(this, "Error", "No email specified");
                return;
            }
        }
        int resolution = preferences.get("resolution") == "low" ? 98 : 196;   // -l 98, -m 196
        Hashtable pageSizes = Job.pagesizes;
        Dimension pagesize = (Dimension) pageSizes.get(preferences.get("pagesize"));   // -s
        String killtime= "000259"; // -k
        int maxdials= 12; // -T
        int maxtries= 3; // -t
        int priority= 127; // -P
        String pagechop= "default";
        int chopthreshold= 3;
        Vector documents= new Vector();
        boolean verbose= false;
        boolean from_is_set= false;
        
        HylaFAXClient c= new HylaFAXClient();
        
        try {
            c.setDebug(verbose);    // enable debug messages
            c.open(host,Integer.parseInt(port));
            // if there's a password we NEED to supply it
            if(c.user(from)) {
                // ask for the user password in a dialog
                if (!(user.equals(passwordUser) && password != null)) {
                    password = SwingTools.askForPassword(null,
                                                         "Password Needed",
                                                         "Enter the password for the HylaFAX account '" + from + "'",
                                                         "Continue",
                                                         "Cancel");
                    if (password == null) return;
                }
                c.pass(password);
            }
            c.noop();   // for the heck of it
            c.tzone(HylaFAXClientProtocol.TZONE_LOCAL);
            
            // schlep file up to server
            if (!new File(filename).exists()) {
                SwingTools.showErrorMessage(this, "Error", "Specified document does not exist");
                return;
            }
            FileInputStream file = new FileInputStream(filename);
            String remote_filename = c.putTemporary(file);
            documents.addElement(remote_filename);
            
            Job job= c.createJob(); // start a new job
            
            // set job properties
            job.setFromUser(from);
            job.setNotifyAddress(notifyaddr);
            job.setKilltime(killtime);
            job.setMaximumDials(maxdials);
            job.setMaximumTries(maxtries);
            job.setPriority(priority);
            job.setDialstring(destination);
            job.setVerticalResolution(resolution);
            job.setPageDimension(pagesize);
            job.setNotifyType(notify);
            job.setChopThreshold(chopthreshold);
            
            // add documents to the job
            for (int i=0; i<documents.size(); i++) {
                String document= (String)documents.elementAt(i);
                job.addDocument(document);
            }
            
            c.submit(job); // submit the job to the scheduler
            
            SwingTools.showMessage(this,
                                   "Fax Queued",
                                   "<html>Fax '" + filename + "'<br>to " + destination + " queued on server for sending<br>"
                                       + ((notify != "none") ? "'" + user + "' will be notified on delivery"
                                              : "there will be no notification on delivery")
                                       + "</html>");
        } catch (Exception e) {
            SwingTools.showErrorMessage(this,
                                        "ERROR: Failed to Queue Fax",
                                        e.toString());
            e.printStackTrace();
        } finally {
            // disconnect from the server
            try {
                c.quit();
            } catch (Exception e) {
                // quit failed, not much we can do now
                e.printStackTrace();
            }
        }
    }
    
    public void about() {
        AboutBox aboutBox = new AboutBox(null,
                                         "HylaFAXSender",
                                         "Version 1.0 (September 2002)\n \nCopyright 2002 Beta Nine B.V.B.A.\nAll Rights Reserved.",
                                         getIcon());
        if (macOS) aboutBox.setJMenuBar(createMenuBar());
        aboutBox.setVisible(true);
        
    }
    
    public Preferences getPreferences() {
        return preferences;
    }
    
    public void preferences() {
        JFrame frame = new HylaFaxSenderPreferences(this);
        
        if (macOS) frame.setJMenuBar(createMenuBar());
        frame.pack();
        frame.setResizable(false);
        SwingTools.centerComponent(frame, null);
        frame.show();
    }
    
    public void handleAbout() {
        about();
    }
    
    public void handleQuit() throws IllegalStateException {
        quit();
    }
    
    public void handlePrefs() throws IllegalStateException {
        preferences();
    }
    
    public static void main(String[] args) {
        if (!macOS) {
            // Set Kunststoff look and feel
            // For those living without Aqua
            try {
                UIManager.put("ClassLoader", HylaFaxSender.class.getClassLoader());
                UIManager.setLookAndFeel("com.incors.plaf.kunststoff.KunststoffLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JFrame frame = new HylaFaxSender();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
}








