/*
 * UnitListCellRenderer.java
 *
 * Created on 26 October 2003, 00:08
 */

package core_src.src.org.jArmyTool.gui.util;

import java.awt.Color;
import java.awt.Component;
import java.util.Iterator;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import org.jArmyTool.data.dataBeans.armylist.ArmylistArmy;
import org.jArmyTool.data.dataBeans.armylist.ArmylistModel;
import org.jArmyTool.data.dataBeans.armylist.ArmylistUnit;
import org.jArmyTool.data.dataBeans.gameSystem.GameSystem;
import org.jArmyTool.gui.components.MainWindow;
import org.jArmyTool.gui.components.UnitPanel;
import org.jArmyTool.gui.engine.GUICore;

/**
 *
 * @author  pasi
 */
public class ArmylistsListCellRenderer extends DefaultListCellRenderer{
     
    private static final int MAX_LENGTH = 31;
    
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
        
        String text = null;
        if(value instanceof String){ //GameSystem
            text = (String)value;
            setBackground(MainWindow.TITLE_BACKGROUND);
            setToolTipText(text);
        }else if(value instanceof ArmylistArmy){//ArmylistArmy
            text = ((ArmylistArmy)value).getName();
            setToolTipText(GUICore.getInfoForArmy((ArmylistArmy)value));
        }else{
            text ="";
        }
        
        
        if( (text).length() > MAX_LENGTH ){
            setText( (text).substring(0, MAX_LENGTH) + "..");
        }else{
            setText(text);
        }
        
        
        
        
	return this;
    }    
    
}
