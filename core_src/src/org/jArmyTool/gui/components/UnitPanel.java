/*
 * UnitPanel.java
 *
 * Created on 22 December 2002, 01:22
 */

package org.jArmyTool.gui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.*;
import java.util.Collection;
import javax.swing.*;
import org.apache.log4j.Logger;

import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.data.dataBeans.army.Model;
import org.jArmyTool.gui.components.OneModelPanel;
import org.jArmyTool.gui.engine.*;
import org.jArmyTool.gui.listeners.*;
import org.jArmyTool.gui.factories.*;
import org.jArmyTool.gui.layout.VerticalFlowLayout;
import org.jArmyTool.gui.util.ImagePanel;


/**
 *
 * @author  pasi
 */
public class UnitPanel extends javax.swing.JPanel {
    private static final String UP_ARROW_LOCATION = "images/upArrow.jpg";
    private static final String DOWN_ARROW_LOCATION = "images/downArrow.jpg";
    private static final String CLOSE_X_LOCATION = "images/closeX.jpg";    
    
    public static final ImageIcon UP_ARROW = new ImageIcon(UP_ARROW_LOCATION);
    public static final ImageIcon DOWN_ARROW = new ImageIcon(DOWN_ARROW_LOCATION);
    public static final ImageIcon CLOSE_X = new ImageIcon(CLOSE_X_LOCATION);
    
    private static final int STAT_START_POINT = 155;
    
    private static final int STAT_INCREMENT = 25;

    private static Logger logger = Logger.getLogger(UnitPanel.class);
    
    private ComponentFactory componentFactory;
    private  GUICore guiCore;
    
    private Unit unit;
    
    private LinkedList modelPanels;
    private HashMap modelPanelsHash;
    
    private LinkedList unitUpdatePanels;
    
    private JTextField nameEditor;
    
    /** Creates new form UnitPanel */
    public UnitPanel(Unit unit,  GUICore guiCore) {
        this.modelPanelsHash = new HashMap();
        this.modelPanels = new LinkedList();
        this.unitUpdatePanels = new LinkedList();
        this.componentFactory = ComponentFactory.getInstance();
        this.guiCore = guiCore;
        
        this.unit = unit;
        initComponents();
        
        this.refreshArmylistData();
        this.refreshPointcost();
        this.addModels();
        this.refreshUpdates();
        
        this.deleteButton.addActionListener(new UnitDeleteListener(this.guiCore, this.unit));
        
        this.name.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        
        this.deleteButton.setIcon(CLOSE_X);
        this.moveUpButton.setIcon(UP_ARROW);
        this.moveDownButton.setIcon(DOWN_ARROW);
    }
    
    public Unit getUnit(){
        return this.unit;
    }
    
    public void refreshArmylistData(){
        this.refreshName();
        this.refreshType();
        Iterator iterator = this.modelPanels.iterator();
        while(iterator.hasNext()){
           ((OneModelPanel)iterator.next()).refreshArmylistData();
        }
        this.refreshArmylistName();
    }
    
    private void refreshArmylistName(){
        if(this.unit.getName().lastIndexOf((this.unit.getArmylistUnit().getName())) == -1){
            this.armylistName.setText("("+ this.unit.getArmylistUnit().getName() +")");
        }else{
           this.armylistName.setText(""); 
        }        
    }
    
    private void refreshType(){
        java.awt.Image typeImage = this.unit.getArmylistUnit().getArmylistArmy().getGameSystem().getUnitTypeByName(this.unit.getArmylistUnit().getUnitType()).getLargeImage();
        
        if(typeImage == null){
            this.typePanel.removeAll();
            this.type.setText(this.unit.getArmylistUnit().getUnitType());
            this.typePanel.add(this.type);
        }else{
            this.typePanel.removeAll();
            ImagePanel imagePanel = new ImagePanel(typeImage);
            imagePanel.setPreferredSize(new java.awt.Dimension(30,30));
            this.typePanel.add(imagePanel);
        }
    }
    
    private void refreshName(){
        this.name.setText(this.unit.getName());
    }
    
    private void addModels(){
        this.unitModels.setLayout(new VerticalFlowLayout(5));
        Iterator iterator = this.unit.getModels().iterator();
        while(iterator.hasNext()){
            Model model = (Model)iterator.next();
            OneModelPanel modelPanel = new OneModelPanel(model, this);
            this.modelPanels.add(modelPanel);
            this.unitModels.add(modelPanel);
            this.modelPanelsHash.put(model, modelPanel);
        }
    }
    
    public void checkExclusivesForModel(Model model){
        this.logger.debug("checking exclusives for model: "+model.getName());
        int modelId = model.getArmylistModel().getArmylistArmy().getIdForModel(model.getArmylistModel());
        Collection exclColl = this.unit.getArmylistUnit().getExclusivesForModel(modelId);
        Collection inclColl = this.unit.getArmylistUnit().getInclusivesForModel(modelId);
        
        if(exclColl != null){
            Iterator iterator = exclColl.iterator();
            while(iterator.hasNext()){
                int id = ((Integer)iterator.next()).intValue();
                this.logger.debug("Found exclusive models: "+id);
                Iterator models = this.unit.getModels().iterator();
                while(models.hasNext()){
                    Model model_ = (Model)models.next();
                    int otherId = this.unit.getArmylistUnit().getArmylistArmy().getIdForModel(model_.getArmylistModel());
                    if(id == otherId){
                        this.logger.debug("Found exclusive for: " + id + " - " + otherId);
                        model_.setModelCount(0);
                        ((OneModelPanel)this.modelPanelsHash.get(model_)).refreshCount();
                        this.refreshPointcost();
                    }
                }
            }
        }
        
        if(inclColl != null){
            Iterator iterator = inclColl.iterator();
            while(iterator.hasNext()){
                int id = ((Integer)iterator.next()).intValue();
                this.logger.debug("Found inclusive models: "+id);
                Iterator models = this.unit.getModels().iterator();
                while(models.hasNext()){
                    Model model_ = (Model)models.next();
                    int otherId = this.unit.getArmylistUnit().getArmylistArmy().getIdForModel(model_.getArmylistModel());
                    if(id == otherId){
                        this.logger.debug("Found inclusive for: " + id + " - " + otherId);
                        model_.setModelCount(model.getModelCount());
                        ((OneModelPanel)this.modelPanelsHash.get(model_)).refreshCount();
                        this.refreshPointcost();
                    }
                }
            }
        }
        
    }
    
/*    private void addModels(){
        Iterator iterator = this.unit.getModels().iterator();
        HashMap map = new HashMap();
        while(iterator.hasNext()){
            Model model = (Model)iterator.next();
            String name = model.getArmylistModel().getStatTypeName();
            if( map.get(name) == null){
                LinkedList list = new LinkedList();
                map.put(name, list);
                list.add(model);
            }else{
                ((LinkedList)map.get(name)).add(model);
            }
        }
        
        Iterator groups = map.keySet().iterator();
        int count = 0;
        while(groups.hasNext()){
            iterator = ((Collection)map.get(((String)groups.next()))).iterator();
            boolean first = true;
            
            java.awt.GridBagConstraints gridBagConstraints;
            while(iterator.hasNext()){

                Model model = (Model)iterator.next();
                ModelPanel tmp = this.componentFactory.createModelPanel(model, this.guiCore, STAT_START_POINT, STAT_INCREMENT, first);


                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = count;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

                this.unitModels.add(tmp, gridBagConstraints);
                this.modelPanels.add(tmp);
                first = false;
                ++count;
            }
        }
    }*/
    
    public void refreshUpdates(){
        Iterator iterator = this.unit.getUpdates().iterator();
        
        if(!iterator.hasNext()){//the unit has no updates!
            this.remove(this.unitUpdatePanelContainer);
            //this.unitUpdatePanelContainer.remove(this.unitUpdatesPanel);
            return;
        }
        
        this.unitUpdatesPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints = null;
        int x = 0;
        int y = 0;

        //System.out.println("max width "+maxWidth);
        while(iterator.hasNext()){
            UnitUpdate unitUpdate = (UnitUpdate)iterator.next();
            UnitUpdatePanel updatePanel = this.componentFactory.createUnitUpdatePanel(unitUpdate, this.guiCore);

            if(x > 1){
                x = 0;
                ++y;
            }
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = x;
            gridBagConstraints.gridy = y;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            
            this.unitUpdatesPanel.add(updatePanel, gridBagConstraints);
            this.unitUpdatePanels.add(updatePanel);
            ++x;
        }        
    }
    
    public void refreshPointcost(){
        double cost = this.unit.getPointCost();
        if(cost == (int)cost){
            this.pointcostLabel.setText(""+(int)cost);
        }else{
            this.pointcostLabel.setText(""+cost);
        }
        
        
        Iterator iterator = this.modelPanels.iterator();
        while(iterator.hasNext()){
            ((OneModelPanel)iterator.next()).refreshPointcost();
        }
        
        iterator = this.unitUpdatePanels.iterator();
        while(iterator.hasNext()){
            ((UnitUpdatePanel)iterator.next()).refreshPointcost();
            //((ModelPanel)iterator.next()).refresOnlyModelsPointcost();
        }      
        
        
        int counted = this.unit.getCountedModels();
        
        if(counted == 1){
            this.unitSizeLabel.setText("");
            this.modelsLabel.setText("");
            return;
        }
        this.modelsLabel.setText("mdls");
        
        String unitSize = ""+counted;
        
        int nonCounted = this.unit.getNonCountedModels();
        if(nonCounted > 0){
            unitSize = unitSize + "+" + nonCounted;
        }
        
        this.unitSizeLabel.setText(unitSize);
        
        if(( counted > this.unit.getArmylistUnit().getMaxUnitSize() && this.unit.getArmylistUnit().getMaxUnitSize() != -1 ) || counted < this.unit.getArmylistUnit().getMinUnitSize()){
            this.unitSizeLabel.setForeground(Color.red);
        }else{
            this.unitSizeLabel.setForeground(Color.black);
        }
        
    }
    
    public String toString(){
        return this.unit.getName();
    }
    
    private void beginNameEdit(){
        this.nameEditor = new JTextField();
        this.nameEditor.setPreferredSize(this.name.getPreferredSize());
        this.nameEditor.setText(this.unit.getName());
        this.namePanel.remove(this.name);
        this.namePanel.add(this.nameEditor, 0);
        this.namePanel.updateUI();
        this.nameEditor.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               endNameEdit();
            }
        });
        this.nameEditor.addFocusListener(new FocusAdapter(){
           public void focusLost(FocusEvent e) {
                endNameEdit();
           }
           
        });
        this.nameEditor.requestFocus();
    }
    
    private void endNameEdit(){
        if(this.nameEditor != null){
            this.namePanel.remove(this.nameEditor);
            this.unit.setName(this.nameEditor.getText());
        }
        this.namePanel.add(this.name, 0);
        this.refreshName();
        this.guiCore.refresUnitTree();
        this.name.setFont(new Font(this.name.getFont().getName(), Font.PLAIN,  this.name.getFont().getSize()));
        
        this.refreshArmylistName();
        this.guiCore.addNamedUnit(this.unit);
    }
    
    public void fireCorePoincostRefresh(){
        this.guiCore.refreshPointCosts();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        UnitInfo = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        typePanel = new javax.swing.JPanel();
        type = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        namePanel = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        armylistName = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        pointcostLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        unitSizeLabel = new javax.swing.JLabel();
        modelsLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        deleteButton = new javax.swing.JButton();
        moveDownButton = new javax.swing.JButton();
        moveUpButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        unitModels = new javax.swing.JPanel();
        unitUpdatePanelContainer = new javax.swing.JPanel();
        unitUpdatesPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2));
        setMaximumSize(new java.awt.Dimension(600, 2147483647));
        UnitInfo.setLayout(new java.awt.BorderLayout());

        UnitInfo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(102, 153, 255));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(102, 153, 255));
        typePanel.setLayout(new java.awt.BorderLayout());

        typePanel.setBackground(new java.awt.Color(255, 255, 255));
        typePanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        type.setBackground(new java.awt.Color(102, 153, 255));
        type.setText("type");
        typePanel.add(type, java.awt.BorderLayout.CENTER);

        jPanel3.add(typePanel, java.awt.BorderLayout.WEST);

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(102, 153, 255));
        namePanel.setLayout(new java.awt.GridBagLayout());

        namePanel.setBackground(new java.awt.Color(102, 153, 255));
        name.setBackground(new java.awt.Color(102, 153, 255));
        name.setFont(new java.awt.Font("Lucida Bright", 0, 18));
        name.setText("Name of the unit");
        name.setToolTipText("Edit name");
        name.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                nameMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nameMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                nameMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                nameMouseEntered(evt);
            }
        });

        namePanel.add(name, new java.awt.GridBagConstraints());

        armylistName.setFont(new java.awt.Font("Dialog", 0, 12));
        armylistName.setText("armylist name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        namePanel.add(armylistName, gridBagConstraints);

        jPanel6.add(namePanel, java.awt.BorderLayout.WEST);

        jPanel3.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4.setBackground(new java.awt.Color(102, 153, 255));
        pointcostLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        pointcostLabel.setText("100");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
        jPanel4.add(pointcostLabel, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel1.setText("pts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel4.add(jLabel1, gridBagConstraints);

        unitSizeLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        unitSizeLabel.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 1);
        jPanel4.add(unitSizeLabel, gridBagConstraints);

        modelsLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        modelsLabel.setText("models");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel4.add(modelsLabel, gridBagConstraints);

        jPanel2.add(jPanel4, java.awt.BorderLayout.EAST);

        UnitInfo.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel5.setLayout(new java.awt.BorderLayout());

        deleteButton.setBackground(new java.awt.Color(255, 255, 255));
        deleteButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        deleteButton.setMaximumSize(new java.awt.Dimension(30, 30));
        deleteButton.setMinimumSize(new java.awt.Dimension(30, 30));
        deleteButton.setPreferredSize(new java.awt.Dimension(30, 30));
        jPanel5.add(deleteButton, java.awt.BorderLayout.EAST);

        moveDownButton.setBackground(new java.awt.Color(255, 255, 255));
        moveDownButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        moveDownButton.setMaximumSize(new java.awt.Dimension(30, 30));
        moveDownButton.setMinimumSize(new java.awt.Dimension(30, 30));
        moveDownButton.setPreferredSize(new java.awt.Dimension(30, 30));
        moveDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveDownButtonActionPerformed(evt);
            }
        });

        jPanel5.add(moveDownButton, java.awt.BorderLayout.CENTER);

        moveUpButton.setBackground(new java.awt.Color(255, 255, 255));
        moveUpButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        moveUpButton.setMaximumSize(new java.awt.Dimension(30, 30));
        moveUpButton.setMinimumSize(new java.awt.Dimension(30, 30));
        moveUpButton.setPreferredSize(new java.awt.Dimension(30, 30));
        moveUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveUpButtonActionPerformed(evt);
            }
        });

        jPanel5.add(moveUpButton, java.awt.BorderLayout.WEST);

        UnitInfo.add(jPanel5, java.awt.BorderLayout.EAST);

        add(UnitInfo, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        unitModels.setLayout(null);

        jPanel1.add(unitModels, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        unitUpdatePanelContainer.setLayout(new java.awt.BorderLayout());

        unitUpdatePanelContainer.setBorder(new javax.swing.border.TitledBorder("Unit Updates"));
        unitUpdatesPanel.setMaximumSize(new java.awt.Dimension(600, 32767));
        unitUpdatePanelContainer.add(unitUpdatesPanel, java.awt.BorderLayout.WEST);

        add(unitUpdatePanelContainer, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents

    private void moveDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveDownButtonActionPerformed
        this.guiCore.moveUnitDown(this, this.unit);
    }//GEN-LAST:event_moveDownButtonActionPerformed

    private void moveUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveUpButtonActionPerformed
        this.guiCore.moveUnitUp(this, this.unit);
    }//GEN-LAST:event_moveUpButtonActionPerformed

    private void nameMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameMouseExited
        this.name.setFont(new Font(this.name.getFont().getName(), Font.PLAIN,  this.name.getFont().getSize()));
    }//GEN-LAST:event_nameMouseExited

    private void nameMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameMousePressed
        this.beginNameEdit();
    }//GEN-LAST:event_nameMousePressed

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
    }//GEN-LAST:event_jPanel2MouseClicked

    private void nameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameMouseClicked
    }//GEN-LAST:event_nameMouseClicked

    private void nameMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameMouseEntered
        this.name.setFont(new Font(this.name.getFont().getName(), Font.BOLD,  this.name.getFont().getSize()));
    }//GEN-LAST:event_nameMouseEntered
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel UnitInfo;
    private javax.swing.JLabel armylistName;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel modelsLabel;
    private javax.swing.JButton moveDownButton;
    private javax.swing.JButton moveUpButton;
    private javax.swing.JLabel name;
    private javax.swing.JPanel namePanel;
    private javax.swing.JLabel pointcostLabel;
    private javax.swing.JLabel type;
    private javax.swing.JPanel typePanel;
    private javax.swing.JPanel unitModels;
    private javax.swing.JLabel unitSizeLabel;
    private javax.swing.JPanel unitUpdatePanelContainer;
    private javax.swing.JPanel unitUpdatesPanel;
    // End of variables declaration//GEN-END:variables
    
}
