/*
 * Model.java
 *
 * Created on 04 December 2002, 21:16
 */

package org.jArmyTool.data.dataBeans.armylist;


import java.util.*;
import java.io.Serializable;
import org.jArmyTool.data.dataBeans.util.*;
/**
 *
 * @author  pasi
 */
public class ArmylistModel implements Serializable{
    private String name;

    private LinkedList updates;
    private int maxCount;
    private int minCount;
    private double pointcostPerModel;
    private double allowedWargear;
    private LinkedList allowedWargearGroups;
    private boolean isLinked;
   
    
    //reference to armylistArmy's stat types
    private String statTypeName;
    
    private LinkedList stats;
    
    private ArmylistArmy armylistArmy;
    
    private boolean isCounted = true;
  
    private int defaultSelectedAmount;
    
    /** Creates a new instance of Model */
    public ArmylistModel(String name, ArmylistArmy army) {
        this.armylistArmy = army;
        
        this.name = name;

        this.updates = new LinkedList();
        this.stats = new LinkedList();
        this.maxCount = -1;
        this.allowedWargearGroups = new LinkedList();
    }
    
    public ArmylistModel(ArmylistModel toClone){
        this.armylistArmy = toClone.armylistArmy;
        this.isCounted = toClone.isCounted;
        this.allowedWargear = toClone.allowedWargear;
        this.pointcostPerModel = toClone.pointcostPerModel;
        this.defaultSelectedAmount = toClone.defaultSelectedAmount;
        this.maxCount = toClone.maxCount;
        this.minCount = toClone.minCount;
        this.name = toClone.name;
        this.statTypeName = toClone.statTypeName;
        if(toClone.allowedWargearGroups != null)
            this.allowedWargearGroups = (LinkedList)toClone.allowedWargearGroups.clone();
        
        if(toClone.stats != null){
            Iterator iterator = toClone.stats.iterator();
            LinkedList list = new LinkedList();
            while(iterator.hasNext()){
                list.add(new ModelStatHolder((ModelStatHolder)iterator.next()));
            }
            this.stats = list;
        }
        
        if(toClone.updates != null){
            Iterator iterator = toClone.updates.iterator();
            LinkedList list = new LinkedList();
            while(iterator.hasNext()){
                list.add( new Integer( this.armylistArmy.mapModelUpdate(new ArmylistModelUpdate(toClone.armylistArmy.getModelUpdate(((Integer)iterator.next()).intValue())))) );            
            }
            this.updates = list;
        }
        
        
    }
    
    public void setIsCounted(boolean isCounted){
        this.isCounted = isCounted;
    }
    
    public boolean isCounted(){
        return this.isCounted;
    }
    
    public void setStatType(String statTypeName){
        if(statTypeName == null)
            return;
        this.statTypeName = statTypeName;
        this.stats.clear();
        if(this.armylistArmy.getGameSystem().getStatType(statTypeName) == null){
            //log!! something is wron with the armylist
            return;
        }
        Iterator iterator = this.armylistArmy.getGameSystem().getStatType(statTypeName).getAllStats().iterator();
        while(iterator.hasNext()){
            ModelStat stat = (ModelStat)iterator.next();
            this.stats.add(new ModelStatHolder(stat));
        }
    }
    
    public void setStatValues(String values, String separator){
        Iterator iterator = this.stats.iterator();
        StringTokenizer sto = new StringTokenizer(values, separator);
        while(iterator.hasNext()){
            ModelStatHolder holder = (ModelStatHolder)iterator.next();
            holder.setValue(sto.nextToken());
        }
    }
    
    public String getStatValues(String separator){
        if(this.stats == null)
            return "";
        
        String ret = "";
        Iterator iterator = this.stats.iterator();
        while(iterator.hasNext()){
            ModelStatHolder holder = (ModelStatHolder)iterator.next();
            if(ret.length() > 0)
                ret = ret  + separator;
            ret = ret + holder.getValue();
        }
        
        return ret;
    }
    
    public Collection getStats(){
        return Collections.unmodifiableCollection(this.stats);
    }
    
    
    public String getStatTypeName(){
        return this.statTypeName;
    }
    
    public ArmylistArmy getArmylistArmy(){
        return this.armylistArmy;
    }
    
    public void setAllowedWargear(double amount){
        this.allowedWargear = amount;
    }   
    
    public double getAllowedWargear(){
        return this.allowedWargear;
    }
    
    public void addWargearGroup(String name){
        this.allowedWargearGroups.add(name);
    }
    
    public Collection getAllowedWargearGroups(){
        return Collections.unmodifiableCollection(this.allowedWargearGroups);
    }
    
    public void emptyWargearGroups(){
        this.allowedWargearGroups.clear();
    }
    
    public double getPointCostPerModel(){
        return this.pointcostPerModel;
    }   
    
    public void setPointCostPerModel(double cost){
        this.pointcostPerModel = cost;
    }
    
    public int getMaxCount(){
        return this.maxCount;
    }
    
    public void setMaxCount(int count){
        this.maxCount = count;
    }

    public int getMinCount(){
        return this.minCount;
    }
    
    public void setMinCount(int count){
        this.minCount = count;
    }    
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }

    
    public void addUpdate(int update){
        this.updates.add(new Integer(update));
    }
    
    public void removeUpdate(int update){
        this.updates.remove(new Integer(update));
    }
    
    public Collection getUpdateIds(){
        return Collections.unmodifiableCollection(this.updates);
    }

    public Collection getUpdates(){
        LinkedList list = new LinkedList();
        Iterator iterator = this.getUpdateIds().iterator();
        while(iterator.hasNext()){
            list.add(this.armylistArmy.getModelUpdate( ((Integer)iterator.next()).intValue()));
        }
        
        return list;         
    }
    
    public void emptyUpdates(){
        this.updates.clear();
    }
    
    public int getDefaultSelectedAmount(){
        return this.defaultSelectedAmount;
    }
    
    public void setDefaultSelectedAmount(int count){
        this.defaultSelectedAmount = count;
    }    
    
    public void setLinked(boolean linked){
        this.isLinked = linked;
    }
    
    public boolean isLinked(){
        return this.isLinked;
    }
    
    
    public boolean moveUpdateDown(ArmylistModelUpdate update){
        Integer id = new Integer(this.armylistArmy.getIdForModelUpdate(update));
        int index = this.updates.indexOf(id);
        if( index < this.updates.size()-1 && index > -1){
            this.updates.remove(index);
            this.updates.add(index + 1, id);
            return true;
        }
        return false;
    }      
    
    public boolean moveUpdateUp(ArmylistModelUpdate update){
        Integer id = new Integer(this.armylistArmy.getIdForModelUpdate(update));
        int index = this.updates.indexOf(id);
        if( index > 0 ){
            this.updates.remove(index);
            this.updates.add(index - 1, id);
            return true;
        }
        return false;
    }    
}
