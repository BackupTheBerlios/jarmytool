/*
 * UnitListCellRenderer.java
 *
 * Created on 26 October 2003, 00:08
 */

package org.jArmyTool.gui.util;

import java.awt.Color;
import java.awt.Component;
import java.util.Iterator;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import org.jArmyTool.data.dataBeans.army.Model;
import org.jArmyTool.data.dataBeans.army.Unit;
import org.jArmyTool.data.dataBeans.armylist.ArmylistModel;
import org.jArmyTool.data.dataBeans.armylist.ArmylistUnit;
import org.jArmyTool.data.dataBeans.gameSystem.GameSystem;
import org.jArmyTool.gui.components.UnitPanel;

/**
 *
 * @author  pasi
 */
public class UnitListCellRenderer extends DefaultListCellRenderer{
    
    private GameSystem gameSystem;
    
    private int lineCount = 0;
    
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
        Unit unit_ = null;
        StringBuffer sb = null;
        
        if(value instanceof UnitPanel){
            unit = ((UnitPanel)value).getUnit().getArmylistUnit();
            
        }else if(value instanceof Unit){
            unit_ = ((Unit)value);
            unit = unit_.getArmylistUnit();
            
            Iterator iterator = unit_.getModels().iterator();
            sb = new StringBuffer("<html><b>"+unit_.getName()+"</b>"); 
            
            if(unit_.getPointCost() == (int)unit_.getPointCost()){
                sb.append(" ("+(int)unit_.getPointCost()+"pts)");
            }else{
                sb.append(" ("+unit_.getPointCost()+"pts)");
            }
            
            sb.append("<br>("+unit_.getArmylistUnit().getName()+")<br>");
            
            //sb.append("<ul>");
            while(iterator.hasNext()){
                Model model = ((Model)iterator.next());
                if(model.getModelCount() > 0){
                    sb.append("&nbsp;&nbsp;<u>");
                    if(model.getModelCount() > 1)
                        sb.append(model.getModelCount()+" ");
                        
                    sb.append(model.getName() + "</u><br>");
                    
                }                
            }
            sb.append("</html>");
            setText(sb.toString());
            setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
            

            
        }else{
            unit = (ArmylistUnit)value;
        }
        
        try{
            setIcon(new ImageIcon(this.gameSystem.getUnitTypeByName(unit.getUnitType()).getSmallImage()));
        }catch(NullPointerException e){
        }
        
        if(sb != null){
            setToolTipText(sb.toString());
        }else{
            Iterator iterator = unit.getModels().iterator();
            sb = new StringBuffer("<html><b>"+unit.getName()+"</b> <ul>");
            while(iterator.hasNext()){
                sb.append("<li>" + ((ArmylistModel)iterator.next()).getName() + "</li>");
            }
            sb.append("</ul></html>");
            setToolTipText(sb.toString());
        }
        

        
        
	return this;
    }    
    
}
