/*
 * UnitTreeCellRenderer.java
 *
 * Created on 25 October 2003, 23:08
 */

package org.jarmytoolplugins.newarmylisteditorplugin.wargearEditor.util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.jArmyTool.internaldata.GUICommands;

/**
 *
 * @author  pasi
 */
public class WargearTreeCellRenderer extends DefaultTreeCellRenderer{
    

    private static final Color HIGHLIGHTED = Color.BLUE;
    private static final Color NORMAL = Color.BLACK;
    
    /** Creates a new instance of UnitTreeCellRenderer */
    public WargearTreeCellRenderer() {
    }
    
    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
        
        if(!expanded)
            tree.expandRow(row);
        
        Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
        
        if(! (userObject instanceof WargearTreeUserObjectContainer) )
            return this;
            
        WargearTreeUserObjectContainer container = (WargearTreeUserObjectContainer)userObject;
        if(container.isHighlighted()){
            this.setBackground(HIGHLIGHTED);
            this.setForeground(HIGHLIGHTED);
            
        }else{
            //this.setBackground(NORMAL);
            //this.setForeground(NORMAL);
        }
        
        if(container.getItem() != null){
            this.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 13));
            this.setIcon(GUICommands.getInstance().getWargearItemIcon());
        }else if(container.getGroup() != null){
            this.setFont(new java.awt.Font("Lucida Sans Typewriter", 1, 14));
            this.setIcon(GUICommands.getInstance().getWargearGroupIcon());
        }else{
            return this;
        }
        
            

        
        
        return this;
    }
    
}
