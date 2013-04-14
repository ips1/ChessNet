/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.game;

/**
 *
 * @author Ips
 */
public enum PieceColor {
    BLACK, WHITE;
    public PieceColor oponent() {
        if (this == BLACK) return WHITE;
        else return BLACK;
    }
}
