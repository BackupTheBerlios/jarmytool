/*
 * Army.java
 *
 * Created on 04 December 2002, 21:07
 */

package core_src.src.org.jArmyTool.data.dataBeans.army;

import java.util.*;
import java.io.Serializable;
import org.jArmyTool.data.dataBeans.armylist.ArmylistArmy;

/**
 *
 * @author  pasi
 */
public class Army implements Serializable{
    private ArmylistArmy armylistArmy;
    
    private String name;
    //private LinkedList selectedUnits;
    
    private HashMap selectedUnitByType;
    
    private double targetPointcost;
    
    /** Creates a new instance of Army */
    public Army(ArmylistArmy armylistArmy) {
        this.armylistArmy = armylistArmy;
        this.targetPointcost = this.armylistArmy.getGameSystem().getDefaultTargetPointcost();
        //this.selectedUnits = new LinkedList();
        
        this.selectedUnitByType = new HashMap();
        String[] types = this.armylistArmy.getGameSystem().getUnitTypeNames();
        for(int i = 0; i < types.length; ++i){
            this.selectedUnitByType.put(types[i], new LinkedList());
        }
    }
    
    public ArmylistArmy getArmylistArmy(){
        return this.armylistArmy;
    }
    
    public String getName(){
        if(this.name == null)
            return this.armylistArmy.getName();
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }  
    
    public void addUnit(Unit unit){
        ((LinkedList)this.selectedUnitByType.get(unit.getArmylistUnit().getUnitType())).add(unit);
        //this.selectedUnits.add(unit);
    }
    
    public boolean removeUnit(Unit unit){
        return ((LinkedList)this.selectedUnitByType.get(unit.getArmylistUnit().getUnitType())).remove(unit);
        //return this.selectedUnits.remove(unit);
    }
    
    public boolean moveUnitUp(Unit unit){
        LinkedList typeList = (LinkedList)this.selectedUnitByType.get(unit.getArmylistUnit().getUnitType());
        int index = typeList.indexOf(unit);
        if(index > 0){
            typeList.remove(index);
            typeList.add(index - 1, unit);
            return true;
        }
        return false;
    }

    public boolean moveUnitDown(Unit unit){
        LinkedList typeList = (LinkedList)this.selectedUnitByType.get(unit.getArmylistUnit().getUnitType());
        int index = typeList.indexOf(unit);
        if(index < typeList.size()-1){
            typeList.remove(index);
            typeList.add(index + 1, unit);
            return true;
        }
        return false;
    }    
    
    public Collection getSelectedUnits(){
        LinkedList allUnits = new LinkedList();
        
        String[] types = this.armylistArmy.getGameSystem().getUnitTypeNames();
        for(int i = 0; i < types.length; ++i){
            allUnits.addAll((LinkedList)this.selectedUnitByType.get(types[i]));
        }       
        
        return allUnits;
        //return Collections.unmodifiableCollection(this.selectedUnits);
    }
    
    public double getPointcostTotal(){
        double cost = 0;
        Iterator iterator = this.getSelectedUnits().iterator();
        while(iterator.hasNext()){
            cost = cost + ((Unit)iterator.next()).getPointCost();
        }
        return cost;
    }

    public int getUnitCountForUnitType(String unitType){
        Iterator iterator = this.getSelectedUnits().iterator();
        int ret = 0;
        while(iterator.hasNext()){
            Unit unit = (Unit)iterator.next();
            if(unit.getArmylistUnit().getUnitType().equalsIgnoreCase(unitType))
                ++ret;
        }
        return ret;
    }
    
    public void setTargetPointcost(double cost){
        this.targetPointcost = cost;
    }
    
    public double getTargetPointcost(){
        return this.targetPointcost;
    }
}
