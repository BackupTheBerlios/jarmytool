/*
 * OneModelStatsCollapsedPanel.java
 *
 * Created on 10 November 2003, 20:29
 */

package org.jArmyTool.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import org.jArmyTool.data.dataBeans.army.Model;

/**
 *
 * @author  pasi
 */
public class OneModelStatsCollapsedPanel extends javax.swing.JPanel {
    
    private Model model;
    
    private OneModelPanel parent;
    
    private Color normalColor;
    
    /** Creates new form OneModelStatsCollapsedPanel */
    public OneModelStatsCollapsedPanel(Model model, OneModelPanel parent) {
        this.parent = parent;
        this.model = model;
        initComponents();
        this.normalColor = this.modelCountSpinner.getBackground();
        this.refreshArmylistData();
        this.initCount();
        this.initStats();
        
        this.nameLabel.setPreferredSize(new Dimension(150, 30));
        
    }
    
    private void initStats(){
        OneModelStatsPanel stats = new OneModelStatsPanel(this.model);
        this.statsPanel.add(stats);
    }
    
    public void refreshArmylistData(){
        this.nameLabel.setText(this.model.getName());
    }
    
    public void showStatModifications()
    {
        this.statsPanel.removeAll();
        OneModelStatsPanel stats = new OneModelStatsPanel(this.parent.getModel());
        this.statsPanel.add(stats);
    }
    
    private void initCount(){
        if(this.model.getArmylistModel().getMaxCount() == 1 && this.model.getArmylistModel().getMinCount() ==1 ){
            this.countPanel.remove(this.modelCountCheckBox);
            this.countPanel.remove(this.modelCountSpinner);
            //this.countPanel.add(new JLabel("1"));
        }else if(this.model.getArmylistModel().getMaxCount() > 1 || this.model.getArmylistModel().getMaxCount() == -1){
            //this.isSingular = false;
            this.countPanel.remove(this.modelCountCheckBox);
            
            
            //this.modelCountSpinner.addChangeListener(this.countListener);
            this.modelCountSpinner.setValue(new Integer(this.model.getModelCount()));            
        }else{
            //this.isSingular = true;
            
            this.countPanel.remove(this.modelCountSpinner);
            if(this.model.getModelCount() == 1){
                this.modelCountCheckBox.setSelected(true);
            }
        }
    }   
    
    public void refreshCount(){
        this.modelCountSpinner.setValue(new Integer(this.model.getModelCount()));   
        if(this.model.getModelCount() == 0){
                this.modelCountCheckBox.setSelected(false);
        }else{
            this.modelCountCheckBox.setSelected(true);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        countPanel = new javax.swing.JPanel();
        modelCountCheckBox = new javax.swing.JCheckBox();
        modelCountSpinner = new javax.swing.JSpinner();
        statsPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridBagLayout());

        nameLabel.setFont(new java.awt.Font("Default", 0, 10));
        nameLabel.setText("error loading model name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        jPanel2.add(nameLabel, gridBagConstraints);

        countPanel.setLayout(new java.awt.BorderLayout());

        countPanel.setMaximumSize(new java.awt.Dimension(50, 28));
        countPanel.setMinimumSize(new java.awt.Dimension(50, 28));
        countPanel.setPreferredSize(new java.awt.Dimension(50, 28));
        modelCountCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modelCountCheckBoxActionPerformed(evt);
            }
        });

        countPanel.add(modelCountCheckBox, java.awt.BorderLayout.CENTER);

        modelCountSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                modelCountSpinnerStateChanged(evt);
            }
        });

        countPanel.add(modelCountSpinner, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel2.add(countPanel, gridBagConstraints);

        statsPanel.setLayout(new java.awt.BorderLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel2.add(statsPanel, gridBagConstraints);

        add(jPanel2, java.awt.BorderLayout.WEST);

    }//GEN-END:initComponents

    private void modelCountSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_modelCountSpinnerStateChanged
            int count = ((Integer)modelCountSpinner.getValue()).intValue();
                
            if(count < this.model.getArmylistModel().getMinCount() || ( this.model.getArmylistModel().getMaxCount() != -1 && count > this.model.getArmylistModel().getMaxCount() )){
                this.modelCountSpinner.setForeground(Color.red);
                this.modelCountSpinner.setBackground(Color.red);
            }else{
                this.modelCountSpinner.setBackground(this.normalColor);
            }
            
            /*if(count == 0 || count == 1)
                this.parent.checkExclusivesForThis();*/

            this.model.setModelCount(((Integer)this.modelCountSpinner.getValue()).intValue());
            this.parent.refreshPointcost();
            this.parent.refrashParentPointcost();
    }//GEN-LAST:event_modelCountSpinnerStateChanged

    private void modelCountCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modelCountCheckBoxActionPerformed
        if(this.modelCountCheckBox.isSelected()){
            this.model.setModelCount(1);
            this.parent.checkExclusivesForThis();
        }else{
            this.model.setModelCount(0);
            this.parent.checkExclusivesForThis();
        }
        this.parent.refreshPointcost();
        this.parent.refrashParentPointcost();
    }//GEN-LAST:event_modelCountCheckBoxActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel countPanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JCheckBox modelCountCheckBox;
    private javax.swing.JSpinner modelCountSpinner;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPanel statsPanel;
    // End of variables declaration//GEN-END:variables
    
}
