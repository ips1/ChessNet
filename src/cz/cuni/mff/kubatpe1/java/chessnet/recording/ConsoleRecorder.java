/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.recording;

import cz.cuni.mff.kubatpe1.java.chessnet.game.IWriter;

/**
 *
 * @author Ips
 */
public class ConsoleRecorder implements IWriter {

    @Override
    public void write(String message) {
        System.out.println(message);
    }
    
    
}
