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
public class King extends GamePiece {

    public King(Game currentGame, GameField currentField, PieceColor owner) {
        super(currentGame, currentField, owner);
    }

    
    @Override
    public String toString() {
        return owner + " King";
    }

    private void tryAddField(Game currentGame, GameField field, List<GameField> targetList) {
        if (field == null ) return;
        if (field.getCurrentPiece() == null || field.getCurrentPiece().getOwner() != owner) {
            targetList.add(field);
        }
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
        tryAddField(currentGame, fieldAt, reachableFields);
        
        i = x + 1;
        j = y;
        fieldAt = currentGame.getField(i, j);
        tryAddField(currentGame, fieldAt, reachableFields);
        
        i = x + 1;
        j = y - 1;
        fieldAt = currentGame.getField(i, j);
        tryAddField(currentGame, fieldAt, reachableFields);
        
        i = x;
        j = y + 1;
        fieldAt = currentGame.getField(i, j);
        tryAddField(currentGame, fieldAt, reachableFields);
        
        i = x;
        j = y - 1;
        fieldAt = currentGame.getField(i, j);
        tryAddField(currentGame, fieldAt, reachableFields);
        
        i = x - 1;
        j = y + 1;
        fieldAt = currentGame.getField(i, j);
        tryAddField(currentGame, fieldAt, reachableFields);
        
        i = x - 1;
        j = y;
        fieldAt = currentGame.getField(i, j);
        tryAddField(currentGame, fieldAt, reachableFields);
        
        i = x - 1;
        j = y - 1;
        fieldAt = currentGame.getField(i, j);
        tryAddField(currentGame, fieldAt, reachableFields);
        

        
       return reachableFields;
    }
    
    
    @Override
    public void move(GameField target, boolean silent) throws InvalidMoveException {
        if (target.getCurrentPiece() instanceof Rook && target.getCurrentPiece().getOwner() == owner) {
            // CASTLING
            if (!silent) {
                List<GameField> available = getAvailableMoves();
                if (!available.contains(target)) throw new InvalidMoveException();
            }
            GameField newRookField;
            GameField newKingField;
            if (target.getX() == currentField.getX() - 4) {
                newRookField = currentGame.getField(currentField.getX() - 1, currentField.getY());
                newKingField = currentGame.getField(currentField.getX() - 2, currentField.getY());
            }
            else if (target.getX() == currentField.getX() + 3) {
                newRookField = currentGame.getField(currentField.getX() + 1, currentField.getY());
                newKingField = currentGame.getField(currentField.getX() + 2, currentField.getY());
            }
            else throw new InvalidMoveException();

            if (!silent) {
                currentGame.log(currentGame.getMoveCode() + ": " + owner + " CASTLING from " + (char)(currentField.getX() + 65) + (currentField.getY() +1)  + " to " + (char)(newKingField.getX() + 65) + (newKingField.getY()+1));
                currentGame.record(currentGame.getMoveNumber() + ": " + currentField.getX() + " " + currentField.getY() + " > " + target.getX() + " " + target.getY());
            }
            
            GamePiece rook = target.getCurrentPiece();
            rook.move(newRookField, true);
            super.move(newKingField, true);

        }
        else {
            super.move(target, silent);
        }     
        
    }
    
    
    @Override
    public List<GameField> getAvailableMoves() {
        List<GameField> availableMoves = super.getAvailableMoves();
        
        // CASTLING
        int x = currentField.getX();
        int y = currentField.getY();
        GameField fieldAt = null;
        if (!wasMoved && currentGame.isKingSafe(owner)) {
            // KINGSIDE CASTLING
            GameField rookField = currentGame.getField(x + 3, y);
            GamePiece piece = rookField.getCurrentPiece();
            if (piece instanceof Rook) {
                Rook rook = (Rook)piece;
                if (!rook.wasMoved()) {
                    boolean canMove = true;
                    for (int i = x+1; i <= x + 2; i++) {
                        // Fields between King and Rook are empty
                        fieldAt = currentGame.getField(i, y);
                        if (fieldAt.getCurrentPiece() != null) {
                            canMove = false;
                            break;
                        }
                        // Fields between King and Rook are safe
                        else {
                            if (!currentGame.isSafeField(owner, fieldAt)) {
                                canMove = false;
                                break;
                            }
                        }
                    }
                    if (canMove) {
                        availableMoves.add(rookField);
                    }
                }
            }
            
            // QUEENSIDE CASTLING
            rookField = currentGame.getField(x - 4, y);
            piece = rookField.getCurrentPiece();
            if (piece instanceof Rook) {
                Rook rook = (Rook)piece;
                if (!rook.wasMoved()) {
                    boolean canMove = true;
                    for (int i = x-1; i >= x - 2; i--) {
                        // Fields between King and Rook are empty
                        fieldAt = currentGame.getField(i, y);
                        if (fieldAt.getCurrentPiece() != null) {
                            canMove = false;
                            break;
                        }
                        // Fields between King and Rook are safe
                        else {
                            if (!currentGame.isSafeField(owner, fieldAt)) {
                                canMove = false;
                                break;
                            }
                        }
                    }
                    fieldAt = currentGame.getField(x-3, y);
                    if (fieldAt.getCurrentPiece() != null) {
                        canMove = false;
                    }
                    if (canMove) {
                        availableMoves.add(rookField);
                    }
                }
            }
            
        }
        return availableMoves;
    }
    
}
