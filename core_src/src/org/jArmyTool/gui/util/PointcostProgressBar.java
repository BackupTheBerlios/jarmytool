/*
 * PointcostProgressBar.java
 *
 * Created on 17 December 2003, 10:12
 */

package org.jArmyTool.gui.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author  pasleh
 */
public class PointcostProgressBar extends JProgressBar{
    
    private static final int X = 6;
    private static final int Y = 20;
    
    private static final Font FONT = new Font("Lucida Bright", 1, 14);
    
    private Color color;
    private String string;
    //private JLabel label;
    
    /** Creates a new instance of PointcostProgressBar */
    public PointcostProgressBar(Color color) {
        super();
        this.color = color;
        //this.label = new JLabel();
        //this.add(this.label);
        //this.label.setForeground(color);
    }
    
    public void setString(String s){
        this.string = s;
        //if(this.label != null)
        //    this.label.setText(s);
    }
    
    public void setColor(Color color){
        this.color = color;
        //this.label.setForeground(color);
    }
    
    public void paint(Graphics g){
        super.paint(g);
        if(this.string == null)
            return;
        g.setColor(this.color);
        g.setFont(FONT);
        g.drawString(this.string, X, Y);
    }
    
}
