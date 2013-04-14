/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.connection;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ips
 */
public class ConnectionSender extends Thread {
    private Connection connection;

    public ConnectionSender(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public void run() {
        while (!connection.finished) {
            connection.transferPending();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                
            }
        }
    }
    
}
