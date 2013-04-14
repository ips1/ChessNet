/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.game;

import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Pawn;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ips
 */
public abstract class GamePiece {
    
    protected GameField currentField;
    protected GameField oldField;
    protected PieceColor owner;
    protected boolean wasMoved;
    protected Game currentGame;
   
    

    public abstract List<GameField> getReachableFields(Game currentGame);

    public GamePiece(Game currentGame, GameField currentField, PieceColor owner) {
        this.currentGame = currentGame;
        this.currentField = currentField;
        this.owner = owner;
        wasMoved = false;
    }    
    
    public PieceColor getOwner() {
        return owner;
    }
    
    /**
     * 
     * @param target
     * @param currentGame
     * @throws InvalidMoveException 
     */
    public void move(GameField target, boolean silent) throws InvalidMoveException {
        if (!silent) {
            List<GameField> available = getAvailableMoves();
            if (!available.contains(target)) throw new InvalidMoveException();
        }

        if (!silent) {
            currentGame.log(currentGame.getMoveCode() + ": " + toString() + " from " + (char)(currentField.getX() + 65) + (currentField.getY() +1)  + " to " + (char)(target.getX() + 65) + (target.getY()+1));
            currentGame.record(currentGame.getMoveNumber() + ": " + currentField.getX() + " " + currentField.getY() + " > " + target.getX() + " " + target.getY());
        }
        currentField.removePiece();
        currentField = target;
        currentField.placePiece(this);

        wasMoved = true;
    }
    
    protected void temporaryMove(GameField target) {
        oldField = currentField;
        currentField.temporaryRemovePiece();
        currentField = target;
        currentField.temporaryPlacePiece(this);
    }
    
    protected void temporaryReturn() {
        currentField.resetTemporaryState();
        currentField = oldField;
        currentField.resetTemporaryState();
        oldField = null;
    }
    
    public List<GameField> getAvailableMoves() {
        List<GameField> reachable = getReachableFields(currentGame);
        List<GameField> availableFields = new ArrayList<GameField>();
        boolean empty = true;
        for (GameField f: reachable) {
            temporaryMove(f);
            if (currentGame.isKingSafe(owner)) {
                availableFields.add(f);
                empty = false;
            }
            temporaryReturn();
        }
        return availableFields;
    }
    
    
    public void taken(GamePiece newPiece, boolean enPassant) {
        if (enPassant) {
            currentGame.log(this + " taken by " + newPiece + " e. p.");
        }
        else {
            currentGame.log(this + " taken by " + newPiece);
        }
        
    }
    
    public GameField getCurrentField() {
        return currentField;
    }
    
    public boolean wasMoved() {
        return wasMoved;
    }
    
}
