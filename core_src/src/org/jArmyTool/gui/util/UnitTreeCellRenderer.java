/*
 * UnitTreeCellRenderer.java
 *
 * Created on 25 October 2003, 23:08
 */

package core_src.src.org.jArmyTool.gui.util;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.jArmyTool.data.dataBeans.gameSystem.GameSystem;
import org.jArmyTool.gui.components.UnitPanel;

/**
 *
 * @author  pasi
 */
public class UnitTreeCellRenderer extends DefaultTreeCellRenderer{
    
    private GameSystem gameSystem;
    
    /** Creates a new instance of UnitTreeCellRenderer */
    public UnitTreeCellRenderer(GameSystem gameSystem) {
        this.gameSystem = gameSystem;
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
        
        if(this.gameSystem == null)
            return this;
        
        Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
        
        //System.out.println("UO: "+userObject);
        
        if(userObject instanceof String){
            String type = (String)userObject;
            if( type.compareToIgnoreCase("root") == 0)
                return this;
            String[] types = this.gameSystem.getUnitTypeNames();
            String rightType = null;
            for(int i = 0; i < types.length; ++i){
                if(type.lastIndexOf(types[i]) != -1){
                    rightType = types[i];
                    break;
                }
            }
            if(rightType == null)
                return this;
            
            setIcon(new ImageIcon(this.gameSystem.getUnitTypeByName(rightType).getLargeImage()));
            this.setFont(new java.awt.Font("Lucida Sans Typewriter", 1, 13));
        }else if(userObject instanceof UnitPanel){
            setIcon(new ImageIcon(this.gameSystem.getUnitTypeByName(((UnitPanel)userObject).getUnit().getArmylistUnit().getUnitType()).getSmallImage()));
            this.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 10));
        }
        /*
        if (leaf && isTutorialBook(value)) {
            setIcon(tutorialIcon);
            setToolTipText("This book is in the Tutorial series.");
        } else {
            setToolTipText(null); //no tool tip
        } */

        return this;
    }
    
}
