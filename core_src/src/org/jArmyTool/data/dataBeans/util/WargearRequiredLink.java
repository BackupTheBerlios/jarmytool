/*
 * WargearRequiredLink.java
 *
 * Created on 12 February 2004, 11:59
 */

package org.jArmyTool.data.dataBeans.util;

import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearGroup;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearItem;

/**
 * This clas is a helper class to describe required relationshoip between wargear
 * items/groups and wargear items
 * @author Pasi Lehtimäki
 */
public class WargearRequiredLink {
    
    private ArmylistWargearGroup group;
    private ArmylistWargearItem item;
    
    /**
     * Creates a new instance of WargearRequiredLink
     * @param group Group where the required item is located
     * @param item The required item
     */
    public WargearRequiredLink(ArmylistWargearGroup group, ArmylistWargearItem item) {
        this.item = item;
        this.group = group;
    }
    
    /**
     * Group where the required item is located
     * @return Required item's group
     */    
    public ArmylistWargearGroup getGroup(){
        return this.group;
    }
    
    
    /**
     * Required item
     * @return the required item
     */    
    public ArmylistWargearItem getItem(){
        return this.item;
    }
}
