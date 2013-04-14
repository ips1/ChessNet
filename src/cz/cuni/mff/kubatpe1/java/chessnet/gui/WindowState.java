/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.gui;

/**
 *
 * @author Ips
 */
public enum WindowState {
    OFFLINE, WAITING_FOR_CONNECTION, CONNECTED, WAITING_FOR_GAME, PLAYING_ONLINE, PLAYING_OFFLINE;
    
    public String getMessage() {
        if (this == OFFLINE) return "Disconnected, ready for offline game.";
        if (this == WAITING_FOR_CONNECTION) return "Waiting for client to connect...";
        if (this == CONNECTED) return "Connected, ready to start a game.";
        if (this == WAITING_FOR_GAME) return "Waiting for client to start a game...";
        if (this == PLAYING_ONLINE) return "Playing online game.";
        if (this == PLAYING_OFFLINE) return "Playing offline game.";
        return "";
    }
}
