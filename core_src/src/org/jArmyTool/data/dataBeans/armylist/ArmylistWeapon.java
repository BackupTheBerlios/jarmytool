/*
 * ArmylistWeapon.java
 *
 * Created on 14 October 2003, 16:36
 */

package org.jArmyTool.data.dataBeans.armylist;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import org.jArmyTool.data.dataBeans.util.WeaponProfile;
/**
 *
 * @author  pasleh
 */
public class ArmylistWeapon {
    private String name;
    private LinkedList stats;
    private WeaponProfile profile;
    
    /** Creates a new instance of ArmylistWeapon */
    public ArmylistWeapon(String name, WeaponProfile profile) {
        this.name = name;
        this.profile = profile;
        this.stats = new LinkedList();
    }
    
    public String getName(){
        return this.name;
    }
    
    public Collection getStats(){
        return Collections.unmodifiableCollection(this.stats);
    }
    
    public void addStat(String stat){
        this.stats.add(stat);
    }
    
    public WeaponProfile getProfile(){
        return this.profile;
    }
    
    public String toString(){
        return this.name;
    }
    
}
