/*
 * UnitListCellRenderer.java
 *
 * Created on 26 October 2003, 00:08
 */

package plugins_src.org.jarmytoolplugins.newarmylisteditorplugin.util;

import java.awt.Component;
import java.util.Iterator;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import org.jArmyTool.data.dataBeans.gameSystem.GameSystem;
import org.jArmyTool.data.dataBeans.gameSystem.UnitType;


/**
 *
 * @author  pasi
 */
public class UnitTypeComboCellRenderer extends DefaultListCellRenderer{
    
    private GameSystem gameSystem;
    
    /** Creates a new instance of UnitListCellRenderer */
    public UnitTypeComboCellRenderer(GameSystem gameSystem) {
        this.gameSystem = gameSystem;
    }
    
    
    public Component getListCellRendererComponent(
        JList list,
	Object value,   // value to display
	int index,      // cell index
	boolean iss,    // is the cell selected
	boolean chf)    // the list and the cell have the focus
    {
        /* The DefaultListCellRenderer class will take care of
         * the JLabels text property, it's foreground and background
         * colors, and so on.
         */
        super.getListCellRendererComponent(list, value, index, iss, chf);

        //System.out.println(value);
        UnitType type = null;
        
        if(value instanceof String){
            type = this.gameSystem.getUnitTypeByName((String)value);
        }else{
            return this;
        }
        
        setIcon(new ImageIcon(type.getSmallImage()));
        return this;
    }    
    
}
