/*
 * UnitUpdatePanel.java
 *
 * Created on 29 December 2002, 02:09
 */

package org.jArmyTool.gui.components;

import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.data.dataBeans.util.ModelStatHolder;
import org.jArmyTool.data.util.statCalc;
import java.util.Map.Entry;
import java.util.*;
/**
 *
 * @author  pasi
 */
public class ModelUpdatePanel extends javax.swing.JPanel {
    private static final int MAX_NAME_LENGTH = 20;
    private static final int SEARCH_START_INDEX = 10;
    
    private ModelUpdate modelUpdate;
    private OneModelPanel oneModelPanel;
    
    //private boolean isSingular;
    
    /** Creates new form UnitUpdatePanel */
    public ModelUpdatePanel(ModelUpdate modelUpdate, OneModelPanel oneModelPanel) {
        
        this.modelUpdate = modelUpdate;
        this.oneModelPanel = oneModelPanel;
        initComponents();
        this.refreshPointcost();
        this.initCount();
        this.refreshArmylistData();
        //modelUpdateCountChangeListener = new ModelUpdateCountChangeListener(this, this.modelUpdate, this.modelPanel, this.guiCore);
        
        //this.initState();
        
    }
    
    public void applyStatModifications()
    { 
        this.oneModelPanel.updateStats(this.modelUpdate);  
    }
    
    public void removeStatModifications()
    {

        this.oneModelPanel.removeStatUpdate(this.modelUpdate);
    }
    
    private void initCount(){
        if(this.modelUpdate.getArmylistModelUpdate().getMinCount() == 1 && this.modelUpdate.getArmylistModelUpdate().getMaxCount() == 1){
            this.countPanel.remove(this.modelUpdateCountCheckBox);
            this.countPanel.remove(this.updateCountSpinner);
        
        }else if(this.modelUpdate.getArmylistModelUpdate().getMaxCount() > 1 || this.modelUpdate.getArmylistModelUpdate().getMaxCount() == -1){
            //this.isSingular = false;
            this.countPanel.remove(this.modelUpdateCountCheckBox);
             
            
            //this.updateCountSpinner.addChangeListener(this.modelUpdateCountChangeListener);
            this.updateCountSpinner.setValue(new Integer(this.modelUpdate.getSelectedCount()));
            
            
        }else{
            //this.isSingular = true;

            this.countPanel.remove(this.updateCountSpinner);
            this.remove(this.updateNameLabel);
            this.modelUpdateCountCheckBox.setText(this.modelUpdate.getName());
            //this.modelUpdateCountCheckBox.addActionListener(this.modelUpdateCountChangeListener);
            if(this.modelUpdate.getSelectedCount() == 1){
                this.modelUpdateCountCheckBox.setSelected(true);
            }
        }
    }
    
    /*private void initState(){
        int count = this.modelUpdate.getSelectedCount();
        if(count <= 0)
            return;
        
        if(this.isSingular){
            this.modelUpdateCountCheckBox.setSelected(true);
        }else{
            this.updateCountSpinner.setValue(new Integer(count));
        }
    }*/
    
    public void refreshPointcost(){
        if(this.modelUpdate.getPointcost() == 0){
            this.updatePointcost.setText("");
        }else{
            double cost = this.modelUpdate.getPointcost();
            if(cost == (int)cost){
                this.updatePointcost.setText(""+(int)cost);
            }else{
                this.updatePointcost.setText(""+cost);
            }
        }
    }
    
    public void refreshArmylistData(){
        this.refreshName();
    }
    
    private void refreshName(){
        String name;
        if(this.modelUpdate.getName().length() > MAX_NAME_LENGTH){
            int space = this.modelUpdate.getName().substring(SEARCH_START_INDEX, MAX_NAME_LENGTH).lastIndexOf(" ") + SEARCH_START_INDEX;
            if(space != -1){
                name = "<html>"+ this.modelUpdate.getName().substring(0, space) +"<p>" +this.modelUpdate.getName().substring(space+1, this.modelUpdate.getName().length()) + "</html>";
            }else{
                if( !this.modelUpdate.getArmylistModelUpdate().isPointcostPerModel() && this.modelUpdate.getName().length() > MAX_NAME_LENGTH+6){
                    name = this.modelUpdate.getName().substring(0, MAX_NAME_LENGTH+6) + "..";
                }else{
                    name = this.modelUpdate.getName().substring(0, MAX_NAME_LENGTH) + "..";
                }
            }
            
        }else{
            name = this.modelUpdate.getName();
        }
        
        
        if(this.modelUpdate.getArmylistModelUpdate().getPointcost() == 0){
            this.modelUpdateCountCheckBox.setText(name);   
            this.updateNameLabel.setText(name);
        }else{
            int round = (int)this.modelUpdate.getArmylistModelUpdate().getPointcost();
            String listCost = null;
            if(round == this.modelUpdate.getArmylistModelUpdate().getPointcost()){
                listCost = "(" + round;
            }else{
                listCost = "(" + this.modelUpdate.getArmylistModelUpdate().getPointcost();
            }

            if(this.modelUpdate.getArmylistModelUpdate().isPointcostPerModel()){
                listCost = listCost + "/model";
            }

            this.modelUpdateCountCheckBox.setText(name + " " +listCost +")");   
            this.updateNameLabel.setText(name + " " + listCost +")");
        }
        this.modelUpdateCountCheckBox.setToolTipText(this.modelUpdate.getName());
        this.updateNameLabel.setToolTipText(this.modelUpdate.getName());
    }

 /*   public int getUpdateCount(){
         if(this.isSingular){
            if(this.modelUpdateCountCheckBox.getSelectedObjects() == null)
                return 0;
            return 1;
        }else{
            return ((Integer)this.updateCountSpinner.getValue()).intValue();
        }
    }*/
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        countPanel = new javax.swing.JPanel();
        updateCountSpinner = new javax.swing.JSpinner();
        modelUpdateCountCheckBox = new javax.swing.JCheckBox();
        updateNameLabel = new javax.swing.JLabel();
        updatePointcost = new javax.swing.JLabel();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 0));

        countPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        updateCountSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updateCountSpinnerStateChanged(evt);
            }
        });

        countPanel.add(updateCountSpinner);

        modelUpdateCountCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modelUpdateCountCheckBoxActionPerformed(evt);
            }
        });

        countPanel.add(modelUpdateCountCheckBox);

        add(countPanel);

        updateNameLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        updateNameLabel.setText("name");
        add(updateNameLabel);

        updatePointcost.setFont(new java.awt.Font("Dialog", 1, 14));
        updatePointcost.setText("cost");
        add(updatePointcost);

    }//GEN-END:initComponents

    private void modelUpdateCountCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modelUpdateCountCheckBoxActionPerformed
        if(this.modelUpdateCountCheckBox.isSelected())
        {
            this.applyStatModifications();
            this.modelUpdate.setSelectedCount(1);
        }else
        {
            this.modelUpdate.setSelectedCount(0);
            this.removeStatModifications();
        }
        
        this.refreshPointcost();
        this.oneModelPanel.refrashParentPointcost();
        this.oneModelPanel.refreshPointcost();
    }//GEN-LAST:event_modelUpdateCountCheckBoxActionPerformed

    private void updateCountSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_updateCountSpinnerStateChanged
        this.modelUpdate.setSelectedCount(((Integer)this.updateCountSpinner.getValue()).intValue());
        this.refreshPointcost();
        this.oneModelPanel.refrashParentPointcost();
        this.oneModelPanel.refreshPointcost();
    }//GEN-LAST:event_updateCountSpinnerStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel countPanel;
    private javax.swing.JCheckBox modelUpdateCountCheckBox;
    private javax.swing.JSpinner updateCountSpinner;
    private javax.swing.JLabel updateNameLabel;
    private javax.swing.JLabel updatePointcost;
    // End of variables declaration//GEN-END:variables
    
}
