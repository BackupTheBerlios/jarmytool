/*
 * UnitGenerator.java
 *
 * Created on 28 December 2003, 02:31
 */

package plugins_src.org.jarmytoolplugins.newrostergenerator.generators;

import java.util.Iterator;
import org.jArmyTool.data.dataBeans.army.Army;
import org.jArmyTool.data.dataBeans.army.Model;
import org.jArmyTool.data.dataBeans.army.ModelUpdate;
import org.jArmyTool.data.dataBeans.army.Unit;
import org.jArmyTool.data.dataBeans.army.UnitUpdate;
import org.jArmyTool.data.dataBeans.gameSystem.GameSystem;
import org.jArmyTool.data.dataBeans.util.ModelStat;
import org.jArmyTool.data.dataBeans.util.ModelStatHolder;

/**
 *
 * @author  pasi
 */
public class UnitGenerator {
    
    private Army army;
    private GameSystem gameSystem;
    private String[] unitTypeNames;
    
    boolean unitsByType = true;
    
    /** Creates a new instance of UnitGenerator */
    public UnitGenerator(Army army) {
        this.army = army;
        this.gameSystem = this.army.getArmylistArmy().getGameSystem();
        this.unitTypeNames = this.gameSystem.getUnitTypeNames();
    }
    
    public void setUnitsByType(boolean byType){
        this.unitsByType = byType;
    }
    
    
    public StringBuffer generateUnits(){
        StringBuffer ret = new StringBuffer();
        
        if(this.unitsByType){
            ret.append(this.generateUnitsByType());
        }else{
            ret.append("This feature not implemented yet :(");
        }
        
        return ret;
    }
    
    private StringBuffer generateUnitsByType(){
        StringBuffer ret = new StringBuffer();
        for(int i = 0; i < this.unitTypeNames.length; ++i){
            ret.append("<h2>"+ this.unitTypeNames[i] +"</h2>");
            Iterator iterator = this.army.getSelectedUnits().iterator();
            while(iterator.hasNext()){
                Unit unit = (Unit)iterator.next();
                if(unit.getArmylistUnit().getUnitType().compareTo(this.unitTypeNames[i]) != 0)
                    continue;
                
                ret.append("<table class=\""+ CSSGenerator.CSS_STYLE_NAME_UNIT_BOX +"\" width=\"500\" border=\"0\" cellspacing=\"0\" cellpadding=\"3\">\n");
                
                if(unit.getName().lastIndexOf((unit.getArmylistUnit().getName())) != -1){
                    ret.append("<tr>\n<td class=\""+ CSSGenerator.CSS_STYLE_NAME_UNIT_NAME +"\">"+ unit.getName() +"</td>\n<td class=\""+ CSSGenerator.CSS_STYLE_NAME_UNIT_NAME +"\" align=\"right\">");
                }else{
                    ret.append("<tr>\n<td class=\""+ CSSGenerator.CSS_STYLE_NAME_UNIT_NAME +"\">"+ unit.getName() +" ("+unit.getArmylistUnit().getName()+")</td>\n<td class=\""+ CSSGenerator.CSS_STYLE_NAME_UNIT_NAME +"\" align=\"right\">");
                }
                
                if(unit.getPointCost() == (int)unit.getPointCost()){
                    ret.append((int)unit.getPointCost()+"pts</td>\n</tr>\n");
                }else{
                    ret.append(unit.getPointCost()+"pts</td>\n</tr>\n");
                }
                
                ret.append("<td class=\"unit_box_border_top\" colspan=\"2\">\n<table class=\""+ CSSGenerator.CSS_STYLE_NAME_UNIT_BOX +"\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"3\"><tr><td>");
                
                
                
                ret.append(this.generateUnitsModels(unit));
                ret.append(this.generateUnitUpdates(unit));
                    
                ret.append("</td>\n</tr>\n</table><br>");
            }
        }
        return ret;
    }
    
    
    private StringBuffer generateUnitsModels(Unit unit){
        StringBuffer ret = new StringBuffer();
        
        Iterator iterator = unit.getModels().iterator();
        String currentStatType = null;
        int lineCount = 0;
        while(iterator.hasNext()){
            Model model = (Model)iterator.next();
            if(model.getModelCount() > 0){
                ++lineCount;
                if(currentStatType == null || currentStatType.compareTo(model.getArmylistModel().getStatTypeName()) != 0){
                    if(currentStatType != null)
                        ret.append("</tr>\n</table>");
                        
                    lineCount = 0;
                    currentStatType = model.getArmylistModel().getStatTypeName();
                    
                    ret.append("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"3\">");
                    
                    ret.append("<tr class=\""+ CSSGenerator.CSS_STYLE_NAME_UNIT_STAT_NAME_BOX +"\"\n><td width=\"30%\">&nbsp;</td>");
                    ret.append("<td width=\"7%\" align=\"right\"><b>#</b></td>");
                    
                    Iterator statIterator = this.gameSystem.getStatType(model.getArmylistModel().getStatTypeName()).getAllStats().iterator();
                    while(statIterator.hasNext()){
                        ModelStat stat = (ModelStat)statIterator.next();
                        ret.append("<td width=\"7%\" align=\"center\"><b>"+stat.getSymbol()+"</b></td>");
                    }
                    ret.append("<td align=\"left\"><b>Equipment</b></td>");
                    ret.append("</tr>\n");
                    
                    
                }
                
                
                
                if(lineCount % 2 == 0){
                    ret.append("<tr class=\""+CSSGenerator.CSS_STYLE_NAME_UNIT_BG_1+"\">");
                }else{
                    ret.append("<tr class=\""+CSSGenerator.CSS_STYLE_NAME_UNIT_BG_2+"\">");
                }
                
                
                ret.append("\n<td class=\""+CSSGenerator.CSS_STYLE_NAME_UNIT_NAME+"\" valign=\"top\">"+ model.getName() +"</td><td align=\"right\" valign=\"top\">" + model.getModelCount()+"</td>\n");
                
                Iterator modelStatIterator = model.getArmylistModel().getStats().iterator();
                while(modelStatIterator.hasNext()){
                    ret.append("<td align=\"center\" valign=\"top\">" + ((ModelStatHolder)modelStatIterator.next()).getValue() + "</td>\n");
                }
                Iterator modelUpdates = model.getUpdates().iterator();
                StringBuffer updateBuffer = new StringBuffer();
                updateBuffer.append("<td align=\"left\" nowrap>");
                int beginLength = updateBuffer.length();
                boolean updates = false;
                while(modelUpdates.hasNext()){
                    ModelUpdate update = (ModelUpdate)modelUpdates.next();
                    if(update.getSelectedCount() > 0){
                        if(updateBuffer.length() > beginLength)
                            updateBuffer.append("<br>");
                        
                        updates = true;
                        updateBuffer.append(update.getName());
                        if(update.getSelectedCount() > 1){
                            updateBuffer.append("(X" +update.getSelectedCount()+")");
                        }
                    }
                    
                }

                Iterator wargear = model.getSelectedWargearNames().iterator();
                while(wargear.hasNext()){
                    if(updateBuffer.length() > beginLength)
                        updateBuffer.append("<br>");
                    updates = true;
                    updateBuffer.append((String)wargear.next());

                }
                
                updateBuffer.append("</td>");
                
                if(updates)
                    ret.append(updateBuffer);

                ret.append("</tr>\n");
            }
        }
        ret.append("</table></tr></table>");
        return ret;
    }
    
    
    private StringBuffer generateUnitUpdates(Unit unit){
        StringBuffer ret = new StringBuffer();
        
        boolean anyEquipment = false;
        ret.append("<br><span class=\""+CSSGenerator.CSS_STYLE_NAME_UNIT_NAME+"\">Equipment:</span><br>");
        int beginLength = ret.length();
        Iterator iterator = unit.getUpdates().iterator();
        while(iterator.hasNext()){
            UnitUpdate update = (UnitUpdate)iterator.next();
            if(update.getSelectedCount() > 0){
                anyEquipment = true;
                if(ret.length() > beginLength)
                    ret.append(", ");  
                ret.append(update.getName());
                if(update.getSelectedCount() > 1)
                    ret.append("(X"+update.getSelectedCount() + ")");  
                
            }
            
        }
        
        if(!anyEquipment)
            return new StringBuffer();
        return ret;
    }
    
}
