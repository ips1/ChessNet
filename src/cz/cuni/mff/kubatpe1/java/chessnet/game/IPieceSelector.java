/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.game;

/**
 *
 * @author Ips
 */
public interface IPieceSelector {
    public GamePiece selectAndPlace(Game currentGame, GameField targetField, PieceColor owner);
}
