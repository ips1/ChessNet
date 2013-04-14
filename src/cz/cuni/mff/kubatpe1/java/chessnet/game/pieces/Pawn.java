/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.game.pieces;

import cz.cuni.mff.kubatpe1.java.chessnet.game.PieceColor;
import cz.cuni.mff.kubatpe1.java.chessnet.game.Game;
import cz.cuni.mff.kubatpe1.java.chessnet.game.GameField;
import cz.cuni.mff.kubatpe1.java.chessnet.game.GamePiece;
import cz.cuni.mff.kubatpe1.java.chessnet.game.InvalidMoveException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ips
 */
public class Pawn extends GamePiece {


    
     public Pawn(Game currentGame, GameField currentField, PieceColor owner) {
        super(currentGame, currentField, owner);
    }
     
    @Override
    public void move(GameField target, boolean silent) throws InvalidMoveException {
        GameField tmpField = currentField;
        super.move(target, silent);
        if (Math.abs(tmpField.getY() - currentField.getY()) == 2) {
            int middleFieldY = tmpField.getY() - ((tmpField.getY() - currentField.getY())/2);
            GameField middleField = currentGame.getField(tmpField.getX(), middleFieldY);
            middleField.setEnPassantTarget(this);
        }
        
        if (tmpField.getY() < currentField.getY() && currentField.getY() == currentGame.ROWS - 1) {
            GamePiece substitute = currentGame.getSelecetor(owner).selectAndPlace(currentGame, currentField, owner);
            currentField.removePiece();
            currentField.placePiece(substitute);
            currentGame.log(this + " switched for " + substitute);
        }
        else if (tmpField.getY() > currentField.getY() && currentField.getY() == 0) {
            GamePiece substitute = currentGame.getSelecetor(owner).selectAndPlace(currentGame, currentField, owner);
            currentField.removePiece();
            currentField.placePiece(substitute);
            currentGame.log(this + " switched for " + substitute);
        }

        
        
    }

    
    @Override
    public String toString() {
        return owner + " Pawn";
    }
    
    /*
    public boolean isEnPassantAvailable() {
        return (enPassantMoveNumber == currentGame.getMoveNumber());
    }
    * */
    
    @Override
    public List<GameField> getReachableFields(Game currentGame) {
        int x = currentField.getX();
        int y = currentField.getY();
        
        int i;
        int j;
        GameField fieldAt;
        List<GameField> reachableFields = new ArrayList<GameField>();
        int n;
        if (owner == PieceColor.WHITE) {
            // moves upward
            n = -1;
        }
        else {
            // moves downward
            n = +1;
        }
        fieldAt = currentGame.getField(x, y+n);
        if (fieldAt != null && fieldAt.getCurrentPiece() == null) {
            reachableFields.add(fieldAt);
            fieldAt = currentGame.getField(x, y+(2*n));
            if (wasMoved == false && fieldAt != null && fieldAt.getCurrentPiece() == null) {
                reachableFields.add(fieldAt);
            }
        }
        fieldAt = currentGame.getField(x+1, y+n);
        if (fieldAt != null) {
            GamePiece targetPiece = fieldAt.getCurrentPiece();
            if (targetPiece != null && targetPiece.getOwner() != owner) {
                reachableFields.add(fieldAt);
            }
            else if (targetPiece == null && fieldAt.getEnPassantTarget() != null && fieldAt.getEnPassantTarget().getOwner() != owner) {
                reachableFields.add(fieldAt);
            }

        }

        
        fieldAt = currentGame.getField(x-1, y+n);
        if (fieldAt != null) {
            GamePiece targetPiece = fieldAt.getCurrentPiece();
            if (targetPiece != null && targetPiece.getOwner() != owner) {
                reachableFields.add(fieldAt);
            }
            else if (targetPiece == null && fieldAt.getEnPassantTarget() != null && fieldAt.getEnPassantTarget().getOwner() != owner) {
                reachableFields.add(fieldAt);
            }

        }
        
        return reachableFields;
    }
    
}
