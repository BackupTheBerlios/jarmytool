/*
 * ModelPanel.java
 *
 * Created on 26 December 2002, 01:57
 */

package org.jArmyTool.gui.components;


import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.data.dataBeans.util.*;
import org.jArmyTool.gui.listeners.*;
import org.jArmyTool.gui.engine.*;
import org.jArmyTool.gui.layout.VerticalFlowLayout;
import org.jArmyTool.gui.factories.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 *
 * @author  pasi
 */
public class ModelPanel extends javax.swing.JPanel {
    private static final int NAME_PANEL_HEIGHT = 40;
    private static final int NAME_PANEL_TWO_ROW_INCREMENT_HEIGHT = 0;
    
    private static final String WARGEAR_BUTTON_TEXT_EXPAND = "Equipment";
    private static final String WARGEAR_BUTTON_TEXT_COLLAPSE = "Collapse";
    
    private Model model;
    private GUICore guiCore;
    private ComponentFactory componentFactory;
    private ModelCountChangeListener countListener;
    
    private ModelUpdateWargearSelectionPanel currentUpdateaAndWargearPanel;
    
    private boolean isSingular;
    
    private int statStartingPoint;
    private int statSpacing;
    private boolean displayStatHeaders;
    
    private Color normalColor;
    
    /** Creates new form ModelPanel */
    public ModelPanel(Model model, GUICore guiCore, int statStartingPoint, int statSpacing, boolean displayStatHeaders) {
        
        this.displayStatHeaders = displayStatHeaders;
        this.statSpacing = statSpacing;
        this.statStartingPoint = statStartingPoint;
        this.componentFactory = ComponentFactory.getInstance();
        this.model = model;
        this.guiCore = guiCore;
        initComponents();
        this.normalColor = this.modelCountSpinner.getBackground();
        
        this.modelNamePanel.setLayout(new VerticalFlowLayout(0));
        this.countListener = new ModelCountChangeListener(this, this.model);
        
        this.refreshStats();
        
        this.initWargearAndUpdates();
        
        this.refreshPointcost();
        
        this.nameAndCountPanel.setPreferredSize(new Dimension(this.statStartingPoint, NAME_PANEL_HEIGHT));
        this.nameAndCountPanel.setMinimumSize(new Dimension(this.statStartingPoint, NAME_PANEL_HEIGHT));
        this.nameAndCountPanel.setMaximumSize(new Dimension(this.statStartingPoint, NAME_PANEL_HEIGHT));
        this.initCount();
        this.refreshName();
        
        //this.initCountSpinnerListener();
    }
    
    
 /*   private void initCountSpinnerListener(){
        this.modelCountSpinner.addChangeListener(new ChangeListener(){
           public void stateChanged(ChangeEvent e) {
                int count = ((Integer)modelCountSpinner.getValue()).intValue();
                
                if(count < model.getArmylistModel().getMinCount() || count > model.getArmylistModel().getMaxCount()){
                    modelCountSpinner.setForeground(Color.red);
                }else{
                    modelCountSpinner.setForeground(normalColor);
                }
                
                model.setModelCount(this.modelPanel.getModelCount());
                refreshPointcost();    
           }
        });
    }*/
    
    private void initWargearAndUpdates(){
         Iterator iterator = this.model.getUpdates().iterator();
        
        if(this.model.getArmylistModel().getAllowedWargear() <= 0 && !iterator.hasNext()){
            this.mainPanel.remove(this.wargearAndUpdatesButton);
            return;
        }
         
        this.wargearAndUpdatesButton.setText(WARGEAR_BUTTON_TEXT_EXPAND);
        
        this.wargearAndUpdatesButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                expandWargear();
            }
        }); 
    }
    
    private void expandWargear(){
        this.wargearAndUpdatesButton.setText(WARGEAR_BUTTON_TEXT_COLLAPSE);
        this.currentUpdateaAndWargearPanel = new ModelUpdateWargearSelectionPanel(this, this.guiCore);
        this.add(currentUpdateaAndWargearPanel, BorderLayout.SOUTH);
        this.updateUI();
        ActionListener[] listeners = this.wargearAndUpdatesButton.getActionListeners();
        for(int i = 0; i < listeners.length; ++i){
            this.wargearAndUpdatesButton.removeActionListener(listeners[i]);
        }
        this.wargearAndUpdatesButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                collapseWargear();
            }
        });        
    }
    
    private void collapseWargear(){
        this.wargearAndUpdatesButton.setText(WARGEAR_BUTTON_TEXT_EXPAND);
        this.remove(this.currentUpdateaAndWargearPanel);
        this.updateUI();
        ActionListener[] listeners = this.wargearAndUpdatesButton.getActionListeners();
        for(int i = 0; i < listeners.length; ++i){
            this.wargearAndUpdatesButton.removeActionListener(listeners[i]);
        }
        this.wargearAndUpdatesButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                expandWargear();
            }
        });             
    }
    
    private void refreshWargear(){
        //If button removed and wg now allowed the button must be added!
        
    }
    
    private void initCount(){
        if(this.model.getArmylistModel().getMaxCount() == 1 && this.model.getArmylistModel().getMinCount() ==1 ){
            this.countPanel.remove(this.modelCountCheckBox);
            this.countPanel.remove(this.modelCountSpinner);
        }else if(this.model.getArmylistModel().getMaxCount() > 1 || this.model.getArmylistModel().getMaxCount() == -1){
            this.isSingular = false;
            this.countPanel.remove(this.modelCountCheckBox);
            
            
            //this.modelCountSpinner.addChangeListener(this.countListener);
            this.modelCountSpinner.setValue(new Integer(this.model.getModelCount()));            
        }else{
            this.isSingular = true;
            
            this.countPanel.remove(this.modelCountSpinner);
            this.modelCountCheckBox.addActionListener(this.countListener);
            if(this.model.getModelCount() == 1){
                this.modelCountCheckBox.setSelected(true);
            }
        }
    }

 /*   private void addUpdates(){
        Iterator iterator = this.model.getUpdates().iterator();
        
        if(!iterator.hasNext()){//no updates
            this.remove(this.modelUpdatesPanel);
        }
        
        while(iterator.hasNext()){
            ModelUpdate modelUpdate = (ModelUpdate)iterator.next();
            this.modelUpdatesPanel.add(this.componentFactory.createModelUpdatePanel(modelUpdate, this, this.guiCore));
        }        
    }*/
    
    public void refreshArmylistData(){
        this.refreshName();
      
        this.refreshStats();
        this.refreshWargear();
    }
    
    private void refreshName(){
        this.modelNamePanel.removeAll();
        String name = this.model.getName();
        JLabel modelNameLabel = new JLabel(name);
        int width = (int)this.nameAndCountPanel.getPreferredSize().getWidth();
        //System.out.println("all width "+ width);
        //System.out.println("modelNameLabel.getWidth() "+modelNameLabel.getPreferredSize().getWidth());
        //System.out.println("this.countPanel.getWidth()  "+this.countPanel.getWidth() );
        if(modelNameLabel.getPreferredSize().getWidth() + this.countPanel.getPreferredSize().getWidth() >= width){//Name annd count doesn't fit. Something must be done.
            int space = name.lastIndexOf(" ");
            String partOne = null;
            String partTwo = null;
            if(space != -1){
                partOne = name.substring(0, space);
                partTwo = name.substring(space, name.length());
            }else{
                int cut = 15;
                if(name.length() < 15)
                    cut = name.length();
                partOne = name.substring(0, cut);
                partTwo = " " + name.substring(cut, name.length()); 
            }
            this.modelNamePanel.add(new JLabel(partOne));
            this.modelNamePanel.add(new JLabel(partTwo));

            this.nameAndCountPanel.setPreferredSize(new Dimension(width, NAME_PANEL_HEIGHT+NAME_PANEL_TWO_ROW_INCREMENT_HEIGHT));
            this.nameAndCountPanel.setMinimumSize(new Dimension(width, NAME_PANEL_HEIGHT+NAME_PANEL_TWO_ROW_INCREMENT_HEIGHT));
            this.nameAndCountPanel.setMaximumSize(new Dimension(width, NAME_PANEL_HEIGHT+NAME_PANEL_TWO_ROW_INCREMENT_HEIGHT));
            
        }else{
           this.modelNamePanel.add(modelNameLabel); 
        }
            
    }
    
    public void refreshPointcost(){
        if(this.model.getArmylistModel().getAllowedWargear() > 0){
//            this.wargearLabel.setText(""+ this.model.getWargearPointcost() + "/" + this.model.getArmylistModel().getAllowedWargear());
        }
        //this.pointcostLabel.setText(""+this.model.getPointcost());
        double cost = this.model.getPointcost();
        if(cost == (int)cost){
            this.pointcostLabel.setText(""+(int)cost);
        }else{
            this.pointcostLabel.setText(""+cost);
        }
        
        if(this.currentUpdateaAndWargearPanel != null)
            this.currentUpdateaAndWargearPanel.refreshPointCost();
        
        this.guiCore.refreshPointCosts();   
    }
    
    public void refresOnlyModelsPointcost(){
        //this.pointcostLabel.setText(""+this.model.getPointcost());
        double cost = this.model.getPointcost();
        if(cost == (int)cost){
            this.pointcostLabel.setText(""+(int)cost);
        }else{
            this.pointcostLabel.setText(""+cost);
        }        
    }
    
    private void refreshStats(){         
        int i = this.statsPanel.getComponentCount();
        for (int j = 0; j < i; ++j){
            this.statsPanel.remove(j);
        }
        this.statsPanel.add(new ModelStatPanel(this.model, this.statSpacing, this.displayStatHeaders));
        
    }
    
    public int getModelCount(){
        if(this.isSingular){
            if(this.modelCountCheckBox.getSelectedObjects() == null)
                return 0;
            return 1;
        }else{
            return ((Integer)this.modelCountSpinner.getValue()).intValue();
        }
    }
    
    public Model getModel(){
        return this.model;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        mainPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        pointcostLabel = new javax.swing.JLabel();
        statsPanel = new javax.swing.JPanel();
        wargearAndUpdatesButton = new javax.swing.JButton();
        nameAndCountPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        modelNamePanel = new javax.swing.JPanel();
        countPanel = new javax.swing.JPanel();
        modelCountCheckBox = new javax.swing.JCheckBox();
        modelCountSpinner = new javax.swing.JSpinner();

        setLayout(new java.awt.BorderLayout());

        mainPanel.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText(" points");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        mainPanel.add(jLabel3, gridBagConstraints);

        pointcostLabel.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        mainPanel.add(pointcostLabel, gridBagConstraints);

        statsPanel.setLayout(new java.awt.BorderLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        mainPanel.add(statsPanel, gridBagConstraints);

        wargearAndUpdatesButton.setText("equipment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        mainPanel.add(wargearAndUpdatesButton, gridBagConstraints);

        nameAndCountPanel.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 3));

        modelNamePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jPanel1.add(modelNamePanel);

        countPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        countPanel.add(modelCountCheckBox);

        modelCountSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                modelCountSpinnerStateChanged(evt);
            }
        });

        countPanel.add(modelCountSpinner);

        jPanel1.add(countPanel);

        nameAndCountPanel.add(jPanel1, java.awt.BorderLayout.WEST);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        mainPanel.add(nameAndCountPanel, gridBagConstraints);

        add(mainPanel, java.awt.BorderLayout.WEST);

    }//GEN-END:initComponents

    private void modelCountSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_modelCountSpinnerStateChanged
            int count = ((Integer)modelCountSpinner.getValue()).intValue();
                
            if(count < this.model.getArmylistModel().getMinCount() || ( this.model.getArmylistModel().getMaxCount() != -1 && count > this.model.getArmylistModel().getMaxCount() ) ){
                this.modelCountSpinner.setForeground(Color.red);
                this.modelCountSpinner.setBackground(Color.red);
                System.out.println("RED");
            }else{
                this.modelCountSpinner.setBackground(this.normalColor);
            }

            this.model.setModelCount(this.getModelCount());
            this.refreshPointcost();   
    }//GEN-LAST:event_modelCountSpinnerStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel countPanel;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JCheckBox modelCountCheckBox;
    private javax.swing.JSpinner modelCountSpinner;
    private javax.swing.JPanel modelNamePanel;
    private javax.swing.JPanel nameAndCountPanel;
    private javax.swing.JLabel pointcostLabel;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JButton wargearAndUpdatesButton;
    // End of variables declaration//GEN-END:variables
    
}
