package org.jArmyTool.data.dataBeans.army;

import org.jArmyTool.data.factories.*;
import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.data.dataBeans.util.ModelStatHolder;
import java.util.*;
import java.io.Serializable;
/**
 * Model-class is armylist model instance in an army.
 * A model can have updates
 * @author  Pasi Lehtim?ki
 */
public class Model implements Serializable {
    
    //Armylist object used to create this object
    private org.jArmyTool.data.dataBeans.armylist.ArmylistModel armylistModel;
    
    //List containing pool-objects
    private LinkedList updates;
    //the name of this model
    private String name;
    //Amount of models selected
    private int selectedModelCount;
    
    private HashMap wargear;
    
    private LinkedList stats;
    
    /** Creates a new instance of Model */
    public Model(ArmylistModel armylistModel) {
        this.armylistModel = armylistModel;
  
        this.updates = new LinkedList();
        
        this.initUpdates();
        this.initWargear();
        this.stats = new LinkedList();
        Iterator i = this.armylistModel.getStats().iterator();
        while(i.hasNext())
        {
            stats.add(new ModelStatHolder((ModelStatHolder)i.next()));
        }
       // this.stats = new LinkedList(this.armylistModel.getStats()); <-- leads to shared statlines. which breaks stat updates.
        
        this.selectedModelCount = this.armylistModel.getDefaultSelectedAmount();
    }
    
    public Model(Model toClone){
        this.armylistModel = toClone.armylistModel;
        this.selectedModelCount = toClone.selectedModelCount;
        this.name = toClone.name;
        
        this.updates = new LinkedList();
        Iterator iterator = toClone.updates.iterator();
        while(iterator.hasNext()){
            this.updates.add(new ModelUpdate((ModelUpdate)iterator.next()));
        }   
        this.wargear = (HashMap)toClone.wargear.clone();
        this.stats = new LinkedList();
        Iterator i = this.armylistModel.getStats().iterator();
        while(i.hasNext())
        {
            stats.add(new ModelStatHolder((ModelStatHolder)i.next()));
        }
        //this.stats = new LinkedList(this.armylistModel.getStats()); <--- same as before, leads to shared statlines
    }
    
   public Collection getStats()
   {
        return Collections.unmodifiableCollection(this.stats);
   }
   
   public void setStat(String Stat, double value)
   {
       Iterator i = this.stats.iterator();
       while(i.hasNext())
       {
           ModelStatHolder st = (ModelStatHolder)i.next();
           if(st.getStat().getSymbol().equalsIgnoreCase(Stat))
           {
               st.setValue(String.valueOf(value));
           }
       }
   }
    
      public void setStat(String Stat, String value)
   {
       Iterator i = this.stats.iterator();
       while(i.hasNext())
       {
           ModelStatHolder st = (ModelStatHolder)i.next();
           if(st.getStat().getSymbol().equalsIgnoreCase(Stat))
           {
               st.setValue(value);
           }
       }
   }
   
    private void initWargear(){
        this.wargear = new HashMap();
        Iterator iterator = this.armylistModel.getAllowedWargearGroups().iterator();
        while(iterator.hasNext()){
            this.wargear.put(iterator.next(), new LinkedList());
        }
    }
    
    private void initUpdates(){
        Iterator iterator = armylistModel.getUpdates().iterator();
        Factory factory = Factory.getInstance();
        while(iterator.hasNext()){
            this.updates.add(factory.createModelUpdate((ArmylistModelUpdate)iterator.next(), this));
        }
    }
    
    public Collection getSelectedWargear(String group){
        return (Collection)this.wargear.get(group);
    }
    
    public Collection getSelectedWargearNames(){
        LinkedList list = new LinkedList();
        Iterator iterator = this.wargear.keySet().iterator();
        while(iterator.hasNext()){
            Iterator it = ((Collection)this.wargear.get(iterator.next())).iterator();
            while(it.hasNext()){
                list.add( ((ArmylistWargearItem)it.next()).getName() );
            }
        }
        return list;
    }
    
    public boolean selectWargear(String group, ArmylistWargearItem item){
        LinkedList temp = (LinkedList)this.wargear.get(group);
        if(temp == null){
            temp = new LinkedList();
            this.wargear.put(group, temp);
        }
            
        temp.add(item);
        return true;
    }

    public boolean deSelectWargear(String group, ArmylistWargearItem item){
        LinkedList temp = (LinkedList)this.wargear.get(group);
        if(temp == null)
            return false;
        return temp.remove(item);
    }    
    
    public void changeWargearSelection(String group, ArmylistWargearItem item){
        LinkedList temp = (LinkedList)this.wargear.get(group);
        if(temp == null)
            return;
        if(temp.contains(item)){
            temp.remove(item);
        }else{
            temp.add(item);
        }
    }
    /**
     * @return Name of this model. Armylist name is returned if no name is set to this model
     */
    public String getName(){
        if(this.name == null)
            return this.armylistModel.getName();
        return this.name;
    }
    
    /**
     * @return true if new value is legal
     */
    public boolean increaseModelCount(){
        ++this.selectedModelCount;
        if(this.armylistModel.getMaxCount() == -1 || this.armylistModel.getMaxCount() >= this.selectedModelCount)
            return true;
        return false;
    }
    
    /**
     * @return true if new value is legal
     */
    public boolean decreaseModelCount(){
        if(this.selectedModelCount > 0)
            --this.selectedModelCount;
        if(this.armylistModel.getMinCount() <= this.selectedModelCount)
            return true;
        return false;
    }    
    
    /**
     * @return true if new value is legal
     */
    public boolean setModelCount(int count){
        if(count >= 0)
            this.selectedModelCount = count;
        if(this.armylistModel.getMinCount() <= this.selectedModelCount){
            if(this.armylistModel.getMaxCount() == -1 || this.armylistModel.getMaxCount() >= this.selectedModelCount)
                return true;
            return false;
        }
        return false;        
    }
   
    public int getModelCount(){
        return this.selectedModelCount;
    }
    
    public double getWargearPointcost(){
        double temp = 0;
        Iterator mapIterator = this.wargear.values().iterator();
        while(mapIterator.hasNext()){
            Iterator iterator = ((Collection)mapIterator.next()).iterator();
            while(iterator.hasNext()){
                temp = temp + ((ArmylistWargearItem)iterator.next()).getPointcost();
            }
        }
        return temp;
    }
    
    public double getPointcost(){
        if(this.selectedModelCount == 0)
            return 0;
        
        double cost = this.selectedModelCount * this.armylistModel.getPointCostPerModel();
        Iterator iterator = this.updates.iterator();
        while(iterator.hasNext()){
            cost = cost + ((ModelUpdate)iterator.next()).getPointcost();
        }
        
        
        return cost + (this.getWargearPointcost() * this.selectedModelCount);
    }
    
    /**
     * Sets the name of this modelobject
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Returns all updates
     * @return Collection containing Update-object
     */

    public Collection getUpdates(){
        return Collections.unmodifiableCollection(this.updates);
    }
    
    public ArmylistModel getArmylistModel(){
        return this.armylistModel;
    }

}
