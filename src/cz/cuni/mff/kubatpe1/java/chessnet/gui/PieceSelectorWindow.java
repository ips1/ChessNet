/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.gui;

import cz.cuni.mff.kubatpe1.java.chessnet.game.Game;
import cz.cuni.mff.kubatpe1.java.chessnet.game.GameField;
import cz.cuni.mff.kubatpe1.java.chessnet.game.GamePiece;
import cz.cuni.mff.kubatpe1.java.chessnet.game.IPieceSelector;
import cz.cuni.mff.kubatpe1.java.chessnet.game.PieceColor;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Bishop;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Knight;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Queen;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Rook;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Ips
 */
public class PieceSelectorWindow implements IPieceSelector {
   
    ResourceLoader rs;
    JFrame mainWindow;

    public PieceSelectorWindow(ResourceLoader rs, JFrame mainWindow) {
        this.rs = rs;
        this.mainWindow = mainWindow;
    }


    
    @Override
    public GamePiece selectAndPlace(Game currentGame, GameField targetField, PieceColor owner) {
        Object[] possibilities = {"Bishop", "Knight", "Rook", "Queen"};
        String s = (String)JOptionPane.showInputDialog(mainWindow, "Select piece to switch for Pawn:", "Piece selection", JOptionPane.QUESTION_MESSAGE, new ImageIcon(rs.pawnWhite), possibilities, "Queen");
        switch (s) {
            case "Bishop" : return new Bishop(currentGame, targetField, owner);
            case "Knight" : return new Knight(currentGame, targetField, owner);
            case "Rook" : return new Rook (currentGame, targetField, owner);
            default : return new Queen (currentGame, targetField, owner);  
        } 
    }
    


    
}
