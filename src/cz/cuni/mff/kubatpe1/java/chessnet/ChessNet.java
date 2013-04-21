/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet;

import cz.cuni.mff.kubatpe1.java.chessnet.game.Game;
import cz.cuni.mff.kubatpe1.java.chessnet.game.PieceColor;
import cz.cuni.mff.kubatpe1.java.chessnet.gui.ResourceLoader;
import cz.cuni.mff.kubatpe1.java.chessnet.gui.Chessboard;
import cz.cuni.mff.kubatpe1.java.chessnet.gui.MainWindow;
import cz.cuni.mff.kubatpe1.java.chessnet.gui.PieceSelectorWindow;
import cz.cuni.mff.kubatpe1.java.chessnet.recording.ListLogger;
import cz.cuni.mff.kubatpe1.java.connection.Client;
import cz.cuni.mff.kubatpe1.java.connection.Connection;
import cz.cuni.mff.kubatpe1.java.connection.ConnectionForm;
import cz.cuni.mff.kubatpe1.java.connection.ConnectionType;
import cz.cuni.mff.kubatpe1.java.connection.Server;
import cz.cuni.mff.kubatpe1.java.netchat.ChatHandler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author Ips
 */
public class ChessNet {

    /**
     * @param args the command line arguments
     */
    private static JTextArea messages;
    //private static JTextPane messages;
    private static JTextField msgField;
    private static Connection connection;
    private static ConnectionType connectionType;
    private static String id;
    private static Chessboard chessboard;
    private static JFrame window;
    private static JPanel chessboardPanel;
    private static Handler messageHandler;
    
    private static final int portNo = 6666;
    
    public static class SendListener implements ActionListener {

        @Override    
        public void actionPerformed(ActionEvent e) {
            String message = msgField.getText();
            if (message.length() == 0) {
                
            }
            else if (message.contains("|")) {
                JOptionPane.showMessageDialog(null, "Message cannot contain \"|\" character!", "Message failed", JOptionPane.ERROR_MESSAGE);
            }
            else {
                writeMessage(id + ": " + message, true);
                connection.send("MSG|" + id + ": " + message);
                msgField.setText("");
            }
        }
        
    }
    
        
    public static class SetSmallListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            chessboard.setPreferredSize(new Dimension(400,400));
            chessboardPanel.setSize(400,400);
            window.setSize(700, 500);
            
        }
    }
    public static class SetMediumListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            chessboard.setPreferredSize(new Dimension(600,600));
            chessboardPanel.setSize(600,600);
            window.setSize(900, 700);
        }
    }
    public static class SetLargeListener implements ActionListener {
        @Override    
        public void actionPerformed(ActionEvent e) {
            chessboard.setPreferredSize(new Dimension(800,800));
            chessboardPanel.setSize(800,800);
            window.setSize(1100, 900);
        }
    }
    
    public static void main(String[] args) {
        
        /*
        if (args.length != 1) return;
        if (args[0].equals("server")) {
            id = "Server";
            Server s = new Server(id);
            connection = s;

        }
        else {
            id = "Client";
            Client s = new Client(id);
            connection = s;

        }
        */
        /*
        
        ConnectionForm form = new ConnectionForm();
        form.show();
        
        while(!form.wasConfirmed()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
        
        connectionType = form.getType();
        messageHandler = new Handler();
        
        if (connectionType == ConnectionType.SERVER) {
            id = "Server";
            Server s = new Server(id, portNo, messageHandler);
            connection = s;

        }
        else {
            id = "Client";
            Client s = new Client(id, form.getTarget(), portNo, messageHandler);
            connection = s;

        }
        
        connection.start();
        
        */
        
        
        final MainWindow win = new MainWindow();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
              public void run() {
                  try { 
                      
                      win.show();
                  }
                  catch (Exception e) { 
                      System.out.println(e);
                      return; 
                  }
              }
              
            });

    }
    
    public static void writeMessage(String message, boolean myMessage) {
        messages.append(message);
        messages.append("\n");
    }
    
    
    /*
    public static void showGUI() throws Exception {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        window = new JFrame("ChessNet");
        
        Container pane = window.getContentPane();
        
        pane.setLayout(new BorderLayout());

        
        ResourceLoader res = new ResourceLoader();
        
        //Game g = new Game(new PieceSelectorWindow(res, window));
        
        GameSynchronizer gs;
        if (connectionType == ConnectionType.SERVER) {
            gs = new GameSynchronizer(g, PieceColor.WHITE, connection);
        }
        else {
            gs = new GameSynchronizer(g, PieceColor.BLACK, connection);
        }
        
        
        // --- Main screen (chessboard) --- 
        chessboard = new Chessboard(res, gs);
        //chessboard.disableBoard();
        chessboardPanel = new JPanel(new GridBagLayout());
        chessboardPanel.add(chessboard);
        
        gs.setChessboard(chessboard);
        
        messageHandler.setSynchronizer(gs);
        
        pane.add(new JScrollPane(chessboardPanel), BorderLayout.CENTER);
        
        
        JPanel rightPanel = new JPanel(new GridLayout(2,1));
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        
        // --- Logging panel ---
        DefaultListModel logData = new DefaultListModel();
                      
        JList log = new JList(logData); 
        log.setBackground(Color.LIGHT_GRAY);
        JScrollPane logArea = new JScrollPane(log);
       
        g.setLogger(new ListLogger(log, logData));
        
        // --- Status panel ---
        JLabel status = new JLabel("<html>Connection status: XXX<br>Game state: ZZZ<br>Player: YYY<br>");
        
        Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        status.setBorder(BorderFactory.createCompoundBorder(border,paddingBorder));
        status.setBackground(Color.DARK_GRAY);
        status.setForeground(Color.WHITE);
        status.setOpaque(true);

        
        infoPanel.add(status, BorderLayout.NORTH);
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

        //  --- Right panel (status + log + messages) ---
        rightPanel.add(infoPanel);
        rightPanel.add(messagePanel);
        
        rightPanel.setPreferredSize(new Dimension(250,600));     
        
        pane.add(rightPanel, BorderLayout.EAST);
        
        
        // --- Menu strip ---
        JMenuBar menu = new JMenuBar(); 
        JMenu menuConnection = new JMenu("Connection");
        JMenu menuGame = new JMenu("Game");
        JMenu menuRecord = new JMenu("Record");
        JMenu menuTools = new JMenu("Tools");
        JMenu menuSize = new JMenu("Size");
        JMenu menuHelp = new JMenu("Help");
        
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
        menu.add(menuRecord);
        menu.add(menuTools);
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
        
    }
*/
}
