package org.jArmyTool.data.dataBeans.army;

/*
 * ModelUpdate.java
 *
 * Created on 04 December 2002, 21:17
 */

import java.util.*;
import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.data.util.statCalc;
import java.io.Serializable;
/**
 *
 * @author  pasi
 */
public class ModelUpdate implements Update, Serializable{
    
    private ArmylistModelUpdate armylistModelUpdate;
    private Model model;
    private String name;
    private int selectedCount;
    
    public statCalc statcalc;
   
    public ModelUpdate(ArmylistModelUpdate armylistModelUpdate, Model model){
        this.armylistModelUpdate = armylistModelUpdate;
        this.model = model;
        this.setSelectedCount(this.armylistModelUpdate.getDefaultCount());
        statcalc = null;
    }
    
    public ModelUpdate(ModelUpdate toClone){
        this.armylistModelUpdate = toClone.armylistModelUpdate;
        this.model = toClone.model;
        this.name = toClone.name;
        this.selectedCount = toClone.selectedCount;
        statcalc = toClone.statcalc;
    }
    
    public String getName(){
        if(this.name == null)
            return this.armylistModelUpdate.getName();
        return this.name;
    }   
    
    public ArmylistModelUpdate getArmylistModelUpdate(){
        return this.armylistModelUpdate;
    }
    
    
    public double getPointcost(){
        double cost = this.armylistModelUpdate.getPointcost();
        if(this.armylistModelUpdate.isPointcostPerModel()){
            return cost * this.selectedCount * this.model.getModelCount();
        }
        return cost * this.selectedCount;
    }
    
    public int getSelectedCount(){
        return this.selectedCount;
    }
    
    public boolean setSelectedCount(int count){
        if(count >= 0)
            this.selectedCount = count;
        if(this.armylistModelUpdate.getMinCount() <= this.selectedCount){
            if(this.armylistModelUpdate.getMaxCount() == -1 || this.armylistModelUpdate.getMaxCount() >= this.selectedCount)
                return true;
            return false;
        }
        return false;           
    }    
}
