package org.jArmyTool.data.dataBeans.armylist;


import java.util.*;
import java.io.Serializable;
/**
 *
 * @author  pasi
 */
public class ArmylistUnit implements Serializable{
    
    private String unitType;
    private String unitCategory;
    private int maxCount;
    private int minCount;
    
    private int maxUnitSize = -1;
    private int minUnitSize;
    
    private String name;
    private String notes;
    
    private LinkedList updates;
    private LinkedList models;
    
    private HashMap exclusiveModels;
    private HashMap inclusiveModels;
    
    private ArmylistArmy armylistArmy;

    /** Creates a new instance of Unit */
    public ArmylistUnit(String name, ArmylistArmy armylistArmy) {
        this.armylistArmy = armylistArmy;
        this.exclusiveModels = new HashMap();
        this.inclusiveModels = new HashMap();
        this.updates = new LinkedList();
        this.models = new LinkedList();
        this.name = name;
        this.maxCount = -1;
        String[] allUnitTypes = this.armylistArmy.getGameSystem().getUnitTypeNames();
        if(allUnitTypes.length > 0)
            this.unitType = allUnitTypes[1];
    }
    
    /**
     * clones a ArmylistUnit
     */
    public ArmylistUnit(ArmylistUnit unitToClone){
        this.name = unitToClone.getName();
        this.maxCount = unitToClone.getMaxCount();
        this.minCount = unitToClone.getMinCount();
        this.unitCategory = unitToClone.getUnitCategory();
        this.unitType = unitToClone.getUnitType();
        this.armylistArmy = unitToClone.getArmylistArmy();
        
        Iterator iterator = unitToClone.models.iterator();
        this.models = new LinkedList();
        while(iterator.hasNext()){
            this.models.add(new Integer(this.armylistArmy.mapModel(new ArmylistModel( unitToClone.getArmylistArmy().getModel(((Integer)iterator.next()).intValue()) ))));
        }
        
        iterator = unitToClone.updates.iterator();
        this.updates = new LinkedList();
        while(iterator.hasNext()){
            this.updates.add(new Integer(this.armylistArmy.mapUnitUpdate(new ArmylistUnitUpdate( unitToClone.getArmylistArmy().getUnitUpdate( ((Integer)iterator.next()).intValue() )))));
        }
        this.notes = unitToClone.notes;
    }
    
    public int getMinCount(){
        return this.minCount;
    }   
    
    public void setMinCount(int count){
        this.minCount = count;
    }
    
    public int getMaxCount(){
        return this.maxCount;
    }   
    
    public void setMaxCount(int count){
        this.maxCount = count;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setUnitType(String type){
        if(this.unitType != null){
            String from = this.unitType;
            this.unitType = type;
            this.armylistArmy.unitTypeChanged(from, this);
        }else{
            this.unitType = type;
        }
    }
    
    public String getUnitType(){
        return this.unitType;
    }
    
    public void setUnitCategory(String category){
        this.unitCategory = category;
    }
    
    public String getUnitCategory(){
        return this.unitCategory;
    }
    
     public void addUpdate(int unitUpdate){
        this.updates.add(new Integer(unitUpdate));
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
            list.add(this.armylistArmy.getUnitUpdate( ((Integer)iterator.next()).intValue()));
        }
        
        return list;
    }    
    
    public void emptyUpdates(){
        this.updates.clear();
    }
    
    public void addModel(int model){
        this.models.add(new Integer(model));
    }
    
    public void removeModel(int model){
        this.models.remove(new Integer(model));
    }
    /*
     * @return Collection of Integers
     */
    public Collection getModelIds(){
        return Collections.unmodifiableCollection(this.models);
    }

    public Collection getModels(){
        LinkedList list = new LinkedList();
        Iterator iterator = this.getModelIds().iterator();
        while(iterator.hasNext()){
            list.add(this.armylistArmy.getModel( ((Integer)iterator.next()).intValue()));
        }
        
        return list;        
    }    
    
    public void emptyModels(){
        this.models.clear();
    }
    
    public String toString(){
        return this.name;
    }
    
    public ArmylistArmy getArmylistArmy(){
        return this.armylistArmy;
    }
    
    public void addExclusiveModelForModel(int model, int exclusive){
        LinkedList list = (LinkedList)this.exclusiveModels.get(new Integer(model));
        if(list == null){
            list = new LinkedList();
            this.exclusiveModels.put(new Integer(model), list);
        }
        
        list.add(new Integer(exclusive));
    }
    
    
    /**
     * @return Collection of Integer objects. Those Integers are model IDs. Null if model has no exclusives (this is the normal state).
     */
    public Collection getExclusivesForModel(int model){
        return (Collection)this.exclusiveModels.get(new Integer(model));
    }
    
    public void crearExclusiveModels(){
        this.exclusiveModels.clear();
    }
    
    public void updateExclusivesForModel(int model, Collection exclusives){
        this.exclusiveModels.put(new Integer(model), exclusives);
    }
    
    /**
     * @return Collectin of Collections of Integer Ids.
     */
/*    public Collection getExclusiveModelGroups(){
        Iterator iterator = this.exclusiveModels.keySet().iterator();
        LinkedList ret = new LinkedList();
        while(iterator.hasNext()){
            Integer id = (Integer)iterator.next();
            LinkedList oneList = (LinkedList)this.exclusiveModels.get(id);
            oneList.add(id);
            Iterator retIterator = ret.iterator();
            boolean already = false;
            while(retIterator.hasNext()){
                LinkedList retList = (LinkedList)retIterator.next();
                if(retList.containsAll(oneList))
                    already = true;
            }
            if(!already)
                ret.add(oneList);
            
        }
        return ret;
    }*/
    
    public HashMap getExclusivesHashMap(){
        return this.exclusiveModels;
    }
    
    
    
    public void addInclusiveModelForModel(int model, int inclusive){
        LinkedList list = (LinkedList)this.inclusiveModels.get(new Integer(model));
        if(list == null){
            list = new LinkedList();
            this.inclusiveModels.put(new Integer(model), list);
        }
        
        list.add(new Integer(inclusive));
    }
    
  
    /**
     * @return Collection of Integer objects. Those Integers are model IDs. Null if model has no inclusives (this is the normal state).
     */
    public Collection getInclusivesForModel(int model){
        return (Collection)this.inclusiveModels.get(new Integer(model));
    }
    
    public void crearInclusiveModels(){
        this.inclusiveModels.clear();
    }    
    
    /**
     * @param exclusives should be Collection containing Integer objects of model Ids
     */
    public void updateInclusivesForModel(int model, Collection exclusives){
        this.inclusiveModels.put(new Integer(model), exclusives);
    }    
    
    /**
     * @return Collectin of Collections of Integer Ids.
     */
 /*   public Collection getInclusiveModelGroups(){
        Iterator iterator = this.inclusiveModels.keySet().iterator();
        LinkedList ret = new LinkedList();
        while(iterator.hasNext()){
            Integer id = (Integer)iterator.next();
            LinkedList oneList = (LinkedList)this.inclusiveModels.get(id);
            oneList.add(id);
            Iterator retIterator = ret.iterator();
            boolean already = false;
            while(retIterator.hasNext()){
                LinkedList retList = (LinkedList)retIterator.next();
                if(retList.containsAll(oneList))
                    already = true;
            }
            if(!already)
                ret.add(oneList);
            
        }
        return ret;
    }  */
    
    public HashMap getInclusivesHashMap(){
        return this.inclusiveModels;
    }    
    
    public boolean moveModelUp(ArmylistModel model){
        Integer id = new Integer(this.armylistArmy.getIdForModel(model));
        int index = this.models.indexOf(id);
        if( index > 0 ){
            this.models.remove(index);
            this.models.add(index - 1, id);
            return true;
        }
        return false;
    }
    
    public boolean moveModelDown(ArmylistModel model){
        Integer id = new Integer(this.armylistArmy.getIdForModel(model));
        int index = this.models.indexOf(id);
        if( index < this.models.size()-1 && index > -1){
            this.models.remove(index);
            this.models.add(index + 1, id);
            return true;
        }
        return false;
    }    
    
    public boolean moveUpdateUp(ArmylistUnitUpdate update){
        Integer id = new Integer(this.armylistArmy.getIdForUnitUpdate(update));
        int index = this.updates.indexOf(id);
        if( index > 0 ){
            this.updates.remove(index);
            this.updates.add(index - 1, id);
            return true;
        }
        return false;
    }
    
    public boolean moveUpdateDown(ArmylistUnitUpdate update){
        Integer id = new Integer(this.armylistArmy.getIdForUnitUpdate(update));
        int index = this.updates.indexOf(id);
        if( index < this.updates.size()-1 && index > -1){
            this.updates.remove(index);
            this.updates.add(index + 1, id);
            return true;
        }
        return false;
    }      
    
    
    public void setMaxUnitSize(int size){
        this.maxUnitSize = size;
    }
    
    public int getMaxUnitSize(){
        return this.maxUnitSize;
    }

    public void setMinUnitSize(int size){
        this.minUnitSize = size;
    }
    
    public int getMinUnitSize(){
        return this.minUnitSize;
    } 
    
    public String getNotes(){
        return this.notes;
    }
    
    public void setNotes(String notes){
        this.notes = notes;
    }
    
    
}
