/*
 * ArmylistEditorMainWindow.java
 *
 * Created on 27 October 2003, 19:12
 */

package plugins_src.org.jarmytoolplugins.newarmylisteditorplugin.components;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.Runnable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import org.jArmyTool.data.dataBeans.armylist.ArmylistArmy;
import org.jArmyTool.data.dataBeans.armylist.ArmylistUnit;
import org.jArmyTool.data.dataBeans.gameSystem.GameSystem;
import org.jarmytoolplugins.newarmylisteditorplugin.engine.ArmylistEditorGUICore;
import org.jarmytoolplugins.newarmylisteditorplugin.util.UnitListCellRenderer;
import org.jarmytoolplugins.newarmylisteditorplugin.util.UnitTypeJToggleButton;
import org.jarmytoolplugins.newarmylisteditorplugin.util.VerticalFlowLayout;
import org.jarmytoolplugins.newarmylisteditorplugin.wargearEditor.engine.WargearEditorGUICore;
import org.jarmytoolplugins.newarmylisteditorplugin.weaponeditor.engine.WeaponEditorCore;

/**
 *
 * @author  pasi
 */
public class ArmylistEditorMainWindow extends javax.swing.JFrame {
    
    private static final Color TITLE_BACKGROUND= new Color(51,153,255);
    
    private static final int WHEEL_STEPS = 25;
    
    private ArmylistEditorGUICore core;
    private ArmylistArmy army;
    private GameSystem gameSystem;
    
    private String currentFiler;
    private LinkedList unitTypeButtons;
    
    private ArmylistEditorUnitPanel unitPanel;
    
    private JPanel loading;
    
    private HashMap undoButtons;
    
    
    /** Creates new form ArmylistEditorMainWindow */
    public ArmylistEditorMainWindow(ArmylistEditorGUICore core, ArmylistArmy army) {
        this.undoButtons = new HashMap();
        this.core = core;
        this.army = army;
        this.gameSystem = this.army.getGameSystem();
        this.unitTypeButtons = new LinkedList();
        initComponents();
        this.filterHeaderPanel.setBackground(TITLE_BACKGROUND);
        this.initTypeButtons();
        
        this.unitList.setCellRenderer(new UnitListCellRenderer(this.army.getGameSystem()));
        
        this.armylistNameField.setText(this.army.getName());
        this.commentField.setText(this.army.getComments());
        this.writerField.setText(this.army.getWriter());
        this.emailField.setText(this.army.getEmail());
        this.webField.setText(this.army.getWeb());
        
        
        this.unitsPanel.addMouseWheelListener(new MouseWheelListener(){
            public void mouseWheelMoved(MouseWheelEvent e){
                
                JScrollBar sb = unitsScrollPanel.getVerticalScrollBar();
                sb.setValue(sb.getValue() + (e.getWheelRotation() * WHEEL_STEPS) );
            }
        });
        
        this.loading = new JPanel();
        this.loading.add(new JLabel("Loading data. Please wait..."));
        
        this.undoUnitDelPanel.setLayout(new VerticalFlowLayout());
    }
    
    
    private void initTypeButtons(){
        
        this.filterButtonsPanel.removeAll();
        this.filterButtonsPanel.setLayout(new VerticalFlowLayout());
        String[] unitTypeNames = gameSystem.getUnitTypeNames();
        
        for(int i = 0; i < unitTypeNames.length; ++i){
            final UnitTypeJToggleButton toggleButton = new UnitTypeJToggleButton(unitTypeNames[i], gameSystem.getUnitTypeByName(unitTypeNames[i]).getLargeImage(), gameSystem.getUnitTypeByName(unitTypeNames[i]).getLargeImageSelected());
            final String type = unitTypeNames[i];
            toggleButton.addActionListener(new ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if(isFilterUnSelected(type)){
                        unToggleAllUnitTypeButtons();
                        core.unitFilterCleared();
                    }else{
                        unToggleAllUnitTypeButtons();
                        core.unitFilterSelected(toggleButton.getUnitType());
                        toggleUnitTypeButton(toggleButton);  
                    }
	        }
            });
            toggleButton.setToolTipText(unitTypeNames[i]);
            this.filterButtonsPanel.add(toggleButton);
            this.unitTypeButtons.add(toggleButton);
        }
        this.filterButtonsPanel.updateUI();   
    }
    
    public boolean isFiltered(){
        return this.currentFiler != null;
    }
    
    private boolean isFilterUnSelected(String type){
        if(this.currentFiler != null && this.currentFiler.compareToIgnoreCase(type) == 0){
            this.currentFiler = null;
            this.filterHeaderLabel.setText("All Units");
            return true;
        }
        this.currentFiler = type;
        this.filterHeaderLabel.setText(type);
        return false;
    }    
    
    private void unToggleAllUnitTypeButtons(){
        Iterator iterator = this.unitTypeButtons.iterator();
        while(iterator.hasNext()){
            ((UnitTypeJToggleButton)iterator.next()).setSelected(false);
        }
    }       
    
    private void toggleUnitTypeButton(UnitTypeJToggleButton button){
        button.setSelected(true);
    }
    

    public void setUnitsListModel(DefaultListModel model){
        this.unitList.setModel(model);  
    }
    
    public void saveData(){
        this.army.setName(this.armylistNameField.getText());
        this.army.setComments(this.commentField.getText());
        this.army.setWriter(this.writerField.getText());
        this.army.setEmail(this.emailField.getText());
        this.army.setWeb(this.webField.getText());
        
        if(this.unitPanel != null)
            this.unitPanel.saveData();
    }
    
    private void setStatusLoading(){
        this.unitsPanel.removeAll();
        this.unitsPanel.add(this.loading);
        this.unitsPanel.updateUI();        
    }
    
    public void setUnit(ArmylistUnit unit){

        if(this.unitPanel != null){
            this.unitPanel.saveData();
        }   

        this.unitPanel = new ArmylistEditorUnitPanel(unit, this);
        this.unitsPanel.removeAll();
        this.unitsPanel.add(this.unitPanel);
        this.unitsPanel.updateUI(); 
    }
    
    
    public void changeTypeInTypeLists(){
        if(this.currentFiler == null){
            core.unitFilterCleared();
        }else{
            core.unitFilterSelected(this.currentFiler);
        }
        /*
        ((DefaultListModel)this.listModelsByType.get(from)).removeElement(unit);
        ((DefaultListModel)this.listModelsByType.get(to)).addElement(unit);*/
    }
    
    public void removeUnit(ArmylistUnit unit){
        this.setUndoUnit(unit);
        this.core.removeUnit(unit);
        this.unitsPanel.removeAll();
        this.unitsPanel.updateUI();
    }
    
    public void setUndoUnit(final ArmylistUnit unit){
        //this.undoUnitDelPanel.removeAll();
        JButton undo = new JButton("Undo "+unit.getName() + " deletion");
        this.undoButtons.put(unit, undo);
        this.undoUnitDelPanel.add(undo);
        this.undoUnitDelPanel.updateUI();
        undo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                core.addUnit(unit);
                removeUndo(unit);
            }
        });
    }
    
    private void removeUndo(ArmylistUnit unit){
        this.undoUnitDelPanel.remove((JButton)this.undoButtons.get(unit));
        this.undoUnitDelPanel.updateUI();
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPane1 = new javax.swing.JSplitPane();
        rightPanel = new javax.swing.JPanel();
        unitsScrollPanel = new javax.swing.JScrollPane();
        unitsPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        armylistPanel = new javax.swing.JPanel();
        filterHeaderPanel = new javax.swing.JPanel();
        filterHeaderLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        unitList = new javax.swing.JList();
        filterButtonsPanel = new javax.swing.JPanel();
        armylistNameField = new javax.swing.JTextField();
        newUnitButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        commentField = new javax.swing.JTextField();
        commmentsPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        writerField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        webField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        undoUnitDelPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        emptyDeletedButton = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        wargearMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        weaponsMenuItem = new javax.swing.JMenuItem();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jSplitPane1.setLastDividerLocation(50);
        rightPanel.setLayout(new java.awt.BorderLayout());

        unitsScrollPanel.setViewportView(unitsPanel);

        rightPanel.add(unitsScrollPanel, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(rightPanel);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        armylistPanel.setLayout(new java.awt.GridBagLayout());

        filterHeaderPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        filterHeaderLabel.setFont(new java.awt.Font("Lucida Bright", 1, 12));
        filterHeaderLabel.setText("All Units");
        filterHeaderPanel.add(filterHeaderLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        armylistPanel.add(filterHeaderPanel, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 180));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 180));
        unitList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                unitListValueChanged(evt);
            }
        });
        unitList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                unitListMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                unitListMouseReleased(evt);
            }
        });

        jScrollPane1.setViewportView(unitList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        armylistPanel.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        armylistPanel.add(filterButtonsPanel, gridBagConstraints);

        armylistNameField.setText("jTextField1");
        armylistNameField.setMaximumSize(new java.awt.Dimension(200, 23));
        armylistNameField.setMinimumSize(new java.awt.Dimension(200, 23));
        armylistNameField.setPreferredSize(new java.awt.Dimension(200, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        armylistPanel.add(armylistNameField, gridBagConstraints);

        newUnitButton.setText("new unit");
        newUnitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUnitButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        armylistPanel.add(newUnitButton, gridBagConstraints);

        jLabel1.setText("Comments & Writer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        armylistPanel.add(jLabel1, gridBagConstraints);

        commentField.setText("jTextField1");
        commentField.setMaximumSize(new java.awt.Dimension(200, 23));
        commentField.setMinimumSize(new java.awt.Dimension(200, 23));
        commentField.setPreferredSize(new java.awt.Dimension(200, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        armylistPanel.add(commentField, gridBagConstraints);

        commmentsPanel.setLayout(new java.awt.GridBagLayout());

        commmentsPanel.setBorder(new javax.swing.border.TitledBorder("Original Rules"));
        jLabel2.setText("Writer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        commmentsPanel.add(jLabel2, gridBagConstraints);

        writerField.setText("jTextField1");
        writerField.setMaximumSize(new java.awt.Dimension(150, 19));
        writerField.setMinimumSize(new java.awt.Dimension(150, 19));
        writerField.setPreferredSize(new java.awt.Dimension(150, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        commmentsPanel.add(writerField, gridBagConstraints);

        jLabel3.setText("e-mail");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        commmentsPanel.add(jLabel3, gridBagConstraints);

        emailField.setText("jTextField2");
        emailField.setMaximumSize(new java.awt.Dimension(150, 19));
        emailField.setMinimumSize(new java.awt.Dimension(150, 19));
        emailField.setPreferredSize(new java.awt.Dimension(150, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        commmentsPanel.add(emailField, gridBagConstraints);

        jLabel4.setText("www");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        commmentsPanel.add(jLabel4, gridBagConstraints);

        webField.setText("jTextField3");
        webField.setMaximumSize(new java.awt.Dimension(150, 19));
        webField.setMinimumSize(new java.awt.Dimension(150, 19));
        webField.setPreferredSize(new java.awt.Dimension(150, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        commmentsPanel.add(webField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        armylistPanel.add(commmentsPanel, gridBagConstraints);

        jPanel3.add(armylistPanel, java.awt.BorderLayout.WEST);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.add(undoUnitDelPanel, java.awt.BorderLayout.CENTER);

        emptyDeletedButton.setText("empty deleted");
        emptyDeletedButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        emptyDeletedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emptyDeletedButtonActionPerformed(evt);
            }
        });

        jPanel4.add(emptyDeletedButton);

        jPanel1.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jScrollPane2.setViewportView(jPanel1);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel2);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Wargear");
        wargearMenuItem.setText("edit wargear...");
        wargearMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wargearMenuItemActionPerformed(evt);
            }
        });

        jMenu1.add(wargearMenuItem);

        jMenuBar2.add(jMenu1);

        jMenu2.setText("Weapons");
        weaponsMenuItem.setText("edit weapons...");
        weaponsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weaponsMenuItemActionPerformed(evt);
            }
        });

        jMenu2.add(weaponsMenuItem);

        jMenuBar2.add(jMenu2);

        setJMenuBar(jMenuBar2);

        pack();
    }//GEN-END:initComponents

    private void emptyDeletedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emptyDeletedButtonActionPerformed
        this.undoButtons.clear();
        this.undoUnitDelPanel.removeAll();
        this.undoUnitDelPanel.updateUI();
    }//GEN-LAST:event_emptyDeletedButtonActionPerformed

    private void weaponsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weaponsMenuItemActionPerformed
        new WeaponEditorCore(this.army).displayEditor(this.getSize());
    }//GEN-LAST:event_weaponsMenuItemActionPerformed

    private void wargearMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wargearMenuItemActionPerformed
        new WargearEditorGUICore(this.army).displayEditor();
    }//GEN-LAST:event_wargearMenuItemActionPerformed

    private void newUnitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUnitButtonActionPerformed
        this.core.newUnit(this.currentFiler);
    }//GEN-LAST:event_newUnitButtonActionPerformed

    private void unitListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_unitListMousePressed
        
        this.setStatusLoading();
    }//GEN-LAST:event_unitListMousePressed

    private void unitListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_unitListMouseReleased
        if(this.unitList.getSelectedIndex() == -1)
            return;
        this.setUnit((ArmylistUnit)this.unitList.getSelectedValue());
    }//GEN-LAST:event_unitListMouseReleased

    private void unitListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_unitListValueChanged

    }//GEN-LAST:event_unitListValueChanged
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.core.editorExit();
    }//GEN-LAST:event_exitForm
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField armylistNameField;
    private javax.swing.JPanel armylistPanel;
    private javax.swing.JTextField commentField;
    private javax.swing.JPanel commmentsPanel;
    private javax.swing.JTextField emailField;
    private javax.swing.JButton emptyDeletedButton;
    private javax.swing.JPanel filterButtonsPanel;
    private javax.swing.JLabel filterHeaderLabel;
    private javax.swing.JPanel filterHeaderPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton newUnitButton;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JPanel undoUnitDelPanel;
    private javax.swing.JList unitList;
    private javax.swing.JPanel unitsPanel;
    private javax.swing.JScrollPane unitsScrollPanel;
    private javax.swing.JMenuItem wargearMenuItem;
    private javax.swing.JMenuItem weaponsMenuItem;
    private javax.swing.JTextField webField;
    private javax.swing.JTextField writerField;
    // End of variables declaration//GEN-END:variables
    
}
