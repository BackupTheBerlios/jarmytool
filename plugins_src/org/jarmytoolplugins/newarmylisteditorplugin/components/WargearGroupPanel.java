/*
 * WargearGroupPanel.java
 *
 * Created on 25 February 2004, 20:11
 */

package org.jarmytoolplugins.newarmylisteditorplugin.components;

import org.jArmyTool.data.dataBeans.armylist.ArmylistModel;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearGroup;

/**
 *
 * @author  pasi
 */
public class WargearGroupPanel extends javax.swing.JPanel {
    
    private ArmylistWargearGroup wgGroup;
    private double allowedAmount;
    private int depth;
    private boolean selected;
    private String hierarchyName;
    private ArmylistModel model;
    
    /** Creates new form WargearGroupPanel */
    public WargearGroupPanel(ArmylistWargearGroup wgGroup, double allowedAmount, int depth, boolean selected, String hierarchyName, ArmylistModel model) {
        this.model = model;
        this.wgGroup = wgGroup;
        this.allowedAmount = allowedAmount;
        this.depth = depth;
        this.selected = selected;
        this.hierarchyName = hierarchyName;
        initComponents();
        this.initData();
    }
    
    private void initData(){
        
        if(this.allowedAmount == (int)this.allowedAmount){
            this.allowedAmountField.setText(""+(int)this.allowedAmount);
        }else{
            this.allowedAmountField.setText(""+this.allowedAmount);
        }
        this.selectionBox.setText(this.wgGroup.getName());
        this.selectionBox.setSelected(this.selected);
        String depthSt = "";
        for(int i = depth; i > 0; --i){
            depthSt = depthSt + " -";
        }
        this.depthLabel.setText(depthSt);
        
    }
    
    public void saveData(){
        if(this.selectionBox.isSelected()){
            this.model.addWargearGroup(this.hierarchyName);
            System.out.println("adding: "+this.hierarchyName);
            
        }
        this.model.setSubWGGroupAllowedAmount(this.hierarchyName, Double.parseDouble(this.allowedAmountField.getText()) );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        depthLabel = new javax.swing.JLabel();
        selectionBox = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        allowedAmountField = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jPanel1.add(depthLabel);

        selectionBox.setText("jCheckBox1");
        selectionBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jPanel1.add(selectionBox);

        add(jPanel1, java.awt.BorderLayout.WEST);

        allowedAmountField.setText("jTextField1");
        allowedAmountField.setMaximumSize(new java.awt.Dimension(40, 19));
        allowedAmountField.setMinimumSize(new java.awt.Dimension(40, 19));
        allowedAmountField.setPreferredSize(new java.awt.Dimension(40, 19));
        jPanel2.add(allowedAmountField);

        add(jPanel2, java.awt.BorderLayout.EAST);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField allowedAmountField;
    private javax.swing.JLabel depthLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JCheckBox selectionBox;
    // End of variables declaration//GEN-END:variables
    
}