/*
 * ModelUpdateWargearPanel.java
 *
 * Created on 25 June 2003, 14:27
 */

package core_src.src.org.jArmyTool.gui.components;

import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.gui.engine.GUICore;
import org.jArmyTool.gui.factories.ComponentFactory;

import java.util.*;
import javax.swing.*;

/**
 *
 * @author  pasleh
 */
public class ModelUpdateWargearSelectionPanel extends javax.swing.JPanel {
    
    private Model model;
    private ModelPanel modelPanel;
    private GUICore guiCore;
    private ComponentFactory componentFactory;
    
    private DefaultListModel selectedWargear;
    private DefaultListModel availiableWargear;  
    
    private HashMap groupIds;
    
    private LinkedList updatePanels;
    
    /** Creates new form ModelUpdateWargearPanel */
    public ModelUpdateWargearSelectionPanel(ModelPanel modelPanel, GUICore guiCore) {
        this.updatePanels = new LinkedList();
        this.groupIds = new HashMap();
        
        this.guiCore = guiCore;
        this.modelPanel = modelPanel;
        this.model = modelPanel.getModel();  
        this.componentFactory = ComponentFactory.getInstance();
        
        
        
        initComponents();
        this.initUpdates();
        this.initWargear();
    }
    
    private void initUpdates(){
         Iterator iterator = this.model.getUpdates().iterator();
        
        if(!iterator.hasNext()){
            this.modelUpdatePanelContainer.remove(this.modelUpdatePanel);
            return;
        }   
         
        
        this.modelUpdatePanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints = null;
        int x = 0;
        int y = 0;
        double currentWidth = 0;
        double maxWidth = this.modelPanel.getMinimumSize().getWidth();
        
  /*      while(iterator.hasNext()){
            ModelUpdate modelUpdate = (ModelUpdate)iterator.next();
            ModelUpdatePanel updatePanel = this.componentFactory.createModelUpdatePanel(modelUpdate, this.modelPanel, this.guiCore);
            currentWidth +=  updatePanel.getPreferredSize().getWidth();
            if(currentWidth >= maxWidth){
                currentWidth = 0;
                x = 0;
                ++y;
            }
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = x;
            gridBagConstraints.gridy = y;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            
            this.modelUpdatePanel.add(updatePanel, gridBagConstraints);
            
            ++x;
            
            this.updatePanels.add(updatePanel);
        } 
         
    */     
        
       /* while(iterator.hasNext()){
            ModelUpdate modelUpdate = (ModelUpdate)iterator.next();
            ModelUpdatePanel updatePanel = this.componentFactory.createModelUpdatePanel(modelUpdate, this.modelPanel, this.guiCore);
            this.modelUpdatePanel.add(updatePanel);   
        } */
    }
    
    
    private void initWargear(){
        if(this.model.getArmylistModel().getAllowedWargear() <= 0){
            this.remove(this.wargearPanel);
            return;
        }
        
        this.availiableWargear = new  DefaultListModel();
        this.selectedWargear = new  DefaultListModel();
        this.availiableWargearList.setModel(this.availiableWargear);
        this.selectedWargearList.setModel(this.selectedWargear);
        
        Iterator groups = this.model.getArmylistModel().getAllowedWargearGroups().iterator();
        
        while(groups.hasNext()){
            String groupName = (String)groups.next();
            ArmylistWargearGroup grp = this.model.getArmylistModel().getArmylistArmy().getWargearGroupbyName(groupName);
            
            Iterator items = grp.getItems().iterator();
            Collection selected = this.model.getSelectedWargear(grp.getName());
            while(items.hasNext()){
                ArmylistWargearItem item = (ArmylistWargearItem)items.next();
                this.groupIds.put(item, groupName);
                if(selected.contains(item)){
                    this.selectedWargear.addElement(item);
                }else{
                    this.availiableWargear.addElement(item);
                }
            }
            
            
        }
        
    }
    
    private void addWargear(){
        if(this.availiableWargearList.getSelectedIndex() != -1){
            ArmylistWargearItem item = (ArmylistWargearItem)this.availiableWargearList.getSelectedValue();
            
            this.model.selectWargear((String)this.groupIds.get(item), item);
            this.selectedWargear.addElement(this.availiableWargearList.getSelectedValue());
            this.availiableWargear.remove(this.availiableWargearList.getSelectedIndex());
            
            this.guiCore.refreshPointCosts();
        }
        
    }
    
    private void removeWargear(){
        if(this.selectedWargearList.getSelectedIndex() != -1){
            ArmylistWargearItem item = (ArmylistWargearItem)this.selectedWargearList.getSelectedValue();
            
            this.model.deSelectWargear((String)this.groupIds.get(item), item);
            
            this.availiableWargear.addElement(this.selectedWargearList.getSelectedValue());
            this.selectedWargear.remove(this.selectedWargearList.getSelectedIndex());
            
            this.guiCore.refreshPointCosts();
        }        
    }
    
    public void refreshPointCost(){
        Iterator iterator = this.updatePanels.iterator();
        while(iterator.hasNext()){
            ((ModelUpdatePanel)iterator.next()).refreshPointcost();
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        modelUpdatePanelContainer = new javax.swing.JPanel();
        modelUpdatePanel = new javax.swing.JPanel();
        wargearPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        selectedWargearList = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        availiableWargearList = new javax.swing.JList();
        removeWargearButton = new javax.swing.JButton();
        addWargearButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        modelUpdatePanelContainer.setLayout(new java.awt.BorderLayout());

        modelUpdatePanel.setLayout(new java.awt.GridBagLayout());

        modelUpdatePanel.setBorder(new javax.swing.border.TitledBorder("Model Updates"));
        modelUpdatePanelContainer.add(modelUpdatePanel, java.awt.BorderLayout.WEST);

        add(modelUpdatePanelContainer, java.awt.BorderLayout.NORTH);

        wargearPanel.setLayout(new java.awt.GridBagLayout());

        wargearPanel.setBorder(new javax.swing.border.TitledBorder("Wargear"));
        jScrollPane3.setViewportView(selectedWargearList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        wargearPanel.add(jScrollPane3, gridBagConstraints);

        jScrollPane4.setViewportView(availiableWargearList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        wargearPanel.add(jScrollPane4, gridBagConstraints);

        removeWargearButton.setText("remove");
        removeWargearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeWargearButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        wargearPanel.add(removeWargearButton, gridBagConstraints);

        addWargearButton.setText("add");
        addWargearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWargearButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        wargearPanel.add(addWargearButton, gridBagConstraints);

        jLabel3.setText("availiable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        wargearPanel.add(jLabel3, gridBagConstraints);

        jLabel4.setText("selected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        wargearPanel.add(jLabel4, gridBagConstraints);

        add(wargearPanel, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void removeWargearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeWargearButtonActionPerformed
        this.removeWargear();
    }//GEN-LAST:event_removeWargearButtonActionPerformed

    private void addWargearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addWargearButtonActionPerformed
        this.addWargear();
    }//GEN-LAST:event_addWargearButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addWargearButton;
    private javax.swing.JList availiableWargearList;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel modelUpdatePanel;
    private javax.swing.JPanel modelUpdatePanelContainer;
    private javax.swing.JButton removeWargearButton;
    private javax.swing.JList selectedWargearList;
    private javax.swing.JPanel wargearPanel;
    // End of variables declaration//GEN-END:variables
    
}
