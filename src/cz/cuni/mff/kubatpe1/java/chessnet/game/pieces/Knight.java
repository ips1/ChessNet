/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.game.pieces;

import cz.cuni.mff.kubatpe1.java.chessnet.game.PieceColor;
import cz.cuni.mff.kubatpe1.java.chessnet.game.Game;
import cz.cuni.mff.kubatpe1.java.chessnet.game.GameField;
import cz.cuni.mff.kubatpe1.java.chessnet.game.GamePiece;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ips
 */
public class Knight extends GamePiece {

    public Knight(Game currentGame, GameField currentField, PieceColor owner) {
        super(currentGame, currentField, owner);
    }

    @Override
    public String toString() {
        return owner + " Knight";
    }
    
    @Override
    public List<GameField> getReachableFields(Game currentGame) {
        int x = currentField.getX();
        int y = currentField.getY();
        
        int i;
        int j;
        GameField fieldAt;
        List<GameField> reachableFields = new ArrayList<GameField>();

        fieldAt = currentGame.getField(x+1, y+2);
        if (fieldAt != null && (fieldAt.getCurrentPiece() == null || fieldAt.getCurrentPiece().getOwner() != owner)) {
            reachableFields.add(fieldAt);
        }
        fieldAt = currentGame.getField(x+1, y-2);
        if (fieldAt != null && (fieldAt.getCurrentPiece() == null || fieldAt.getCurrentPiece().getOwner() != owner)) {
            reachableFields.add(fieldAt);
        }
        fieldAt = currentGame.getField(x-1, y+2);
        if (fieldAt != null && (fieldAt.getCurrentPiece() == null || fieldAt.getCurrentPiece().getOwner() != owner)) {
            reachableFields.add(fieldAt);
        }
        fieldAt = currentGame.getField(x-1, y-2);
        if (fieldAt != null && (fieldAt.getCurrentPiece() == null || fieldAt.getCurrentPiece().getOwner() != owner)) {
            reachableFields.add(fieldAt);
        }
        
        fieldAt = currentGame.getField(x+2, y+1);
        if (fieldAt != null && (fieldAt.getCurrentPiece() == null || fieldAt.getCurrentPiece().getOwner() != owner)) {
            reachableFields.add(fieldAt);
        }
        fieldAt = currentGame.getField(x+2, y-1);
        if (fieldAt != null && (fieldAt.getCurrentPiece() == null || fieldAt.getCurrentPiece().getOwner() != owner)) {
            reachableFields.add(fieldAt);
        }
        fieldAt = currentGame.getField(x-2, y+1);
        if (fieldAt != null && (fieldAt.getCurrentPiece() == null || fieldAt.getCurrentPiece().getOwner() != owner)) {
            reachableFields.add(fieldAt);
        }
        fieldAt = currentGame.getField(x-2, y-1);
        if (fieldAt != null && (fieldAt.getCurrentPiece() == null || fieldAt.getCurrentPiece().getOwner() != owner)) {
            reachableFields.add(fieldAt);
        }
        return reachableFields;
    }
    
}
