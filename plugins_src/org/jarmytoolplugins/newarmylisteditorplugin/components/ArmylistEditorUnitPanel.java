/*
 * ArmylistUnitPanel.java
 *
 * Created on 27 October 2003, 20:02
 */

package org.jarmytoolplugins.newarmylisteditorplugin.components;

import java.awt.Component;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.apache.log4j.Logger;
import org.jArmyTool.data.dataBeans.armylist.ArmylistArmy;
import org.jArmyTool.data.dataBeans.armylist.ArmylistModel;
import org.jArmyTool.data.dataBeans.armylist.ArmylistUnit;
import org.jArmyTool.data.dataBeans.armylist.ArmylistUnitUpdate;
import org.jarmytoolplugins.newarmylisteditorplugin.components.ArmylistEditorMainWindow;
import org.jarmytoolplugins.newarmylisteditorplugin.util.ModelListItem;
import org.jarmytoolplugins.newarmylisteditorplugin.util.UnitTypeComboCellRenderer;
import org.jarmytoolplugins.newarmylisteditorplugin.util.VerticalFlowLayout;

/**
 *
 * @author  pasi
 */
public class ArmylistEditorUnitPanel extends javax.swing.JPanel {
    
    private static Logger logger = Logger.getLogger(ArmylistEditorUnitPanel.class);
    
    private ArmylistUnit unit;
    private ArmylistEditorMainWindow mainWindow;
    private LinkedList modelPanels;
    private LinkedList unitUpdatesPanels;
    
    /** Creates new form ArmylistUnitPanel */
    public ArmylistEditorUnitPanel(ArmylistUnit unit_, ArmylistEditorMainWindow mainWindow_) {
        this.modelPanels = new LinkedList();
        this.unitUpdatesPanels = new LinkedList();
        this.unit = unit_;
        this.mainWindow = mainWindow_;
        initComponents();
        this.initData();
        
        
        unitTypeCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                //String from = unit.getUnitType();
                saveData();
                mainWindow.changeTypeInTypeLists();
            }
        });
        this.initModels();
        this.initUnitUpdates();
    }
    
    private void initData(){
        this.unitNameField.setText(this.unit.getName());
        this.minCountSpinner.setValue(new Integer(this.unit.getMinCount()));
        this.maxCountSpinner.setValue(new Integer(this.unit.getMaxCount()));
        this.maxUnitSizeSpinner.setValue(new Integer(this.unit.getMaxUnitSize()));
        this.minUnitSizeSpinner.setValue(new Integer(this.unit.getMinUnitSize()));
        
        this.initTypes();
    }
    
    private void initTypes(){
        DefaultComboBoxModel unitTypes = new DefaultComboBoxModel();
        String[] unitTypeNames = this.unit.getArmylistArmy().getGameSystem().getUnitTypeNames();
        this.unitTypeCombo.setModel(unitTypes); 
        for(int i = 0; i < unitTypeNames.length; ++i){
            unitTypes.addElement(unitTypeNames[i]);
            if(this.unit != null && this.unit.getUnitType() != null && this.unit.getUnitType().equalsIgnoreCase(unitTypeNames[i]))
                this.unitTypeCombo.setSelectedIndex(i);
        }
        
        this.unitTypeCombo.setRenderer(new UnitTypeComboCellRenderer(this.unit.getArmylistArmy().getGameSystem()));
    }    
    
    private void initModels(){
        this.modelsPanel.setLayout(new VerticalFlowLayout(5));
        Iterator iterator = this.unit.getModels().iterator();
        while(iterator.hasNext()){
            ArmylistModel model = (ArmylistModel)iterator.next();
            if(model.isLinked()){
               ArmylistEditorLinkedModelPanel panel = new ArmylistEditorLinkedModelPanel( model, this);
               this.modelsPanel.add(panel);
            }else{
               ArmylistEditorModelPanel panel = new ArmylistEditorModelPanel( model, this); 
               this.modelPanels.add(panel);
               this.modelsPanel.add(panel);                   
            }
        }
        this.modelsPanel.updateUI();
    }
    
    private void initUnitUpdates(){
        this.unitUpdatesPanel.setLayout(new VerticalFlowLayout());
        Iterator iterator = this.unit.getUpdates().iterator();
        while(iterator.hasNext()){
            ArmylistUnitUpdate update = (ArmylistUnitUpdate)iterator.next();
            ArmylistEditorUnitUpdatePanel panel = new ArmylistEditorUnitUpdatePanel(update, this);
            this.unitUpdatesPanel.add(panel);
            this.unitUpdatesPanels.add(panel);
            this.unitUpdatesPanel.updateUI();            
        }
    }
    
    
    public void saveData(){
        this.unit.setName(this.unitNameField.getText());
        try{
            this.unit.setMinCount( ((Integer)this.minCountSpinner.getValue()).intValue() );
        }catch(Exception e){}
        try{
            this.unit.setMaxCount( ((Integer)this.maxCountSpinner.getValue()).intValue() );
        }catch(Exception e){}
        try{
            this.unit.setMaxUnitSize( ((Integer)this.maxUnitSizeSpinner.getValue()).intValue() );
        }catch(Exception e){}
        try{
            this.unit.setMinUnitSize( ((Integer)this.minUnitSizeSpinner.getValue()).intValue() );
        }catch(Exception e){}
        
        this.unit.setUnitType((String)this.unitTypeCombo.getSelectedItem());
        
        Iterator iterator = this.modelPanels.iterator();
        while(iterator.hasNext()){
            ((ArmylistEditorModelPanel)iterator.next()).saveData();
        }
        
        iterator = this.unitUpdatesPanels.iterator();
        while(iterator.hasNext()){
            ((ArmylistEditorUnitUpdatePanel)iterator.next()).saveData();
        }
    }
    
    public void addModel(ArmylistModel model){
        this.unit.addModel(this.unit.getArmylistArmy().mapModel(model));
        
            if(model.isLinked()){
               ArmylistEditorLinkedModelPanel panel = new ArmylistEditorLinkedModelPanel( model, this);
               this.modelsPanel.add(panel);
            }else{
               ArmylistEditorModelPanel panel = new ArmylistEditorModelPanel( model, this); 
               this.modelPanels.add(panel);
               this.modelsPanel.add(panel);                   
            }        
        
        this.modelsPanel.updateUI();
        
        this.requestExclusivesUpdate();
    }
    
    public void addModel(int modelId){
        this.unit.addModel(modelId);
        ArmylistModel model = this.unit.getArmylistArmy().getModel(modelId);
        
            if(model.isLinked()){
               ArmylistEditorLinkedModelPanel panel = new ArmylistEditorLinkedModelPanel( model, this);
               this.modelsPanel.add(panel);
            }else{
               ArmylistEditorModelPanel panel = new ArmylistEditorModelPanel( model, this); 
               this.modelPanels.add(panel);
               this.modelsPanel.add(panel);                   
            }

        this.modelsPanel.updateUI();  
        this.requestExclusivesUpdate();
    }
    
    private JPopupMenu createModelCopyPopupMenu(){
        this.saveData();
        
        JPopupMenu menu = new JPopupMenu("copy model");
        String[] typeNames = this.unit.getArmylistArmy().getGameSystem().getUnitTypeNames();
        HashMap typeMenus = new HashMap();
        for(int i = 0; i < typeNames.length; ++i){
            JMenu typeMenu = new JMenu(typeNames[i]);
            typeMenus.put(typeNames[i], typeMenu);
            menu.add(typeMenu);
        }
        
        Iterator units = this.unit.getArmylistArmy().getUnits().iterator();
        while(units.hasNext()){
            ArmylistUnit unit = (ArmylistUnit)units.next();
            /*if(unit == this.unit)
                continue;*/
            
            
            JMenu unitMenu = null;
            Iterator models = unit.getModels().iterator();
            if(models.hasNext()){
                JMenu typeMenu = (JMenu)typeMenus.get(unit.getUnitType());
                unitMenu = new JMenu(unit.getName());
                typeMenu.add(unitMenu);
            }else{
                continue;               
            }
                
            while(models.hasNext()){
                final ArmylistModel model = (ArmylistModel)models.next();
                JMenuItem modelItem = new JMenuItem(model.getName());
                unitMenu.add(modelItem);
                modelItem.addActionListener(new java.awt.event.ActionListener() {
                     public void actionPerformed(java.awt.event.ActionEvent evt) {
                        cloneModel(model);
                    }
                });
                
            }
            
        }   
        return menu;
    }
    
    
    private JPopupMenu createModelLinkPopupMenu(){
        JPopupMenu menu = new JPopupMenu("link model");
        String[] typeNames = this.unit.getArmylistArmy().getGameSystem().getUnitTypeNames();
        HashMap typeMenus = new HashMap();
        for(int i = 0; i < typeNames.length; ++i){
            JMenu typeMenu = new JMenu(typeNames[i]);
            typeMenus.put(typeNames[i], typeMenu);
            menu.add(typeMenu);
        }
        
        Iterator units = this.unit.getArmylistArmy().getUnits().iterator();
        while(units.hasNext()){
            ArmylistUnit unit = (ArmylistUnit)units.next();
            /*if(unit == this.unit)
                continue;*/
            
            
            JMenu unitMenu = null;
            Iterator models = unit.getModels().iterator();
            if(models.hasNext()){
                JMenu typeMenu = (JMenu)typeMenus.get(unit.getUnitType());
                unitMenu = new JMenu(unit.getName());
                typeMenu.add(unitMenu);
            }else{
                continue;               
            }
                
            while(models.hasNext()){
                final ArmylistModel model = (ArmylistModel)models.next();
                JMenuItem modelItem = new JMenuItem(model.getName());
                unitMenu.add(modelItem);
                modelItem.addActionListener(new java.awt.event.ActionListener() {
                     public void actionPerformed(java.awt.event.ActionEvent evt) {
                        linkModel(model);
                    }
                });
                
            }
            
        }   
        return menu;
    }      

    
    private void linkModel(ArmylistModel toLink){
        int id = toLink.getArmylistArmy().getIdForModel(toLink);
        toLink.setLinked(true);
        ModelListItem item = new ModelListItem(id, toLink);
        

        this.addModel(id);
        
        //this.selectedModels.addElement(item);
        //this.modelSelectedList.setSelectedValue(item, true);
        //this.core.setModelDataPanel( ((ModelListItem)this.selectedModels.getElementAt(this.modelSelectedList.getSelectedIndex())).getKey() );
    }
    
    private void cloneModel(ArmylistModel toClone){
        ArmylistModel model = new ArmylistModel(toClone);
        int id = this.unit.getArmylistArmy().mapModel(model);
        
        ModelListItem item  = new ModelListItem(id, model);
        this.addModel(id);
        
        //this.selectedModels.addElement(item);
        //this.modelSelectedList.setSelectedValue(item, true);
        //this.core.setModelDataPanel( ((ModelListItem)this.selectedModels.getElementAt(this.modelSelectedList.getSelectedIndex())).getKey() );    
    }    
    
    public void removeModel(ArmylistEditorModelPanel panel){
        this.unit.removeModel(this.unit.getArmylistArmy().getIdForModel(panel.getModel()));
        this.modelPanels.remove(panel);
        this.modelsPanel.remove(panel);
       
        this.modelsPanel.updateUI();
    }
    
    public void removeExpandedLinkedModel(ArmylistEditorModelPanel panel, ArmylistEditorLinkedModelPanel panelL){
        this.unit.removeModel(this.unit.getArmylistArmy().getIdForModel(panel.getModel()));
        this.modelPanels.remove(panel);
        this.modelsPanel.remove(panelL);
       
        this.modelsPanel.updateUI();
    }
  
    
    public void removeLinked(ArmylistEditorLinkedModelPanel panel){
        this.unit.removeModel(this.unit.getArmylistArmy().getIdForModel(panel.getModel()));
        this.modelsPanel.remove(panel);
        this.modelsPanel.updateUI();
    }
    
    public void linkedModelExpanded(ArmylistEditorModelPanel panel){
        this.modelPanels.add(panel);
    }
    
    public void removeUnitUpdate(ArmylistEditorUnitUpdatePanel updatePanel){
        this.unit.removeUpdate(this.unit.getArmylistArmy().getIdForUnitUpdate(updatePanel.getUnitUpdate()));
        this.unitUpdatesPanels.remove(updatePanel);
        this.unitUpdatesPanel.remove(updatePanel);
        this.unitUpdatesPanel.updateUI();
    }
    
    
    private void addUnitUpdate(ArmylistUnitUpdate update){
        this.unit.addUpdate(this.unit.getArmylistArmy().mapUnitUpdate(update));
        ArmylistEditorUnitUpdatePanel panel = new ArmylistEditorUnitUpdatePanel(update, this);
        this.unitUpdatesPanel.add(panel);
        this.unitUpdatesPanels.add(panel);
        this.unitUpdatesPanel.updateUI();
    }
    
    private JPopupMenu createUnitUpdatePopupMenu(){
        JPopupMenu menu = new JPopupMenu("copy unit update");
        String[] typeNames = this.unit.getArmylistArmy().getGameSystem().getUnitTypeNames();
        HashMap typeMenus = new HashMap();
        for(int i = 0; i < typeNames.length; ++i){
            JMenu typeMenu = new JMenu(typeNames[i]);
            typeMenus.put(typeNames[i], typeMenu);
            menu.add(typeMenu);
        }
        
        Iterator units = this.unit.getArmylistArmy().getUnits().iterator();
        while(units.hasNext()){
            ArmylistUnit unit = (ArmylistUnit)units.next();
            /*if(unit == this.unit)
                continue;*/
            
            
            JMenu unitMenu = null;
            Iterator unitUpdates = unit.getUpdates().iterator();
            if(unitUpdates.hasNext()){
                JMenu typeMenu = (JMenu)typeMenus.get(unit.getUnitType());
                unitMenu = new JMenu(unit.getName());
                typeMenu.add(unitMenu);
            }else{
                continue;               
            }
                
            while(unitUpdates.hasNext()){
                final ArmylistUnitUpdate unitUpdate = (ArmylistUnitUpdate)unitUpdates.next();
                JMenuItem updateItem = new JMenuItem(unitUpdate.getName());
                unitMenu.add(updateItem);
                updateItem.addActionListener(new java.awt.event.ActionListener() {
                     public void actionPerformed(java.awt.event.ActionEvent evt) {
                        cloneUnitUpdate(unitUpdate);
                    }
                });
                
            }
            
        }   
        return menu;
    }    
    
    private void cloneUnitUpdate(ArmylistUnitUpdate unitUpdate){
        ArmylistUnitUpdate clone = new ArmylistUnitUpdate(unitUpdate);
        this.addUnitUpdate(clone);
    }
    
    private void addNewUnitUpdate(){
        ArmylistUnitUpdate newU = new ArmylistUnitUpdate("no name", this.unit.getArmylistArmy());
        this.addUnitUpdate(newU);
    }
    
    public ArmylistArmy getArmylistArmy(){
        return this.unit.getArmylistArmy();
    }
    
    public void moveModelUp(ArmylistModel model, Component modelPanel){
        if(this.unit.moveModelUp(model)){
            Component[] components = this.modelsPanel.getComponents();
            int index = -1;
            for(int i = 0; i < components.length; ++i){
                if(components[i] == modelPanel){
                    index = i;
                    break;
                }
            }
            if(index > 0){
                this.modelsPanel.remove(index);
                this.modelsPanel.add(modelPanel, index - 1);
                this.updateUI();
            }
        }
    }
 
    public void moveModelDown(ArmylistModel model, Component modelPanel){
        if(this.unit.moveModelDown(model)){
            Component[] components = this.modelsPanel.getComponents();
            int index = -1;
            for(int i = 0; i < components.length; ++i){
                if(components[i] == modelPanel){
                    index = i;
                    break;
                }
            }
            if(index < components.length - 1 && index != -1){
                this.modelsPanel.remove(index);
                this.modelsPanel.add(modelPanel, index + 1);
                this.updateUI();
            }
        }
    }    
    
    public Collection getAllModelPanels(){
        return Collections.unmodifiableCollection(this.modelPanels);
    }
    
    public void requestExclusivesUpdate(){
        Iterator iterator = this.modelPanels.iterator();
        while(iterator.hasNext()){
            ((ArmylistEditorModelPanel)iterator.next()).updateExclusives();
        }
    }
    
    public ArmylistUnit getUnit(){
        return this.unit;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        titlePanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        unitNameField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        minCountSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        maxCountSpinner = new javax.swing.JSpinner();
        minUnitSizeLabel = new javax.swing.JLabel();
        minUnitSizeSpinner = new javax.swing.JSpinner();
        maxUnitSizeLabel = new javax.swing.JLabel();
        maxUnitSizeSpinner = new javax.swing.JSpinner();
        deletePanel = new javax.swing.JPanel();
        deleteButton = new javax.swing.JButton();
        unitTypePanel = new javax.swing.JPanel();
        unitTypeCombo = new javax.swing.JComboBox();
        dataPanel = new javax.swing.JPanel();
        modelsContainer = new javax.swing.JPanel();
        modelsPanel = new javax.swing.JPanel();
        controlsPanel = new javax.swing.JPanel();
        neModelButton = new javax.swing.JButton();
        copyModelButton = new javax.swing.JButton();
        linkModelButton = new javax.swing.JButton();
        unitUpdatesContainer = new javax.swing.JPanel();
        unitUpdatesPanel = new javax.swing.JPanel();
        unitUpdatesControlPanel = new javax.swing.JPanel();
        newUnitUpdateButton = new javax.swing.JButton();
        copyUnitUpdateButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2));
        titlePanel.setLayout(new java.awt.BorderLayout());

        titlePanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));
        unitNameField.setText("jTextField1");
        unitNameField.setMinimumSize(new java.awt.Dimension(200, 23));
        unitNameField.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel1.add(unitNameField, new java.awt.GridBagConstraints());

        jLabel1.setText("minCount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel1, gridBagConstraints);

        minCountSpinner.setMinimumSize(new java.awt.Dimension(42, 24));
        minCountSpinner.setPreferredSize(new java.awt.Dimension(42, 24));
        jPanel1.add(minCountSpinner, new java.awt.GridBagConstraints());

        jLabel2.setText("maxCount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel2, gridBagConstraints);

        maxCountSpinner.setMinimumSize(new java.awt.Dimension(42, 24));
        maxCountSpinner.setPreferredSize(new java.awt.Dimension(42, 24));
        jPanel1.add(maxCountSpinner, new java.awt.GridBagConstraints());

        minUnitSizeLabel.setText("minUnitSize");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(minUnitSizeLabel, gridBagConstraints);

        minUnitSizeSpinner.setMinimumSize(new java.awt.Dimension(42, 24));
        minUnitSizeSpinner.setPreferredSize(new java.awt.Dimension(42, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel1.add(minUnitSizeSpinner, gridBagConstraints);

        maxUnitSizeLabel.setText("maxUnitSize");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(maxUnitSizeLabel, gridBagConstraints);

        maxUnitSizeSpinner.setMinimumSize(new java.awt.Dimension(42, 24));
        maxUnitSizeSpinner.setPreferredSize(new java.awt.Dimension(42, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        jPanel1.add(maxUnitSizeSpinner, gridBagConstraints);

        titlePanel.add(jPanel1, java.awt.BorderLayout.CENTER);

        deletePanel.setLayout(new java.awt.BorderLayout());

        deletePanel.setBackground(new java.awt.Color(102, 153, 255));
        deleteButton.setText("Delete Unit");
        deleteButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        deletePanel.add(deleteButton, java.awt.BorderLayout.CENTER);

        titlePanel.add(deletePanel, java.awt.BorderLayout.EAST);

        unitTypePanel.setBackground(new java.awt.Color(102, 153, 255));
        unitTypePanel.add(unitTypeCombo);

        titlePanel.add(unitTypePanel, java.awt.BorderLayout.WEST);

        add(titlePanel, java.awt.BorderLayout.NORTH);

        dataPanel.setLayout(new java.awt.BorderLayout());

        modelsContainer.setLayout(new java.awt.BorderLayout());

        modelsContainer.add(modelsPanel, java.awt.BorderLayout.CENTER);

        neModelButton.setText("new model");
        neModelButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        neModelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                neModelButtonActionPerformed(evt);
            }
        });

        controlsPanel.add(neModelButton);

        copyModelButton.setText("copy");
        copyModelButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        copyModelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyModelButtonActionPerformed(evt);
            }
        });
        copyModelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                copyModelButtonMousePressed(evt);
            }
        });

        controlsPanel.add(copyModelButton);

        linkModelButton.setText("link");
        linkModelButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        linkModelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                linkModelButtonActionPerformed(evt);
            }
        });
        linkModelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                linkModelButtonMousePressed(evt);
            }
        });

        controlsPanel.add(linkModelButton);

        modelsContainer.add(controlsPanel, java.awt.BorderLayout.SOUTH);

        dataPanel.add(modelsContainer, java.awt.BorderLayout.NORTH);

        unitUpdatesContainer.setLayout(new java.awt.BorderLayout());

        unitUpdatesContainer.setBorder(new javax.swing.border.TitledBorder("Unit Updates"));
        unitUpdatesContainer.add(unitUpdatesPanel, java.awt.BorderLayout.CENTER);

        newUnitUpdateButton.setText("new unit update");
        newUnitUpdateButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        newUnitUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUnitUpdateButtonActionPerformed(evt);
            }
        });

        unitUpdatesControlPanel.add(newUnitUpdateButton);

        copyUnitUpdateButton.setText("copy from another");
        copyUnitUpdateButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        copyUnitUpdateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                copyUnitUpdateButtonMousePressed(evt);
            }
        });

        unitUpdatesControlPanel.add(copyUnitUpdateButton);

        unitUpdatesContainer.add(unitUpdatesControlPanel, java.awt.BorderLayout.SOUTH);

        dataPanel.add(unitUpdatesContainer, java.awt.BorderLayout.CENTER);

        add(dataPanel, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        this.mainWindow.removeUnit(this.unit);
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void newUnitUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUnitUpdateButtonActionPerformed
        this.addNewUnitUpdate();
    }//GEN-LAST:event_newUnitUpdateButtonActionPerformed

    private void copyUnitUpdateButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_copyUnitUpdateButtonMousePressed
        this.saveData();
        this.createUnitUpdatePopupMenu().show(evt.getComponent(), evt.getX(), evt.getY());
    }//GEN-LAST:event_copyUnitUpdateButtonMousePressed

    private void linkModelButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_linkModelButtonMousePressed
        this.saveData();
        this.createModelLinkPopupMenu().show(evt.getComponent(), evt.getX(), evt.getY());
    }//GEN-LAST:event_linkModelButtonMousePressed

    private void copyModelButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_copyModelButtonMousePressed
        this.createModelCopyPopupMenu().show(evt.getComponent(), evt.getX(), evt.getY());
    }//GEN-LAST:event_copyModelButtonMousePressed

    private void linkModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_linkModelButtonActionPerformed
    }//GEN-LAST:event_linkModelButtonActionPerformed

    private void copyModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyModelButtonActionPerformed
    }//GEN-LAST:event_copyModelButtonActionPerformed

    private void neModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_neModelButtonActionPerformed
        this.addModel(new ArmylistModel("no name", this.unit.getArmylistArmy()));
    }//GEN-LAST:event_neModelButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JButton copyModelButton;
    private javax.swing.JButton copyUnitUpdateButton;
    private javax.swing.JPanel dataPanel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel deletePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton linkModelButton;
    private javax.swing.JSpinner maxCountSpinner;
    private javax.swing.JLabel maxUnitSizeLabel;
    private javax.swing.JSpinner maxUnitSizeSpinner;
    private javax.swing.JSpinner minCountSpinner;
    private javax.swing.JLabel minUnitSizeLabel;
    private javax.swing.JSpinner minUnitSizeSpinner;
    private javax.swing.JPanel modelsContainer;
    private javax.swing.JPanel modelsPanel;
    private javax.swing.JButton neModelButton;
    private javax.swing.JButton newUnitUpdateButton;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextField unitNameField;
    private javax.swing.JComboBox unitTypeCombo;
    private javax.swing.JPanel unitTypePanel;
    private javax.swing.JPanel unitUpdatesContainer;
    private javax.swing.JPanel unitUpdatesControlPanel;
    private javax.swing.JPanel unitUpdatesPanel;
    // End of variables declaration//GEN-END:variables
    
}
