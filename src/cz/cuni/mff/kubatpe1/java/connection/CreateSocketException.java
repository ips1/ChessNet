/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.connection;

/**
 *
 * @author Ips
 */
public class CreateSocketException extends Exception {
    public CreateSocketException() { super(); }
    public CreateSocketException(String message) { super(message); }
    public CreateSocketException(String message, Throwable cause) { super(message, cause); }
    public CreateSocketException(Throwable cause) { super(cause); }
}
