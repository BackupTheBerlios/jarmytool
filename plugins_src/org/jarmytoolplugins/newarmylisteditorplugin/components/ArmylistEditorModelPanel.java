/*
 * ArmylistEditorModelPanel.java
 *
 * Created on 27 October 2003, 21:17
 */

package plugins_src.org.jarmytoolplugins.newarmylisteditorplugin.components;

import java.awt.Component;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.jArmyTool.data.dataBeans.armylist.ArmylistArmy;
import org.jArmyTool.data.dataBeans.armylist.ArmylistModel;
import org.jArmyTool.data.dataBeans.armylist.ArmylistModelUpdate;
import org.jArmyTool.data.dataBeans.armylist.ArmylistUnit;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearGroup;
import org.jArmyTool.data.dataBeans.util.ModelStat;
import org.jArmyTool.data.dataBeans.util.ModelStatHolder;
import org.jArmyTool.internaldata.GUICommands;
import org.jarmytoolplugins.newarmylisteditorplugin.components.ArmylistEditorExclusiveModelsPanel;
import org.jarmytoolplugins.newarmylisteditorplugin.util.ExclusiveCheckBox;
import org.jarmytoolplugins.newarmylisteditorplugin.util.ModelUpdateListItem;
import org.jarmytoolplugins.newarmylisteditorplugin.util.VerticalFlowLayout;

/**
 *
 * @author  pasi
 */
public class ArmylistEditorModelPanel extends javax.swing.JPanel {    
    private ArmylistModel model;
    private DefaultComboBoxModel statTypecomboModel;
    
    private LinkedList allWargearGroups;
    private LinkedList updatePanels;
    
    private ArmylistEditorUnitPanel unitPanel;
    
    private HashMap previousStats;
    
    private ArmylistEditorLinkedModelPanel linkedPanel;
    
    private ModelStatsPanel modelStatsPanel;
    
    private ArmylistEditorExclusiveModelsPanel inclPanel;
    private ArmylistEditorExclusiveModelsPanel exclPanel;
    
    /** Creates new form ArmylistEditorModelPanel */
    public ArmylistEditorModelPanel(ArmylistModel model, ArmylistEditorUnitPanel unitPanel) {
        this.previousStats = new HashMap();
        this.unitPanel = unitPanel;
        this.model = model;
        initComponents();
        this.initData();
        this.initStats();
        this.initWargearGroups();
        this.initUpdates();
        
        this.moveDownButton.setIcon(GUICommands.getInstance().getMovedDownArrow());
        this.moveUpButton.setIcon(GUICommands.getInstance().getMoveUpArrow());
    }
    
    public ArmylistEditorModelPanel(ArmylistModel model, ArmylistEditorUnitPanel unitPanel, ArmylistEditorLinkedModelPanel linkedPanel) {
        this.previousStats = new HashMap();
        this.unitPanel = unitPanel;
        this.model = model;
        initComponents();
        this.initData();
        this.initStats();
        this.initWargearGroups();
        this.initUpdates();
        this.linkedPanel = linkedPanel;
    }
    
    
    private void initData(){
        this.nameField.setText(this.model.getName());
        this.minCountSpinner.setValue(new Integer(this.model.getMinCount()));
        this.maxCountSpinner.setValue(new Integer(this.model.getMaxCount()));
        this.defaultCountSpinner.setValue(new Integer(this.model.getDefaultSelectedAmount()));
        
        if(this.model.getPointCostPerModel() == (int)this.model.getPointCostPerModel()){
            this.pointcostField.setText(""+(int)this.model.getPointCostPerModel());
        }else{
            this.pointcostField.setText(""+this.model.getPointCostPerModel());
        }
        
        
        
        this.countedCheckbox.setSelected(this.model.isCounted());
        
        if(this.model.getAllowedWargear() == (int)this.model.getAllowedWargear()){
            this.allowedWargearField.setText(""+(int)this.model.getAllowedWargear());
        }else{
            this.allowedWargearField.setText(""+this.model.getAllowedWargear());
        }
        
        if(this.model.isLinked()){
            this.isLinkedLabel.setText("(linked)");
            this.upDownButoonsPanel.removeAll();
        }
        
        this.updateExclusives();
    }
    
    public void saveData(){
        this.model.setName(this.nameField.getText());
        this.model.setMinCount( ((Integer)this.minCountSpinner.getValue()).intValue() );
        this.model.setMaxCount( ((Integer)this.maxCountSpinner.getValue()).intValue() );
        this.model.setDefaultSelectedAmount( ((Integer)this.defaultCountSpinner.getValue()).intValue() );
        try{
            this.model.setPointCostPerModel(Double.parseDouble(this.pointcostField.getText()));
        }catch(NumberFormatException ex){}
        this.model.setIsCounted(this.countedCheckbox.isSelected());
        
        try{
            this.model.setAllowedWargear(Double.parseDouble(this.allowedWargearField.getText()));
        }catch(NumberFormatException ex){}
        
        Iterator wgGroups = this.allWargearGroups.iterator();
        this.model.emptyWargearGroups();
        while(wgGroups.hasNext()){
            JCheckBox box = (JCheckBox)wgGroups.next();
            if(box.isSelected()){
                this.model.addWargearGroup(box.getText());
            }
        }
        
        Iterator iterator = this.updatePanels.iterator();
        while(iterator.hasNext()){
            ((ArmylistEditorModelUpdatePanel)iterator.next()).saveData();
        }
        this.saveStats();
        this.saveExclusives();
    }
    
    private void saveStats(){
        this.model.setStatValues(this.modelStatsPanel.getAsText(","),",");
    }    
    
    private void initStats(){
        String selectedType = this.model.getStatTypeName();

        this.statTypecomboModel = statTypecomboModel = new DefaultComboBoxModel();
        Iterator names = this.model.getArmylistArmy().getGameSystem().getStatTypeNames().iterator();
        while(names.hasNext()){
            statTypecomboModel.addElement(names.next());
        }
        if(selectedType == null){
            selectedType = (String)this.statTypecomboModel.getElementAt(0);
            this.model.setStatType(selectedType);
        }
        
        this.statTypecomboModel.setSelectedItem(selectedType);
       
        this.statTypeComboBox.setModel(this.statTypecomboModel);
        
        if(this.previousStats.get(this.model.getStatTypeName()) != null  ){
                this.model.setStatValues((String)this.previousStats.get(this.model.getStatTypeName()), ",");
        }
        
        this.modelStatsPanel = new ModelStatsPanel(this.model);
        this.statsPanel.removeAll();
        this.statsPanel.add(this.modelStatsPanel);
    } 
    
    
    private void initUpdates(){
        this.updatesPanel.setLayout(new VerticalFlowLayout());
        this.updatePanels = new LinkedList();
        
        Iterator iterator = this.model.getUpdates().iterator();
        while(iterator.hasNext()){
            ArmylistEditorModelUpdatePanel panel = new ArmylistEditorModelUpdatePanel((ArmylistModelUpdate)iterator.next(), this);
            this.updatePanels.add(panel);
            this.updatesPanel.add(panel);
        }
    }   
    
    public void moveUpdateUp(ArmylistEditorModelUpdatePanel updatePanel){
        if(this.model.moveUpdateUp(updatePanel.getModelUpdate())){
            Component[] components = this.updatesPanel.getComponents();
            int index = -1;
            for(int i = 0; i < components.length; ++i){
                if(components[i] == updatePanel){
                    index = i;
                    break;
                }
            }
            if(index > 0){
                this.updatesPanel.remove(index);
                this.updatesPanel.add(updatePanel, index - 1);
                this.updateUI();
            }
        }
    }

    public void moveUpdateDown(ArmylistEditorModelUpdatePanel updatePanel){
        if(this.model.moveUpdateDown(updatePanel.getModelUpdate())){
            Component[] components = this.updatesPanel.getComponents();
            int index = -1;
            for(int i = 0; i < components.length; ++i){
                if(components[i] == updatePanel){
                    index = i;
                    break;
                }
            }
            if(index < components.length - 1 && index != -1){
                this.updatesPanel.remove(index);
                this.updatesPanel.add(updatePanel, index + 1);
                this.updateUI();
            }
        }
    }    
    
    public void addModelUpdate(ArmylistModelUpdate modelUpdate){
        this.model.addUpdate(this.model.getArmylistArmy().mapModelUpdate(modelUpdate));
        ArmylistEditorModelUpdatePanel panel = new ArmylistEditorModelUpdatePanel(modelUpdate, this);
        this.updatePanels.add(panel);
        this.updatesPanel.add(panel);
        this.updatesPanel.updateUI();
    }    
/*    
    public void addModelUpdate(int updateId){
        ArmylistModelUpdate modelUpdate = this.model.getArmylistArmy().getModelUpdate(updateId);
        ArmylistEditorModelUpdatePanel panel = new ArmylistEditorModelUpdatePanel(modelUpdate);
        this.updatePanels.add(panel);
        this.updatesPanel.add(panel);
    }       */
    
    private void initWargearGroups(){
        this.wargearGroupsPanel.setLayout(new VerticalFlowLayout());
        this.allWargearGroups = new LinkedList();
        Iterator iterator = this.model.getArmylistArmy().getWargearGroups().iterator();
        Collection selectedWargearGroups = this.model.getAllowedWargearGroups();
        while(iterator.hasNext()){
            ArmylistWargearGroup wgGroup = (ArmylistWargearGroup)iterator.next();
            JCheckBox box = new JCheckBox(wgGroup.getName());
            if(selectedWargearGroups.contains(wgGroup.getName()))
                box.setSelected(true);
            this.wargearGroupsPanel.add(box);
            this.allWargearGroups.add(box);
        }
    }    
    
    public ArmylistModel getModel(){
        return this.model;
    }
    
    
    private JPopupMenu createModelUpdatePopupMenu(){
        JPopupMenu menu = new JPopupMenu("copy model");
        String[] typeNames = this.model.getArmylistArmy().getGameSystem().getUnitTypeNames();
        HashMap typeMenus = new HashMap();
        for(int i = 0; i < typeNames.length; ++i){
            JMenu typeMenu = new JMenu(typeNames[i]);
            typeMenus.put(typeNames[i], typeMenu);
            menu.add(typeMenu);
        }
        
        Iterator units = this.model.getArmylistArmy().getUnits().iterator();
        while(units.hasNext()){
            ArmylistUnit unit = (ArmylistUnit)units.next();      
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
                if(this.model==model)
                    continue;
                
                
                JMenu modelMenu = new JMenu(model.getName());
                unitMenu.add(modelMenu);
                
                final Collection allModelUpdates = model.getUpdates();
                Iterator modelUpdates = allModelUpdates.iterator();
                
                if(modelUpdates.hasNext()){
                    JMenuItem allMenuItem = new JMenuItem("add all");
                    modelMenu.add(allMenuItem);
                    allMenuItem.addActionListener(new java.awt.event.ActionListener() {
                         public void actionPerformed(java.awt.event.ActionEvent evt) {
                             Iterator iterator = allModelUpdates.iterator();
                             while(iterator.hasNext()){
                                cloneModelUpdate((ArmylistModelUpdate)iterator.next());
                             }
                        }
                    });
                }
                
                while(modelUpdates.hasNext()){
                    final ArmylistModelUpdate update = (ArmylistModelUpdate)modelUpdates.next();
                    
                    JMenuItem updateItem = new JMenuItem(update.getName());
                    modelMenu.add(updateItem);
                    updateItem.addActionListener(new java.awt.event.ActionListener() {
                         public void actionPerformed(java.awt.event.ActionEvent evt) {
                            cloneModelUpdate(update);
                        }
                    }); 
                
                }
                
            }
            
        }   
        return menu;
    }    
    
    private void cloneModelUpdate(ArmylistModelUpdate modelUpdate){
        this.addModelUpdate(new ArmylistModelUpdate(modelUpdate));
    }    
    
    public void removeModelUpdate(ArmylistEditorModelUpdatePanel updatePanel){
        this.model.removeUpdate(this.model.getArmylistArmy().getIdForModelUpdate(updatePanel.getModelUpdate()));
        this.updatePanels.remove(updatePanel);
        this.updatesPanel.remove(updatePanel);
        this.updatesPanel.updateUI();
    }
    

    public ArmylistArmy getArmylistArmy(){
        return this.model.getArmylistArmy();
    }   
    
    
    public void updateExclusives(){
        this.saveExclusives();
        
        this.exclusivesPanel.removeAll();
        this.exclPanel = new ArmylistEditorExclusiveModelsPanel(this.unitPanel.getUnit(), this.model, false);
        this.exclusivesPanel.add(this.exclPanel);
        
        
        this.inclusivesPanel.removeAll();
        this.inclPanel = new ArmylistEditorExclusiveModelsPanel(this.unitPanel.getUnit(), this.model, true);
        this.inclusivesPanel.add(this.inclPanel);
        
        this.updateUI();
    }
    
    private void saveExclusives(){
        if(this.exclPanel != null)
            this.exclPanel.saveData();
        if(this.inclPanel != null)
            this.inclPanel.saveData();
    }
    

    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        nameField = new javax.swing.JTextField();
        minCountSpinner = new javax.swing.JSpinner();
        maxCountSpinner = new javax.swing.JSpinner();
        defaultCountSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        statTypeComboBox = new javax.swing.JComboBox();
        countedCheckbox = new javax.swing.JCheckBox();
        isLinkedLabel = new javax.swing.JLabel();
        statsPanel = new javax.swing.JPanel();
        pointsPanel = new javax.swing.JPanel();
        pointcostField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        updatesPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        newUpdateButton = new javax.swing.JButton();
        copyButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        wargearPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        allowedWargearField = new javax.swing.JTextField();
        wargearGroupsPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        inclusivesPanel = new javax.swing.JPanel();
        exclusivesPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        upDownButoonsPanel = new javax.swing.JPanel();
        moveUpButton = new javax.swing.JButton();
        moveDownButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        nameField.setText("jTextField1");
        nameField.setMaximumSize(new java.awt.Dimension(130, 23));
        nameField.setMinimumSize(new java.awt.Dimension(130, 23));
        nameField.setPreferredSize(new java.awt.Dimension(130, 23));
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameFieldFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel1.add(nameField, gridBagConstraints);

        minCountSpinner.setMinimumSize(new java.awt.Dimension(42, 24));
        minCountSpinner.setPreferredSize(new java.awt.Dimension(42, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(minCountSpinner, gridBagConstraints);

        maxCountSpinner.setMinimumSize(new java.awt.Dimension(42, 24));
        maxCountSpinner.setPreferredSize(new java.awt.Dimension(42, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(maxCountSpinner, gridBagConstraints);

        defaultCountSpinner.setMinimumSize(new java.awt.Dimension(42, 24));
        defaultCountSpinner.setPreferredSize(new java.awt.Dimension(42, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(defaultCountSpinner, gridBagConstraints);

        jLabel1.setText("min");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText("max");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setText("default");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel3, gridBagConstraints);

        statTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statTypeComboBoxActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(statTypeComboBox, gridBagConstraints);

        countedCheckbox.setText("counted");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(countedCheckbox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel1.add(isLinkedLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        jPanel1.add(statsPanel, gridBagConstraints);

        pointcostField.setText("jTextField2");
        pointcostField.setMinimumSize(new java.awt.Dimension(45, 23));
        pointcostField.setPreferredSize(new java.awt.Dimension(45, 23));
        pointsPanel.add(pointcostField);

        jLabel5.setText("pts");
        pointsPanel.add(jLabel5);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(pointsPanel, gridBagConstraints);

        jPanel2.add(jPanel1, java.awt.BorderLayout.WEST);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(new javax.swing.border.TitledBorder("updates"));
        jPanel4.add(updatesPanel, java.awt.BorderLayout.CENTER);

        newUpdateButton.setText("new");
        newUpdateButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        newUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUpdateButtonActionPerformed(evt);
            }
        });

        jPanel5.add(newUpdateButton);

        copyButton.setText("copy");
        copyButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        copyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                copyButtonMousePressed(evt);
            }
        });

        jPanel5.add(copyButton);

        jPanel4.add(jPanel5, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(new javax.swing.border.TitledBorder("Wargear"));
        wargearPanel.setLayout(new java.awt.GridBagLayout());

        jLabel4.setText("Allowed: ");
        wargearPanel.add(jLabel4, new java.awt.GridBagConstraints());

        allowedWargearField.setText("jTextField1");
        allowedWargearField.setMaximumSize(new java.awt.Dimension(50, 23));
        allowedWargearField.setMinimumSize(new java.awt.Dimension(50, 23));
        allowedWargearField.setPreferredSize(new java.awt.Dimension(50, 23));
        wargearPanel.add(allowedWargearField, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        wargearPanel.add(wargearGroupsPanel, gridBagConstraints);

        jPanel8.add(wargearPanel, java.awt.BorderLayout.NORTH);

        jPanel3.add(jPanel8, java.awt.BorderLayout.WEST);

        jPanel9.setLayout(new java.awt.BorderLayout());

        inclusivesPanel.setBorder(new javax.swing.border.TitledBorder("Selecting this model also selects:"));
        jPanel9.add(inclusivesPanel, java.awt.BorderLayout.CENTER);

        exclusivesPanel.setBorder(new javax.swing.border.TitledBorder("Selecting this model deselects:"));
        jPanel9.add(exclusivesPanel, java.awt.BorderLayout.NORTH);

        jPanel3.add(jPanel9, java.awt.BorderLayout.SOUTH);

        add(jPanel3, java.awt.BorderLayout.SOUTH);

        jPanel6.setLayout(new java.awt.BorderLayout());

        upDownButoonsPanel.setLayout(new java.awt.GridBagLayout());

        moveUpButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        moveUpButton.setMaximumSize(new java.awt.Dimension(35, 35));
        moveUpButton.setMinimumSize(new java.awt.Dimension(35, 35));
        moveUpButton.setPreferredSize(new java.awt.Dimension(35, 35));
        moveUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveUpButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        upDownButoonsPanel.add(moveUpButton, gridBagConstraints);

        moveDownButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        moveDownButton.setMaximumSize(new java.awt.Dimension(35, 35));
        moveDownButton.setMinimumSize(new java.awt.Dimension(35, 35));
        moveDownButton.setPreferredSize(new java.awt.Dimension(35, 35));
        moveDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveDownButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        upDownButoonsPanel.add(moveDownButton, gridBagConstraints);

        jPanel6.add(upDownButoonsPanel, java.awt.BorderLayout.NORTH);

        add(jPanel6, java.awt.BorderLayout.WEST);

        deleteButton.setText("del");
        deleteButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        add(deleteButton, java.awt.BorderLayout.EAST);

    }//GEN-END:initComponents

    private void nameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFieldFocusLost
        this.saveData();
        this.unitPanel.requestExclusivesUpdate();
    }//GEN-LAST:event_nameFieldFocusLost

    private void moveDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveDownButtonActionPerformed
        this.unitPanel.moveModelDown(this.model, this);
    }//GEN-LAST:event_moveDownButtonActionPerformed

    private void moveUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveUpButtonActionPerformed
        this.unitPanel.moveModelUp(this.model, this);
    }//GEN-LAST:event_moveUpButtonActionPerformed

    private void copyButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_copyButtonMousePressed
        this.createModelUpdatePopupMenu().show(evt.getComponent(), evt.getX(), evt.getY());
    }//GEN-LAST:event_copyButtonMousePressed

    private void newUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUpdateButtonActionPerformed
        this.addModelUpdate(new ArmylistModelUpdate("no name", this.model.getArmylistArmy()));
    }//GEN-LAST:event_newUpdateButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        if(this.linkedPanel != null){
            this.unitPanel.removeExpandedLinkedModel(this, this.linkedPanel);
        }else{
            this.unitPanel.removeModel(this);
        }
        this.unitPanel.requestExclusivesUpdate();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void statTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statTypeComboBoxActionPerformed
        this.previousStats.put(this.model.getStatTypeName(), this.modelStatsPanel.getAsText(","));
        this.model.setStatType((String)this.statTypecomboModel.getSelectedItem());
        this.initStats();
    }//GEN-LAST:event_statTypeComboBoxActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField allowedWargearField;
    private javax.swing.JButton copyButton;
    private javax.swing.JCheckBox countedCheckbox;
    private javax.swing.JSpinner defaultCountSpinner;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel exclusivesPanel;
    private javax.swing.JPanel inclusivesPanel;
    private javax.swing.JLabel isLinkedLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSpinner maxCountSpinner;
    private javax.swing.JSpinner minCountSpinner;
    private javax.swing.JButton moveDownButton;
    private javax.swing.JButton moveUpButton;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton newUpdateButton;
    private javax.swing.JTextField pointcostField;
    private javax.swing.JPanel pointsPanel;
    private javax.swing.JComboBox statTypeComboBox;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JPanel upDownButoonsPanel;
    private javax.swing.JPanel updatesPanel;
    private javax.swing.JPanel wargearGroupsPanel;
    private javax.swing.JPanel wargearPanel;
    // End of variables declaration//GEN-END:variables
    
}
