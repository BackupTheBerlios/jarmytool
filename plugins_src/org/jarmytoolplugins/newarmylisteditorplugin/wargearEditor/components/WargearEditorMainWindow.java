/*
 * WargearEditorMainWindow.java
 *
 * Created on 20 May 2003, 16:23
 */

package org.jarmytoolplugins.newarmylisteditorplugin.wargearEditor.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import org.jArmyTool.data.dataBeans.armylist.*;
import org.jarmytoolplugins.newarmylisteditorplugin.wargearEditor.engine.WargearEditorGUICore;

import java.util.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import javax.swing.JScrollBar;
import javax.swing.JTree;
import javax.swing.table.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.jArmyTool.internaldata.GUICommands;
import org.jarmytoolplugins.newarmylisteditorplugin.wargearEditor.util.WargearTreeCellRenderer;
import org.jarmytoolplugins.newarmylisteditorplugin.wargearEditor.util.WargearTreeUserObjectContainer;


/**
 *
 * @author  Pasi Lehtimäki
 */
public class WargearEditorMainWindow extends javax.swing.JFrame {

    private ArmylistArmy armylistArmy; 
    private WargearEditorGUICore core;
    
    private WargearItemPanel currentItemPanel;
    private WargearGroupPanel currentGroupPanel;
    private WargearTreeUserObjectContainer currentContainer;
    
    
    private  DefaultMutableTreeNode wargearTreeRootNode;
    
    
    /** Creates new form WargearEditorMainWindow */
    public WargearEditorMainWindow(ArmylistArmy armylistArmy, WargearEditorGUICore core) {
        this.core = core;
        this.armylistArmy = armylistArmy;
        initComponents();
        this.initData();
        
        this.scrollPane.addMouseWheelListener(new MouseWheelListener(){
            public void mouseWheelMoved(MouseWheelEvent e){
                
                JScrollBar sb = scrollPane.getVerticalScrollBar();
                sb.setValue(sb.getValue() + (e.getWheelRotation() * GUICommands.getInstance().getWheelSteps()) );
            }
        });
        
        this.wargearTreeScrollPane.addMouseWheelListener(new MouseWheelListener(){
            public void mouseWheelMoved(MouseWheelEvent e){
                
                JScrollBar sb = wargearTreeScrollPane.getVerticalScrollBar();
                sb.setValue(sb.getValue() + (e.getWheelRotation() * GUICommands.getInstance().getWheelSteps()) );
            }
        });
    }
    
    private void addGroupToNode(DefaultMutableTreeNode node, ArmylistWargearGroup group){
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();
        newNode.setUserObject(new WargearTreeUserObjectContainer(group, newNode, node));
        node.add(newNode);
        
        Iterator subGroups = group.getSubGroups().iterator();
        while(subGroups.hasNext()){
            ArmylistWargearGroup subGroup = (ArmylistWargearGroup)subGroups.next();
            this.addGroupToNode(newNode, subGroup);
        }
        
        Iterator items = group.getItems().iterator();
        while(items.hasNext()){
            DefaultMutableTreeNode temp = new DefaultMutableTreeNode();
            ArmylistWargearItem item = (ArmylistWargearItem)items.next();
            WargearTreeUserObjectContainer container = new WargearTreeUserObjectContainer(item, temp, newNode);
            container.setItemGroup(group);
            temp.setUserObject(container);
            newNode.add(temp);
        }
        
    }
    
    private void initData(){
        this.wargearTreeRootNode = new DefaultMutableTreeNode("root");
        
        Iterator groups = this.armylistArmy.getWargearGroups().iterator();
        while(groups.hasNext()){
            ArmylistWargearGroup group = (ArmylistWargearGroup)groups.next();
            this.addGroupToNode(this.wargearTreeRootNode, group);
        }
        
        this.wargearTree = new JTree(this.wargearTreeRootNode);
        this.wargearTreeHolderPanel.removeAll();
        this.wargearTreeHolderPanel.add(this.wargearTree);
        this.wargearTree.setRootVisible(false);
        
        MouseListener ml = new MouseAdapter() {
             public void mousePressed(MouseEvent e) {
                 int selRow = wargearTree.getRowForLocation(e.getX(), e.getY());
                 TreePath selPath = wargearTree.getPathForLocation(e.getX(), e.getY());
                 if(selRow != -1) {
                     if(e.getClickCount() == 1) {
                         nodeSelectedInWGTree(selRow, selPath);
                     }
                     else if(e.getClickCount() == 2) {
                         nodeSelectedInWGTree(selRow, selPath);
                     }
                 }
             }
         };
         this.wargearTree.addMouseListener(ml);        
         this.wargearTree.setCellRenderer(new WargearTreeCellRenderer());
         this.wargearTree.updateUI();

    }
    
    private void nodeSelectedInWGTree(int selRow, TreePath selPath ){
        DefaultMutableTreeNode node = ((DefaultMutableTreeNode)selPath.getLastPathComponent());
        WargearTreeUserObjectContainer container = (WargearTreeUserObjectContainer )node.getUserObject();
        
        if(container.getItem() != null){
            this.startItemEdit(container);
            return;
        }else if(container.getGroup() != null){
            this.startGroupEdit(container);
        }
        
    }
    
    private void startGroupEdit(WargearTreeUserObjectContainer container){
        this.saveData();
        if(this.currentContainer != null)
            this.currentContainer.unHighlightPath();
        
        WargearGroupPanel panel = new WargearGroupPanel(container.getGroup());
        this.currentGroupPanel = panel;
        this.currentContainer = container;
        
        this.editorPane.removeAll();
        this.editorPane.add(this.currentGroupPanel);
        this.editorPane.updateUI();
        
        this.currentContainer.highlightPath();
        this.wargearTree.updateUI();
    }
    
    
    private void startItemEdit(WargearTreeUserObjectContainer container){
        this.saveData();
        if(this.currentContainer != null)
            this.currentContainer.unHighlightPath();
        
        WargearItemPanel panel = new WargearItemPanel(this.armylistArmy, container.getItemGroup(), container.getItem(), this);
        this.currentItemPanel = panel;
        this.currentContainer = container;
        this.editorPane.removeAll();
        this.editorPane.add(this.currentItemPanel);
        this.editorPane.updateUI();
        
        this.currentContainer.highlightPath();
        this.wargearTree.updateUI();
    }
    
    
    
    
    public void saveData(){
        if(this.currentItemPanel != null)
            this.currentItemPanel.saveData();
        
        if(this.currentGroupPanel != null)
            this.currentGroupPanel.saveData();
    }
    
    public void deleteCurrentItem(){
        System.out.println(this.currentContainer.getItemGroup().getName() + " - " +this.currentContainer.getItem());
        
        if(this.currentContainer == null || this.currentItemPanel == null)
            return;
        
        this.currentContainer.getItemGroup().removeItem(this.currentContainer.getItem());
        this.editorPane.removeAll();
        this.editorPane.updateUI();
        
        this.currentContainer.getParent().remove(this.currentContainer.getNode());
        this.wargearTree.updateUI();
        
        this.currentContainer = null;
        this.currentItemPanel = null;
    }
    
    private void newGroup(){
        if(this.currentContainer == null)
            return;
        
        DefaultMutableTreeNode node = null;
        
        ArmylistWargearGroup parentGroup = null;
        if(this.currentContainer.getGroup() != null){
            parentGroup = this.currentContainer.getGroup();
            node = this.currentContainer.getNode();
        }
        if(parentGroup == null && this.currentContainer.getItemGroup() != null){
            parentGroup = this.currentContainer.getItemGroup();
            node = this.currentContainer.getParent();
        }
        if(parentGroup == null)
            return;
        
        ArmylistWargearGroup newGroup = new ArmylistWargearGroup("new group");
        
        parentGroup.addSubGroup(newGroup);
        this.addGroupToNode(node, newGroup);
        this.wargearTree.updateUI();
        
    }
    
    private void newItem(){
        
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jSplitPane1 = new javax.swing.JSplitPane();
        treePanel = new javax.swing.JPanel();
        treeControlPanel = new javax.swing.JPanel();
        newGroupButton = new javax.swing.JButton();
        newItem = new javax.swing.JButton();
        wargearTreeScrollPane = new javax.swing.JScrollPane();
        wargearTreeHolderPanel = new javax.swing.JPanel();
        wargearTree = new javax.swing.JTree();
        editorPanelContainer = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        editorPane = new javax.swing.JPanel();

        setTitle("Wargear Editor");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jSplitPane1.setDividerLocation(250);
        treePanel.setLayout(new java.awt.BorderLayout());

        newGroupButton.setText("newGroup");
        newGroupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGroupButtonActionPerformed(evt);
            }
        });

        treeControlPanel.add(newGroupButton);

        newItem.setText("newItem");
        newItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newItemActionPerformed(evt);
            }
        });

        treeControlPanel.add(newItem);

        treePanel.add(treeControlPanel, java.awt.BorderLayout.SOUTH);

        wargearTreeHolderPanel.setLayout(new java.awt.BorderLayout());

        wargearTreeHolderPanel.add(wargearTree, java.awt.BorderLayout.CENTER);

        wargearTreeScrollPane.setViewportView(wargearTreeHolderPanel);

        treePanel.add(wargearTreeScrollPane, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(treePanel);

        editorPanelContainer.setLayout(new java.awt.BorderLayout());

        scrollPane.setViewportView(editorPane);

        editorPanelContainer.add(scrollPane, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(editorPanelContainer);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void newItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newItemActionPerformed
        this.newItem();
    }//GEN-LAST:event_newItemActionPerformed

    private void newGroupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGroupButtonActionPerformed
        this.newGroup();
    }//GEN-LAST:event_newGroupButtonActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.core.closeWindow();
    }//GEN-LAST:event_exitForm

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel editorPane;
    private javax.swing.JPanel editorPanelContainer;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JButton newGroupButton;
    private javax.swing.JButton newItem;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel treeControlPanel;
    private javax.swing.JPanel treePanel;
    private javax.swing.JTree wargearTree;
    private javax.swing.JPanel wargearTreeHolderPanel;
    private javax.swing.JScrollPane wargearTreeScrollPane;
    // End of variables declaration//GEN-END:variables
    
}
