/*
 * WargearTreeUserObjectContainer.java
 *
 * Created on 12 February 2004, 14:19
 */

package org.jarmytoolplugins.newarmylisteditorplugin.wargearEditor.util;

import javax.swing.tree.DefaultMutableTreeNode;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearGroup;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearItem;

/**
 *
 * @author  Pasi Lehtimäki
 */
public class WargearTreeUserObjectContainer {
    
    private ArmylistWargearGroup group;
    private ArmylistWargearItem item;
    private ArmylistWargearGroup itemGroup;
    
    private DefaultMutableTreeNode node;
    private DefaultMutableTreeNode parent;
    
    private boolean highlighted = false;
    
    /** Creates a new instance of WargearTreeUserObjectContainer */
    public WargearTreeUserObjectContainer(Object groupOrItem, DefaultMutableTreeNode node, DefaultMutableTreeNode parent) {
        if(groupOrItem instanceof ArmylistWargearGroup){
            this.group = (ArmylistWargearGroup)groupOrItem;
        }else if(groupOrItem instanceof ArmylistWargearItem){
            this.item = (ArmylistWargearItem)groupOrItem;
        }
        
        this.node = node;
        this.parent = parent;
    }
    
    public ArmylistWargearGroup getGroup(){
        return this.group;
    }
    
    public ArmylistWargearItem getItem(){
        return this.item;
    }
    
    public void setItemGroup(ArmylistWargearGroup itemGroup){
        this.itemGroup = itemGroup;
    }
    
    public ArmylistWargearGroup getItemGroup(){
        return this.itemGroup;
    }
    
    public DefaultMutableTreeNode getNode(){
        return this.node;
    }
    
    public DefaultMutableTreeNode getParent(){
        return this.parent;
    }
    
    public void highlightPath(){
        this.highlighted = true;
        if(this.parent.getUserObject() instanceof WargearTreeUserObjectContainer)
            ((WargearTreeUserObjectContainer)this.parent.getUserObject()).highlightPath();
    }

    public void unHighlightPath(){
        this.highlighted = false;
        if(this.parent.getUserObject() instanceof WargearTreeUserObjectContainer)
            ((WargearTreeUserObjectContainer)this.parent.getUserObject()).unHighlightPath();
    }    
    
    public boolean isHighlighted(){
        return this.highlighted;
        
    }
    
    public String toString(){
        if(this.group != null)
            return this.group.getName();
        if(this.item != null)
            return this.item.getName();
        return "";
    }
}
