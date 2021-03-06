/*
 * OneModelUpdatesCollapsed.java
 *
 * Created on 10 November 2003, 21:50
 */

package org.jArmyTool.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jArmyTool.data.dataBeans.army.Model;
import org.jArmyTool.data.dataBeans.army.ModelUpdate;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearGroup;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearItem;
import org.jArmyTool.gui.layout.VerticalFlowLayout;

/**
 *
 * @author  pasi
 */
public class OneModelUpdatesCollapsed extends javax.swing.JPanel {
    private static final int MAX_NAME_LENGTH = 24;
    
    private static final int SEARCH_START_INDEX = 15;    
   
    private static final int SUB_WG_GROUP_TAB = 5;
    
    public static final int WG_GROUP_ROOT_COLOR_R =  52;
    public static final int WG_GROUP_ROOT_COLOR_G =  152;
    public static final int WG_GROUP_ROOT_COLOR_B =  204;
    public static final int WG_GROUP_ROOT_COLOR_SHIFT =  15;    
    
    private Model model;
    
    private OneModelPanel parent;  
    
    /** Creates new form OneModelUpdatesCollapsed */
    public OneModelUpdatesCollapsed(Model model, OneModelPanel parent) {
        this.model = model;
        this.parent = parent;
        initComponents();
        this.updatesContainer.setLayout(new VerticalFlowLayout());
        this.wargearContainer.setLayout(new VerticalFlowLayout());
        this.wargearContainer.setMinimumSize(new Dimension(250, (int)this.wargearContainer.getMinimumSize().getHeight()));
        this.initUpdates();
        this.initWargear();
        
        
        
        /*Dimension size = new Dimension((int)parent.getSize().getWidth()+100, (int)this.getPreferredSize().getHeight());
        this.setPreferredSize(size);
        this.setMaximumSize(size);*/
    }
    
    private void initUpdates(){
        Iterator iterator = this.model.getUpdates().iterator();
        
        while(iterator.hasNext()){
            final ModelUpdate update = (ModelUpdate)iterator.next();
            JLabel updateLabel = null;
            if(update.getSelectedCount() > 0){
                String pointcost;
                if(update.getPointcost() == (int)update.getPointcost()){
                    pointcost = "" + (int)update.getPointcost();
                }else{
                    pointcost = "" + update.getPointcost();
                }
                
                if(update.getPointcost() == 0)
                    pointcost = "";
                
                if(update.getSelectedCount() > 1){ 
                    updateLabel = new JLabel(update.getSelectedCount()  + "X" + update.getName() + ((update.getPointcost() == 0)? "" : "(" + pointcost+ ")") );
                }else{
                    updateLabel = new JLabel(update.getName() + " " + ((update.getPointcost() == 0)? "" : "(" + pointcost+ ")") );
                }
                this.updatesContainer.add(updateLabel);
            }else{
                continue;
            }
            if(update.getArmylistModelUpdate().getMinCount() != 1 || update.getArmylistModelUpdate().getMaxCount() != 1){
                final JLabel tempLabel = updateLabel;
                updateLabel.setCursor(MainWindow.DEL_CURSOR);
                updateLabel.addMouseListener(new MouseAdapter(){
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        mouseExited_(tempLabel);
                    }
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        mouseEntered_(tempLabel);
                    }        
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        mouseClicked_(update, tempLabel);
                    }                
                });
            }
        }
    }
    
    public void mouseEntered_(JLabel target){
        target.setFont(new Font(target.getFont().getName(), Font.BOLD, target.getFont().getSize()));
    }
    
    private void mouseExited_(JLabel target){
        target.setFont(new Font(target.getFont().getName(), Font.PLAIN, target.getFont().getSize()));
    }
    
    private void mouseClicked_(ModelUpdate targetUpdate, JLabel target){
        Iterator iterator = this.model.getUpdates().iterator();
        while(iterator.hasNext()){
            ModelUpdate update = (ModelUpdate)iterator.next();
            if(update == targetUpdate){
                update.setSelectedCount(update.getSelectedCount()-1);
                
                if(update.getSelectedCount() <= 0){
                    this.updatesContainer.remove(target);
                    }else{
                        String pointcost;
                        if(update.getPointcost() == (int)update.getPointcost()){
                            pointcost = "" + (int)update.getPointcost();
                        }else{
                            pointcost = "" + update.getPointcost();
                        }

                        if(update.getPointcost() == 0)
                            pointcost = "";
                        if(update.getSelectedCount() > 1){ 
                        target.setText(update.getSelectedCount()  + "X" + update.getName() + ((update.getPointcost() == 0)? "" : "(" + pointcost+ ")") );
                    }else{
                        target.setText(update.getName() + " " + ((update.getPointcost() == 0)? "" : "(" + pointcost+ ")") );
                    }
                }
                this.parent.updateWithUpgrades();
                this.updatesContainer.updateUI();
                this.parent.refrashParentPointcost();
                this.parent.refreshPointcost();
                return;
            }
        }
    }
    
    private void initWargear(){
        this.wargearContainer.removeAll();
        
        if(this.model.getArmylistModel().getAllowedWargear() == 0)
            return;
        
        Iterator groups = this.model.getArmylistModel().getAllowedWargearGroups().iterator();
        while(groups.hasNext()){
            final ArmylistWargearGroup group = this.model.getArmylistModel().getArmylistArmy().getWargearGroupbyName((String)groups.next());
            if(group == null)
                continue;
            boolean hasItems = false;
            
            JPanel groupHeaderPanelSelected = new JPanel();
            groupHeaderPanelSelected.add(new JLabel(group.getName()));
            groupHeaderPanelSelected.setBackground(new Color(WG_GROUP_ROOT_COLOR_R, WG_GROUP_ROOT_COLOR_G, WG_GROUP_ROOT_COLOR_B));
            groupHeaderPanelSelected.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
            
            
            
            final JPanel selectedPanelGroup = new JPanel();
            selectedPanelGroup.setLayout(new VerticalFlowLayout());
            selectedPanelGroup.add(groupHeaderPanelSelected);
            selectedPanelGroup.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
            
            
            Collection selectedInGroup = this.model.getSelectedWargear(group.getName());
            
            Iterator itemsInGroup = group.getItems().iterator();
            while(itemsInGroup.hasNext()){
                final ArmylistWargearItem item = (ArmylistWargearItem)itemsInGroup.next();
                String name = item.getName();
                /*if(item.getName().length() > MAX_NAME_LENGTH){
                    name = item.getName().substring(0, MAX_NAME_LENGTH) + "..";
                }else{
                    name = item.getName();
                }*/
                final JLabel selectedLabel;
                
                if(item.getPointcost() == (int)item.getPointcost()){
                    selectedLabel = new JLabel(name+ " " + ((int)item.getPointcost()));
                }else{
                    selectedLabel = new JLabel(name + " " + item.getPointcost());
                }
                selectedLabel.setToolTipText(item.getName());
                
                if(selectedInGroup.contains(item)){
                    hasItems = true;
                    selectedPanelGroup.add(selectedLabel);
                    selectedLabel.addMouseListener(new MouseAdapter(){
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                removeWG(selectedPanelGroup, selectedLabel, group.getName(), item);
                            }       
                            
                            public void mouseEntered(java.awt.event.MouseEvent evt) {
                                selectedLabel.setFont(new Font(selectedLabel.getFont().getName(), Font.BOLD, selectedLabel.getFont().getSize()));
                            }        
                            public void mouseExited(java.awt.event.MouseEvent evt) {
                                selectedLabel.setFont(new Font(selectedLabel.getFont().getName(), Font.PLAIN, selectedLabel.getFont().getSize()));
                            }   
                    });         
                    selectedLabel.setCursor(MainWindow.DEL_CURSOR);
                }
            }
           /* if(hasItems)
                this.wargearContainer.add(selectedPanelGroup);*/
            
            //Process subGroups:
            Iterator subGroups = group.getSubGroups().iterator();
            while(subGroups.hasNext()){
                ArmylistWargearGroup subGroup = (ArmylistWargearGroup)subGroups.next();
                JPanel subSelectedParentPanel = new JPanel();
                

                this.initSubWGGroups(group.getName()+".", 1, subGroup, subSelectedParentPanel);
  //                this.allWG.add(subAllParentPanel);
                    selectedPanelGroup.add(subSelectedParentPanel);
            }            
            this.wargearContainer.add(selectedPanelGroup);
        }        
        
        
    }
    
    
    private void initSubWGGroups(final String path, int depth, final ArmylistWargearGroup group, final JPanel selectedParent){
            
            Iterator allowedSubs = this.model.getArmylistModel().getAllowedWargearGroups().iterator();
            boolean allowed = false;
            while(allowedSubs.hasNext()){
                String temp = (String)allowedSubs.next();
                System.out.println(temp + " - " + path+group.getName());
                
                if( (temp).compareToIgnoreCase(path+group.getName()) == 0 ){
                    allowed = true;
                    break;
                }
                
                
                    
                
            }
            
            // System.out.println("--------- "+allowed);
            if(!allowed)
                return;
        
            final JPanel allWGPanelSub = new JPanel();
            JPanel tabPanel = new JPanel();
            tabPanel.setPreferredSize(new Dimension(depth * SUB_WG_GROUP_TAB , 0));
            tabPanel.setMaximumSize(new Dimension(depth * SUB_WG_GROUP_TAB , 0));
            tabPanel.setMinimumSize(new Dimension(depth * SUB_WG_GROUP_TAB , 0));
            
//            allParent.setLayout(new BorderLayout());
//            allParent.add(tabPanel, BorderLayout.WEST);
//            allParent.add(allWGPanelSub, BorderLayout.CENTER);
            allWGPanelSub.setLayout(new VerticalFlowLayout());
        
            final JPanel selectedWGPanelSub = new JPanel();
            
            JPanel tabPanel2 = new JPanel();
            tabPanel2.setPreferredSize(new Dimension(depth * SUB_WG_GROUP_TAB , 0));
            tabPanel2.setMaximumSize(new Dimension(depth * SUB_WG_GROUP_TAB , 0));
            tabPanel2.setMinimumSize(new Dimension(depth * SUB_WG_GROUP_TAB , 0));
            
            selectedParent.setLayout(new BorderLayout());
            selectedParent.add(tabPanel2, BorderLayout.WEST);
            selectedParent.add(selectedWGPanelSub, BorderLayout.CENTER);            
            selectedWGPanelSub.setLayout(new VerticalFlowLayout());
            
            
            JPanel groupHeaderPanelAll = new JPanel();
            groupHeaderPanelAll.add(new JLabel(group.getName()));
            groupHeaderPanelAll.setBackground(new Color(WG_GROUP_ROOT_COLOR_R + WG_GROUP_ROOT_COLOR_SHIFT*depth, WG_GROUP_ROOT_COLOR_G + WG_GROUP_ROOT_COLOR_SHIFT*depth, WG_GROUP_ROOT_COLOR_B + WG_GROUP_ROOT_COLOR_SHIFT*depth));
            groupHeaderPanelAll.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
            
            allWGPanelSub.add(groupHeaderPanelAll);
            
            
            JPanel groupHeaderPanelSelected = new JPanel();
            groupHeaderPanelSelected.add(new JLabel(group.getName()));
            groupHeaderPanelSelected.setBackground(new Color(WG_GROUP_ROOT_COLOR_R + WG_GROUP_ROOT_COLOR_SHIFT*depth, WG_GROUP_ROOT_COLOR_G + WG_GROUP_ROOT_COLOR_SHIFT*depth, WG_GROUP_ROOT_COLOR_B + WG_GROUP_ROOT_COLOR_SHIFT*depth));
            groupHeaderPanelSelected.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));            
            
            selectedWGPanelSub.add(groupHeaderPanelSelected);
            
            
            Collection selectedInGroup = this.model.getSelectedWargear(path + group.getName());
            
            
           // System.out.println("check selected for: " + path + group.getName() + " got: "+selectedInGroup);
            
            Iterator itemsInGroup = group.getItems().iterator();
            while(itemsInGroup.hasNext()){
                final ArmylistWargearItem item = (ArmylistWargearItem)itemsInGroup.next();
                String name;
                
                boolean needEndHTML = false;
                if(item.getName().length() > MAX_NAME_LENGTH){
                    int space = item.getName().substring(SEARCH_START_INDEX, MAX_NAME_LENGTH).lastIndexOf(" ") + SEARCH_START_INDEX;
                    if(space != -1){
                        name = "<html>"+ item.getName().substring(0, space) +"<p>" +item.getName().substring(space+1, item.getName().length());
                        needEndHTML = true;
                    }else{
                        name = item.getName().substring(0, MAX_NAME_LENGTH) + "..";
                    }
                }else{
                    name = item.getName();
                }
//                final JCheckBox box;
                final JLabel selectedLabel;
                
                if(item.getPointcost() == (int)item.getPointcost()){
 //                   box = new JCheckBox(name+ " " + ((int)item.getPointcost()));
                    if(needEndHTML){
                        selectedLabel = new JLabel(name+ " " + ((int)item.getPointcost()) + "</html>");
                    }else{
                        selectedLabel = new JLabel(name+ " " + ((int)item.getPointcost()));
                    }
                }else{
//                    box = new JCheckBox(name+ " " + item.getPointcost());
                    if(needEndHTML){
                        selectedLabel = new JLabel(name + " " + item.getPointcost() + "</html>");
                    }else{
                        selectedLabel = new JLabel(name + " " + item.getPointcost());
                    }
                }
                    
//                box.setToolTipText(item.getName());
                selectedLabel.setToolTipText(item.getName());
                
               // new JCheckBox(item.getName()+ " " + (item.getPointcost() == ((int)item.getPointcost()) ? ((int)item.getPointcost()) : (int)item.getPointcost())  );
                 //= new JLabel(item.getName() + " " + (item.getPointcost()== ((int)item.getPointcost()) ? ((int)item.getPointcost()) : (int)item.getPointcost())  );
                if(selectedInGroup != null && selectedInGroup.contains(item)){
                    selectedWGPanelSub.add(selectedLabel);
                    selectedLabel.addMouseListener(new MouseAdapter(){
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                removeWG(selectedWGPanelSub, selectedLabel, path + group.getName(), item);
                            }       
                            
                            public void mouseEntered(java.awt.event.MouseEvent evt) {
                                selectedLabel.setFont(new Font(selectedLabel.getFont().getName(), Font.BOLD, selectedLabel.getFont().getSize()));
                            }        
                            public void mouseExited(java.awt.event.MouseEvent evt) {
                                selectedLabel.setFont(new Font(selectedLabel.getFont().getName(), Font.PLAIN, selectedLabel.getFont().getSize()));
                            }   
                    });         
                    selectedLabel.setCursor(MainWindow.DEL_CURSOR);
//                    box.setSelected(true);
                }
/*                box.addActionListener(new ActionListener(){
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        if(box.isSelected()){
                            addWG(path + group.getName(), item, selectedWGPanelSub, box);
                        }else{
                            removeWG(selectedWGPanelSub, selectedLabel, path + group.getName(), item, box);
                        }
                    }                    
                });
                
                allWGPanelSub.add(box);*/
                
            }     
            
            //Process subGroups:
            Iterator subGroups = group.getSubGroups().iterator();
            while(subGroups.hasNext()){
                ArmylistWargearGroup subGroup = (ArmylistWargearGroup)subGroups.next();
                JPanel subSelectedParentPanel = new JPanel();
                JPanel subAllParentPanel = new JPanel();

                this.initSubWGGroups(path+ group.getName()+".", depth+1, subGroup, subSelectedParentPanel);
                //allWGPanelSub.add(subAllParentPanel);
                //selectedWGPanelSub.add(subSelectedParentPanel);
                
                selectedParent.add(subSelectedParentPanel, BorderLayout.SOUTH);    
 //               allParent.add(subAllParentPanel, BorderLayout.SOUTH);
            }            
        
    }    
    
    
    private void removeWG(JPanel selectedGroupPanel, JLabel itemLabel, String group, ArmylistWargearItem item){
        this.model.deSelectWargear(group, item);
        selectedGroupPanel.remove(itemLabel);
        selectedGroupPanel.updateUI();
        this.parent.refrashParentPointcost();
        this.parent.refreshPointcost();
        this.initWargear();
    }    
    
    
    public void refresUpdateCosts(){
        this.updatesContainer.removeAll();
        this.initUpdates();
        this.updatesContainer.updateUI();
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        updatesContainer = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        wargearContainer = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        setMaximumSize(new java.awt.Dimension(600, 2147483647));
        updatesContainer.setMaximumSize(new java.awt.Dimension(600, 32767));
        add(updatesContainer, java.awt.BorderLayout.WEST);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.add(wargearContainer, java.awt.BorderLayout.WEST);

        add(jPanel1, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel updatesContainer;
    private javax.swing.JPanel wargearContainer;
    // End of variables declaration//GEN-END:variables
    
}
