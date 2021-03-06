/*
 * WeaponEditorMainWindow.java
 *
 * Created on 14 October 2003, 16:09
 */

package org.jarmytoolplugins.newarmylisteditorplugin.weaponeditor.components;

import javax.swing.JScrollPane;
import org.jarmytoolplugins.newarmylisteditorplugin.weaponeditor.engine.WeaponEditorCore;

/**
 *
 * @author  pasleh
 */
public class WeaponEditorMainWindow extends javax.swing.JFrame {
    
    private WeaponEditorCore core;
    
    /** Creates new form WeaponEditorMainWindow */
    public WeaponEditorMainWindow(WeaponEditorCore core) {
        this.core =core;
        initComponents();
    }
    
    
    public void addWeaponTypePanel(WeaponTypePanel panel){
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.add(panel);
        this.tabPanel.addTab(panel.getWeaponProfile().getName(), panel);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        doneButton = new javax.swing.JButton();
        dataPanel = new javax.swing.JPanel();
        tabPanel = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Weapon Editor");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        doneButton.setText("Done");
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });

        controlPanel.add(doneButton);

        jPanel1.add(controlPanel, java.awt.BorderLayout.SOUTH);

        dataPanel.setLayout(new java.awt.BorderLayout());

        dataPanel.add(tabPanel, java.awt.BorderLayout.CENTER);

        jPanel1.add(dataPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        this.core.saveOnExit();
        this.dispose();
    }//GEN-LAST:event_doneButtonActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.core.saveOnExit();
        this.dispose();
    }//GEN-LAST:event_exitForm

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel dataPanel;
    private javax.swing.JButton doneButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane tabPanel;
    // End of variables declaration//GEN-END:variables
    
}
