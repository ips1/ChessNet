/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ips
 */
public class Client extends Connection  {
    private Socket s;
    private boolean changed = false;
    private int changed_x = 0;
    private int changed_y = 0;
    private char changed_c = 0;
    private char client_c = 'o';
    
    public Client(String id, String targetIP, int port, MessageHandler handler) throws SocketException {
        try {
            InetAddress target = InetAddress.getByName(targetIP);
            s = new Socket(target, port);
            out = new PrintWriter(s.getOutputStream());
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        } catch(SocketException ex) {
            throw ex;
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(1);
        }
        this.id = id;            
        this.handler = handler;
    }
    
    @Override
    public void close() {
        super.close();
        try {
            s.close();
        } catch (IOException ex) {
            
        }
    }
    
    
    
}
