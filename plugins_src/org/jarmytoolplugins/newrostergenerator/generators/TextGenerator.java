/*
 * BBCodeGenerator.java
 *
 * Created on 16 January 2004, 02:19
 */

package plugins_src.org.jarmytoolplugins.newrostergenerator.generators;

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
public class TextGenerator {
    
    /** Creates a new instance of BBCodeGenerator */
    private TextGenerator() {
    }
    
    public static StringBuffer generateText(Army army, boolean isPreview){
        StringBuffer buffer = new StringBuffer();
        if(isPreview)
            buffer.append("<html>");
        
       if(army.getArmylistArmy().getName().compareTo(army.getName()) != 0 ){
            buffer.append(army.getName() + lineBreak(isPreview));
            buffer.append("(" + army.getArmylistArmy().getName() + ")"+lineBreak(isPreview) );
            
        }else{
            buffer.append(army.getName() + lineBreak(isPreview));            
         
        }
        
        if( (int)army.getPointcostTotal() == army.getPointcostTotal() ){
                buffer.append((int)army.getTargetPointcost() + " Points"+lineBreak(isPreview)+lineBreak(isPreview));
            }else{
                buffer.append(army.getTargetPointcost() + " Points"+lineBreak(isPreview)+lineBreak(isPreview));
        }           
        
        
        
        
        buffer.append(generateUnits(army, isPreview));
        if(army.getPointcostTotal() == (int)army.getPointcostTotal()){
            buffer.append(lineBreak(isPreview)+"Total: "+(int)army.getPointcostTotal());
        }else{
            buffer.append(lineBreak(isPreview)+"Total: "+army.getPointcostTotal() + " pts");
        }
        if(isPreview)
            buffer.append("</html>");
        return buffer;
    }    
    
    
    private static StringBuffer generateUnits(Army army, boolean isPreview){
        StringBuffer buffer = new StringBuffer();
        String[] unitTypeNames = army.getArmylistArmy().getGameSystem().getUnitTypeNames();
        
        for(int i = 0; i < unitTypeNames.length; ++i){
            buffer.append(unitTypeNames[i] +lineBreak(isPreview));    
        
            Iterator units = army.getSelectedUnits().iterator();
            while(units.hasNext()){
                Unit unit = (Unit)units.next();
                ArmylistUnit armylistUnit = unit.getArmylistUnit();
                if(armylistUnit.getUnitType().compareTo(unitTypeNames[i]) != 0)
                    continue;
                
                if(unit.getPointCost() == (int)unit.getPointCost()){
                    buffer.append(armylistUnit.getName()+" ("+ (int)unit.getPointCost()+"pts)");
                }else{
                    buffer.append(""+armylistUnit.getName()+" ("+ unit.getPointCost()+"pts)");
                }

                StringBuffer modelBuf = new StringBuffer();
                boolean needModels = false;
                int differentModels = 0;
                Iterator models = unit.getModels().iterator();
                while(models.hasNext()){
                    Model model = (Model)models.next();
                    if(model.getModelCount() > 0){
                        modelBuf.append(lineBreak(isPreview));
                        if(model.getModelCount() > 1){
                            modelBuf.append(space(isPreview)+model.getModelCount());
                            needModels = true;
                        }
                        
                        
                        modelBuf.append(model.getArmylistModel().getName());
                        
                        StringBuffer updates = generateModelUpdaes(model, isPreview);
                        if(updates.length() > 0){
                            needModels = true;
                            modelBuf.append(lineBreak(isPreview)+space(isPreview)+space(isPreview)+updates);
                        }
                        
                        ++differentModels;
                    }
                }

                if(needModels || differentModels > 1)                
                    buffer.append(modelBuf);

                buffer.append(lineBreak(isPreview)+lineBreak(isPreview));
            }
        }
        
        return buffer;
    }
    
    
    private static StringBuffer generateModelUpdaes(Model model, boolean isPreview){
        StringBuffer ret = new StringBuffer();
        Iterator updates = model.getUpdates().iterator();
        boolean isNeeded = false;
        ret.append(" ");
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
        if(isNeeded)
            return ret;
        return new StringBuffer();
    }
    
    private static StringBuffer generateUnitUpdates(Unit unit, boolean isPreview){
      StringBuffer ret = new StringBuffer();
        ret.append(lineBreak(isPreview));
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
        
        
        return ret;
    }
    
    
    private static String lineBreak(boolean isPreview){
        if(isPreview)
            return "<br>";
        return "\n";
    }
    
    private static String space(boolean isPreview){
        if(isPreview)
            return "&nbsp;";
        return " ";
    }    
}
