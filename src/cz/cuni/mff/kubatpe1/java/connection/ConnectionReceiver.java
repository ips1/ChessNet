/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.connection;

import java.io.IOException;

/**
 *
 * @author Ips
 */
public class ConnectionReceiver extends Thread {
    private Connection connection;

    public ConnectionReceiver(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public void run() {
        while (!connection.finished) {
            try {
                connection.refresh();
            }
            catch (IOException ex) {
                // CONNECTION FAIL - TODO
                System.err.println("Connection lost!");
                connection.reportFailure();
                return;
            }
        }
    }
    
}
