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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Ips
 */
public class PieceNetworkSelector implements IPieceSelector {

    private String lastSelected;
    
    private Game gameFix;
    private GameField targetFix;
    private PieceColor ownerFix;
    
    public void setSelection(String message) {
        lastSelected = message;

    }
    
    @Override
    public GamePiece selectAndPlace(Game currentGame, GameField targetField, PieceColor owner) {
        switch (lastSelected) {
            case "Bishop" : return new Bishop(currentGame, targetField, owner); 
            case "Knight" : return new Knight(currentGame, targetField, owner); 
            case "Rook" : return new Rook (currentGame, targetField, owner); 
            default : return new Queen (currentGame, targetField, owner); 
        } 
    }
    
}
