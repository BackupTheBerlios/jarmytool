/*
 * UnitUpdate.java
 *
 * Created on 04 December 2002, 21:17
 */

package core_src.src.org.jArmyTool.data.dataBeans.armylist;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author  pasi
 */
public class ArmylistUnitUpdate implements Serializable{
    
    private String shortExplanation;
    private String name;
    private int minCount;
    private int maxCount;
    private double pointcost;
    private boolean pointcostPerModel = false;
    private int defaultCount;
    private ArmylistArmy army;
    
    private LinkedList weapons;
    
    /** Creates a new instance of UnitUpdate */
    public ArmylistUnitUpdate(String name, ArmylistArmy army) {
        this.army = army;
        this.name = name;
        this.maxCount = -1;
        this.minCount = 0;
        this.weapons = new LinkedList();
    }
    
    public ArmylistUnitUpdate(ArmylistUnitUpdate toClone){
        this.army = toClone.army;
        this.name = toClone.name;
        this.shortExplanation = toClone.shortExplanation;
        this.pointcostPerModel = toClone.pointcostPerModel;
        this.pointcost = toClone.pointcost;
        this.maxCount = toClone.maxCount;
        this.minCount = toClone.minCount;
        this.defaultCount = toClone.defaultCount;
        this.weapons = (LinkedList)toClone.weapons.clone();
    }
   

    public void setPointcostPerModel(boolean perModel){
        this.pointcostPerModel = perModel;
    }
    
    public boolean isPointcostPerModel(){
        return this.pointcostPerModel;
    }    
    
    public void setPointcost(double cost){
        this.pointcost = cost;
    }
    
    public double getPointcost(){
        return this.pointcost;
    }
    
    public void setMaxCount(int count){
        this.maxCount = count;
    }
    
    public int getMaxCount(){
        return this.maxCount;
    }
    
    public void setMinCount(int count){
        this.minCount = count;
    }
    
    public int getMinCount(){
        return this.minCount;
    }
    
    public String getLongExplanation() {
        return null;
    }
    
    public String getShortExplanation() {
        return null;
    }
    
    public void setSortExplanation(String explanation){
        this.shortExplanation = explanation;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setDefaultCount(int count){
        this.defaultCount = count;
    }
    
    public int getDefaultCount(){
        return this.defaultCount;
    }
    
    public boolean isWeapon(){    
        if(this.weapons.size() > 0)
            return true;
        return false;
    }
    
    public Collection getWeapons(){
        LinkedList ret = new LinkedList();
        Iterator iterator = this.weapons.iterator();
        while(iterator.hasNext()){
            ret.add(this.army.getWeaponByName((String)iterator.next()));
        }
        
        return ret;
    }
    
    public void addWeapon(ArmylistWeapon weapon){
        this.weapons.add(weapon.getName());
    }    
    
    public void addWeapon(String weapon){
        this.weapons.add(weapon);
    }        
    
    public void emptyWeapons(){
        this.weapons.clear();
    }    
}
