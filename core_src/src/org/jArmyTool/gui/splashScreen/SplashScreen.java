/*
 * SplashScreen.java
 *
 * Created on 11 July 2003, 00:47
 */

package core_src.src.org.jArmyTool.gui.splashScreen;

import org.jArmyTool.gui.engine.GUICore;
import org.jArmyTool.gui.util.ImagePanel;
import org.jArmyTool.util.Config;
import java.awt.*;
import javax.swing.ImageIcon;
import org.jArmyTool.gui.components.MainWindow;

/**
 *
 * @author  pasi
 */
public class SplashScreen extends javax.swing.JFrame {
    
    private static final String ICON_LOCATION = "images/icon.gif";
    
    private GUICore guiCore;
    private Config config;
    
    /** Creates new form SplashScreen */
    public SplashScreen(GUICore guiCore) {
        this.setIconImage(new ImageIcon(ICON_LOCATION).getImage());
        
        this.config = Config.getInstance("splashScreen");
        this.guiCore = guiCore;
        initComponents();
        this.versionLabel.setText(this.guiCore.getVersion());
        this.initGraphics();
        this.setFocusable(false);
        this.setTitle("Starting jArmyTool");
        this.center();
        this.setCursor(MainWindow.DEL_CURSOR);
    }
    
    private void center(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension d = toolkit.getScreenSize();

        Point p = new Point();
        p.x += ((d.width - this.getWidth()) / 2);
        p.y += ((d.height - this.getHeight()) / 2);

        if (p.x < 0) {
          p.x = 0;
        }

        if (p.y < 0) {
          p.y = 0;
        }

        this.setLocation(p);        
    }
    
    private void initGraphics(){
        Image image = Toolkit.getDefaultToolkit().getImage(this.config.getProperty("splashScreenImage"));
        ImagePanel imagePanel = new ImagePanel(image);
        this.graphicsPanel.add(imagePanel);
    }
    
    public void setStatus(String newStatus){
        this.statusLabel.setText(newStatus);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        graphicsPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        statusPanel = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        setUndecorated(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel3.setPreferredSize(new java.awt.Dimension(235, 208));
        graphicsPanel.setLayout(new java.awt.BorderLayout());

        graphicsPanel.setBackground(new java.awt.Color(255, 255, 255));
        graphicsPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                graphicsPanelMousePressed(evt);
            }
        });

        jPanel3.add(graphicsPanel, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Version ");
        jPanel2.add(jLabel2, new java.awt.GridBagConstraints());

        versionLabel.setText("jLabel1");
        jPanel2.add(versionLabel, new java.awt.GridBagConstraints());

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        statusPanel.setLayout(new java.awt.GridBagLayout());

        statusPanel.setBackground(new java.awt.Color(255, 255, 255));
        statusLabel.setFont(new java.awt.Font("Lucida Bright", 1, 14));
        statusLabel.setText("status");
        statusLabel.setPreferredSize(new java.awt.Dimension(235, 19));
        statusPanel.add(statusLabel, new java.awt.GridBagConstraints());

        jPanel1.add(statusPanel, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel1, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel3, java.awt.BorderLayout.NORTH);

        pack();
    }//GEN-END:initComponents

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jPanel1MousePressed

    private void graphicsPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_graphicsPanelMousePressed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_graphicsPanelMousePressed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_exitForm
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel graphicsPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
    
}