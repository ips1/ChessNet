/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.game;

/**
 *
 * @author Ips
 */
public class EmptyGame implements IChessGame {

    private GameField emptyField;
    
    public EmptyGame() {
        emptyField = new GameField(0,0);
        
    }
    
    

    @Override
    public void movePiece(int sourceX, int sourceY, int targetX, int targetY) throws InvalidMoveException {
        
    }

    @Override
    public boolean isWaiting() {
        return true;
    }

    @Override
    public GameField getField(int x, int y) {
        return emptyField;
    }

    @Override
    public PieceColor getCurrentPlayer() {
        return PieceColor.WHITE;
    }

    @Override
    public boolean isCheckmate() {
        return false;
    }

    @Override
    public boolean isStalemate() {
        return false;
    }
    
}
