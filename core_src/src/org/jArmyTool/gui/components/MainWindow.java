/*
 * MainWindow.java
 *
 * Created on 20 December 2002, 23:18
 */

package core_src.src.org.jArmyTool.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;
import java.awt.event.*;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.log4j.Logger;
import org.jArmyTool.data.dataBeans.army.Unit;
import org.jArmyTool.data.dataBeans.armylist.ArmylistArmy;

import org.jArmyTool.data.dataBeans.gameSystem.GameSystem;
import org.jArmyTool.gui.components.UnitPanel;
import org.jArmyTool.gui.layout.VerticalFlowLayout;
import org.jArmyTool.gui.engine.GUICore;
import org.jArmyTool.gui.util.PointcostProgressBar;
import org.jArmyTool.gui.util.UnitListCellRenderer;
import org.jArmyTool.gui.util.UnitTreeCellRenderer;
import org.jArmyTool.gui.util.UnitTypeJToggleButton;

/**
 *
 * @author  pasi
 */
public class MainWindow extends javax.swing.JFrame {
    
    public static final int WHEEL_STEPS = 30;
    private static final String ICON_LOCATION = "images/icon.gif";
    
    
    public static final Cursor DEL_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(Toolkit.getDefaultToolkit().getImage("images/delCursor.gif"),  new Point(15,15), "deleteCursor");
    public static final Image PROGRAM_ICON = new ImageIcon(ICON_LOCATION).getImage();
    public static final ImageIcon PLUS_ICON = new ImageIcon("images/plusIcon.jpg");
    
    private static final String INFO_ICON_LOCATION = "images/info.jpg";
    private static final String INFO_DISABLED_ICON_LOCATION = "images/info_disabled.jpg";
    
    private static final int UNIT_PANEL_SPACING = 5;
    private static final int UNIT_TYPE_PANEL_SPACING = 5;
    
    public static final Color TITLE_BACKGROUND= new Color(51,153,255);
    
    private static final Dimension PROGRESS_BAR_SIZE = new Dimension(150, 30);
    
    private LinkedList units;
    private Logger logger;
    
    private GUICore guiCore;
    
    private JPopupMenu armylistPopupMenu;
    private JPopupMenu namedUnitsPopupMenu;
    
    private HashMap unitPanelsByType;
    private HashMap unitTypeTreeNodes;
    
    private DefaultMutableTreeNode unitTreeRootNode;
    
    private JTree unitTree;
    private String[] unitTypeNames;
    
    private LinkedList unitTypeButtons;
    
    private JMenu pluginMenu;
    
    private String currentSelectedArmylist;
    
    private String currentFiler;
    
    private JTextField armylistNameEditor;
    private JTextField targetPointcostEditor;
    
    private boolean targetPoincotEditorValid;
    
    private GameSystem gameSystem;
    
    private Color pointsRemainingNormal= Color.black;
    private Color pointsRemainingExeeded = Color.red;
    
    private PointcostProgressBar pointcostProgressBar;
   
    
    /** Creates new form MainWindow */
    public MainWindow(GUICore core) {

        
        
        this.setIconImage(PROGRAM_ICON);
        
        
        this.unitTypeButtons = new LinkedList();
        this.unitPanelsByType = new HashMap();
        this.unitTypeTreeNodes = new HashMap();
        this.guiCore = core;
        this.logger = Logger.getLogger(MainWindow.class);
        this.units = new LinkedList();
        initComponents();
        
        this.infoLabel.setIcon(new ImageIcon(INFO_ICON_LOCATION));
        
        this.unitListAddButton.setIcon(PLUS_ICON);
        this.deletedUnitsAddButton.setIcon(PLUS_ICON);
        this.namedUnitsAddButton.setIcon(PLUS_ICON);
        
        
        this.pointcostProgressBar = new PointcostProgressBar(pointsRemainingNormal);
        this.progressBarPanel.add(this.pointcostProgressBar);    
        this.pointcostProgressBar.setPreferredSize(PROGRESS_BAR_SIZE);
        //this.progressBarPanel.setMinimumSize(PROGRESS_BAR_SIZE);
        
        this.filterHeaderPanel.setBackground(TITLE_BACKGROUND);
        this.armyTitlePanel.setBackground(TITLE_BACKGROUND);
        this.deletedHeaderPanel.setBackground(TITLE_BACKGROUND);
        this.namedHeaderPanel.setBackground(TITLE_BACKGROUND);
        //this.armylistSectionTitlePanel.setBackground(TITLE_BACKGROUND);
        
        this.armyNameLabel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        this.targetPointcostLabel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        
        this.unitContainer.setLayout(new VerticalFlowLayout(UNIT_TYPE_PANEL_SPACING));
 
        this.unitContainerJScrollPane.addMouseWheelListener(new MouseWheelListener(){
            public void mouseWheelMoved(MouseWheelEvent e){
                
                JScrollBar sb = unitContainerJScrollPane.getVerticalScrollBar();
                sb.setValue(sb.getValue() + (e.getWheelRotation() * WHEEL_STEPS) );
            }
        });
        
        this.initArmylistPopupMenu();
        
        this.armylistsCombo.setRenderer(new org.jArmyTool.gui.util.ArmylistsListCellRenderer());
    }
    
    public void refreshUnitTypeButtons(GameSystem gameSystem){
        this.unitTypeButtonsPanel.removeAll();
        this.unitTypeButtonsPanel.setLayout(new VerticalFlowLayout());
        String[] unitTypeNames = gameSystem.getUnitTypeNames();
        
        for(int i = 0; i < unitTypeNames.length; ++i){
            final UnitTypeJToggleButton toggleButton = new UnitTypeJToggleButton(unitTypeNames[i], gameSystem.getUnitTypeByName(unitTypeNames[i]).getLargeImage(), gameSystem.getUnitTypeByName(unitTypeNames[i]).getLargeImageSelected());
            final String type = unitTypeNames[i];
            toggleButton.addActionListener(new ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if(isFilterUnSelected(type)){
                        unToggleAllUnitTypeButtons();
                        guiCore.unitTypeFilterUnselected(); 
                    }else{
                        unToggleAllUnitTypeButtons();
                        guiCore.unitTypeSelectedInUnitList(toggleButton.getUnitType());
                        toggleUnitTypeButton(toggleButton);  
                    }
	        }
            });
            toggleButton.setToolTipText(unitTypeNames[i]);
            this.unitTypeButtonsPanel.add(toggleButton);
            this.unitTypeButtons.add(toggleButton);
        }
        this.unitTypeButtonsPanel.updateUI();
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
    
    private boolean isFilterUnSelected(String type){
        if(this.currentFiler != null && this.currentFiler.compareToIgnoreCase(type) == 0){
            this.currentFiler = null;
            this.filterHeader.setText("All Units");
            return true;
        }
        this.currentFiler = type;
        this.filterHeader.setText(type);
        return false;
    }
    
    private void initUnitTree(GameSystem gameSystem){
        String[] unitTypeNames = gameSystem.getUnitTypeNames();
        this.unitTypeNames = unitTypeNames;
        this.unitTreeRootNode = new DefaultMutableTreeNode("root");
        
        for(int i = 0; i < unitTypeNames.length; ++i){
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(unitTypeNames[i]);
            this.unitTypeTreeNodes.put(unitTypeNames[i], node);
            this.unitTreeRootNode.add(node);
        }
        this.unitTree = new JTree(this.unitTreeRootNode);
        this.unitTree.setRootVisible(false);
        this.unitTreePanel.removeAll();
        this.unitTreePanel.add(this.unitTree);
        
        MouseListener ml = new MouseAdapter() {
             public void mousePressed(MouseEvent e) {
                 int selRow = unitTree.getRowForLocation(e.getX(), e.getY());
                 TreePath selPath = unitTree.getPathForLocation(e.getX(), e.getY());
                 if(selRow != -1) {
                     if(e.getClickCount() == 1) {
                         nodeSelectedInUnitTree(selRow, selPath);
                     }
                     else if(e.getClickCount() == 2) {
                         nodeSelectedInUnitTree(selRow, selPath);
                     }
                 }
             }
         };
         this.unitTree.addMouseListener(ml);        
         this.unitTree.setCellRenderer(new UnitTreeCellRenderer(gameSystem));
         //this.unitTree.setExpandsSelectedPaths(true);
         //this.unitTree.putClientProperty("JTree.lineStyle", "Horizontal");
         
    }
    
    private void nodeSelectedInUnitTree(int selRow, TreePath selPath ){
        DefaultMutableTreeNode node = ((DefaultMutableTreeNode)selPath.getLastPathComponent());
        UnitPanel selected = null;
        if(node.getUserObject() instanceof UnitPanel){
            selected = (UnitPanel)node.getUserObject();
        }else{
            return;
        }
  
        double fromTop = 0;

        for(int j = 0; j < this.unitTypeNames.length; j++){
            
            if(this.unitTypeNames[j].equalsIgnoreCase(selected.getUnit().getArmylistUnit().getUnitType())){
                JPanel panel = ((JPanel)this.unitPanelsByType.get(this.unitTypeNames[j]));
                java.awt.Component[] components = panel.getComponents();
                for(int i = 0; i < components.length; ++i){
                    if(components[i] == selected)                    
                        break;
                    fromTop = fromTop + components[i].getSize().getHeight() + UNIT_PANEL_SPACING;
                }
                break;
            }else{  
                fromTop = fromTop + ((JPanel)this.unitPanelsByType.get(this.unitTypeNames[j])).getSize().getHeight() + UNIT_TYPE_PANEL_SPACING;
            }
        }
        this.unitContainerJScrollPane.getVerticalScrollBar().setValue((int)fromTop);
    }
    
    
    private void initArmylistPopupMenu(){
        this.armylistPopupMenu = new JPopupMenu();
/*        JMenuItem cloneItem = new JMenuItem("clone");
        cloneItem.setToolTipText("clone this armylist unit");
        cloneItem.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                guiCore.cloneArmylistUnit(unitList.getSelectedValue());
            }
        });*/
        
/*        JMenuItem removeItem = new JMenuItem("remove");
        removeItem.setToolTipText("remove this armylist unit");
        removeItem.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                guiCore.removeArmylistUnit(unitList.getSelectedValue());
            }
        });*/
        
//        this.armylistPopupMenu.add(cloneItem); 
//        this.armylistPopupMenu.add(removeItem);
        
        this.namedUnitsPopupMenu = new JPopupMenu();
        
        JMenuItem removeItem = new JMenuItem("remove");
        removeItem.setToolTipText("Remove this unit from the list");
        removeItem.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent e){
                guiCore.removeNamedUnitFromList();
            }
        });
        
  
        this.namedUnitsPopupMenu.add(removeItem);        
        
        
        this.namedUnitsList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    namedUnitsList.setSelectedIndex(namedUnitsList.locationToIndex(new java.awt.Point(evt.getX(), evt.getY())));
                    namedUnitsPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                }else if (evt.getClickCount() == 2){
                    namedUnitsList.setSelectedIndex(namedUnitsList.locationToIndex(new java.awt.Point(evt.getX(), evt.getY())));
                    guiCore.addNamedUnitToArmy();
                }
            }
        });        
        
        this.deletedUnitsList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.getClickCount() == 2){
                    Unit unit = (Unit)getDeletedUnitListSelected();
                    guiCore.addDeletedUnitToArmy(unit);
                }
            }
        });
        
        this.unitList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    unitList.setSelectedIndex(unitList.locationToIndex(new java.awt.Point(evt.getX(), evt.getY())));
                    armylistPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
                }else if (evt.getClickCount() == 2){
                    org.jArmyTool.data.dataBeans.armylist.ArmylistUnit unit = (org.jArmyTool.data.dataBeans.armylist.ArmylistUnit)getUnitListSelected();
                    guiCore.addUnitToArmy(org.jArmyTool.data.factories.Factory.getInstance().createUnit(unit));
                }
            }
        }); 
    }
    
    public void setInfo(String info){
        
        if(info != null && info.length() > 0){
            this.infoLabel.setIcon(new ImageIcon(INFO_ICON_LOCATION));
            this.infoLabel.setToolTipText(info);
        }else{
            this.infoLabel.setToolTipText(null);
            this.infoLabel.setIcon(new ImageIcon(INFO_DISABLED_ICON_LOCATION));
        }
            
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPane1 = new javax.swing.JSplitPane();
        helperPanel1 = new javax.swing.JPanel();
        helperPanel2 = new javax.swing.JPanel();
        listPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        unitList = new javax.swing.JList();
        unitListAddButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        deletedUnitsList = new javax.swing.JList();
        deletedUnitsAddButton = new javax.swing.JButton();
        unitTypeButtonsPanel = new javax.swing.JPanel();
        filterHeaderPanel = new javax.swing.JPanel();
        filterHeader = new javax.swing.JLabel();
        armylistSectionTitlePanel = new javax.swing.JPanel();
        armylistsCombo = new javax.swing.JComboBox();
        infoImagePanel = new javax.swing.JPanel();
        infoLabel = new javax.swing.JLabel();
        deletedHeaderPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        emptyDeletedUnitsButton = new javax.swing.JButton();
        namedHeaderPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        namedUnitsList = new javax.swing.JList();
        namedUnitsAddButton = new javax.swing.JButton();
        armyPanel = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        summaryPanel = new javax.swing.JPanel();
        treePanel = new javax.swing.JPanel();
        unitTreeScrollPanel = new javax.swing.JScrollPane();
        unitTreePanel = new javax.swing.JPanel();
        armyControlButtonsPanel = new javax.swing.JPanel();
        newButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        pointcostPanel = new javax.swing.JPanel();
        totalPanel = new javax.swing.JPanel();
        pointcostTotalLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pointcostTargetLabel = new javax.swing.JLabel();
        progressBarPanel = new javax.swing.JPanel();
        unitContainerJScrollPane = new javax.swing.JScrollPane();
        unitContainer = new javax.swing.JPanel();
        armyTitlePanel = new javax.swing.JPanel();
        armyNameLabel = new javax.swing.JLabel();
        armyListNameLabel = new javax.swing.JLabel();
        targetPointcostLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jSplitPane1.setOneTouchExpandable(true);
        helperPanel1.setLayout(new java.awt.BorderLayout());

        helperPanel2.setLayout(new java.awt.BorderLayout());

        listPanel.setLayout(new java.awt.GridBagLayout());

        listPanel.setMinimumSize(new java.awt.Dimension(210, 200));
        jScrollPane1.setMaximumSize(new java.awt.Dimension(200, 32767));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 180));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 180));
        unitList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                unitListMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(unitList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        listPanel.add(jScrollPane1, gridBagConstraints);

        unitListAddButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        unitListAddButton.setMaximumSize(new java.awt.Dimension(30, 30));
        unitListAddButton.setMinimumSize(new java.awt.Dimension(30, 30));
        unitListAddButton.setPreferredSize(new java.awt.Dimension(30, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        listPanel.add(unitListAddButton, gridBagConstraints);

        jScrollPane3.setMaximumSize(new java.awt.Dimension(100, 32767));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(200, 180));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(200, 180));
        deletedUnitsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deletedUnitsListMouseClicked(evt);
            }
        });

        jScrollPane3.setViewportView(deletedUnitsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        listPanel.add(jScrollPane3, gridBagConstraints);

        deletedUnitsAddButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        deletedUnitsAddButton.setMaximumSize(new java.awt.Dimension(30, 30));
        deletedUnitsAddButton.setMinimumSize(new java.awt.Dimension(30, 30));
        deletedUnitsAddButton.setPreferredSize(new java.awt.Dimension(30, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        listPanel.add(deletedUnitsAddButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        listPanel.add(unitTypeButtonsPanel, gridBagConstraints);

        filterHeaderPanel.setLayout(new java.awt.GridBagLayout());

        filterHeaderPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        filterHeaderPanel.setMinimumSize(new java.awt.Dimension(200, 20));
        filterHeaderPanel.setPreferredSize(new java.awt.Dimension(200, 20));
        filterHeader.setFont(new java.awt.Font("Lucida Bright", 1, 12));
        filterHeader.setText("All Units");
        filterHeaderPanel.add(filterHeader, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        listPanel.add(filterHeaderPanel, gridBagConstraints);

        armylistSectionTitlePanel.setLayout(new java.awt.GridBagLayout());

        armylistsCombo.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 10));
        armylistsCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                armylistsComboActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        armylistSectionTitlePanel.add(armylistsCombo, gridBagConstraints);

        infoImagePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        infoImagePanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        infoImagePanel.add(infoLabel);

        armylistSectionTitlePanel.add(infoImagePanel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 4, 0);
        listPanel.add(armylistSectionTitlePanel, gridBagConstraints);

        deletedHeaderPanel.setLayout(new java.awt.GridBagLayout());

        deletedHeaderPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jLabel3.setFont(new java.awt.Font("Lucida Bright", 1, 12));
        jLabel3.setText("Deleted Units");
        deletedHeaderPanel.add(jLabel3, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        listPanel.add(deletedHeaderPanel, gridBagConstraints);

        emptyDeletedUnitsButton.setText("Empty Deleted Units");
        emptyDeletedUnitsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emptyDeletedUnitsButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        listPanel.add(emptyDeletedUnitsButton, gridBagConstraints);

        namedHeaderPanel.setLayout(new java.awt.GridBagLayout());

        namedHeaderPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        jLabel4.setFont(new java.awt.Font("Lucida Bright", 1, 12));
        jLabel4.setText("Named Units");
        namedHeaderPanel.add(jLabel4, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        listPanel.add(namedHeaderPanel, gridBagConstraints);

        jScrollPane4.setMaximumSize(new java.awt.Dimension(100, 32767));
        jScrollPane4.setMinimumSize(new java.awt.Dimension(200, 180));
        jScrollPane4.setPreferredSize(new java.awt.Dimension(200, 180));
        jScrollPane4.setViewportView(namedUnitsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        listPanel.add(jScrollPane4, gridBagConstraints);

        namedUnitsAddButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        namedUnitsAddButton.setMaximumSize(new java.awt.Dimension(30, 30));
        namedUnitsAddButton.setMinimumSize(new java.awt.Dimension(30, 30));
        namedUnitsAddButton.setPreferredSize(new java.awt.Dimension(30, 30));
        namedUnitsAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namedUnitsAddButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        listPanel.add(namedUnitsAddButton, gridBagConstraints);

        helperPanel2.add(listPanel, java.awt.BorderLayout.NORTH);
        listPanel.getAccessibleContext().setAccessibleParent(jSplitPane1);

        helperPanel1.add(helperPanel2, java.awt.BorderLayout.WEST);

        jSplitPane1.setLeftComponent(helperPanel1);

        armyPanel.setLayout(new java.awt.BorderLayout());

        jSplitPane2.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        jSplitPane2.setOneTouchExpandable(true);
        summaryPanel.setLayout(new java.awt.BorderLayout());

        treePanel.setLayout(new java.awt.BorderLayout());

        unitTreePanel.setLayout(new java.awt.BorderLayout());

        unitTreePanel.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 10));
        unitTreeScrollPanel.setViewportView(unitTreePanel);

        treePanel.add(unitTreeScrollPanel, java.awt.BorderLayout.CENTER);

        summaryPanel.add(treePanel, java.awt.BorderLayout.CENTER);

        armyControlButtonsPanel.setLayout(new java.awt.GridBagLayout());

        newButton.setText("new");
        newButton.setMargin(new java.awt.Insets(2, 2, 2, 2));
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        armyControlButtonsPanel.add(newButton, gridBagConstraints);

        jButton1.setText("save");
        jButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        armyControlButtonsPanel.add(jButton1, gridBagConstraints);

        jButton2.setText("load");
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        armyControlButtonsPanel.add(jButton2, gridBagConstraints);

        summaryPanel.add(armyControlButtonsPanel, java.awt.BorderLayout.SOUTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(new javax.swing.border.TitledBorder("Points"));
        pointcostPanel.setLayout(new java.awt.GridBagLayout());

        totalPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        pointcostTotalLabel.setFont(new java.awt.Font("Lucida Bright", 1, 14));
        pointcostTotalLabel.setText("jLabel2");
        totalPanel.add(pointcostTotalLabel);

        jLabel1.setText("/");
        totalPanel.add(jLabel1);

        pointcostTargetLabel.setFont(new java.awt.Font("Lucida Bright", 1, 14));
        pointcostTargetLabel.setText("jLabel2");
        totalPanel.add(pointcostTargetLabel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pointcostPanel.add(totalPanel, gridBagConstraints);

        progressBarPanel.setLayout(new java.awt.BorderLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        pointcostPanel.add(progressBarPanel, gridBagConstraints);

        jPanel1.add(pointcostPanel, java.awt.BorderLayout.WEST);

        summaryPanel.add(jPanel1, java.awt.BorderLayout.NORTH);

        jSplitPane2.setRightComponent(summaryPanel);
        summaryPanel.getAccessibleContext().setAccessibleParent(jSplitPane2);

        unitContainerJScrollPane.setMaximumSize(new java.awt.Dimension(300, 32767));
        unitContainerJScrollPane.setMinimumSize(new java.awt.Dimension(200, 22));
        unitContainer.setMaximumSize(new java.awt.Dimension(3000, 32767));
        unitContainer.setMinimumSize(new java.awt.Dimension(200, 10));
        unitContainerJScrollPane.setViewportView(unitContainer);

        jSplitPane2.setLeftComponent(unitContainerJScrollPane);

        armyPanel.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        armyTitlePanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        armyNameLabel.setText("jLabel4");
        armyNameLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                armyNameLabelMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                armyNameLabelMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                armyNameLabelMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                armyNameLabelMouseEntered(evt);
            }
        });

        armyTitlePanel.add(armyNameLabel);

        armyListNameLabel.setText("jLabel5");
        armyTitlePanel.add(armyListNameLabel);

        targetPointcostLabel.setText("jLabel4");
        targetPointcostLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                targetPointcostLabelMousePressed(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                targetPointcostLabelMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                targetPointcostLabelMouseEntered(evt);
            }
        });

        armyTitlePanel.add(targetPointcostLabel);

        jLabel7.setText("points");
        armyTitlePanel.add(jLabel7);

        armyPanel.add(armyTitlePanel, java.awt.BorderLayout.NORTH);

        jSplitPane1.setRightComponent(armyPanel);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void namedUnitsAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namedUnitsAddButtonActionPerformed
        if(this.namedUnitsList.getSelectedIndex() != -1)
            this.guiCore.addNamedUnitToArmy();
    }//GEN-LAST:event_namedUnitsAddButtonActionPerformed

    private void emptyDeletedUnitsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emptyDeletedUnitsButtonActionPerformed
        this.guiCore.clearDeletedUnits();
    }//GEN-LAST:event_emptyDeletedUnitsButtonActionPerformed

    private void deletedUnitsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deletedUnitsListMouseClicked
    }//GEN-LAST:event_deletedUnitsListMouseClicked

    private void targetPointcostLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_targetPointcostLabelMousePressed
        this.beginTargetPointcostEdit();
    }//GEN-LAST:event_targetPointcostLabelMousePressed

    private void armyNameLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_armyNameLabelMousePressed
        this.beginArmyNameEdit();
    }//GEN-LAST:event_armyNameLabelMousePressed

    private void armyNameLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_armyNameLabelMouseClicked
        // Add your handling code here:
    }//GEN-LAST:event_armyNameLabelMouseClicked

    private void targetPointcostLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_targetPointcostLabelMouseExited
        this.targetPointcostLabel.setFont(new Font(this.armyNameLabel.getFont().getName(), Font.PLAIN,  this.armyNameLabel.getFont().getSize()));
    }//GEN-LAST:event_targetPointcostLabelMouseExited

    private void targetPointcostLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_targetPointcostLabelMouseEntered
        this.targetPointcostLabel.setFont(new Font(this.armyNameLabel.getFont().getName(), Font.BOLD,  this.armyNameLabel.getFont().getSize()));
    }//GEN-LAST:event_targetPointcostLabelMouseEntered

    private void armyNameLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_armyNameLabelMouseExited
        this.armyNameLabel.setFont(new Font(this.armyNameLabel.getFont().getName(), Font.PLAIN,  this.armyNameLabel.getFont().getSize()));
    }//GEN-LAST:event_armyNameLabelMouseExited

    private void armyNameLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_armyNameLabelMouseEntered
        this.armyNameLabel.setFont(new Font(this.armyNameLabel.getFont().getName(), Font.BOLD,  this.armyNameLabel.getFont().getSize()));
    }//GEN-LAST:event_armyNameLabelMouseEntered

    private void armylistsComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_armylistsComboActionPerformed
        if(this.getSelectedArmylist() == null)
            return;
        
        if( this.currentSelectedArmylist == null || this.currentSelectedArmylist.compareToIgnoreCase(this.getSelectedArmylist().getName()) != 0){
            this.guiCore.userChangedArmylist(this.getSelectedArmylist());
            this.armylistsCombo.setToolTipText(this.getSelectedArmylist().getName());
        }
    }//GEN-LAST:event_armylistsComboActionPerformed

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        this.endTargetPointcosteditWithoutSaving();
        this.endArmyNameEdit();
        this.guiCore.newArmy();
    }//GEN-LAST:event_newButtonActionPerformed

    private void unitListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_unitListMouseClicked

    }//GEN-LAST:event_unitListMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JFileChooser chooser = null;
        if(this.guiCore.getUserProperty(GUICore.USER_SAVE_DIRECTORY_KEY) != null ){
            chooser = new JFileChooser(new File(this.guiCore.getUserProperty(GUICore.USER_SAVE_DIRECTORY_KEY)));
        }else{
            chooser = new JFileChooser();
        }
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            this.guiCore.loadArmy(chooser.getSelectedFile());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.endTargetPointcostEdit();
        this.endTargetPointcosteditWithoutSaving();
        this.endArmyNameEdit();
        
        JFileChooser chooser = null;
        if(this.guiCore.getUserProperty(GUICore.USER_SAVE_DIRECTORY_KEY) != null ){
            chooser = new JFileChooser(new File(this.guiCore.getUserProperty(GUICore.USER_SAVE_DIRECTORY_KEY)));
        }else{
            chooser = new JFileChooser();
        }
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        int returnVal = chooser.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            this.guiCore.saveArmy(chooser.getSelectedFile(), true);
        }
                
    }//GEN-LAST:event_jButton1ActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.guiCore.closeMainWindow();
    }//GEN-LAST:event_exitForm
    
    protected void newArmylistNameDialogReturn(String name, String gameSystemName){
        this.guiCore.createNewArmylist(name, gameSystemName);
    }

/*    protected void cloneArmylistNameDialogReturn(String name, String toClone){
        this.guiCore.cloneNewArmylist(name, toClone);
    }*/
    /**
     * This method sets the values dislplayed in unitlist list
     */
    public void setUnitListModel(DefaultListModel model){
        //logger.debug("setUtilListModel");
        this.unitList.setModel(model);         
    }
    
    public void setNamedUnitsListModel(DefaultListModel model){
        //logger.debug("setUtilListModel");
        this.namedUnitsList.setModel(model);         
    }    
    
    public void refreshNamedList(){
        this.namedUnitsList.updateUI();
    }

    public void setDeletedUnitListModel(DefaultListModel model){
        this.deletedUnitsList.setModel(model);
    }
    
    public void setUnitListsCellRenderer(GameSystem gameSystem){
        UnitListCellRenderer renderer = new UnitListCellRenderer(gameSystem);
        this.deletedUnitsList.setCellRenderer(renderer);
        this.unitList.setCellRenderer(renderer);
        this.namedUnitsList.setCellRenderer(renderer);
    }
    

    
    public void setArmylistsModel(DefaultComboBoxModel model){
        this.armylistsCombo.setModel(model); 
    }
    
    public void setArmylistsComboSelected(ArmylistArmy selected){
        this.armylistsCombo.getModel().setSelectedItem(selected);
        this.currentSelectedArmylist = selected.getName();
    }
    
    public void setPointcostTotal(double pointcost, double remaining){
        if(pointcost == (int)pointcost && remaining == (int)remaining){
            this.pointcostTotalLabel.setText(""+(int)pointcost);
            this.pointcostProgressBar.setString(""+(int)remaining + " remaining");
            //this.pointcostRemainingLabel.setText(""+(int)remaining);
            this.pointcostTargetLabel.setText(""+(int)(pointcost + remaining));
            this.pointcostProgressBar.repaint();
        }else{
            this.pointcostTotalLabel.setText(""+pointcost);
            //this.pointcostRemainingLabel.setText(""+remaining);
            this.pointcostProgressBar.setString(""+remaining + " remaining");
            this.pointcostTargetLabel.setText(""+(pointcost + remaining));
        }
        
        if(remaining < 0){
             this.pointcostProgressBar.setColor(this.pointsRemainingExeeded);
            //this.pointcostRemainingLabel.setForeground(this.pointsRemainingExeeded);
        }else{
            this.pointcostProgressBar.setColor(this.pointsRemainingNormal);
            //this.pointcostRemainingLabel.setForeground(this.pointsRemainingNormal);
        }
        
        this.pointcostProgressBar.setMaximum( (int)(pointcost+remaining) );
        this.pointcostProgressBar.setValue( (int)pointcost );
    }
    
    /**
     * This method removes all UnitPanels from unit panel and unit tree
     */
    public void clearUnits(){
        /*Iterator iterator = this.units.iterator();
        while(iterator.hasNext()){
            this.unitContainer.remove((UnitPanel)iterator.next());
        }
        */
        if(this.unitTypeNames != null && this.unitPanelsByType != null){
            for(int i = 0; i < this.unitTypeNames.length; ++i){
                ((Container)this.unitPanelsByType.get(unitTypeNames[i])).removeAll();
            }
        }
            
        this.units = new LinkedList(); 
        if(this.gameSystem != null)
            this.initUnitTree(this.gameSystem);
        
        this.unitContainer.updateUI();
    }
    
    /**
     * Adds new unit to unit panel and unit tree
     */
    public void addUnit(UnitPanel unitPanel){
        this.units.add(unitPanel); 
        //this.unitContainer.add(unitPanel);
        String unitType = unitPanel.getUnit().getArmylistUnit().getUnitType();
        JPanel panel = (JPanel)this.unitPanelsByType.get(unitType);
        panel.add(unitPanel);
        
        ((DefaultMutableTreeNode)this.unitTypeTreeNodes.get(unitType)).add(new DefaultMutableTreeNode(unitPanel));
        
        this.refreshUnitTreeUnitCounts();
        this.refreshUnitTree();
        this.unitContainer.updateUI();
    }
    
    public void scrollTo(UnitPanel unitPanel){
        double fromTop = 0;

        for(int j = 0; j < this.unitTypeNames.length; j++){
            
            if(this.unitTypeNames[j].equalsIgnoreCase(unitPanel.getUnit().getArmylistUnit().getUnitType())){
                JPanel panel_ = ((JPanel)this.unitPanelsByType.get(this.unitTypeNames[j]));
                java.awt.Component[] components = panel_.getComponents();
                for(int i = 0; i < components.length; ++i){
                    if(components[i] == unitPanel)                    
                        break;
                    fromTop = fromTop + components[i].getSize().getHeight() + UNIT_PANEL_SPACING;
                }
                break;
            }else{  
                fromTop = fromTop + ((JPanel)this.unitPanelsByType.get(this.unitTypeNames[j])).getSize().getHeight() + UNIT_TYPE_PANEL_SPACING;
            }
        }
        this.unitContainerJScrollPane.getVerticalScrollBar().setValue((int)fromTop);        
    }
    
    public void initUnitPanels(GameSystem gameSystem){
        this.gameSystem = gameSystem;
        String[] unitTypeNames = gameSystem.getUnitTypeNames();
        this.unitTypeNames = unitTypeNames;
        this.unitContainer.removeAll();
        for(int i = 0; i < unitTypeNames.length; ++i){
            JPanel panel = new JPanel();
            panel.setLayout(new VerticalFlowLayout(UNIT_PANEL_SPACING));
            panel.setBorder(new javax.swing.border.TitledBorder(unitTypeNames[i]));
            this.unitPanelsByType.put(unitTypeNames[i], panel);
            this.unitContainer.add(panel);
        }
        this.initUnitTree(gameSystem);
    }
    
    public void removeUnit(UnitPanel unitPanel){
        Iterator iterator = this.unitPanelsByType.keySet().iterator();
        while(iterator.hasNext()){
            String unitType  = (String)iterator.next();
            JPanel panel = (JPanel)this.unitPanelsByType.get(unitType);
            panel.remove(unitPanel);
        }
        
        iterator = this.unitTypeTreeNodes.keySet().iterator();
        while(iterator.hasNext()){
            String unitType  = (String)iterator.next();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.unitTypeTreeNodes.get(unitType);
            Enumeration children = node.children();
            while(children.hasMoreElements()){
                DefaultMutableTreeNode child = (DefaultMutableTreeNode)children.nextElement();
                if( child.getUserObject() == unitPanel )
                    node.remove(child);
            }
            
        }
        
        this.units.remove(unitPanel); 
        this.unitContainer.updateUI();
        
        this.refreshUnitTreeUnitCounts();
        this.refreshUnitTree();
    }
    
        
    
    public Object getUnitListSelected(){
          Object temp = this.unitList.getSelectedValue();
          return temp;
    }
    
    public Object getDeletedUnitListSelected(){
          Object temp = this.deletedUnitsList.getSelectedValue();
          return temp;
    }    
    
    public Object getNamedUnitListSelected(){
        return this.namedUnitsList.getSelectedValue();
    }

    public int getDeletedUnitListSelectedIndex(){
          return this.deletedUnitsList.getSelectedIndex();
    }    
    
    public void setUnitListListener(ActionListener listener){
        this.unitListAddButton.addActionListener(listener);
    }
    
    public void setDeletedUnitListListener(ActionListener listener){
        this.deletedUnitsAddButton.addActionListener(listener);
    }    
    
    public ArmylistArmy getSelectedArmylist(){
        if(this.armylistsCombo.getSelectedItem() instanceof String)
            return null;
        return ((ArmylistArmy)this.armylistsCombo.getSelectedItem());
    }
    
/*    public void setArmylistsActionListener(ActionListener listener){
        this.armylistsCombo.addActionListener(listener);
    }
  */  
    public void refreshArmylistData(){
        Iterator iterator = this.units.iterator();
        while(iterator.hasNext()){
            ((UnitPanel)iterator.next()).refreshArmylistData();
        }
        this.armyListNameLabel.setText("("+this.guiCore.getArmylistArmy().getName()+")");
    }
    
    public void setArmylistNameLabelText(String text){
        this.armyListNameLabel.setText(text);
    }
    
    public void setArmyNameLabelText(String text){
        this.armyNameLabel.setText(text);
    }
    
    public void setTargetPointcostLabelText(String text){
        this.targetPointcostLabel.setText(text);
        this.pointcostTargetLabel.setText(text);
    }
    
    public void refreshUnitTree(){
        this.unitTree.updateUI();
    }
    
    public void refreshUnitTreeUnitCounts(){
        Iterator iterator = this.unitTypeTreeNodes.keySet().iterator();
        while(iterator.hasNext()){
            String type = (String)iterator.next();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.unitTypeTreeNodes.get(type);
            node.setUserObject(type + " (" +this.guiCore.getArmy().getUnitCountForUnitType(type)+ ")");
        }
    }
    
    public int getFirstSplitLocation(){
        return this.jSplitPane1.getDividerLocation();
    }

    public int getSecondSplitLocation(){
        return this.jSplitPane2.getDividerLocation();
    }
    
    public void setFirstSplitLocation(int location){
        this.jSplitPane1.setDividerLocation(location);
    }
    
    public void setSecondSplitLocation(int location){
        this.jSplitPane2.setDividerLocation(location);
    }
        
    public void setMenuBar(JMenuBar menuBar){
        this.setJMenuBar(menuBar);
    }
    
    
    private void beginTargetPointcostEdit(){
        
        
        this.targetPointcostEditor = new JTextField();
        final Color normal = this.targetPointcostEditor.getForeground();
        
        this.targetPointcostEditor.setPreferredSize( new Dimension( (int)(this.targetPointcostLabel.getPreferredSize().getWidth()+5), (int)this.targetPointcostLabel.getPreferredSize().getHeight() ) );
        this.targetPointcostEditor.setText(this.targetPointcostLabel.getText());
        
        this.armyTitlePanel.remove(this.targetPointcostLabel);
        this.armyTitlePanel.add(this.targetPointcostEditor, 2);
        
        this.armyTitlePanel.updateUI();
        this.targetPointcostEditor.requestFocus();
        this.targetPointcostEditor.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               endTargetPointcostEdit();
            }
        });
        this.targetPointcostEditor.addFocusListener(new FocusAdapter(){
           public void focusLost(FocusEvent e) {
                endTargetPointcostEdit();
           }
           
        });
        
        targetPoincotEditorValid = true;
        this.targetPointcostEditor.getDocument().addDocumentListener(new DocumentListener(){
            public void changedUpdate(DocumentEvent e) {
                try{
                    Double.parseDouble(targetPointcostEditor.getText());
                    targetPointcostEditor.setForeground(normal);
                    targetPoincotEditorValid = true;
                    System.out.println("now true");
                }catch(NumberFormatException ex){
                    targetPointcostEditor.setForeground(Color.RED);
                    targetPoincotEditorValid = false;
                    System.out.println("now false");
                }      
            }
            
            public void insertUpdate(DocumentEvent e) {
                try{
                    Double.parseDouble(targetPointcostEditor.getText());
                    targetPointcostEditor.setForeground(normal);
                    targetPoincotEditorValid = true;
                    System.out.println("now true");
                }catch(NumberFormatException ex){
                    targetPointcostEditor.setForeground(Color.RED);
                    targetPoincotEditorValid = false;
                    System.out.println("now false");
                }
            }
            public void removeUpdate(DocumentEvent e) {
                try{
                    Double.parseDouble(targetPointcostEditor.getText());
                    targetPointcostEditor.setForeground(normal);
                    targetPoincotEditorValid = true;
                    System.out.println("now true");
                }catch(NumberFormatException ex){
                    targetPointcostEditor.setForeground(Color.RED);
                    targetPoincotEditorValid = false;
                    System.out.println("now false");
                }
            }
        });
        
        
        
    }
    
    public void endTargetPointcostEdit(){
        if(!this.targetPoincotEditorValid){
            //System.out.println("not ending edit. valid: "+this.targetPoincotEditorValid);
            return;
        }
        if(this.targetPointcostEditor != null){
            this.armyTitlePanel.remove(this.targetPointcostEditor);
            this.guiCore.setTargetPointcost(this.targetPointcostEditor.getText());
            this.targetPointcostLabel.setText(this.targetPointcostEditor.getText());
        
            this.armyTitlePanel.add(this.targetPointcostLabel, 2);
        
            this.targetPointcostLabel.setFont(new Font(this.targetPointcostLabel.getFont().getName(), Font.PLAIN,  this.targetPointcostLabel.getFont().getSize()));
        }
    }  
    
    public void endTargetPointcosteditWithoutSaving(){
        if(this.targetPointcostEditor != null){
            this.armyTitlePanel.remove(this.targetPointcostEditor);
            this.armyTitlePanel.add(this.targetPointcostLabel, 2);
            this.targetPointcostLabel.setFont(new Font(this.targetPointcostLabel.getFont().getName(), Font.PLAIN,  this.targetPointcostLabel.getFont().getSize()));
        }
    }
    
    
    
    private void beginArmyNameEdit(){
        
        this.armylistNameEditor = new JTextField();
        this.armylistNameEditor.setPreferredSize(this.armyNameLabel.getPreferredSize());
        this.armylistNameEditor.setText(this.armyNameLabel.getText());
        
        this.armyTitlePanel.remove(this.armyNameLabel);
        this.armyTitlePanel.add(this.armylistNameEditor, 0);
        this.armyTitlePanel.updateUI();
        this.armylistNameEditor.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               endArmyNameEdit();
            }
        });
        this.armylistNameEditor.addFocusListener(new FocusAdapter(){
           public void focusLost(FocusEvent e) {
                endArmyNameEdit();
           }
           
        });
        
        this.armylistNameEditor.requestFocus();
    }
    
    public void endArmyNameEdit(){
        if(this.armylistNameEditor != null){
            this.armyTitlePanel.remove(this.armylistNameEditor);
            this.guiCore.setArmyName(this.armylistNameEditor.getText());
            this.armyNameLabel.setText(this.armylistNameEditor.getText());
        
            this.armyTitlePanel.add(this.armyNameLabel, 0);
        
            this.armyNameLabel.setFont(new Font(this.armyNameLabel.getFont().getName(), Font.PLAIN,  this.armyNameLabel.getFont().getSize()));
        }
    } 
    
    public void moveUnitPanelUp(UnitPanel  panel, String type){
        JPanel typePanel = (JPanel)this.unitPanelsByType.get(type);
        Component[] components = typePanel.getComponents();
        int index = -1;
        for(int i = 0; i < components.length; ++i){
            if(components[i] == panel){
                index = i;
                break;
            }
        }
        if(index > 0){
            typePanel.remove(panel);
            typePanel.add(panel, index - 1);
            DefaultMutableTreeNode temp = (DefaultMutableTreeNode)((DefaultMutableTreeNode)this.unitTypeTreeNodes.get(type)).getChildAt(index);
            ((DefaultMutableTreeNode)this.unitTypeTreeNodes.get(type)).remove(index);
            ((DefaultMutableTreeNode)this.unitTypeTreeNodes.get(type)).insert(temp, index - 1);            
        }
        typePanel.updateUI();
        this.unitTree.updateUI();
    }
    
    public void moveUnitPanelDown(UnitPanel  panel, String type){
        JPanel typePanel = (JPanel)this.unitPanelsByType.get(type);
        Component[] components = typePanel.getComponents();
        int index = -1;
        for(int i = 0; i < components.length; ++i){
            if(components[i] == panel){
                index = i;
                break;
            }
        }
        if(index != -1 && index < components.length){
            typePanel.remove(panel);
            typePanel.add(panel, index + 1);
            DefaultMutableTreeNode temp = (DefaultMutableTreeNode)((DefaultMutableTreeNode)this.unitTypeTreeNodes.get(type)).getChildAt(index);
            ((DefaultMutableTreeNode)this.unitTypeTreeNodes.get(type)).remove(index);
            ((DefaultMutableTreeNode)this.unitTypeTreeNodes.get(type)).insert(temp, index + 1);
        }
        typePanel.updateUI();
        this.unitTree.updateUI();
        
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel armyControlButtonsPanel;
    private javax.swing.JLabel armyListNameLabel;
    private javax.swing.JLabel armyNameLabel;
    private javax.swing.JPanel armyPanel;
    private javax.swing.JPanel armyTitlePanel;
    private javax.swing.JPanel armylistSectionTitlePanel;
    private javax.swing.JComboBox armylistsCombo;
    private javax.swing.JPanel deletedHeaderPanel;
    private javax.swing.JButton deletedUnitsAddButton;
    private javax.swing.JList deletedUnitsList;
    private javax.swing.JButton emptyDeletedUnitsButton;
    private javax.swing.JLabel filterHeader;
    private javax.swing.JPanel filterHeaderPanel;
    private javax.swing.JPanel helperPanel1;
    private javax.swing.JPanel helperPanel2;
    private javax.swing.JPanel infoImagePanel;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JPanel listPanel;
    private javax.swing.JPanel namedHeaderPanel;
    private javax.swing.JButton namedUnitsAddButton;
    private javax.swing.JList namedUnitsList;
    private javax.swing.JButton newButton;
    private javax.swing.JPanel pointcostPanel;
    private javax.swing.JLabel pointcostTargetLabel;
    private javax.swing.JLabel pointcostTotalLabel;
    private javax.swing.JPanel progressBarPanel;
    private javax.swing.JPanel summaryPanel;
    private javax.swing.JLabel targetPointcostLabel;
    private javax.swing.JPanel totalPanel;
    private javax.swing.JPanel treePanel;
    private javax.swing.JPanel unitContainer;
    private javax.swing.JScrollPane unitContainerJScrollPane;
    private javax.swing.JList unitList;
    private javax.swing.JButton unitListAddButton;
    private javax.swing.JPanel unitTreePanel;
    private javax.swing.JScrollPane unitTreeScrollPanel;
    private javax.swing.JPanel unitTypeButtonsPanel;
    // End of variables declaration//GEN-END:variables
    
}
