/*
 * StatSelectPanel.java
 *
 * Created on August 1, 2004, 5:28 PM
 */

package org.jarmytoolplugins.newarmylisteditorplugin.components;

import org.jArmyTool.data.dataBeans.armylist.ArmylistModel;
import org.jArmyTool.data.dataBeans.util.ModelStatHolder;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author  monkeigh
 */
public class StatSelectPanel extends javax.swing.JPanel
{    
    private ArmylistModel model;
    private StatPanel statPanel;
    
    /** Creates new form StatSelectPanel */
    public StatSelectPanel(ArmylistModel model, StatPanel statPanel) 
    {
        this.model = model;
        this.statPanel = statPanel; 
        initComponents();
        this.initSelector();
    }
    
    private void initSelector()
    {
        Iterator i = model.getStats().iterator();
        while(i.hasNext())
        {
            this.statSelector.addItem(((ModelStatHolder)i.next()).getStat().getSymbol());
        }
    }
    
    public void setValues(String stat, String value)
    {
        int statindex = -1;
        for(int i = 0; i < statSelector.getItemCount(); i++)
            if(statSelector.getItemAt(i).equals(stat))
                statindex = i;
        if(statindex == -1)
            return;
        statSelector.setSelectedIndex(statindex);
        updateValue.setText(value);
    }
    
    public HashMap modification()
    {
        HashMap map = new HashMap();
        map.put(statSelector.getSelectedItem(), updateValue.getText());
        return map;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        statSelector = new javax.swing.JComboBox();
        updateValue = new javax.swing.JTextField();
        remover = new javax.swing.JButton();

        statSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statSelectorActionPerformed(evt);
            }
        });

        add(statSelector);

        updateValue.setMaximumSize(new java.awt.Dimension(200, 23));
        updateValue.setMinimumSize(new java.awt.Dimension(200, 23));
        updateValue.setPreferredSize(new java.awt.Dimension(200, 23));
        add(updateValue);

        remover.setText("Remove");
        remover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removerActionPerformed(evt);
            }
        });

        add(remover);

    }//GEN-END:initComponents

    private void removerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removerActionPerformed
        statPanel.removeStatPanel(this);
        
    }//GEN-LAST:event_removerActionPerformed

    private void statSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statSelectorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statSelectorActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton remover;
    private javax.swing.JComboBox statSelector;
    private javax.swing.JTextField updateValue;
    // End of variables declaration//GEN-END:variables
    
}
