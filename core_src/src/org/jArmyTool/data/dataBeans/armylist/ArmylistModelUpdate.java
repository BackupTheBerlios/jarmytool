package core_src.src.org.jArmyTool.data.dataBeans.armylist;
import java.util.*;
import java.io.Serializable;
/**
 *
 * @author  pasi
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

    
    /** Creates a new instance of ModelUpdate */
    public ArmylistModelUpdate(String name, ArmylistArmy army) {
        this.army = army;
        this.name = name;
        this.maxCount = -1;
        this.weapons = new LinkedList();
    }
    
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
    
    public void setPointcostPerModel(boolean perModel){
        this.pointcostPerModel = perModel;
    }
    
    public boolean isPointcostPerModel(){
        return this.pointcostPerModel;
    }
    
    public String getLongExplanation() {
        return this.longExplanation;
    }
    
    public void setPointcost(double cost){
        this.pointcost = cost;
    }
    
    public double getPointcost() {
        return this.pointcost;
    }
    
    public String getShortExplanation() {
        return this.shortExplanation;
    }
    
    public void addStatModification(String stat, int modification){
        this.statModifications.put(stat, new Integer(modification));
    }
    
    public Map getStatModifications(){
        return Collections.unmodifiableMap(this.statModifications);
    }
    
    public String getName() {
        return this.name;
    }   
    
    public void setName(String name){
        this.name = name;
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
            String temp = (String)iterator.next();
            if(this.army.getWeaponByName(temp) != null)
                ret.add(this.army.getWeaponByName(temp));
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
