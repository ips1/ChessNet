/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet;

import cz.cuni.mff.kubatpe1.java.chessnet.game.PieceColor;
import cz.cuni.mff.kubatpe1.java.chessnet.game.Game;
import cz.cuni.mff.kubatpe1.java.chessnet.game.InvalidMoveException;
import cz.cuni.mff.kubatpe1.java.chessnet.game.IChessGame;
import cz.cuni.mff.kubatpe1.java.chessnet.game.GameField;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Pawn;
import cz.cuni.mff.kubatpe1.java.chessnet.gui.Chessboard;
import cz.cuni.mff.kubatpe1.java.connection.Connection;

/**
 *
 * @author Ips
 */
public class GameSynchronizer implements IChessGame {
    private Game currentGame;
    private PieceColor player;
    private Connection currentConnection;
    private boolean waitingForResponse;
    
    private int tmpSourceX;
    private int tmpSourceY;
    private int tmpTargetX;
    private int tmpTargetY;
    
    private Chessboard gameBoard;

    public GameSynchronizer(Game currentGame, PieceColor player, Connection currentConnection) {
        this.currentGame = currentGame;
        this.player = player;
        this.currentConnection = currentConnection;
        waitingForResponse = false;
    }   
    
    public void setChessboard(Chessboard gameBoard) {
        this.gameBoard = gameBoard;
    }
    
    @Override
    public void movePiece(int sourceX, int sourceY, int targetX, int targetY) throws InvalidMoveException {
        if (currentGame.getCurrentPlayer() != player) throw new InvalidMoveException();
        tmpSourceX = sourceX;
        tmpSourceY = sourceY;
        tmpTargetX = targetX;
        tmpTargetY = targetY;
        currentConnection.send("MV|" + sourceX + "|" + sourceY + "|" + targetX + "|" + targetY);
        waitingForResponse = true;
    } 
    
    public void movementConfirmed() throws InvalidMoveException {
        if (!waitingForResponse) throw new InvalidMoveException();
        waitingForResponse = false;
        
        boolean selection = false;
                
        if (currentGame.getField(tmpSourceX, tmpSourceY).getCurrentPiece() instanceof Pawn) {
            if ((tmpSourceY < tmpTargetY && tmpTargetY == currentGame.ROWS - 1) || (tmpSourceY > tmpTargetY && tmpTargetY == 0)) {
                selection = true;
                
            }
        }
        
        currentGame.movePiece(tmpSourceX, tmpSourceY, tmpTargetX, tmpTargetY);

        if (gameBoard != null) {
            gameBoard.resume();
        }
        
        if (selection) {
            currentConnection.send("PIECESEL|" + currentGame.getField(tmpTargetX,tmpTargetY).getCurrentPiece().toString());
        }
    }
    
    public void invokeMove(int sourceX, int sourceY, int targetX, int targetY) throws InvalidMoveException {
        if (waitingForResponse) throw new InvalidMoveException();
        
        // will be a piece selection
        if (currentGame.getField(sourceX, sourceY).getCurrentPiece() instanceof Pawn) {
            if ((sourceY < targetY && targetY == currentGame.ROWS - 1) || (sourceY > targetY && targetY == 0)) {
                tmpSourceX = sourceX;
                tmpSourceY = sourceY;
                tmpTargetX = targetX;
                tmpTargetY = targetY;
                currentConnection.send("MVCONFIRM");
                return;
            }
        }
        currentGame.movePiece(sourceX, sourceY, targetX, targetY);        
        if (gameBoard != null) {
            gameBoard.resume();
        }
        currentConnection.send("MVCONFIRM");
    }
    
    public void resumeMove() throws InvalidMoveException {
        currentGame.movePiece(tmpSourceX, tmpSourceY, tmpTargetX, tmpTargetY);        
        if (gameBoard != null) {
            gameBoard.resume();
        }
    }

    @Override
    public boolean isWaiting() {
        return (waitingForResponse || (currentGame.getCurrentPlayer() != player));
    }

    @Override
    public GameField getField(int x, int y) {
        return currentGame.getField(x, y);
    }

    @Override
    public PieceColor getCurrentPlayer() {
        return currentGame.getCurrentPlayer();
    }

    @Override
    public boolean isCheckmate() {
        return currentGame.isCheckmate();
    }

    @Override
    public boolean isStalemate() {
        return currentGame.isStalemate();
    }
}
