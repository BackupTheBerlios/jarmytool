/*
 * UnitUpdate.java
 *
 * Created on 04 December 2002, 21:17
 */

package core_src.src.org.jArmyTool.data.dataBeans.army;

import org.jArmyTool.data.dataBeans.armylist.*;
import java.io.Serializable;
/**
 *
 * @author  pasi
 */
public class UnitUpdate implements Update, Serializable{
    
    private ArmylistUnitUpdate armylistUnitUpdate;
    private String name;
    private int selectedCount;
    private Unit unit;
    
    /** Creates a new instance of UnitUpdate */
    public UnitUpdate(ArmylistUnitUpdate armylistUnitUpdate, Unit unit) {
        this.armylistUnitUpdate = armylistUnitUpdate;
        this.setSelectedCount(this.armylistUnitUpdate.getDefaultCount());
        this.unit = unit;
    }
  
    public UnitUpdate(UnitUpdate toClone, Unit parent){
        this.armylistUnitUpdate = toClone.armylistUnitUpdate;
        this.name = toClone.name;
        this.selectedCount = toClone.selectedCount;
        this.unit = parent;
    }
    
    public ArmylistUnitUpdate getArmylistUnitUpdate(){
        return this.armylistUnitUpdate;
    }
    
    public double getPointcost(){
        
        double cost = this.armylistUnitUpdate.getPointcost();
        
        if(this.armylistUnitUpdate.isPointcostPerModel()){
            return cost * this.unit.getCountedModels() * this.selectedCount;
        }      
        
        return cost * this.selectedCount;
    }
    
    public String getName(){
        if(this.name == null)
            return this.armylistUnitUpdate.getName();
        return this.name;
    }
    
    public int getSelectedCount(){
        return this.selectedCount;
    }
    
    public boolean setSelectedCount(int count){
        if(count >= 0)
            this.selectedCount = count;
        if(this.armylistUnitUpdate.getMinCount() <= this.selectedCount){
            if(this.armylistUnitUpdate.getMaxCount() == -1 || this.armylistUnitUpdate.getMaxCount() >= this.selectedCount)
                return true;
            return false;
        }
        return false;           
    }
    
}
