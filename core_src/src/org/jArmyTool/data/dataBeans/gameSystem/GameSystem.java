/*
 * GameSystem.java
 *
 * Created on 01 July 2003, 13:05
 */

package org.jArmyTool.data.dataBeans.gameSystem;

import java.util.*;

import org.jArmyTool.data.dataBeans.util.*;
/**
 *
 * @author  pasi
 */
public class GameSystem {

    private String name;
    private String shortName;
    
    private HashMap statTypes;
    
    private LinkedList unitTypes;
    
    private LinkedList weaponProfiles;
    
    private double defaultTargetPointcost;
    
    /** Creates a new instance of GameSystem */
    public GameSystem(String name) {
        this.name = name;
        this.weaponProfiles = new LinkedList();
        this.statTypes = new HashMap();
        this.unitTypes = new LinkedList();
        this.weaponProfiles = new LinkedList();
    }
    
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getShortName(){
        return this.shortName;
    }
    
    public void setShortName(String shortName){
        this.shortName = shortName;
    }    
    
    public void addStatType(StatType type, String name){
        this.statTypes.put(name, type);
    }
    
    public StatType getStatType(String name){
        return (StatType)this.statTypes.get(name);
    }
    
    public Collection getStatTypeNames(){
        return this.statTypes.keySet();
    }    
    
    public void addUnitType(UnitType unitType){
        this.unitTypes.add(unitType);
    }
    
    public void removeUnitType(UnitType unitType){
        this.unitTypes.remove(unitTypes);
    }
    
    public String[] getUnitTypeNames(){
        String[] ret = new String[this.unitTypes.size()];
        Iterator iterator = this.unitTypes.iterator();
        int i = 0;
        while(iterator.hasNext()){
            ret[i] = ((UnitType)iterator.next()).getName();
            ++i;
        }
        return ret;
    }
    
    public UnitType getUnitTypeByName(String name){
        Iterator iterator = this.unitTypes.iterator();
        while(iterator.hasNext()){
            UnitType unitType = (UnitType)iterator.next();
            if( unitType.getName().equalsIgnoreCase(name) )
                return unitType;
        }
        return null;
    }
    
    public void emptyWeaponProfiles(){
        this.weaponProfiles = new LinkedList();
    }
    
    public void addWeaponProfile(WeaponProfile weaponProfile){
        this.weaponProfiles.add(weaponProfile);
    }
    
    public Collection getWeaponProfiles(){
        return Collections.unmodifiableCollection(this.weaponProfiles);
    }
    
    public WeaponProfile getWeaponProfile(String name){
        Iterator iterator = this.weaponProfiles.iterator();
        while(iterator.hasNext()){
            WeaponProfile profile = (WeaponProfile)iterator.next();
            if(name.compareToIgnoreCase(profile.getName()) == 0)
                return profile;
        }
        return null;
    }
    
    public void setDefaultTargetPointcost(double cost){
        this.defaultTargetPointcost = cost;
    }
    
    public double getDefaultTargetPointcost(){
        return this.defaultTargetPointcost;
    }
}
