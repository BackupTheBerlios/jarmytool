/*
 * ArmylistWargearGroup.java
 *
 * Created on 16 January 2003, 00:04
 */

package org.jArmyTool.data.dataBeans.armylist;


import java.util.*;
import java.io.Serializable;
/**
 *
 * @author  pasi
 */
public class ArmylistWargearGroup implements Serializable{
    
    private String name;
    private LinkedList items;
    
    /** Creates a new instance of ArmylistWargearGroup */
    public ArmylistWargearGroup(String name) {
        this.name = name;
        this.items = new LinkedList();
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
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
    
    public void removeItem(ArmylistWargearItem item){
        this.items.remove(item);
    }
    
    public Collection getItems(){
        return Collections.unmodifiableCollection(this.items);
    }
    
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
