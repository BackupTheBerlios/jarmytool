/*
 * BBCodeGenerator.java
 *
 * Created on 16 January 2004, 02:19
 */

package org.jarmytoolplugins.newrostergenerator.generators;

import java.lang.StringBuffer;
import java.util.Collection;
import java.util.Iterator;
import org.jArmyTool.data.dataBeans.army.Army;
import org.jArmyTool.data.dataBeans.army.Model;
import org.jArmyTool.data.dataBeans.army.ModelUpdate;
import org.jArmyTool.data.dataBeans.army.Unit;
import org.jArmyTool.data.dataBeans.army.UnitUpdate;
import org.jArmyTool.data.dataBeans.armylist.ArmylistUnit;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearGroup;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearItem;

/**
 *
 * @author  pasi
 */
public class BBCodeGenerator {
    
    /** Creates a new instance of BBCodeGenerator */
    private  BBCodeGenerator() {
    }
    
    public static StringBuffer generateBBCode(Army army){
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html>");
        buffer.append(generateUnits(army));
        if(army.getPointcostTotal() == (int)army.getPointcostTotal()){
            buffer.append("<h4>[u]Total: "+(int)army.getPointcostTotal()+"[/u]</h4>");
        }else{
            buffer.append("<h4>[u]Total: "+army.getPointcostTotal()+"[/u]</h4>");
        }
        buffer.append("</html>");
        return buffer;
    }    
    
    
    private static StringBuffer generateUnits(Army army){
        StringBuffer buffer = new StringBuffer();
        String[] unitTypeNames = army.getArmylistArmy().getGameSystem().getUnitTypeNames();
        
        for(int i = 0; i < unitTypeNames.length; ++i){
            buffer.append("<h3><u>[size=15][u][b]"+ unitTypeNames[i] +"[/b][/u][/size]</u></h3>");    
        
            Iterator units = army.getSelectedUnits().iterator();
            while(units.hasNext()){
                Unit unit = (Unit)units.next();
                ArmylistUnit armylistUnit = unit.getArmylistUnit();
                if(armylistUnit.getUnitType().compareTo(unitTypeNames[i]) != 0)
                    continue;
                
                if(unit.getPointCost() == (int)unit.getPointCost()){
                    buffer.append("<h3>[b]"+armylistUnit.getName()+" ("+ (int)unit.getPointCost()+")" +"[/b]</h3>\n");
                }else{
                    buffer.append("<h3>[b]"+armylistUnit.getName()+" ("+ unit.getPointCost()+")" +"[/b]</h3>\n");
                }

                StringBuffer modelBuf = new StringBuffer();
                boolean needModels = false;
                int differentModels = 0;
                Iterator models = unit.getModels().iterator();
                while(models.hasNext()){
                    Model model = (Model)models.next();
                    if(model.getModelCount() > 0){
                        modelBuf.append("<p>[u]<u>");
                        if(model.getModelCount() > 1){
                            modelBuf.append(model.getModelCount() + " ");
                            needModels = true;
                        }
                        
                        
                        modelBuf.append(model.getArmylistModel().getName()+"</u>[/u]");
                        
                        StringBuffer updates = generateModelUpdaes(model);
                        if(updates.length() > 0){
                            needModels = true;
                            modelBuf.append(updates);
                        }
                        modelBuf.append("</p>\n");
                        
                        ++differentModels;
                    }
                }

                if(needModels || differentModels > 1)                
                    buffer.append(modelBuf);
                
                buffer.append(generateUnitUpdates(unit));

                buffer.append("<p></p>");
            }
        }
        
        return buffer;
    }
    
    
    private static StringBuffer generateModelUpdaes(Model model){
        StringBuffer ret = new StringBuffer();
        Iterator updates = model.getUpdates().iterator();
        boolean isNeeded = false;
        ret.append(" [i]<i>");
        int start = ret.length();
        while(updates.hasNext()){
            ModelUpdate update = (ModelUpdate)updates.next();
            if(update.getSelectedCount() > 0){
                if(ret.length() > start)
                    ret.append(", ");
                ret.append(update.getArmylistModelUpdate().getName());
                if(update.getSelectedCount() > 1){
                    ret.append("(X"+update.getSelectedCount()+")");
                }
                if(update.getPointcost() > 0){
                    if(update.getPointcost() == (int)update.getPointcost()){
                        ret.append(" "+(int)update.getPointcost() +"pts");
                    }else{
                        ret.append(" "+update.getPointcost() +"pts");
                    }
                    
                }
                isNeeded=true;
            }
        }
        
        Iterator wgGroups = model.getArmylistModel().getArmylistArmy().getWargearGroups().iterator();
        while(wgGroups.hasNext()){
            Collection coll = model.getSelectedWargear( ((ArmylistWargearGroup)wgGroups.next()).getName() );
            if(coll == null)
                continue;
            Iterator wargear = coll.iterator();
            while(wargear.hasNext()){
                ArmylistWargearItem item = (ArmylistWargearItem)wargear.next();
                if(ret.length() > start)
                    ret.append(", ");
                ret.append(item.getName());
                if(item.getPointcost() > 0){
                    if(item.getPointcost() == (int)item.getPointcost()){
                        ret.append(" "+(int)item.getPointcost() +"pts");
                    }else{
                        ret.append(" "+item.getPointcost() +"pts");
                    }

                }
                isNeeded=true;
            }
        }  
        ret.append("</i>[/i]");
        if(isNeeded)
            return ret;
        return new StringBuffer();
    }
    
    private static StringBuffer generateUnitUpdates(Unit unit){
        StringBuffer ret = new StringBuffer();
        ret.append("<p>[i]<i>");
        int start = ret.length();
        
        Iterator updates = unit.getUpdates().iterator();
        while(updates.hasNext()){
            UnitUpdate update = (UnitUpdate)updates.next();
            if(update.getSelectedCount() > 0){
                if(ret.length() > start)
                    ret.append(", ");
                ret.append(update.getArmylistUnitUpdate().getName());
                if(update.getSelectedCount() > 1){
                    ret.append("(X"+update.getSelectedCount()+")");
                }
                if(update.getPointcost() > 0){
                    if(update.getPointcost() == (int)update.getPointcost()){
                        ret.append(" "+(int)update.getPointcost() +"pts");
                    }else{
                        ret.append(" "+update.getPointcost() +"pts");
                    }
                    
                }
            }
        }
        
        ret.append("</i>[/i]</p>");
        return ret;
    }
}
