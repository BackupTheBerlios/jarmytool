/*
 * ArmylistWargearItem.java
 *
 * Created on 16 January 2003, 00:02
 */

package org.jArmyTool.data.dataBeans.armylist;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author  pasi
 */
public class ArmylistWargearItem implements Serializable{
    
    private String name;
    private double pointcost;
    
    private LinkedList weapons;
    
    private ArmylistArmy army;
    
    /** Creates a new instance of ArmylistWargearItem */
    public ArmylistWargearItem(String name, ArmylistArmy army) {
        this.name = name;
        this.weapons = new LinkedList();
        this.army = army;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setPointcost(double cost){
        this.pointcost = cost;
    }
    
    public double getPointcost(){
        return this.pointcost;
    }
    
    public String toString(){
        return this.name;
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
 
    
    public void addWeapon(String weapon){
        this.weapons.add(weapon);
    }    
    
    public void addWeapon(ArmylistWeapon weapon){
        this.weapons.add(weapon.getName());
    }    
    
    public void emptyWeapons(){
        this.weapons.clear();
    }        
}
