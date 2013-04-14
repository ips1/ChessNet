/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet;

import cz.cuni.mff.kubatpe1.java.chessnet.game.InvalidMoveException;
import cz.cuni.mff.kubatpe1.java.chessnet.gui.MainWindow;
import cz.cuni.mff.kubatpe1.java.chessnet.gui.PieceNetworkSelector;
import cz.cuni.mff.kubatpe1.java.connection.MessageHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ips
 */
public class Handler implements MessageHandler {

    private GameSynchronizer synchronizer;
    private MainWindow window;
    

    public Handler(MainWindow window) {
        this.window = window;
    }
    
    public void setSynchronizer (GameSynchronizer synchronizer) {
        this.synchronizer = synchronizer;
    }
    
    @Override
    public void reportFailure() {
        window.lostConnection();
    }
    
    @Override
    public void handle(String message) throws IOException {
        

        final String[] parts = message.split("\\|");
        if (parts[0].equals("MSG") && parts.length == 2) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    window.writeMessage(parts[1], false);
                }
                
            });
        }
        else if (parts[0].equals("START") && parts.length == 1) {
            window.invokeStart();
            /*
             javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    
                }
                
            });
            * */
        }
        else if (parts[0].equals("STCONFIRM") && parts.length == 1) {
            window.confirmStart();
            /*
             javax.swing.SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    window.confirmStart();
                }
                
            });
            * */
        }
        else if (parts[0].equals("STDENY") && parts.length == 1) {
            window.denyStart();

        }
        else if (parts[0].equals("QGAME") && parts.length == 1) {
            window.forceQuitGame();

        }
        else if (parts[0].equals("PIECESEL") && parts.length == 2) {
            window.setPieceSelection(parts[1]);
            try {
                synchronizer.resumeMove();
            } catch (InvalidMoveException ex) {
                System.err.println("Cannot invoke the move!");
            }

        }
        else if (parts[0].equals("MV") && parts.length == 5) {
            if (synchronizer == null) throw new IOException();
            int sourceX = Integer.parseInt(parts[1]);
            int sourceY = Integer.parseInt(parts[2]);
            int targetX = Integer.parseInt(parts[3]);
            int targetY = Integer.parseInt(parts[4]);
            try {
                synchronizer.invokeMove(sourceX, sourceY, targetX, targetY);
            }
            catch (InvalidMoveException e) {
                System.err.println("Cannot invoke the move!");
            }
        }
        else if (parts[0].equals("MVCONFIRM") && parts.length == 1) {
            if (synchronizer == null) throw new IOException();
            try {
                synchronizer.movementConfirmed();
            }
            catch (InvalidMoveException e) {
                System.err.println("Cannot confirm the move!");
            }
        }
        else throw new IOException();
    }
    
}
