/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ips
 */
public class Server extends Connection  {
    private Socket s;
    private ServerSocket ss;
    private boolean myTurn = true;

    private boolean changed = false;
    private int changed_x = 0;
    private int changed_y = 0;
    private char changed_c = 0;
    private char server_c = 'x';
    

    
    public Server(String id, MessageHandler handler) {

        
        this.id = id;
        this.handler = handler;  
    }
    
    public void waitForConnection(int port, boolean showWaitForm) throws SocketException, CreateSocketException {
        try {
            ss = new ServerSocket(port);
        } catch (Exception ex) {
            throw new CreateSocketException(ex);
        }
        
        ServerWaitForm form = new ServerWaitForm();
        if (showWaitForm) {

            form.show();
        }
        try {
            s = ss.accept();
            if (showWaitForm) {
                form.hide();
            }
            
            out = new PrintWriter(s.getOutputStream());
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ss.close();
        } catch (SocketException ex) {
            throw ex;
        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }

    }
    
    @Override
    public void close() {
        super.close();
        if (s != null) {
            try {
                s.close();
            } catch (IOException ex) {

            }
        }
        else if (ss != null) {
            try {
                ss.close();
            } catch (IOException ex) {
                
            }
        }

    }
    

    

    

}

