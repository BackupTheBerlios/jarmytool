/*
 * StatPanel.java
 *
 * Created on August 1, 2004, 5:33 PM
 */

package org.jarmytoolplugins.newarmylisteditorplugin.components;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.jArmyTool.data.dataBeans.armylist.ArmylistArmy;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWeapon;
import org.jArmyTool.data.dataBeans.util.WeaponProfile;
import org.jArmyTool.data.dataBeans.armylist.ArmylistModel;
import org.jarmytoolplugins.newarmylisteditorplugin.util.VerticalFlowLayout;


/**
 *
 * @author  Oscar Almer
 */
public class StatPanel extends javax.swing.JPanel 
{
    private ArmylistArmy army;
    private LinkedList panels;
    private ArmylistModel model;
    private ArmylistEditorModelUpdatePanel updatePanel;
    
    /** Creates new form StatPanel */
    public StatPanel(ArmylistEditorModelUpdatePanel updatePanel, ArmylistArmy army, ArmylistModel model) 
    {
        this.panels = new LinkedList();
        this.army = army;
        this.model = model;
        this.updatePanel = updatePanel;
        
        initComponents();
        
    }
    
    public void addStatPanel()
    {
        StatSelectPanel pane = new StatSelectPanel(this.model, this);
        panels.add(pane);
        updatesPanel.add(pane);
        updatePanel.updateUI();
    }
    
    public void addStatPanel(String stat, String value)
    {
        StatSelectPanel pane = new StatSelectPanel(this.model, this);
        pane.setValues(stat, value);
        panels.add(pane);
        updatesPanel.add(pane);
        updatePanel.updateUI();
    }
    
    public void removeStatPanel(StatSelectPanel statPanel)
    {
        panels.remove(statPanel);
        updatesPanel.remove(statPanel);
        if(panels.size() == 0)
        {
            updatePanel.removeUpdateSelector();
        }
        updatePanel.updateUI();
    }
    
    public Map modifications()
    {
        Map map = new HashMap();
        Iterator i = panels.iterator();
        while(i.hasNext())
        {
            map.putAll(((StatSelectPanel)i.next()).modification());
        }
        
        return java.util.Collections.unmodifiableMap(map);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        updatesPanel = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));

        buttonPanel.setLayout(new javax.swing.BoxLayout(buttonPanel, javax.swing.BoxLayout.X_AXIS));

        jButton1.setText("New");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        buttonPanel.add(jButton1);

        add(buttonPanel);

        updatesPanel.setLayout(new javax.swing.BoxLayout(updatesPanel, javax.swing.BoxLayout.Y_AXIS));

        add(updatesPanel);

    }//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.addStatPanel();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel updatesPanel;
    // End of variables declaration//GEN-END:variables
    
}
