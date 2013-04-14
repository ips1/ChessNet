/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.recording;

import cz.cuni.mff.kubatpe1.java.chessnet.game.IWriter;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 *
 * @author Ips
 */
public class ListLogger implements IWriter {

    private JList targetList;
    private DefaultListModel targetModel;
    
    
    public ListLogger(JList targetList, DefaultListModel targetModel) {
        this.targetList = targetList;
        this.targetModel = targetModel;
    }
    
    @Override
    public void write(String message) {
        targetModel.addElement(message);
        targetList.setSelectedIndex(targetModel.size() - 1);
        targetList.ensureIndexIsVisible(targetModel.size() - 1);

    }
    
}