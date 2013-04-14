/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.connection;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Ips
 */
public class ConnectionForm {
    
    private boolean confirmed = false;
    private String target;
    private ConnectionType type;
    
    
    private JRadioButton server;
    private JRadioButton client;
    private JTextField targetIP;
    private JFrame window;
    public class ConfirmListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            confirmed = true;
            target = targetIP.getText();
            if (server.isSelected()) type = ConnectionType.SERVER;
            else type = ConnectionType.CLIENT;
                
            window.setVisible(false);
        }
    
    }
    
    private void showGUI() throws Exception {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        window = new JFrame("Connection");
        
        Container pane = window.getContentPane();
        
               
        pane.setLayout(new GridLayout(0,1));
        
        //pane.add(mainPanel);
        
        server = new JRadioButton("Server");
        server.setSelected(true);
        client = new JRadioButton("Client");
        client.setSelected(false);
        
        ButtonGroup group = new ButtonGroup();
        group.add(server);
        group.add(client);

        
        JTextField myIP = new JTextField(InetAddress.getLocalHost().getHostAddress());
        myIP.setEditable(false);
        
        targetIP = new JTextField();
        
        JButton confirm = new JButton("OK");
        confirm.addActionListener(new ConfirmListener());
        pane.add(server);
        pane.add(client);
        
        pane.add(myIP);
        pane.add(targetIP);
        
        pane.add(confirm);
        



        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.pack();
        window.setSize(200, 150);
        window.setVisible(true);

    }
    
    public ConnectionType getType() {
        return type;
    }
    
    public boolean wasConfirmed() {
        return confirmed;
    }
    
    public String getTarget() {
        return target;
    }
    
    public void show() {


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


    }
    
    public boolean showDialog() throws UnknownHostException {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0,1));
        server = new JRadioButton("Server");
        server.setSelected(true);
        client = new JRadioButton("Client");
        client.setSelected(false);
        
        ButtonGroup group = new ButtonGroup();
        group.add(server);
        group.add(client);

        
        JTextField myIP = new JTextField(InetAddress.getLocalHost().getHostAddress());
        myIP.setEditable(false);
        
        targetIP = new JTextField();
        
        JButton confirm = new JButton("OK");
        confirm.addActionListener(new ConfirmListener());
        
        mainPanel.add(server);
        mainPanel.add(client);
        
        mainPanel.add(myIP);
        mainPanel.add(targetIP);
        
        String[] buttons = { "OK", "Cancel"};
        
        int result = JOptionPane.showOptionDialog(
                null,
                mainPanel,
                "Connection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                buttons[0]
         );
        
        if (result != 0) return false;
        confirmed = true;
        target = targetIP.getText();
        if (server.isSelected()) type = ConnectionType.SERVER;
        else type = ConnectionType.CLIENT;
        return true;
        
    }
    
}
