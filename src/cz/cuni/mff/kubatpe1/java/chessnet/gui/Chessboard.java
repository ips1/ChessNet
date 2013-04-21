/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.gui;

import cz.cuni.mff.kubatpe1.java.chessnet.game.PieceColor;
import cz.cuni.mff.kubatpe1.java.chessnet.game.Game;
import cz.cuni.mff.kubatpe1.java.chessnet.game.GamePiece;
import cz.cuni.mff.kubatpe1.java.chessnet.game.InvalidMoveException;
import cz.cuni.mff.kubatpe1.java.chessnet.game.GameField;
import cz.cuni.mff.kubatpe1.java.chessnet.*;
import cz.cuni.mff.kubatpe1.java.chessnet.game.EmptyGame;
import cz.cuni.mff.kubatpe1.java.chessnet.game.IChessGame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;


/**
 *
 * @author Ips
 */
public class Chessboard extends JPanel {
    private static final int size = 8;
    
    private ChessboardField selected = null;
    
    private ChessboardField [][] fields;
    
    private IChessGame currentGame;
    
    
    private ChessboardState currentState;
    
    private ResourceLoader res;
    
    private PieceColor currentPlayer;
    
    private JLabel statusLabel;
    
    public Chessboard(ResourceLoader res, IChessGame g, JLabel statusLabel) {
        
        super(new GridLayout(size,size));
        
        this.res = res;
        
        this.statusLabel = statusLabel;
        
        currentGame = g;
        
        currentPlayer = PieceColor.WHITE;
        
        currentState = ChessboardState.FIELD_SELECTION;
        
        Color black = new Color(139,69,19);
        Color white = new Color(245,245,220);
        
        fields = new ChessboardField[size][size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
            {
                if (i % 2 == 0){
                    if (j % 2 == 0) fields[j][i] = new ChessboardField(white, this, j, i);
                    else fields[j][i] = new ChessboardField(black, this, j, i);
                }
                else {
                    if (j % 2 == 0) fields[j][i] = new ChessboardField(black, this, j, i);
                    else fields[j][i] = new ChessboardField(white, this, j, i);
                }
                this.add(fields[j][i]);
                fields[j][i].setIcon(res.getImage(currentGame.getField(j, i).getCurrentPiece()));
                GamePiece currentPiece = currentGame.getField(j, i).getCurrentPiece();
                if (currentPiece != null && currentPiece.getOwner() == currentPlayer){
                    fields[j][i].setAvailability(FieldAvailability.FIELD_SELECTION);
                }
            }        
        }
        if (currentGame.isWaiting()) {
            currentState = ChessboardState.WAITING;
            disableBoard();
        }
        
        //this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        this.setPreferredSize(new Dimension(600,600));
        this.setMinimumSize(new Dimension(600,600));
        this.setMaximumSize(new Dimension(600,600));
    }
    
    public void reset(IChessGame newGame) {
        
        currentGame = newGame;
        currentPlayer = newGame.getCurrentPlayer();
        
        currentState = ChessboardState.FIELD_SELECTION;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
            {
                fields[j][i].setIcon(res.getImage(currentGame.getField(j, i).getCurrentPiece()));
                GamePiece currentPiece = currentGame.getField(j, i).getCurrentPiece();
                if (currentPiece != null && currentPiece.getOwner() == currentPlayer){
                    fields[j][i].setAvailability(FieldAvailability.FIELD_SELECTION);
                }
            }        
        }
        if (currentGame.isWaiting()) {
            currentState = ChessboardState.WAITING;
            disableBoard();
        }
        refresh();
    }
    
    public void setReversed(boolean reversed){
        if (reversed) {
            this.removeAll();
            for (int i = size - 1; i >= 0; i--) {
                for (int j = size - 1; j >= 0; j--) {
                    this.add(fields[j][i]);
                }        
            }
            
            
        }

        else {
            this.removeAll();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    this.add(fields[j][i]);
                }        
            }
        }
        refresh();
        repaint();
        validate();
    }

    private void refresh() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[j][i].setIcon(res.getImage(currentGame.getField(j, i).getCurrentPiece()));
                fields[j][i].repaint();
            }
        }
        refreshStatus();
    }
    
    public void disableBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[i][j].setAvailability(FieldAvailability.NONE);
            }
            
        }
    }
    
    private void markAllFieldSelection() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
            {
                GamePiece currentPiece = currentGame.getField(j, i).getCurrentPiece();
                if (currentPiece != null && currentPiece.getOwner() == currentPlayer){
                    fields[j][i].setAvailability(FieldAvailability.FIELD_SELECTION);
                }
                else
                {
                    fields[j][i].setAvailability(FieldAvailability.NONE);
                }
            }
        }
    }
    
    public void leftClicked(int x, int y) {
        if (currentState == ChessboardState.FIELD_SELECTION) {
            GameField field = currentGame.getField(x, y);
            if (fields[x][y].availableForSelection()) {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++)
                    {
                        fields[i][j].setAvailability(FieldAvailability.NONE);
                    }
                }
                
                List<GameField> toMark = field.getCurrentPiece().getAvailableMoves();
                for (GameField f : toMark) {
                    int x1 = f.getX();
                    int y1 = f.getY();
                    fields[x1][y1].setAvailability(FieldAvailability.MOVE_SELECTION);
                }
                fields[x][y].setAvailability(FieldAvailability.SELECTED);
                selected = fields[x][y];
                currentState = ChessboardState.MOVE_SELECTION;
            }

        }
        else if (currentState == ChessboardState.MOVE_SELECTION) {
            if (fields[x][y].availableForMove()) {
                try {
                    currentGame.movePiece(selected.getIndexX(), selected.getIndexY(), x, y);
                }
                catch (InvalidMoveException e) {
                    // TODO Osetreni chyboveho stavu
                    JOptionPane.showMessageDialog(this, "ERROR", "Invalid move!", JOptionPane.ERROR_MESSAGE);
                }
                currentPlayer = currentGame.getCurrentPlayer();
                if (currentGame.isWaiting()) {
                    currentState = ChessboardState.WAITING;
                    disableBoard();
                }
                else { 
                    currentState = ChessboardState.FIELD_SELECTION;
                    markAllFieldSelection();
                }
                // TODO Zmenit na GameState.WAITING;
                
                refresh();
                if (currentGame.isCheckmate()) {
                    PieceColor winner = currentGame.getCurrentPlayer().oponent();
                    JOptionPane.showMessageDialog(this, "Victory", "Player has won: " + winner, JOptionPane.INFORMATION_MESSAGE);
                    disableBoard();
                }
                else if (currentGame.isStalemate()) {
                    PieceColor winner = currentGame.getCurrentPlayer().oponent();
                    JOptionPane.showMessageDialog(this, "Stalemate", "Game has ended with a stalemate", JOptionPane.INFORMATION_MESSAGE);
                    disableBoard();
                }
            }

        }
        refreshStatus();
    }
    
    public void rightClicked(int x, int y) {
        if (currentState == ChessboardState.MOVE_SELECTION) {
            markAllFieldSelection();
        }
        currentState = ChessboardState.FIELD_SELECTION;
        
   }
    
    
    public void resume() {
        if (currentState != ChessboardState.WAITING) return;
        currentPlayer = currentGame.getCurrentPlayer();
        if (currentGame.isWaiting()) {
            currentState = ChessboardState.WAITING;
            disableBoard();
        }
        else { 
            currentState = ChessboardState.FIELD_SELECTION;
            markAllFieldSelection();
        }
        refresh();
        if (currentGame.isCheckmate()) {
            PieceColor winner = currentGame.getCurrentPlayer().oponent();
            JOptionPane.showMessageDialog(this, "Victory", "Player has won: " + winner, JOptionPane.INFORMATION_MESSAGE);
            disableBoard();
        }
        else if (currentGame.isStalemate()) {
            PieceColor winner = currentGame.getCurrentPlayer().oponent();
            JOptionPane.showMessageDialog(this, "Stalemate", "Game has ended with a stalemate", JOptionPane.INFORMATION_MESSAGE);
            disableBoard();
        }
        refreshStatus();
    }
    
    private void refreshStatus() {
        if (currentGame instanceof EmptyGame) {
            statusLabel.setText("");
            return;
        }
        if (currentGame.isCheckmate()) {
            PieceColor winner = currentGame.getCurrentPlayer();
            statusLabel.setText("<html><b>Checkmate</b><br>Player has won: " + winner + "<br>Start new game from the menu!</html>");
        }
        else if (currentGame.isStalemate()) {
            statusLabel.setText("<html><b>Stalemate!</b><br>Start new game from the menu!</html>");
        }
        else {
            String rest;
            if (!currentGame.isWaiting()) {
                rest = "It's your turn! Move some piece.";
            }
            else {
                rest = "It's oponent's turn! Wait for his move.";
            }
            statusLabel.setText("<html><b>Playing game</b><br>Current player: " + currentGame.getCurrentPlayer() + "<br>" + rest + "</html>");
        }
        
    }

}
