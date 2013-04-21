/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.gui;

import cz.cuni.mff.kubatpe1.java.chessnet.ChessNet;
import static cz.cuni.mff.kubatpe1.java.chessnet.ChessNet.writeMessage;
import cz.cuni.mff.kubatpe1.java.chessnet.GameSynchronizer;
import cz.cuni.mff.kubatpe1.java.chessnet.Handler;
import cz.cuni.mff.kubatpe1.java.chessnet.game.EmptyGame;
import cz.cuni.mff.kubatpe1.java.chessnet.game.Game;
import cz.cuni.mff.kubatpe1.java.chessnet.game.GameLoadException;
import cz.cuni.mff.kubatpe1.java.chessnet.game.GameLoader;
import cz.cuni.mff.kubatpe1.java.chessnet.game.PieceColor;
import cz.cuni.mff.kubatpe1.java.chessnet.recording.BufferedRecorder;
import cz.cuni.mff.kubatpe1.java.chessnet.recording.ListLogger;
import cz.cuni.mff.kubatpe1.java.connection.Client;
import cz.cuni.mff.kubatpe1.java.connection.Connection;
import cz.cuni.mff.kubatpe1.java.connection.ConnectionForm;
import cz.cuni.mff.kubatpe1.java.connection.ConnectionType;
import cz.cuni.mff.kubatpe1.java.connection.CreateSocketException;
import cz.cuni.mff.kubatpe1.java.connection.Server;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 *
 * @author Ips
 */
public class MainWindow {
    private JTextArea messages;
    //private static JTextPane messages;
    private JTextField msgField;
    private Connection connection;
    private ConnectionType connectionType;
    private String id;
    private Chessboard chessboard;
    private JFrame window;
    private JPanel chessboardPanel;
    private Handler messageHandler;
    private JLabel connectionStatus;
    private JLabel gameStatus;
    private JLabel statusHeader;
    
    private ResourceLoader res;
    
    private JMenuItem menuConnect;
    private JMenuItem menuDisconnect;
    
    private JMenuItem menuStart;
    private JMenuItem menuSave;
    private JMenuItem menuLoad;
    private JMenuItem menuQuit;
    
    private Game currentGame;
    private GameSynchronizer currentSynchronizer;
    
    private Game tmpGame;
    
    private DefaultListModel logData;
    private JList log;

    private Thread serverWaitThread;
    
    private WindowState currentState;
    
    private PieceMemorySelector currentSelector;
    
    private BufferedRecorder saveGameRecorder;
    
    private final int portNo = 6666;
    
    private final String version = "1.0";

    public MainWindow() {
        connection = null;
        connectionType = null;
        
        res = new ResourceLoader();
        
        currentSelector = new PieceMemorySelector();
        
        saveGameRecorder = new BufferedRecorder();

    }
    
    private void changeState(WindowState newState) {
        currentState = newState;
        statusHeader.setText(newState.getMessage());
        if (currentState == WindowState.PLAYING_OFFLINE || currentState == WindowState.PLAYING_ONLINE) {
            menuStart.setEnabled(false);
            menuSave.setEnabled(true);
            menuLoad.setEnabled(false);
            menuQuit.setEnabled(true);
        }
        else {
            menuStart.setEnabled(true);
            menuSave.setEnabled(false);
            menuLoad.setEnabled(true);
            menuQuit.setEnabled(false);
        }
        if (currentState == WindowState.WAITING_FOR_CONNECTION) {
            menuStart.setEnabled(false);
            menuSave.setEnabled(false);
            menuLoad.setEnabled(false);
            menuQuit.setEnabled(false);
            menuConnect.setEnabled(false);
            
            menuDisconnect.setEnabled(true);
        }
        if (currentState == WindowState.WAITING_FOR_GAME) {
            menuStart.setEnabled(false);
            menuSave.setEnabled(false);
            menuLoad.setEnabled(false);
            menuQuit.setEnabled(false);
            menuConnect.setEnabled(false);
            menuDisconnect.setEnabled(true);
        }
        refreshStatus();
    }
    
    private void quitCurrentGame() {
        Object[] options = { "Yes", "No" };
        int result = JOptionPane.showOptionDialog(null, "Do you really want to quit current game?", "Quit game", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (result != 0) {
            return;
        }
        
        promptSave();
        
        if (currentState == WindowState.PLAYING_ONLINE) {
            connection.send("QGAME");
        }
        chessboard.reset(new EmptyGame());
        logData.clear();
        if (connectionType != null) {
            changeState(WindowState.CONNECTED);
            
        }
        else {
            changeState(WindowState.OFFLINE);
        }
        
    }
    
    public void forceQuitGame() {
        if (currentState == WindowState.PLAYING_OFFLINE || currentState == WindowState.PLAYING_ONLINE) {
            promptSave();
        }
        
        chessboard.reset(new EmptyGame());
        logData.clear();
        if (connectionType != null) {
            changeState(WindowState.CONNECTED);
            
        }
        else {
            changeState(WindowState.OFFLINE);
        }
    }
    
    public void setPieceSelection(String message) {
        currentSelector.setSelection(message);
    }

    
    public synchronized void connect() {
        if (currentState == WindowState.PLAYING_OFFLINE) {
            Object[] options = { "Yes", "No" };
            int res = JOptionPane.showOptionDialog(null, "Establishing a connection will quit your current game. Do you wish to continue?", "Connection", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (res != 0) {
                return;
            }
        }
        
        
        forceQuitGame();
        
        ConnectionForm form = new ConnectionForm();
        boolean result = false;
        try {
            result = form.showDialog();
        } catch (UnknownHostException ex) {
            JOptionPane.showMessageDialog(window, "UnknownHostException!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!result) return;
/*
        while(!form.wasConfirmed()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
*/
        
        connectionType = form.getType();
        messageHandler = new Handler(this);
        
        if (connectionType == ConnectionType.SERVER) {
            id = "Server";
            changeState(WindowState.WAITING_FOR_CONNECTION);        
                        
            final Server s = new Server(id, messageHandler);
            connection = s;
            waitForConnection(s);
            return;
   
            
            

        }
        else {
            Client s;
            try {
                s = new Client(id, form.getTarget(), portNo, messageHandler);
            }
            catch (SocketException ex) {
                JOptionPane.showMessageDialog(window, "Unable to connect to a server!", "ERROR", JOptionPane.ERROR_MESSAGE);
                connectionType = null;
                messageHandler = null;
                return;
            }
            connection = s;
            id = "Client";
        }
        
        connection.start();
        
        refreshStatus();
        
        changeState(WindowState.CONNECTED);
        menuConnect.setEnabled(false);
        menuDisconnect.setEnabled(true);
    }
    
    private void waitForConnection(final Server s) {
        
        changeState(WindowState.WAITING_FOR_CONNECTION);
        
        serverWaitThread = new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    s.waitForConnection(portNo, false);
                } catch (SocketException ex) {
                    return;
                } catch (CreateSocketException ex) {
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        

                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(window, "Cannot create server on port " + portNo +". Maybe another server already running?", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    
                    disconnect();
                    return;
                }
                
                
                        
                connection.start();

                refreshStatus();

                changeState(WindowState.CONNECTED);
                menuConnect.setEnabled(false);
                menuDisconnect.setEnabled(true);
            }
            
            
        });
        
        serverWaitThread.start();

        
    }
    
    public synchronized void disconnect() {
        if (serverWaitThread != null && serverWaitThread.isAlive()) {
            //serverWaitThread.interrupt();
        }
        connection.close();
        connection = null;
        connectionType = null;
        refreshStatus();
        
        forceQuitGame();
        
        changeState(WindowState.OFFLINE);
        menuConnect.setEnabled(true);
        menuDisconnect.setEnabled(false);
    }
    
    public void writeMessage(String message, boolean myMessage) {
        messages.append(message);
        messages.append("\n");
    }
    
    public void refreshStatus() {
        String connectionStatus;
        if (connectionType == null) {
            connectionStatus = "OFFLINE";
        }
        else {
            connectionStatus = connectionType.toString();
        }
        this.connectionStatus.setText(connectionStatus);
    }  
    
    
    public void startGame(File inputFile) {
               
        if (connectionType == ConnectionType.CLIENT) {
            JOptionPane.showMessageDialog(window, "Only server can start the game!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if (connectionType == ConnectionType.SERVER) {
            logData.clear();
            if (inputFile != null) {
                GameLoader gl = new GameLoader(true);
                try {
                    saveGameRecorder.clearBuffer();
                    tmpGame = gl.loadFromFile(inputFile, new ListLogger(log, logData), saveGameRecorder);
                }
                catch (GameLoadException e) {
                    JOptionPane.showMessageDialog(window, "Cannot load from file!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    logData.clear();
                    return;
                }
                String buffer = gl.getBufferContent();
                changeState(WindowState.WAITING_FOR_GAME);
                connection.send("START" + "|" + buffer);
            }
            else {
                changeState(WindowState.WAITING_FOR_GAME);
                connection.send("START");
            }

        }
        else {
            logData.clear();
            if (inputFile != null) {
                GameLoader gl = new GameLoader(false);
                try {
                    saveGameRecorder.clearBuffer();
                    currentGame = gl.loadFromFile(inputFile, new ListLogger(log, logData), saveGameRecorder);
                }
                catch (GameLoadException e) {
                    JOptionPane.showMessageDialog(window, "Cannot load from file!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    logData.clear();
                    return;
                }
            }
            else {
                currentGame = new Game(new PieceSelectorWindow(res, window), new PieceSelectorWindow(res, window));
                currentGame.setLogger(new ListLogger(log, logData));
                currentGame.setRecorder(saveGameRecorder);
                saveGameRecorder.clearBuffer();
            }
            changeState(WindowState.PLAYING_OFFLINE);
            
            currentSynchronizer = null;
            chessboard.setReversed(false);
            
            chessboard.reset(currentGame);
        }
        
    }
    
    public void invokeStart(String buffer) {
        if (connectionType != ConnectionType.CLIENT) {
            JOptionPane.showMessageDialog(window, "Game cannot be started!", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Object[] options = { "Yes", "No" };
        int result = JOptionPane.showOptionDialog(null, "Server wants to start a new game. Are you ready?", "New game", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (result != 0) {
            connection.send("STDENY");
            return;
        }
        changeState(WindowState.PLAYING_ONLINE);
        logData.clear();
        if (buffer != null) {
            try {
                GameLoader gl = new GameLoader(false);
                saveGameRecorder.clearBuffer();
                currentGame = gl.loadFromBuffer(buffer, new ListLogger(log, logData), saveGameRecorder);
            }
            catch (GameLoadException e) {
                JOptionPane.showMessageDialog(window, "Game cannot be loaded!", "ERROR", JOptionPane.ERROR_MESSAGE);
                connection.send("STDENY");
                return;
            }
        }
        else {
            currentGame = new Game(currentSelector, new PieceSelectorWindow(res, window));
            currentGame.setLogger(new ListLogger(log, logData));
            currentGame.setRecorder(saveGameRecorder);
            saveGameRecorder.clearBuffer();
        }

        currentSynchronizer = new GameSynchronizer(currentGame, PieceColor.BLACK, connection);
        chessboard.setReversed(true);
        chessboard.reset(currentSynchronizer);
        currentSynchronizer.setChessboard(chessboard);
        messageHandler.setSynchronizer(currentSynchronizer);
        connection.send("STCONFIRM");
    }
    
    public void confirmStart() {
        changeState(WindowState.PLAYING_ONLINE);
        
        if (tmpGame != null) {
            currentGame = tmpGame;
            tmpGame = null;
        }
        else {
            currentGame = new Game(new PieceSelectorWindow(res, window), currentSelector);
            currentGame.setLogger(new ListLogger(log, logData));
            currentGame.setRecorder(saveGameRecorder);
            saveGameRecorder.clearBuffer();
            logData.clear();
        }

        currentSynchronizer = new GameSynchronizer(currentGame, PieceColor.WHITE, connection);
        chessboard.setReversed(false);
        chessboard.reset(currentSynchronizer);
        currentSynchronizer.setChessboard(chessboard);
        messageHandler.setSynchronizer(currentSynchronizer);
    }
    
    public void denyStart() {
        if (tmpGame != null) {
            tmpGame = null;
        }
        logData.clear();
        changeState(WindowState.CONNECTED);
    }
    
    private void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(window);
        File selectedFile;
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        }
        else {
            return;
        }
        try {
            saveGameRecorder.saveBuffer(selectedFile);
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(window, "Error while saving the game!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void promptSave() {
        Object[] options = { "Yes", "No" };
        int result = JOptionPane.showOptionDialog(null, "Do you want to save the game?", "Save game", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (result == 0) {
            saveGame();
        }
    }
    
    public void otherSideQuit() {
        JOptionPane.showMessageDialog(window, "Other side quit the game!", "ERROR", JOptionPane.ERROR_MESSAGE);
        forceQuitGame();
    }
    
    
    public synchronized void lostConnection() {
        if (connection != null) {
            connection.close();
        }
        System.err.println("ERROR");
        
        JOptionPane.showMessageDialog(window, "Connection lost!", "ERROR", JOptionPane.ERROR_MESSAGE);
        
        connection = null;
        connectionType = null;
        refreshStatus();
        
        if (currentState == WindowState.PLAYING_ONLINE) {
            forceQuitGame();
        }
        
        changeState(WindowState.OFFLINE);
        menuConnect.setEnabled(true);
        menuDisconnect.setEnabled(false);
    }
    
    private void showAbout() {
        JPanel aboutPanel = new JPanel(new GridLayout(0,1));
        
        JLabel name = new JLabel("<html><b><font size=\"7\">ChessNet</font></b></html>");
        
        name.setForeground(Color.DARK_GRAY);
        
        JLabel ver = new JLabel("<html><font size=\"4\">Version: " + version + "</font></html>");
        
        JLabel rest = new JLabel("<html>Petr Kubát<br>2013</html>");
        
        aboutPanel.add(name);
        aboutPanel.add(ver);
        aboutPanel.add(rest);
        
        //aboutPanel.validate();
        
        JOptionPane.showMessageDialog(window, aboutPanel, "About ChessNet", JOptionPane.PLAIN_MESSAGE, new ImageIcon(res.pawnWhite));
        
    }
    
    
    public void show() throws Exception {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        window = new JFrame("ChessNet");
        
        Container pane = window.getContentPane();
        
        pane.setLayout(new BorderLayout());
        /*
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 600;
        c.gridwidth = 600;
*/
        
        
        

        gameStatus = new JLabel("");
        
        // --- Main screen (chessboard) --- 
        chessboard = new Chessboard(res, new EmptyGame(), gameStatus);
        //chessboard.disableBoard();
        chessboardPanel = new JPanel(new GridBagLayout());
        chessboardPanel.add(chessboard);
        
        pane.add(new JScrollPane(chessboardPanel), BorderLayout.CENTER);
        
        
        JPanel rightPanel = new JPanel(new GridLayout(2,1));
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        
        // --- Logging panel ---
        logData = new DefaultListModel();              
        log = new JList(logData); 
        log.setBackground(Color.LIGHT_GRAY);
        JScrollPane logArea = new JScrollPane(log);
        
        // --- Status panel ---
        statusHeader = new JLabel("");
        connectionStatus = new JLabel("");
        
        Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        
        connectionStatus.setBorder(paddingBorder);
        connectionStatus.setBackground(Color.DARK_GRAY);
        connectionStatus.setForeground(Color.YELLOW);
        connectionStatus.setOpaque(true);
        
        gameStatus.setBorder(paddingBorder);
        gameStatus.setBackground(Color.DARK_GRAY);
        gameStatus.setForeground(Color.WHITE);
        gameStatus.setOpaque(true);
        
        statusHeader.setBorder(paddingBorder);
        statusHeader.setBackground(Color.DARK_GRAY);
        statusHeader.setForeground(Color.GREEN);
        statusHeader.setOpaque(true);

        JPanel statusPanel = new JPanel(new BorderLayout());
        
        statusPanel.add(statusHeader, BorderLayout.NORTH);
        statusPanel.add(connectionStatus, BorderLayout.CENTER);
        statusPanel.add(gameStatus, BorderLayout.SOUTH);

        
        infoPanel.add(statusPanel, BorderLayout.NORTH);
        infoPanel.add(logArea);
        
        // --- Message panel ---
        JPanel messagePanel = new JPanel(new BorderLayout());
        
        messages = new JTextArea();
        messages.setEditable(false);
        JScrollPane scrollArea = new JScrollPane(messages);
        
        messagePanel.add(scrollArea);
        
        JPanel messageControl = new JPanel(new BorderLayout());
        
        msgField = new JTextField();
        JButton msgButton = new JButton("Send");
        msgButton.addActionListener(new SendListener());
        msgField.addActionListener(new SendListener());        
        
        messageControl.add(msgField);
        messageControl.add(msgButton, BorderLayout.EAST);
        
        messagePanel.add(messageControl, BorderLayout.SOUTH);
        
        /*
        c.gridx = 600;
        c.gridy = 0;
        c.gridheight = 400;
        c.gridwidth = 600;
        
        * 
        * 
        */
        
        //  --- Right panel (status + log + messages) ---
        rightPanel.add(infoPanel);
        rightPanel.add(messagePanel);
        
        rightPanel.setPreferredSize(new Dimension(250,600));     
        
        pane.add(rightPanel, BorderLayout.EAST);
        
        
        // --- Menu strip ---
        JMenuBar menu = new JMenuBar(); 
        JMenu menuConnection = new JMenu("Connection");
        JMenu menuGame = new JMenu("Game");
        //JMenu menuRecord = new JMenu("Record");
        //JMenu menuTools = new JMenu("Tools");
        JMenu menuSize = new JMenu("Size");
        JMenu menuHelp = new JMenu("Help");
        
        menuConnect = new JMenuItem("Connect");
        menuDisconnect = new JMenuItem("Disconnect");
        
        menuDisconnect.setEnabled(false);
        
        menuConnect.addActionListener(new ConnectListener());
        menuDisconnect.addActionListener(new DisconnectListener());
        
        menuConnection.add(menuConnect);
        menuConnection.add(menuDisconnect);
        
        menuStart = new JMenuItem("New game");
        menuSave = new JMenuItem("Save game");
        menuLoad = new JMenuItem("Load game");
        menuQuit = new JMenuItem("Quit game");
        
        menuStart.addActionListener(new StartListener());
        menuSave.addActionListener(new SaveListener());
        menuLoad.addActionListener(new LoadListener());
        menuQuit.addActionListener(new QuitListener());
        
        menuGame.add(menuStart);
        menuGame.add(menuSave);
        menuGame.add(menuLoad);
        menuGame.add(menuQuit);
        
        JMenuItem menuGuide = new JMenuItem("User guide");
        JMenuItem menuAbout = new JMenuItem("About");
        
        menuAbout.addActionListener(new AboutListener());
        
        menuHelp.add(menuGuide);
        menuHelp.add(menuAbout);
        
        JRadioButtonMenuItem menuSizeSmall = new JRadioButtonMenuItem("Small");
        JRadioButtonMenuItem menuSizeMedium = new JRadioButtonMenuItem("Medium");
        JRadioButtonMenuItem menuSizeLarge = new JRadioButtonMenuItem("Large");
        
        ButtonGroup menuSizeGroup = new ButtonGroup();
        menuSizeGroup.add(menuSizeSmall);
        menuSizeGroup.add(menuSizeMedium);
        menuSizeGroup.add(menuSizeLarge);
        
        menuSize.add(menuSizeSmall);
        menuSize.add(menuSizeMedium);
        menuSize.add(menuSizeLarge);
        
        menuSizeMedium.setSelected(true);
        
        menuSizeSmall.addActionListener(new SetSmallListener());
        menuSizeMedium.addActionListener(new SetMediumListener());
        menuSizeLarge.addActionListener(new SetLargeListener());
    
        menu.add(menuConnection);
        menu.add(menuGame);
        //menu.add(menuRecord);
        //menu.add(menuTools);
        menu.add(menuSize);
        menu.add(menuHelp);
        
        window.setJMenuBar(menu);
        
        // --- Window init ---
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //window.setResizable(false);
        window.pack();
        window.setSize(900, 700);
        window.setLocationRelativeTo( null );
        window.setVisible(true);
        
        changeState(WindowState.OFFLINE);
    }



    public boolean isOnline() {
        if (currentState == WindowState.CONNECTED || currentState == WindowState.WAITING_FOR_GAME || currentState == WindowState.PLAYING_ONLINE){
            return true;
        }
        else return false;
    }

    public class SendListener implements ActionListener {

        @Override    
        public void actionPerformed(ActionEvent e) {
            String message = msgField.getText();
            if (message.length() == 0) {
                
            }
            else if (message.contains("|")) {
                JOptionPane.showMessageDialog(null, "Message cannot contain \"|\" character!", "Message failed", JOptionPane.ERROR_MESSAGE);
            }
            else {
                if (isOnline()) {
                    writeMessage(id + ": " + message, true);
                    connection.send("MSG|" + id + ": " + message);
                    msgField.setText("");
                }
                else {
                    writeMessage("(Offline message): " + message, true);
                    msgField.setText("");
                }

            }
        }
        
    }
    
    public class ConnectListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            connect();
        }
    }
    
    public class DisconnectListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            disconnect();
        }
    }
    
    public class StartListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            startGame(null);
        }
    }
    
    public class LoadListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(window);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                startGame(selectedFile);
            }
            

            
        }
    }
    
    public class SaveListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            saveGame();
        }
    }
    
    
    public class QuitListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            quitCurrentGame();
        }
    }
    
    public class AboutListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            showAbout();
        }
    }
    
        
    public class SetSmallListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            chessboard.setPreferredSize(new Dimension(400,400));
            chessboardPanel.setSize(400,400);
            window.setSize(700, 500);
            
        }
    }
    public class SetMediumListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            chessboard.setPreferredSize(new Dimension(600,600));
            chessboardPanel.setSize(600,600);
            window.setSize(900, 700);
        }
    }
    public class SetLargeListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            chessboard.setPreferredSize(new Dimension(800,800));
            chessboardPanel.setSize(800,800);
            window.setSize(1100, 900);
        }
    }
    
}
