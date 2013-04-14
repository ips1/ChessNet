/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.game;

/**
 *
 * @author Ips
 */
public class InvalidMoveException extends Exception {
    public InvalidMoveException() { super(); }
    public InvalidMoveException(String message) { super(message); }
    public InvalidMoveException(String message, Throwable cause) { super(message, cause); }
    public InvalidMoveException(Throwable cause) { super(cause); }
    
}
