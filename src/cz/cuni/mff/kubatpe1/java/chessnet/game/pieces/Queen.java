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
public class Queen extends GamePiece {

    public Queen(Game currentGame, GameField currentField, PieceColor owner) {
        super(currentGame, currentField, owner);
    }

    @Override
    public String toString() {
        return owner + " Queen";
    }
    
    @Override
    public List<GameField> getReachableFields(Game currentGame) {
        int x = currentField.getX();
        int y = currentField.getY();
        
        int i;
        int j;
        GameField fieldAt;
        List<GameField> reachableFields = new ArrayList<GameField>();
        
        i = x + 1;
        j = y + 1;
        fieldAt = currentGame.getField(i, j);
        while (fieldAt != null) {
            if (fieldAt.getCurrentPiece() != null && fieldAt.getCurrentPiece().getOwner() == owner) break;
            reachableFields.add(fieldAt);
            if (fieldAt.getCurrentPiece() != null) break;
            fieldAt = currentGame.getField(++i, ++j);
        }
        
        i = x - 1;
        j = y + 1;
        fieldAt = currentGame.getField(i, j);
        while (fieldAt != null) {
            if (fieldAt.getCurrentPiece() != null && fieldAt.getCurrentPiece().getOwner() == owner) break;
            reachableFields.add(fieldAt);
            if (fieldAt.getCurrentPiece() != null) break;
            fieldAt = currentGame.getField(--i, ++j);
        }
        
        i = x - 1;
        j = y - 1;
        fieldAt = currentGame.getField(i, j);
        while (fieldAt != null) {
            if (fieldAt.getCurrentPiece() != null && fieldAt.getCurrentPiece().getOwner() == owner) break;
            reachableFields.add(fieldAt);
            if (fieldAt.getCurrentPiece() != null) break;
            fieldAt = currentGame.getField(--i, --j);
        }
        
        i = x + 1;
        j = y - 1;
        fieldAt = currentGame.getField(i, j);
        while (fieldAt != null) {
            if (fieldAt.getCurrentPiece() != null && fieldAt.getCurrentPiece().getOwner() == owner) break;
            reachableFields.add(fieldAt);
            if (fieldAt.getCurrentPiece() != null) break;
            fieldAt = currentGame.getField(++i, --j);
        }
        
                i = x + 1;
        j = y;
        fieldAt = currentGame.getField(i, j);
        while (fieldAt != null) {
            if (fieldAt.getCurrentPiece() != null && fieldAt.getCurrentPiece().getOwner() == owner) break;
            reachableFields.add(fieldAt);
            if (fieldAt.getCurrentPiece() != null) break;
            fieldAt = currentGame.getField(++i, j);
        }
        
        i = x - 1;
        j = y;
        fieldAt = currentGame.getField(i, j);
        while (fieldAt != null) {
            if (fieldAt.getCurrentPiece() != null && fieldAt.getCurrentPiece().getOwner() == owner) break;
            reachableFields.add(fieldAt);
            if (fieldAt.getCurrentPiece() != null) break;
            fieldAt = currentGame.getField(--i, j);
        }
        
        i = x;
        j = y + 1;
        fieldAt = currentGame.getField(i, j);
        while (fieldAt != null) {
            if (fieldAt.getCurrentPiece() != null && fieldAt.getCurrentPiece().getOwner() == owner) break;
            reachableFields.add(fieldAt);
            if (fieldAt.getCurrentPiece() != null) break;
            fieldAt = currentGame.getField(i, ++j);
        }
        
        i = x;
        j = y - 1;
        fieldAt = currentGame.getField(i, j);
        while (fieldAt != null) {
            if (fieldAt.getCurrentPiece() != null && fieldAt.getCurrentPiece().getOwner() == owner) break;
            reachableFields.add(fieldAt);
            if (fieldAt.getCurrentPiece() != null) break;
            fieldAt = currentGame.getField(i, --j);
        }
        
        return reachableFields;
    }
    
}
