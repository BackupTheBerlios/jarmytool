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
 * This class describes one model of the armylist
 * @author pasi
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
    
    /**
     * Creates a new instance of ArmylistModel.
     * Once new model is created it should be inserted into
     * the armylist by calling ArmylistArmy.mapModel().
     * @param name Name of the model
     * @param army in which armylist this model is in.
     */
    public ArmylistModel(String name, ArmylistArmy army) {
        this.armylistArmy = army;
        
        this.name = name;

        this.updates = new LinkedList();
        this.stats = new LinkedList();
        this.maxCount = -1;
        this.allowedWargearGroups = new LinkedList();
    }
    
    /**
     * Clones model
     *
     * !This contructor does NOT work.
     * @param toClone model from which data is copied
     */    
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
    
    /**
     * If model is counted it is taken into account while counting points for
     * model/unit updates
     * which are set to be "per model".
     * Also when counting unit size only counted models are
     * counted.
     *
     * ie. Transports should be set as not counted.
     * @param isCounted set to true if counted
     * set to false if not counted
     */    
    public void setIsCounted(boolean isCounted){
        this.isCounted = isCounted;
    }
    
    /**
     * See if this model is counted
     * @return true if counted. false if not counted
     */    
    public boolean isCounted(){
        return this.isCounted;
    }
    
    /**
     * Set stat type of this model.
     * @param statTypeName The name of stat type. This name should be one of
     * the stat types found in game system used by the list of this model.
     */    
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
    
    /**
     * Used to set stat values.
     * @param values a string containing all values separaed by separator
     * @param separator separator string which separates individual stat values
     */    
    public void setStatValues(String values, String separator){
        Iterator iterator = this.stats.iterator();
        StringTokenizer sto = new StringTokenizer(values, separator);
        while(iterator.hasNext()){
            ModelStatHolder holder = (ModelStatHolder)iterator.next();
            holder.setValue(sto.nextToken());
        }
    }
    
    /**
     * Get alla stat values
     * @param separator separator used to separate individual stat vaules in returned string
     * @return all stat vaules separatd by the separator
     */    
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
    
    /**
     * Get all stats
     * @return Collection containing ModelStatHolder objects
     */    
    public Collection getStats(){
        return Collections.unmodifiableCollection(this.stats);
    }
    
    
    /**
     * get stat type name of this model
     * @return name of the stat type used in this model
     */    
    public String getStatTypeName(){
        return this.statTypeName;
    }
    
    /**
     * get armylist used in this model
     * @return armylist used in this model
     */    
    public ArmylistArmy getArmylistArmy(){
        return this.armylistArmy;
    }
    
    /**
     * set amount of allowed wargear total
     * @param amount total amount of allowed wargear. -1 indicates unlimited amount.
     */    
    public void setAllowedWargear(double amount){
        this.allowedWargear = amount;
    }   
    
    /**
     * get allowed amount wargear in total
     * @return allowed amount of wargear total. -1 indicates unlimited amount
     */    
    public double getAllowedWargear(){
        return this.allowedWargear;
    }
    
    /**
     * Add allowed root wargear group into this model.
     * @param name Name of wargear group defined in armylist
     */    
    public void addWargearGroup(String name){
        this.allowedWargearGroups.add(name);
    }
    
    /**
     * get all allowed root wargear groups
     * @return Collection containing String objects of root wargear group names
     */    
    public Collection getAllowedWargearGroups(){
        return Collections.unmodifiableCollection(this.allowedWargearGroups);
    }
    
    /**
     * empties all allowed wargear groups
     */    
    public void emptyWargearGroups(){
        this.allowedWargearGroups.clear();
    }
    
    /**
     * pointcost of one model of this type
     * @return pointcost of one model
     */    
    public double getPointCostPerModel(){
        return this.pointcostPerModel;
    }   
    
    /**
     * set pointcost of one model
     * @param cost new pointcost of one model
     */    
    public void setPointCostPerModel(double cost){
        this.pointcostPerModel = cost;
    }
    
    /**
     * Maximum amount of models of this type.
     * -1 indicates unlimited amout
     * @return max amount of models
     */    
    public int getMaxCount(){
        return this.maxCount;
    }
    
    /**
     * Set maximum amount of models of this type.
     * If this value is 1 main UI shows checkbox instead of spinner.
     * -1 indicates unlimited amount
     * @param count new count
     */    
    public void setMaxCount(int count){
        this.maxCount = count;
    }

    /**
     * Minimum amount of models
     * @return min count
     */    
    public int getMinCount(){
        return this.minCount;
    }
    
    /**
     * Set minimum amount of models. If this is set to 1 and max count is also 1
     * then no selector is shown in main UI.
     * Please note that default count is used to initialize model amout. If min and max
     * is
     * set to 1 but default is != 1 then information presented to end user can be
     * false.
     * @param count min count
     */    
    public void setMinCount(int count){
        this.minCount = count;
    }    
    
    /**
     * change name of this model
     * @param name new name
     */    
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * name of this model
     * @return name
     */    
    public String getName(){
        return this.name;
    }

    
    /**
     * add model update to this model
     * @param update ID of model update
     */    
    public void addUpdate(int update){
        this.updates.add(new Integer(update));
    }
    
    /**
     * remove model update from this model
     * @param update update to remove
     */    
    public void removeUpdate(int update){
        this.updates.remove(new Integer(update));
    }
    
    /**
     * get model update ids
     * @return Collection of Integer objects presenting all model update IDs
     */    
    public Collection getUpdateIds(){
        return Collections.unmodifiableCollection(this.updates);
    }

    /**
     * get actual model updates
     * @return Coillection of ArmylistModelUpdate objcets
     */    
    public Collection getUpdates(){
        LinkedList list = new LinkedList();
        Iterator iterator = this.getUpdateIds().iterator();
        while(iterator.hasNext()){
            list.add(this.armylistArmy.getModelUpdate( ((Integer)iterator.next()).intValue()));
        }
        
        return list;         
    }
    
    /**
     * clears all model updates
     */    
    public void emptyUpdates(){
        this.updates.clear();
    }
    
    /**
     * delault amount of models.
     * @return delault selected amount
     */    
    public int getDefaultSelectedAmount(){
        return this.defaultSelectedAmount;
    }
    
    /**
     * Set default selected amount.
     * This value is used when ne Model is constructed to initialize selected amount.
     * @param count default count
     */    
    public void setDefaultSelectedAmount(int count){
        this.defaultSelectedAmount = count;
    }    
    
    /**
     * This method can set linked flag on/off. If linked is true
     * then editor knows that this same model is used in
     * multible units and modifying this vill effect all those units.
     * This information is only relevant when contructing UI. This does not
     * effect the internal data at all.
     * @param linked new value. True if linked. False if not
     */    
    public void setLinked(boolean linked){
        this.isLinked = linked;
    }
    
    /**
     * get linked value
     * @return true if linked. False if not
     */    
    public boolean isLinked(){
        return this.isLinked;
    }
    
    
    /**
     * This method is used to move model update downwards
     * in this model.
     * @param update update to move
     * @return true if update was succesfully moved. false if no (ie. allready at bottom).
     */    
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
    
    /**
     * This method is used to move model update upwards
     * in this model.
     * @param update update to move
     * @return true if update was succesfully moved. false if no (ie. allready at top).
     */    
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
