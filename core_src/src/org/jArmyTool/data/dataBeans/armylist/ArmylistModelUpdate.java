package org.jArmyTool.data.dataBeans.armylist;
import java.util.*;
import java.io.Serializable;
/**
 * This class describes
 * @author Pasi Lehtim?ki
 */
public class ArmylistModelUpdate implements Serializable{
    
    private String name;
    private String shortExplanation;
    private String longExplanation;
    private double pointcost;
    private HashMap statModifications;
    private int maxCount;
    private int minCount;
    private int defaultCount;
    private boolean pointcostPerModel = false;
    
    private LinkedList weapons;
    
    private ArmylistArmy army;

    
    /**
     * Creates a new instance of ModelUpdate
     * @param name Name of this update
     * @param army list this update is in
     */
    public ArmylistModelUpdate(String name, ArmylistArmy army) {
        this.army = army;
        this.name = name;
        this.maxCount = -1;
        this.weapons = new LinkedList();
        this.statModifications = new HashMap();
    }
    
    /**
     * Clones model update.
     * ! This contructor does NOT work
     * @param toClone model update from which to copy the data
     */    
    public ArmylistModelUpdate(ArmylistModelUpdate toClone) {
        this.pointcostPerModel = toClone.pointcostPerModel;
        this.pointcost = toClone.pointcost;
        this.maxCount = toClone.maxCount;
        this.minCount = toClone.minCount;
        this.defaultCount = toClone.defaultCount;
        this.longExplanation = toClone.longExplanation;
        this.name = toClone.name;
        this.shortExplanation = toClone.shortExplanation;
        this.army = toClone.army;
        
        //NOTE! this does not clone the actual modifications!
        if(toClone.statModifications != null)
            this.statModifications = (HashMap)toClone.statModifications.clone();
        
        this.weapons = (LinkedList)toClone.weapons.clone();
    }
    
    /**
     * Is the pointcost per model.
     * If true the poincost is multiplied by counted models amount.
     * @param perModel true if per model. False if not
     */    
    public void setPointcostPerModel(boolean perModel){
        this.pointcostPerModel = perModel;
    }
    
    /**
     * Is the pointcost per model.
     * If true the poincost is multiplied by counted models amount.
     * @return true if per model. False if not
     */    
    public boolean isPointcostPerModel(){
        return this.pointcostPerModel;
    }
    
    /**
     * not used
     * @return not used
     */    
    public String getLongExplanation() {
        return this.longExplanation;
    }
    
    /**
     * Pointcost of one update
     * @param cost cost of one update
     */    
    public void setPointcost(double cost){
        this.pointcost = cost;
    }
    
    /**
     * get pointcost of one model
     * @return pointcost of one model
     */    
    public double getPointcost() {
        return this.pointcost;
    }
    
    /**
     * not used
     * @return not used
     */    
    public String getShortExplanation() {
        return this.shortExplanation;
    }
    
    /**
     * not working yet
     * @param stat not working yet
     * @param modification not working yet
     */    
    public void addStatModification(String stat, String modification){
        this.statModifications.put(stat, modification);
    }
    
    public void addStatModification(Map map)
    {
            this.statModifications.putAll(map);
    }
    
    /**
     * not working yet
     * @return not working yet
     */    
    public Map getStatModifications(){
        return Collections.unmodifiableMap(this.statModifications);
    }
    
    /**
     * name of this update
     * @return name
     */    
    public String getName() {
        return this.name;
    }   
    
    /**
     * change name of this update
     * @param name new name
     */    
    public void setName(String name){
        this.name = name;
    }

    /**
     * maximum count of these updates.
     * If max count is 1 then checkbox is presendet in main UI instead
     * of spinner.
     * -1 indicates unlimited amount
     * @param count new max count
     */    
    public void setMaxCount(int count){
        this.maxCount = count;
    }
    
    /**
     * get maximum amount
     * -1 indicates unlimited amount
     * @return max count
     */    
    public int getMaxCount(){
        return this.maxCount;
    }
    
    /**
     * Minimum amount of updates.
     * If max and min is set to 1 no selector is presented in main UI.
     * @param count new min count
     */    
    public void setMinCount(int count){
        this.minCount = count;
    }
    
    /**
     * minimum amount
     * @return min count
     */    
    public int getMinCount(){
        return this.minCount;
    }
    
    /**
     * Used to set default count. This value is used to initialize
     * ModelUpdate.
     * Note that if min and max count is se to 1 and default is != 1
     * then main UI will show false info to end user.
     * @param count new default count
     */    
    public void setDefaultCount(int count){
        this.defaultCount = count;
    }
    
    /**
     * default count
     * @return default count
     */    
    public int getDefaultCount(){
        return this.defaultCount;
    }
    
    
    /**
     * is this update a weapon
     * @return true if this update is weapon(s).
     * False if not
     */    
    public boolean isWeapon(){
        if(this.weapons.size() > 0)
            return true;
        return false;
    }
    
    /**
     * Get weapons in this update
     * @return Collection of String objects presenting names oif weapons
     */    
    public Collection getWeapons(){
        LinkedList ret = new LinkedList();
        Iterator iterator = this.weapons.iterator();
        while(iterator.hasNext()){
            String temp = (String)iterator.next();
            if(this.army.getWeaponByName(temp) != null)
                ret.add(this.army.getWeaponByName(temp));
        }
        
        return ret;
    }
    
    /**
     * Add weapon to this update
     * @param weapon Name of weapon is taken from this and stored into this update
     */    
    public void addWeapon(ArmylistWeapon weapon){
        this.weapons.add(weapon.getName());
    }

    
    /**
     * Add weapon to this update
     * @param weapon Name of weapon  to be added
     */    
    public void addWeapon(String weapon){
        this.weapons.add(weapon);
    }
    
    /**
     * empties all weapons in this update
     */    
    public void emptyWeapons(){
        this.weapons.clear();
    }
   
}
