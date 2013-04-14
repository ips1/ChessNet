/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.recording;

import cz.cuni.mff.kubatpe1.java.chessnet.game.IWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 *
 * @author Ips
 */
public class FileRecorder implements IWriter {

    private PrintWriter output;
    
    public FileRecorder(String fileName) throws FileNotFoundException {
        output = new PrintWriter(new FileOutputStream(fileName));
    }

    @Override
    public void write(String message) {
       output.println(message);
       output.flush();
    }
    
}
