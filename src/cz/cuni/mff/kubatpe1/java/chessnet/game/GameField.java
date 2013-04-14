/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.game;

/**
 *
 * @author Ips
 */
public class GameField {
    private GamePiece currentPiece;
    private GamePiece oldPiece;
    private GamePiece enPassantTarget;
    private GamePiece oldEnPassantTarget;
    private int enPassantCounter = 0;
    private boolean temporaryState;
    private int x;
    private int y;

    public GameField(int x, int y) {
        this.x = x;
        this.y = y;
        temporaryState = false;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void removePiece() {
        currentPiece = null;
    }
    
    public void placePiece(GamePiece newPiece) {
        if (currentPiece != null) {
            currentPiece.taken(newPiece, false);
        }
        if (enPassantTarget != null) {
            enPassantTarget.taken(newPiece, true);
            enPassantTarget.getCurrentField().removePiece();
            enPassantTarget = null;
        }
        currentPiece = newPiece;
    }
    
    public void temporaryPlacePiece(GamePiece newPiece) {
        oldPiece = currentPiece;
        oldEnPassantTarget = enPassantTarget;
        currentPiece = newPiece;  
        if (enPassantTarget != null) {
            enPassantTarget.getCurrentField().temporaryRemovePiece();
        }
        enPassantTarget = null;
        temporaryState = true;
    }
    
    public void temporaryRemovePiece() {
        oldPiece = currentPiece;
        currentPiece = null; 
        temporaryState = true;
    }
    
    public void resetTemporaryState() {
        if (!temporaryState) return;
        currentPiece = oldPiece;
        enPassantTarget = oldEnPassantTarget;
        if (enPassantTarget != null) {
            enPassantTarget.getCurrentField().resetTemporaryState();
        }
        temporaryState = false;
    }
    
    public void setEnPassantTarget(GamePiece target) {
        if (currentPiece == null) {
            enPassantTarget = target;
            enPassantCounter = 2;
        }
    }
    
    public void dropEnPassantTarget() {
        enPassantCounter--;
        if (enPassantCounter <= 0) {
            enPassantCounter = 0;
            enPassantTarget = null;
        }
    }
    
    public GamePiece getEnPassantTarget() {
        return enPassantTarget;
    }
    
    
    
    
    public GamePiece getCurrentPiece() {
        return currentPiece;
    }
}
