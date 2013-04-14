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
public interface MessageHandler {
    public void handle(String message) throws IOException;
    public void reportFailure();
}
