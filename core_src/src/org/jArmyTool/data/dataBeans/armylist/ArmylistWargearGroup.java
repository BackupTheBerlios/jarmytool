/*
 * ArmylistWargearGroup.java
 *
 * Created on 16 January 2003, 00:04
 */

package org.jArmyTool.data.dataBeans.armylist;


import java.util.*;
import java.io.Serializable;
/**
 * This class describes one Wargear group
 * @author Pasi Lehtimäki
 */
public class ArmylistWargearGroup implements Serializable{
    
    private String name;
    private LinkedList items;
    
    private LinkedList subGroups;
    
    /**
     * Creates a new instance of ArmylistWargearGroup
     * @param name Name of this group.
     * Please note that this is used as unique identifier.
     * Two groups with same name can cause some problems.
     */
    public ArmylistWargearGroup(String name) {
        this.name = name;
        this.items = new LinkedList();
        this.subGroups = new LinkedList();
    }
    
    /**
     * Change name
     * @param name New name.
     * Please note that this is used as unique identifier.
     * Two groups with same name can cause some problems.
     */    
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Get the name
     * @return Name of this group
     */    
    public String getName(){
        return this.name;
    }
    
    /**
     * Add new wargear item into this group
     * @param item The item to be added
     */    
    public void addItem(ArmylistWargearItem item){
        int index = 0;
        Iterator iterator = this.items.iterator();
        while(iterator.hasNext()){
            if( ((ArmylistWargearItem)iterator.next()).getName().compareTo(item.getName()) < 0 ){
                ++index;
            }else{
                break;
            }
        }
        this.items.add(index, item);
    }
    
    /**
     * Remove one item from this group
     * @param item Item to be removed
     */    
    public void removeItem(ArmylistWargearItem item){
        this.items.remove(item);
    }
    
    /**
     * Get all items in this group
     * @return Collection of ArmylistWargearItem objects.
     */    
    public Collection getItems(){
        return Collections.unmodifiableCollection(this.items);
    }
    
    /**
     * This methos searches all items of this group until it encounters
     * one by given name.
     * If no matching item is found it returns <I>null</I>
     * @return One item
     * @param name Name of item
     */    
    public ArmylistWargearItem getItemByName(String name){
        Iterator iterator = this.items.iterator();
        while(iterator.hasNext()){
            ArmylistWargearItem item = (ArmylistWargearItem)iterator.next();
            if(item.getName().equals(name))
                return item;
        }
        return null;
    }
    
    
}
