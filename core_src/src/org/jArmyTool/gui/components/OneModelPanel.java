/*
 * OneModelPanel.java
 *
 * Created on 10 November 2003, 20:11
 */

package org.jArmyTool.gui.components;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.jArmyTool.data.dataBeans.army.Model;
import org.jArmyTool.data.dataBeans.army.ModelUpdate;
import org.jArmyTool.data.dataBeans.util.ModelStatHolder;
import org.jArmyTool.data.dataBeans.armylist.ArmylistModelUpdate;
import org.jArmyTool.data.util.statCalc;

/**
 *
 * @author  pasi
 */
public class OneModelPanel extends javax.swing.JPanel {
    
    private static final String EXPAND_ICON_LOCATION = "images/expandUpdates.jpg";
    private static final String COLLAPSE_ICON_LOCATION = "images/collapseUpdates.jpg";
    
    private Model model;
    
    private UnitPanel parent;
    
    private OneModelStatsCollapsedPanel collapsedStatsPanel;
    
    private OneModelUpdatesCollapsed collapsedUpdatesPanel;
    private OneModelUpdatesExpanded expandedUpdatesPanel;
    
    private boolean updatesCollapsed = true;
    
    /** Creates new form OneModelPanel */
    public OneModelPanel(Model model, UnitPanel parent) {
        this.parent = parent;
        
        this.model = model;
        initComponents();
        this.refreshPointcost();
        this.collapsedStatsPanel = new OneModelStatsCollapsedPanel(this.model, this);
        this.statContainer.add(this.collapsedStatsPanel);
        
        this.initUpdates();
        
        this.totalPointcostPanel.setBackground(MainWindow.TITLE_BACKGROUND);
        
        this.expandUpdatesButton.setIcon(new ImageIcon(EXPAND_ICON_LOCATION));
        
        this.updateWithUpgrades();
    }
    
    private void initUpdates(){
        if(this.model.getUpdates().isEmpty() && this.model.getArmylistModel().getAllowedWargear() == 0){
            this.detailPanelsContainerPanel.remove(this.updateContainer);
            return;
        }
        this.collapsedUpdatesPanel = new OneModelUpdatesCollapsed(this.model, this);
        this.updateContainer.add(this.collapsedUpdatesPanel);
        
        if(this.onlyExpandedUpdates()){
            this.swapUpdatePanel();
            this.expandButtonPanel.remove(this.expandUpdatesButton);
            
            JPanel empty = new JPanel();
            empty.setPreferredSize(new Dimension(35,35));
            this.expandButtonPanel.add(empty);
        }else if(this.onlyCollapsedUpdates()){
            this.expandButtonPanel.remove(this.expandUpdatesButton);
            
            JPanel empty = new JPanel();
            empty.setPreferredSize(new Dimension(35,35));
            this.expandButtonPanel.add(empty);
        }
        
    }
    
    private boolean onlyExpandedUpdates(){
        if(this.model.getUpdates().size() <= 2 && this.model.getArmylistModel().getAllowedWargear() == 0){
            return true;
        }
        return false;
    }
    
    private boolean onlyCollapsedUpdates(){
        if(this.model.getArmylistModel().getAllowedWargear() != 0)
            return false;
        Iterator iterator = this.model.getArmylistModel().getUpdates().iterator();
        while(iterator.hasNext()){
           ArmylistModelUpdate update = (ArmylistModelUpdate) iterator.next();
           if(update.getMaxCount() != 1 || update.getMinCount() != 1)
               return false;
        }
        
        return true;
    }
    
    
    public Model getModel()
    {
        return model;
    }
    
    public void refreshPointcost(){
        int round = (int)this.model.getPointcost();
        if(round == this.model.getPointcost()){
           this.totalPointcostLabel.setText(""+round); 
        }else{
           this.totalPointcostLabel.setText(""+this.model.getPointcost());
        }
        if(this.expandedUpdatesPanel != null)
            this.expandedUpdatesPanel.refresUpdatePointcosts(); 
        if(this.collapsedUpdatesPanel != null)
            this.collapsedUpdatesPanel.refresUpdateCosts(); 
    }
    
    public void refrashParentPointcost(){
        this.parent.refreshPointcost();
        this.parent.fireCorePoincostRefresh();
    }
    
    public void refreshArmylistData(){
        this.collapsedStatsPanel.refreshArmylistData();
    }
    
    private void swapUpdatePanel(){
        if(this.updatesCollapsed){
            this.expandedUpdatesPanel = new OneModelUpdatesExpanded(this.model, this);
            this.updateContainer.remove(this.collapsedUpdatesPanel);
            this.updateContainer.add(this.expandedUpdatesPanel);
            this.updateContainer.updateUI();
            this.updatesCollapsed = false;
            this.expandUpdatesButton.setIcon(new ImageIcon(COLLAPSE_ICON_LOCATION));
        }else{
           this.collapsedUpdatesPanel = new OneModelUpdatesCollapsed(this.model, this);
           this.updateContainer.remove(this.expandedUpdatesPanel);
           this.updateContainer.add(this.collapsedUpdatesPanel); 
           this.updateContainer.updateUI();
           this.updatesCollapsed = true;
           this.expandUpdatesButton.setIcon(new ImageIcon(EXPAND_ICON_LOCATION));
        }
        this.collapsedStatsPanel.showStatModifications();
    }
    
    public void checkExclusivesForThis(){
        this.parent.checkExclusivesForModel(this.model);
    }
    
    public void refreshCount(){
        this.collapsedStatsPanel.refreshCount();
    }
    
    // enable the modelUpdate m on this model
    public void updateStats(ModelUpdate modelUpdate)
    {
        System.out.println("reCalculateStats in OneModelPanel called");
        
        Iterator modificationTreeIterator = modelUpdate.getArmylistModelUpdate().getStatModificationTreeIterator();
        Model modeltomodify = this.model;
        
        while(modificationTreeIterator.hasNext())
        {
            Map.Entry entry = (Map.Entry)(modificationTreeIterator.next());
            statCalc tree = new statCalc((statCalc)entry.getValue());
            String stat = (String)entry.getKey();
            HashMap m = new HashMap();
            ModelStatHolder modifyStatHolder = null;
            Iterator i2 = modeltomodify.getStats().iterator();
            while(i2.hasNext())
            {
                ModelStatHolder holder = (ModelStatHolder)i2.next();
                m.put(holder.getStat().getSymbol(), holder.calcValue());
                if( stat.equalsIgnoreCase(holder.getStat().getSymbol()))
                {
                    modifyStatHolder = holder;
                }
            }
            tree.replaceStats(m);
            modifyStatHolder.setCalc(tree);
           
            
        }
        
    }
    
    
    public void updateWithUpgrades()
    {
        Iterator statHolders = this.model.getStats().iterator();
        while(statHolders.hasNext())
        {
            ((ModelStatHolder)statHolders.next()).clearCalc();
        }
        Iterator updates = this.model.getUpdates().iterator();
        while(updates.hasNext())
        {
            ModelUpdate currentUpdate = (ModelUpdate)updates.next();
            for(int i = 0; i < currentUpdate.getSelectedCount(); i++)
            {
                this.updateStats(currentUpdate);
            }
        }
        this.collapsedStatsPanel.showStatModifications();
    }
 
    
    public void showStatModifications()
    {
        this.collapsedStatsPanel.showStatModifications();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        detailPanelsContainerPanel = new javax.swing.JPanel();
        statContainer = new javax.swing.JPanel();
        modelStatsPanelsContainer = new javax.swing.JPanel();
        updateContainer = new javax.swing.JPanel();
        modelUpdatesContainerPanel = new javax.swing.JPanel();
        expandButtonPanel = new javax.swing.JPanel();
        expandUpdatesButton = new javax.swing.JButton();
        totalPointcostPanel = new javax.swing.JPanel();
        totalPointcostLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        setMaximumSize(new java.awt.Dimension(600, 2147483647));
        detailPanelsContainerPanel.setLayout(new java.awt.BorderLayout());

        statContainer.setLayout(new java.awt.BorderLayout());

        statContainer.add(modelStatsPanelsContainer, java.awt.BorderLayout.CENTER);

        detailPanelsContainerPanel.add(statContainer, java.awt.BorderLayout.NORTH);

        updateContainer.setLayout(new java.awt.BorderLayout());

        updateContainer.add(modelUpdatesContainerPanel, java.awt.BorderLayout.CENTER);

        expandUpdatesButton.setBackground(new java.awt.Color(255, 255, 255));
        expandUpdatesButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        expandUpdatesButton.setMaximumSize(new java.awt.Dimension(30, 30));
        expandUpdatesButton.setMinimumSize(new java.awt.Dimension(30, 30));
        expandUpdatesButton.setPreferredSize(new java.awt.Dimension(30, 30));
        expandUpdatesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expandUpdatesButtonActionPerformed(evt);
            }
        });

        expandButtonPanel.add(expandUpdatesButton);

        updateContainer.add(expandButtonPanel, java.awt.BorderLayout.WEST);

        detailPanelsContainerPanel.add(updateContainer, java.awt.BorderLayout.SOUTH);

        add(detailPanelsContainerPanel, java.awt.BorderLayout.CENTER);

        totalPointcostPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        totalPointcostPanel.setMaximumSize(new java.awt.Dimension(40, 31));
        totalPointcostPanel.setMinimumSize(new java.awt.Dimension(40, 31));
        totalPointcostPanel.setPreferredSize(new java.awt.Dimension(40, 31));
        totalPointcostLabel.setText("jLabel1");
        totalPointcostPanel.add(totalPointcostLabel);

        add(totalPointcostPanel, java.awt.BorderLayout.EAST);

    }//GEN-END:initComponents

    private void expandUpdatesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expandUpdatesButtonActionPerformed
        this.swapUpdatePanel();
    }//GEN-LAST:event_expandUpdatesButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel detailPanelsContainerPanel;
    private javax.swing.JPanel expandButtonPanel;
    private javax.swing.JButton expandUpdatesButton;
    private javax.swing.JPanel modelStatsPanelsContainer;
    private javax.swing.JPanel modelUpdatesContainerPanel;
    private javax.swing.JPanel statContainer;
    private javax.swing.JLabel totalPointcostLabel;
    private javax.swing.JPanel totalPointcostPanel;
    private javax.swing.JPanel updateContainer;
    // End of variables declaration//GEN-END:variables
    
}
