/*
 * RemainingIndicatorPanel.java
 *
 * Created on 21 May 2004, 23:18
 */

package org.jArmyTool.gui.components.util;

import javax.swing.JPanel;
import java.awt.*;

/**
 *
 * @author  pasi
 */
public class RemainingIndicatorPanel extends JPanel {
    
    private double max;
    private double current;
    
    /** Creates new form RemainingIndicatorPanel */
    public RemainingIndicatorPanel(double max) {
        this.max = max;
        initComponents();
    }
    
    public void setMax(double max){
        this.max = max;
    }
    
    public void setCurrent(double current){
        this.current = current;
    }
    
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        
        
        double remaining = max - current;
        
        double height = this.getSize().getHeight();
        double width = this.getSize().getWidth();
        
        double persentWidth = (this.current / this.max) * width;
        
        g2.setColor(Color.RED);
        
        //Rectangle rec = new Rectangle(0,0, (int)persentWidth, (int)height);
        g2.fill3DRect(0,0, (int)persentWidth, (int)height, true);
        
        g2.setColor(Color.GREEN);
        g2.fill3DRect((int)persentWidth,0, (int)width, (int)height, true);

        
     /*   g2.setColor(Color.YELLOW);
        if(remaining == (int)remaining){
           g2.drawString("remaining: " +  (int)remaining, 1, (int)height -15);
        }else{
           g2.drawString("remaining: " +  remaining, 1, (int)height -15); 
        }*/
        
        
        //g2.draw3DRect(0,0, (int)persentWidth, (int)height, false);
        
        
        
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        
        setLayout(new java.awt.BorderLayout());
        
    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}