/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.gui;

import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Pawn;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Rook;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Queen;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.King;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Knight;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Bishop;
import cz.cuni.mff.kubatpe1.java.chessnet.game.GamePiece;
import cz.cuni.mff.kubatpe1.java.chessnet.game.PieceColor;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Ips
 */
public class ResourceLoader {
    public Image pawnBlack;
    public Image pawnWhite;
    public Image knightBlack;
    public Image knightWhite;
    public Image bishopBlack;
    public Image bishopWhite;
    public Image rookBlack;
    public Image rookWhite;
    public Image queenBlack;
    public Image queenWhite;
    public Image kingBlack;
    public Image kingWhite;

    public ResourceLoader() {
        
        pawnBlack = new ImageIcon( getClass().getResource("img/chess_pieces_06.png") ).getImage();
        pawnWhite = new ImageIcon( getClass().getResource("img/chess_pieces_12.png") ).getImage();
        knightBlack = new ImageIcon( getClass().getResource("img/chess_pieces_05.png") ).getImage();
        knightWhite = new ImageIcon( getClass().getResource("img/chess_pieces_11.png") ).getImage();
        bishopBlack = new ImageIcon( getClass().getResource("img/chess_pieces_02.png") ).getImage();
        bishopWhite = new ImageIcon( getClass().getResource("img/chess_pieces_08.png") ).getImage();
        rookBlack = new ImageIcon( getClass().getResource("img/chess_pieces_01.png") ).getImage();
        rookWhite = new ImageIcon( getClass().getResource("img/chess_pieces_07.png") ).getImage();
        queenBlack = new ImageIcon( getClass().getResource("img/chess_pieces_03.png") ).getImage();
        queenWhite = new ImageIcon( getClass().getResource("img/chess_pieces_09.png") ).getImage();
        kingBlack = new ImageIcon( getClass().getResource("img/chess_pieces_04.png") ).getImage();
        kingWhite = new ImageIcon( getClass().getResource("img/chess_pieces_10.png") ).getImage();        
    }
    
    public Image getImage(GamePiece piece) {
        if (piece == null) return null;
        
        if (piece.getOwner() == PieceColor.BLACK) {
            if (Bishop.class.isInstance(piece)) return bishopBlack;
            if (King.class.isInstance(piece)) return kingBlack;
            if (Knight.class.isInstance(piece)) return knightBlack;
            if (Pawn.class.isInstance(piece)) return pawnBlack;
            if (Queen.class.isInstance(piece)) return queenBlack;
            if (Rook.class.isInstance(piece)) return rookBlack;
        }
        
        else {
            if (Bishop.class.isInstance(piece)) return bishopWhite;
            if (King.class.isInstance(piece)) return kingWhite;
            if (Knight.class.isInstance(piece)) return knightWhite;
            if (Pawn.class.isInstance(piece)) return pawnWhite;
            if (Queen.class.isInstance(piece)) return queenWhite;
            if (Rook.class.isInstance(piece)) return rookWhite;
        }
        
        return null;
    }
    
    
}
