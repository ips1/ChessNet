/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kubatpe1.java.chessnet.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Ips
 */
public class ChessboardField extends JPanel{

//    private ResourceLoader resources;
    private Image icon;
    private FieldAvailability availability;
    private final Chessboard parent;
    private final int x;
    private final int y;
    
    private final Color SELECT_OVER = Color.red;
    private final Color AVAILABLE = new Color(76,196,23);
    private final Color AVAILABLE_OVER = new Color(126,246,73);
    
    public ChessboardField(Color c,  Chessboard board, int i, int j) {
        this.setBackground(c);
        this.parent = board;
        this.x = i;
        this.y = j;
        
//        this.resources = resources;
        
        
        
        final ChessboardField currentField = this;
        this.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!currentField.isEnabled()) return;
                if (availability == FieldAvailability.FIELD_SELECTION) {
                    currentField.setBorder(BorderFactory.createLineBorder(SELECT_OVER, 4));
                }
                else if (availability == FieldAvailability.MOVE_SELECTION) {
                    currentField.setBorder(BorderFactory.createLineBorder(AVAILABLE_OVER, 8));
                }

            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (availability == FieldAvailability.FIELD_SELECTION) {
                    currentField.setBorder(null);
                }
                else if (availability == FieldAvailability.MOVE_SELECTION) {
                    currentField.setBorder(BorderFactory.createLineBorder(AVAILABLE, 8));
                }
                else if (availability == FieldAvailability.NONE) {
                    currentField.setBorder(null);
                }
                else if (availability == FieldAvailability.SELECTED) {
                    currentField.setBorder(BorderFactory.createLineBorder(SELECT_OVER, 4));
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // LMB
                    
                    parent.leftClicked(x, y);
                }
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    // RMB
                    
                    parent.rightClicked(x, y);
                }
            }
            
        });
    }
    
    public void setAvailability(FieldAvailability newAvailability)
    {
        availability = newAvailability;
        if (availability == FieldAvailability.FIELD_SELECTION) {
            this.setBorder(null);
        }
        else if (availability == FieldAvailability.NONE) {
            this.setBorder(null);
        }
        else if (availability == FieldAvailability.SELECTED) {
            this.setBorder(BorderFactory.createLineBorder(SELECT_OVER, 4));
        }
        else if (availability == FieldAvailability.MOVE_SELECTION) {
            this.setBorder(BorderFactory.createLineBorder(AVAILABLE, 8));
        }
    }
    
    public boolean availableForMove() {
        return (availability == FieldAvailability.MOVE_SELECTION);
    }
    
    public boolean availableForSelection() {
        return (availability == FieldAvailability.FIELD_SELECTION);
    }
    
    public void setIcon(Image icon) {
        this.icon = icon;
    }
    
    public int getIndexX() {
        return x;
    }
    
    public int getIndexY() {
        return y;
    }
    
    /*
    public void markForMove() {
        this.availability = FieldAvailability.MOVE_SELECTION;
        this.setBorder(BorderFactory.createLineBorder(new Color(152,251,152), 8));
    }
    
    public void unmark() {
        this.availability = FieldAvailability.FIELD_SELECTION;
        this.setBorder(null);
    }
    
    */
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Image im2 = im.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
        if (icon != null) {
            int halfFieldX = this.getWidth() / 2;
            int halfFieldY = this.getHeight() / 2;
            int halfImgX = icon.getWidth(this) / 2;
            int halfImgY = icon.getHeight(this) / 2;


            g.drawImage(icon, halfFieldX - halfImgX, halfFieldY - halfImgY, this);

        }

    }
    
}
