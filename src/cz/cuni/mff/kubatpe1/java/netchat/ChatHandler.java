/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.netchat;

import cz.cuni.mff.kubatpe1.java.connection.MessageHandler;
import java.io.IOException;

/**
 *
 * @author Ips
 */
public class ChatHandler implements MessageHandler {

    @Override
    public void handle(String message) throws IOException {
        final String[] parts = message.split("\\|");
        if (parts[0].equals("MSG") && parts.length == 2) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    NetChat.writeMessage(parts[1], false);
                }
                
            });
        }
        else throw new IOException();
    }

    @Override
    public void reportFailure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
