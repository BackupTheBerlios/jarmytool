/*
 * UnitListCellRenderer.java
 *
 * Created on 26 October 2003, 00:08
 */

package org.jarmytoolplugins.newarmylisteditorplugin.util;

import java.awt.Component;
import java.util.Iterator;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import org.jArmyTool.data.dataBeans.armylist.ArmylistModel;
import org.jArmyTool.data.dataBeans.armylist.ArmylistUnit;
import org.jArmyTool.data.dataBeans.gameSystem.GameSystem;


/**
 *
 * @author  pasi
 */
public class UnitListCellRenderer extends DefaultListCellRenderer{
    
    private GameSystem gameSystem;
    
    /** Creates a new instance of UnitListCellRenderer */
    public UnitListCellRenderer(GameSystem gameSystem) {
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
        ArmylistUnit unit = null;
        
        if(value instanceof ArmylistUnit){
            unit = (ArmylistUnit)value;
        }else{
            return this;
        }
        
        
        setIcon(new ImageIcon(this.gameSystem.getUnitTypeByName(unit.getUnitType()).getSmallImage()));

        Iterator iterator = unit.getModels().iterator();
        StringBuffer sb = new StringBuffer("<html><b>"+unit.getName()+"</b> <ul>");
        while(iterator.hasNext()){
            sb.append("<li>" + ((ArmylistModel)iterator.next()).getName() + "</li>");
        }
        sb.append("</ul></html>");
        setToolTipText(sb.toString());

        /* We additionally set the JLabels icon property here.
         */
     /*   String s = value.toString();
	setIcon((s.length > 10) ? longIcon : shortIcon);
*/
	return this;
    }    
    
}
