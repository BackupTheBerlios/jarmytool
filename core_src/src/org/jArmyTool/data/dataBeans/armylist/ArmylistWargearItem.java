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
 * This class describes a single Wargear item
 * @author Pasi Lehtimäki
 */
public class ArmylistWargearItem implements Serializable{
    
    private String name;
    private double pointcost;
    
    private LinkedList weapons;
    
    private ArmylistArmy army;
    
    /**
     * Creates a new instance of ArmylistWargearItem
     * @param name Name of this itm. Please note that wargear item's name
     * is used as unique identifier in it's group. If you have
     * two items with same name in same group you might encounter
     * some problems.
     * @param army The armylist army this item is in
     */
    public ArmylistWargearItem(String name, ArmylistArmy army) {
        this.name = name;
        this.weapons = new LinkedList();
        this.army = army;
    }
    
    /**
     * Changes the name
     * @param name New name of this item.
     * Please note that wargear item's name
     * is used as unique identifier in it's group. If you have
     * two items with same name in same group you might encounter
     * some problems.
     */    
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Get the name
     * @return Name
     */    
    public String getName(){
        return this.name;
    }
    
    /**
     * Set the pointcost
     * @param cost Pointcost of this item
     */    
    public void setPointcost(double cost){
        this.pointcost = cost;
    }
    
    /**
     * Get the pointcost
     * @return Pointcost
     */    
    public double getPointcost(){
        return this.pointcost;
    }
    
    /**
     * String presentation of this item (name)
     * @return Name of this item
     */    
    public String toString(){
        return this.name;
    }
    
    
    
    /**
     * This method can be used to check if this item is a weapon
     * @return true if this item is a weapon
     */    
    public boolean isWeapon(){    
        if(this.weapons.size() > 0)
            return true;
        return false;
    }
    
    /**
     * If this item is weapon(s) use this method to get them
     * @return Collection of Strings. Each String in the collection
     * is weapon name.
     */    
    public Collection getWeapons(){
        LinkedList ret = new LinkedList();
        Iterator iterator = this.weapons.iterator();
        while(iterator.hasNext()){
            ret.add(this.army.getWeaponByName((String)iterator.next()));
        }
        
        return ret;        
    }
 
    
    /**
     * This method adds one weapon to this item
     * @param weapon Name of the weapon to be added
     */    
    public void addWeapon(String weapon){
        this.weapons.add(weapon);
    }    
    
    /**
     * Alternate method to add weapon
     * @param weapon The weapon to be added. Only the name is stored.
     */    
    public void addWeapon(ArmylistWeapon weapon){
        this.weapons.add(weapon.getName());
    }    
    
    /**
     * Use this method to clear all weapons.
     */    
    public void emptyWeapons(){
        this.weapons.clear();
    }        
}
