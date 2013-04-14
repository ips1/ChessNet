/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.netchat;

import cz.cuni.mff.kubatpe1.java.connection.Connection;
import cz.cuni.mff.kubatpe1.java.connection.ConnectionType;
import cz.cuni.mff.kubatpe1.java.connection.Server;
import cz.cuni.mff.kubatpe1.java.connection.Client;
import cz.cuni.mff.kubatpe1.java.connection.ConnectionForm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.*;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author Ips
 */
public class NetChat {

    /**
     * @param args the command line arguments
     */
    private static JTextArea messages;
    //private static JTextPane messages;
    private static JTextField msgField;
    private static Connection connection;
    private static String id;
    
    private static final int portNo = 6666;
    
    public static class SendListener implements ActionListener {

        @Override    
        public void actionPerformed(ActionEvent e) {
            String message = msgField.getText();
            if (message.length() == 0) {
                
            }
            else if (message.contains("|")) {
                JOptionPane.showMessageDialog(null, "Message cannot contain \"|\" character!", "Message failed", JOptionPane.ERROR_MESSAGE);
            }
            else {
                writeMessage(id + ": " + message, true);
                connection.send("MSG|" + id + ": " + message);
                msgField.setText("");
            }
        }
        
    }
    
    public static void main(String[] args) {
        
        /*
        if (args.length != 1) return;
        if (args[0].equals("server")) {
            id = "Server";
            Server s = new Server(id);
            connection = s;

        }
        else {
            id = "Client";
            Client s = new Client(id);
            connection = s;

        }
        */
        /*
        ConnectionForm form = new ConnectionForm();
        form.show();
        while(!form.confirmed) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
        
        if (form.type == ConnectionType.SERVER) {
            id = "Server";
            Server s = new Server(id, portNo, new ChatHandler());
            connection = s;

        }
        else {
            id = "Client";
            Client s = new Client(id, form.target, portNo, new ChatHandler());
            connection = s;

        }
        
        connection.start();
        


        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
              public void run() {
                  try { 
                      showGUI(); 
                  }
                  catch (Exception e) { 
                      System.out.println(e);
                      return; 
                  }
              }
              
            });
*/
    }
    
    public static void writeMessage(String message, boolean myMessage) {
        messages.append(message);
        messages.append("\n");
    }
    
    
    
    public static void showGUI() throws Exception {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        JFrame window = new JFrame("ChessNet - " + id);
        
        Container pane = window.getContentPane();
        
        pane.setLayout(new BorderLayout());
        
        messages = new JTextArea();
        messages.setEditable(false);
        JScrollPane scrollArea = new JScrollPane(messages);
        
        pane.add(scrollArea);
        
        JPanel messageControl = new JPanel(new BorderLayout());
        
        msgField = new JTextField();
        JButton msgButton = new JButton("Send");
        msgButton.addActionListener(new SendListener());
        msgField.addActionListener(new SendListener());        
        
        messageControl.add(msgField);
        messageControl.add(msgButton, BorderLayout.EAST);
        
        pane.add(messageControl, BorderLayout.SOUTH);
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.pack();
        window.setSize(500, 300);
        window.setVisible(true);
        
    }
}
