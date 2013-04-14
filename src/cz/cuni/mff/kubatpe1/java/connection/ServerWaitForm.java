/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.connection;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

/**
 *
 * @author Ips
 */
public class ServerWaitForm {
    private JFrame window;
    private void showGUI() throws Exception {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        window = new JFrame("Connection");
        
        Container pane = window.getContentPane();
        
        pane.setLayout(new BorderLayout(10,10));
        
        pane.add(new JLabel("Waiting for client to connect..."), BorderLayout.CENTER);
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.pack();
        window.setSize(250, 75);
        window.setVisible(true);
        
    }
    
    private void hideGUI() {
        window.setVisible(false);
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
    
    public void hide() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
              public void run() {

                hideGUI(); 


              }
              
            });
    }
}
