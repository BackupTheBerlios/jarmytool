/*
 * Unit.java
 *
 * Created on 04 December 2002, 21:08
 */

package org.jArmyTool.data.dataBeans.army;

import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.data.factories.*;
import java.util.*;
import java.io.Serializable;

/**
 *
 * @author  pasi
 */
public class Unit implements Serializable {
    
    private org.jArmyTool.data.dataBeans.armylist.ArmylistUnit armylistUnit;
    
    private String name;
    
    private LinkedList models;
    private LinkedList updates;
    
    
    /** Creates a new instance of Unit */
    public Unit(ArmylistUnit armylistUnit) {
        this.armylistUnit = armylistUnit;
     
        this.models = new LinkedList();
        this.updates = new LinkedList();
        this.initModels();
        this.initUpdates();
        this.name = this.armylistUnit.getName();
    }
    
    public Unit(Unit toClone){
        this.name = toClone.name;
        this.armylistUnit = toClone.armylistUnit;
        
        this.models = new LinkedList();
        Iterator iterator = toClone.models.iterator();
        while(iterator.hasNext()){
            this.models.add(new Model((Model)iterator.next()));
        }
        
        this.updates = new LinkedList();
        iterator = toClone.updates.iterator();
        while(iterator.hasNext()){
            this.updates.add(new UnitUpdate( (UnitUpdate)iterator.next(), this) );
        }
        
    }
 
    private void initModels(){
        Iterator iterator = this.armylistUnit.getModels().iterator();
        Factory factory = Factory.getInstance();
        while(iterator.hasNext()){
            this.models.add(factory.createModel((ArmylistModel)iterator.next()));
        }
    }
    
    
    private void initUpdates(){
        //insert valid unit updates
        Iterator iterator = this.armylistUnit.getUpdates().iterator();
        Factory factory = Factory.getInstance();
        while(iterator.hasNext()){
            this.updates.add(factory.createUnitUpdate((ArmylistUnitUpdate)iterator.next(), this));
        }        
    }
    
    public ArmylistUnit getArmylistUnit(){
        return this.armylistUnit;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public double getPointCost(){
        double cost = 0;
        Iterator iterator = this.models.iterator();
        while(iterator.hasNext()){
            cost = cost + ((Model)iterator.next()).getPointcost();
        }
        iterator = this.updates.iterator();
        while(iterator.hasNext()){
            cost = cost + ((UnitUpdate)iterator.next()).getPointcost();
        }
        
        return cost;
    }
    
    
    public Collection getModels(){
        return Collections.unmodifiableCollection(this.models);
    }
    
    public Collection getUpdates(){
        return Collections.unmodifiableCollection(this.updates);
    }  
    
    public String toString(){
        return this.name;
    }
    
    public int getUnitSize(){
        int temp = 0;
        Iterator iterator = this.models.iterator();
        while(iterator.hasNext()){
            temp = temp + ((Model)iterator.next()).getModelCount();
        }
        return temp;
    }
    
    public int getCountedModels(){
        int temp = 0;
        Iterator iterator = this.models.iterator();
        while(iterator.hasNext()){
            Model model = (Model)iterator.next();
            if(model.getArmylistModel().isCounted())
                temp = temp + model.getModelCount();
        }
        return temp;        
    }
    
    
    public int getNonCountedModels(){
        int temp = 0;
        Iterator iterator = this.models.iterator();
        while(iterator.hasNext()){
            Model model = (Model)iterator.next();
            if(!model.getArmylistModel().isCounted())
                temp = temp + model.getModelCount();
        }
        return temp;        
    }    
}
