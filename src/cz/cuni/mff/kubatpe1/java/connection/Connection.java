/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.connection;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
/**
 *
 * @author Ips
 */
public abstract class Connection  {
    
    protected PrintWriter out;
    protected BufferedReader in;
    
    protected MessageHandler handler;
    
    protected List<String> pending;
    
    protected String id;
    
    protected boolean finished;
    
    protected Thread receiver;
    protected Thread sender;

    public Connection() {
        this.pending = new ArrayList<String>();
        
        finished = false;
        
        receiver = new ConnectionReceiver(this);
        sender = new ConnectionSender(this);
       
        
    }
    
    public void start() {
        receiver.start();
        sender.start();
    }
    
    public void close() {
        finished = true;
        //receiver.interrupt();
        //sender.interrupt();
    }
    
     /**
     * Check for new incomming messages, if there is none, puts itself in sleep for 100ms
     * @throws IOException 
     */
    public void refresh() throws IOException {
        String line;
        line = in.readLine();
        if (line == null) {
            throw new IOException();
           
        }
        System.out.println(id + " - handling cmd: " + line);
        handler.handle(line);
        
    }   
    
    public void reportFailure() {
        handler.reportFailure();
    }
    
    
    
    public synchronized void send(String cmd) {
        System.out.println(id + " - adding cmd to queue: " + cmd);
        pending.add(cmd);        
    }

    
    protected synchronized void transferPending() {
        //System.out.println(id + " - transfering queue");
        for (String m : pending) {
            transfer(m);
        }
        pending.clear();
    }
    
    private void transfer(String cmd) {
        System.out.println(id + " - sending cmd: " + cmd);
        out.println(cmd);
        out.flush();
    }
    
    
}
