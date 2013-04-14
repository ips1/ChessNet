/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.game;

import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Rook;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Pawn;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Queen;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.King;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Knight;
import cz.cuni.mff.kubatpe1.java.chessnet.game.pieces.Bishop;
import cz.cuni.mff.kubatpe1.java.chessnet.recording.ConsoleRecorder;
import cz.cuni.mff.kubatpe1.java.chessnet.recording.FileRecorder;
import java.util.List;

/**
 *
 * @author Ips
 */
public class Game implements IChessGame {
    
    private GameField[][] chessboard;
    public final int ROWS = 8;
    public final int COLS = 8;
    
    private int moveNumber;
    
    private PieceColor currentPlayer;
    
    private PieceColor checked;
    
    /*
    private boolean waiting;
    private boolean finished;
    */
    
    private GameState currentState;
    
    private IWriter gameRecorder;
    private IWriter gameLogger;
    
    private King blackKing;
    private King whiteKing;
    
    private IPieceSelector whiteSelector;
    private IPieceSelector blackSelector;

    public Game(IPieceSelector whiteSelector, IPieceSelector blackSelector) {
        chessboard = new GameField[COLS][ROWS];
        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                chessboard[i][j] = new GameField(i, j);
            }
        }
        setDefaultPieces();
        currentPlayer = PieceColor.WHITE;
        moveNumber = 1;
        currentState = GameState.RUNNING;
        
        // TODO Nastaveni recorderu
        try {
            gameRecorder = new FileRecorder("record.txt");
        }
        catch (Exception e) {
            gameRecorder = new ConsoleRecorder();
        }
        gameLogger = new ConsoleRecorder();
        
        this.whiteSelector = whiteSelector;
        this.blackSelector = blackSelector;
    }
    
    public void setLogger(IWriter newLogger) {
        if (newLogger != null) {
            gameLogger = newLogger;
        }
    }
       
    public void endRound() {
        
    }
    
    /*
    public boolean isSafeField(PieceColor player, GameField field, boolean exceptKing) {
        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                GamePiece p = chessboard[i][j].getCurrentPiece();
                if (p != null && p.getOwner() != player && (!exceptKing || (!King.class.isInstance(p)))) {
                    List<GameField> endangered = p.getReachableFields(this, true);
                    if (endangered.contains(field)) return false;
                }
            }
        }
        return true;
    }
    * */
    
    public boolean isKingSafe(PieceColor player) {
        PieceColor oponent;
        King playerKing;
        if (player == PieceColor.BLACK) {
            oponent = PieceColor.WHITE;
            playerKing = blackKing;
        }
        else {
            oponent = PieceColor.BLACK;
            playerKing = whiteKing;
        }
        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                GamePiece p = chessboard[i][j].getCurrentPiece();
                if (p != null && p.getOwner() != player) {
                    List<GameField> endangered = p.getReachableFields(this);
                    if (endangered.contains(playerKing.getCurrentField())) return false;
                }
            }
        }
        return true;
    }
    
    public boolean isSafeField(PieceColor player, GameField field) {
        PieceColor oponent = player.oponent();
        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                GamePiece p = chessboard[i][j].getCurrentPiece();
                if (p != null && p.getOwner() != player) {
                    List<GameField> endangered = p.getReachableFields(this);
                    if (endangered.contains(field)) return false;
                }
            }
        }
        return true;
    }
    
    public GameField getField(int x, int y) {
        if (x < 0 || x >= COLS) return null;
        if (y < 0 || y >= ROWS) return null;
        return chessboard[x][y];
    }
    
    private void setDefaultPieces() {
        chessboard[0][0].placePiece(new Rook(this, chessboard[0][0], PieceColor.BLACK));
        chessboard[1][0].placePiece(new Knight(this, chessboard[1][0], PieceColor.BLACK));
        chessboard[2][0].placePiece(new Bishop(this, chessboard[2][0], PieceColor.BLACK));
        chessboard[3][0].placePiece(new Queen(this, chessboard[3][0], PieceColor.BLACK));
        blackKing = new King(this, chessboard[4][0], PieceColor.BLACK);
        chessboard[4][0].placePiece(blackKing);
        chessboard[5][0].placePiece(new Bishop(this, chessboard[5][0], PieceColor.BLACK));
        chessboard[6][0].placePiece(new Knight(this, chessboard[6][0], PieceColor.BLACK));
        chessboard[7][0].placePiece(new Rook(this, chessboard[7][0], PieceColor.BLACK));
        
        for (int i = 0; i < 8; i++) {
            chessboard[i][1].placePiece(new Pawn(this, chessboard[i][1], PieceColor.BLACK));
        }
        
        chessboard[0][7].placePiece(new Rook(this, chessboard[0][7], PieceColor.WHITE));
        chessboard[1][7].placePiece(new Knight(this, chessboard[1][7], PieceColor.WHITE));
        chessboard[2][7].placePiece(new Bishop(this, chessboard[2][7], PieceColor.WHITE));
        chessboard[3][7].placePiece(new Queen(this, chessboard[3][7], PieceColor.WHITE));
        whiteKing = new King(this, chessboard[4][7], PieceColor.WHITE);
        chessboard[4][7].placePiece(whiteKing);
        chessboard[5][7].placePiece(new Bishop(this, chessboard[5][7], PieceColor.WHITE));
        chessboard[6][7].placePiece(new Knight(this, chessboard[6][7], PieceColor.WHITE));
        chessboard[7][7].placePiece(new Rook(this, chessboard[7][7], PieceColor.WHITE));
        
                
        for (int i = 0; i < 8; i++) {
            chessboard[i][6].placePiece(new Pawn(this, chessboard[i][6], PieceColor.WHITE));
        }
        
        // DELETE
        /*
        whiteKing = new King(chessboard[4][4], PieceColor.WHITE);
        chessboard[4][4].placePiece(whiteKing);
        chessboard[4][2].placePiece(new King(chessboard[4][2], PieceColor.BLACK));
        chessboard[5][3].placePiece(new Pawn(chessboard[5][3], PieceColor.WHITE));
        chessboard[6][2].placePiece(new Bishop(chessboard[6][2], PieceColor.BLACK));
        */
        
    }

    @Override
    public void movePiece(int sourceX, int sourceY, int targetX, int targetY) throws InvalidMoveException {
        if (currentState != GameState.RUNNING) throw new InvalidMoveException();
        GamePiece sourcePiece = chessboard[sourceX][sourceY].getCurrentPiece();
        if (sourcePiece.getOwner() != currentPlayer) throw new InvalidMoveException();
        
        GameField targetField = chessboard[targetX][targetY];
        sourcePiece.move(targetField, false);

        currentPlayer = currentPlayer.oponent();
       
        if(!isKingSafe(currentPlayer)) {
            checked = currentPlayer;
            log("CHECK - " + currentPlayer + " King by " + sourcePiece.toString());
        }
        else if (!isKingSafe(currentPlayer.oponent())) {
            checked = currentPlayer.oponent();
            log("CHECK - " + currentPlayer.oponent() + " King by " + sourcePiece.toString());
        }
        else {
            checked = null;
        }
        
        if(!hasAvailableMoves(currentPlayer)) {
            if(!isKingSafe(currentPlayer)) {
                currentState = GameState.CHECKMATE;
                log("MATE - " + currentPlayer + " wins");
            }
            else {
                currentState = GameState.STALEMATE;
                log("STALEMATE");
            }
        }      
        
        moveNumber++;
        dropEnPassantTargets();
    }
    
    public void log(String message) {
        gameLogger.write(message);
    }
    
    public void record(String message) {
        gameRecorder.write(message);
    }
    
    private void dropEnPassantTargets() {
        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                chessboard[i][j].dropEnPassantTarget();
            }
        }
    }
    
    private boolean hasAvailableMoves(PieceColor player) {
        boolean hasMoves = false;
        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                GamePiece p = chessboard[i][j].getCurrentPiece();
                if (p != null && p.getOwner() == player) {
                    List<GameField> moves = p.getAvailableMoves();
                    if (moves.size() > 0) hasMoves = true;
                }
            }
        }
        return hasMoves;
    }
    
    public boolean isCheckmate() {
        return currentState == GameState.CHECKMATE;
    }
    
    public boolean isStalemate() {
        return currentState == GameState.STALEMATE;
    }
    
    @Override
    public boolean isWaiting() {
        return false;
    }
    
    public PieceColor getCurrentPlayer() {
        return currentPlayer;
    }
    
    public int getMoveNumber() {
        return moveNumber;
    }
    
    public String getMoveCode() {
        return ((moveNumber + 1) / 2 + currentPlayer.toString().substring(0, 1));
    }
    
    public IPieceSelector getSelecetor(PieceColor player) {
        if (player == PieceColor.WHITE) return whiteSelector;
        else return blackSelector;
    }
    
}
