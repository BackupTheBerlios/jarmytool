/*
 * WargearItemPanel.java
 *
 * Created on 12 January 2004, 12:53
 */

package org.jarmytoolplugins.newarmylisteditorplugin.wargearEditor.components;

import java.util.Iterator;
import javax.swing.DefaultComboBoxModel;
import org.apache.log4j.Logger;
import org.jArmyTool.data.dataBeans.armylist.ArmylistArmy;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearGroup;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearItem;
import org.jarmytoolplugins.newarmylisteditorplugin.components.WeaponPanel;


/**
 *
 * @author  pasleh
 */
public class WargearItemPanel extends javax.swing.JPanel {
    private ArmylistArmy army;
    private ArmylistWargearGroup group;
    private ArmylistWargearItem item;
    private WeaponPanel weaponsPanel;
    private WargearEditorMainWindow parent;
    
    private static Logger logger = Logger.getLogger(WargearItemPanel.class);
    
    /** Creates new form WargearItemPanel */
    public WargearItemPanel(ArmylistArmy army, ArmylistWargearGroup group, ArmylistWargearItem item, WargearEditorMainWindow parent) {
        this.army = army;
        this.group = group;
        this.item = item;
        this.parent = parent;
        
        initComponents();
        
        this.initData();
        
        this.initWeapons();
        

    }
    
    private void initData(){
        this.name.setText(this.item.getName());
        if(this.item.getPointcost() == (int)this.item.getPointcost()){
            this.points.setText(""+(int)this.item.getPointcost());
        }else{
            this.points.setText(""+this.item.getPointcost());
        }
        
    }
    
    private void initWeapons(){
        if(this.item.isWeapon())
            this.activateWeaponMenu();
    }
    
  
    
    private void activateWeaponMenu(){
        this.weaponPanel.removeAll();
        this.weaponsPanel = new WeaponPanel(this.army, this.item.getWeapons(), this.item.getName());
        this.weaponPanel.add(weaponsPanel);
        this.weaponPanel.updateUI();
    }
    
    public void saveData(){
        if(this.group == null || this.item == null){
            logger.error("WargearGroup: "+ this.group + " WargearItem: "+this.item);
            return;
        }
        
        this.item.setName(this.name.getText());
        this.item.setPointcost(Double.parseDouble(this.points.getText()));
        
        //this.group.removeItem(this.item);

        if(this.weaponsPanel != null){
            this.item.emptyWeapons();
            Iterator iterator = this.weaponsPanel.getSelectedWeapons().iterator();
            while(iterator.hasNext()){
                this.item.addWeapon((String)iterator.next());
            }
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        name = new javax.swing.JTextField();
        points = new javax.swing.JTextField();
        weaponPanel = new javax.swing.JPanel();
        isWeaponCheckBox = new javax.swing.JCheckBox();
        delButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(new javax.swing.border.TitledBorder("Wargear Item"));
        name.setText("jTextField1");
        name.setMaximumSize(new java.awt.Dimension(250, 28));
        name.setMinimumSize(new java.awt.Dimension(250, 28));
        name.setPreferredSize(new java.awt.Dimension(250, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(name, gridBagConstraints);

        points.setText("jTextField2");
        points.setMaximumSize(new java.awt.Dimension(50, 28));
        points.setMinimumSize(new java.awt.Dimension(50, 28));
        points.setPreferredSize(new java.awt.Dimension(50, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(points, gridBagConstraints);

        isWeaponCheckBox.setText("is weapon");
        isWeaponCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isWeaponCheckBoxActionPerformed(evt);
            }
        });

        weaponPanel.add(isWeaponCheckBox);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(weaponPanel, gridBagConstraints);

        delButton.setText("del");
        delButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        delButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        jPanel1.add(delButton, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel1.setText("Item Name: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel3.setText("Pointcost: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel3, gridBagConstraints);

        add(jPanel1, java.awt.BorderLayout.WEST);

    }//GEN-END:initComponents

    private void delButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delButtonActionPerformed
        this.group.removeItem(this.item);
        this.parent.deleteCurrentItem();
    }//GEN-LAST:event_delButtonActionPerformed

    private void isWeaponCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isWeaponCheckBoxActionPerformed
        this.activateWeaponMenu();
    }//GEN-LAST:event_isWeaponCheckBoxActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton delButton;
    private javax.swing.JCheckBox isWeaponCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField name;
    private javax.swing.JTextField points;
    private javax.swing.JPanel weaponPanel;
    // End of variables declaration//GEN-END:variables
    
}
