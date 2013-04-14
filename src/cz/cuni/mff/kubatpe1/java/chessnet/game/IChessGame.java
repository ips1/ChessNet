/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.game;

/**
 *
 * @author Ips
 */
public interface IChessGame {
    public void movePiece(int sourceX, int sourceY, int targetX, int targetY) throws InvalidMoveException;
    public boolean isWaiting();
    public GameField getField(int x, int y);
    public PieceColor getCurrentPlayer();
    public boolean isCheckmate();
    public boolean isStalemate();
}
