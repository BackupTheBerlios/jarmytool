package core_src.src.org.jArmyTool.data.dataBeans.armylist;

import java.util.*;
import java.io.Serializable;

import org.jArmyTool.data.dataBeans.gameSystem.*;

import org.jArmyTool.data.dataBeans.util.*;
/**
 *
 * @author  pasi
 */
public class ArmylistArmy implements Serializable {
    
    /*private static final String modelIdPrefix = "model";
    private static final String modelIUpdateIdPrefix = "modelUpdate";
    private static final String unitIdPrefix = "unit";
    private static final String unitUpdateIdPrefix = "unitUpdate";
    
    private int lastUsedModelId;
    private int lastUsedModelUpdateId;
    private int lastUsedUnitId;
    private int lastUsedUnitUpdateId;*/
    
    private String name;
    private LinkedList units;
    private LinkedList wargearGroups;
    
    private HashMap allUnitUpdates;
    private HashMap allModels;
    private HashMap allModelUpdates;
    
    private int nextUnitUpdateId = 1;
    private int nextModelUpdateId = 1;
    private int nextModelId = 1;
    
    private GameSystem gameSystem;
    
    private HashMap weaponsByProfileName;
    private LinkedList allWeapons;
    
    private HashMap unitsByType;
    
    private String comments = "";
    private String writer = "";
    private String email = "";
    private String web = "";
    
    /** Creates a new instance of Army */
    public ArmylistArmy(String name, GameSystem gameSystem) {
        this.name = name;
        this.units = new LinkedList();
        this.unitsByType = new HashMap();
        this.wargearGroups = new LinkedList();
        this.allUnitUpdates = new HashMap();
        this.allModelUpdates = new HashMap();
        this.allModels = new HashMap();
        this.weaponsByProfileName = new HashMap();
        this.allWeapons = new LinkedList();
        this.setGameSystem(gameSystem);
        this.comments = "";
    }
    
    /**
     * This constructor is un finished... won't copy all internal data.. only links.<br>
     *
     * THIS CONSTRUCTOR DOESN'T WORK!!!!
     */
    public ArmylistArmy(String name, ArmylistArmy clone){
        this.name = name;
        this.units = (LinkedList)clone.units.clone();
        this.wargearGroups = (LinkedList)clone.wargearGroups.clone();
        this.allUnitUpdates = (HashMap)clone.allUnitUpdates.clone();
        this.allModelUpdates = (HashMap)clone.allModelUpdates.clone();
        this.allModels = (HashMap)clone.allModels.clone();
        this.gameSystem = clone.gameSystem;
        this.weaponsByProfileName = (HashMap)clone.weaponsByProfileName.clone();
        this.allWeapons = (LinkedList)clone.allWeapons.clone();
        //Clone unitsByType!!!
        this.comments = clone.comments;
        this.web = clone.web;
        this.email = clone.email;
        this.writer = clone.writer;
        
        this.initUnitByType();
    }
    
    private void initUnitByType(){
        String[] types = this.gameSystem.getUnitTypeNames();
        for(int i = 0; i < types.length; ++i){
            LinkedList list = new LinkedList();
            this.unitsByType.put(types[i], list);
        }
    }
    

    
    public Collection getAllUnitUpdateIds(){
        return this.allUnitUpdates.keySet();
    }

    public ArmylistUnitUpdate getUnitUpdate(int id){
        return (ArmylistUnitUpdate)this.allUnitUpdates.get(new Integer(id));
    }
    
    public int mapUnitUpdate(ArmylistUnitUpdate update){
        int assosiatedId = this.nextUnitUpdateId ++;
        
        this.allUnitUpdates.put(new Integer(assosiatedId), update);
        
        return assosiatedId;
    }
    
    /** CAUTION! id might already be in use. Use carefully.
     *
     */
    public int mapUnitUpdate(ArmylistUnitUpdate update, int id){
        if(this.nextUnitUpdateId <= id)
            this.nextUnitUpdateId = id + 1;
        
        this.allUnitUpdates.put(new Integer(id), update);
        return id;
    }
    
    public ArmylistModelUpdate getModelUpdate(int id){
        return (ArmylistModelUpdate)this.allModelUpdates.get(new Integer(id));
    }
    
    public int mapModelUpdate(ArmylistModelUpdate update){
        int assosiatedId = this.nextModelUpdateId ++;
        
        this.allModelUpdates.put(new Integer(assosiatedId), update);
        
        return assosiatedId;
    }
    
    public int mapModelUpdate(ArmylistModelUpdate update, int id){
        if(this.nextModelUpdateId <= id)
            this.nextModelUpdateId = id + 1;
        
        this.allModelUpdates.put(new Integer(id), update);
        return id;
    }
    
    public Collection getAllModelUpdateIds(){
        return this.allModelUpdates.keySet();
    }


    public ArmylistModel getModel(int id){
        return (ArmylistModel)this.allModels.get(new Integer(id));
    }
    
    public int mapModel(ArmylistModel update){
        int assosiatedId = this.nextModelId ++;
        
        this.allModels.put(new Integer(assosiatedId), update);
        
        return assosiatedId;
    }
    
    public int mapModel(ArmylistModel update, int id){
        if(this.nextModelId <= id)
            this.nextModelId = id + 1;
        
        this.allModels.put(new Integer(id), update);
        return id;
    }    
    
    public Collection getAllModelIds(){
        return this.allModels.keySet();
    }
    
    
    public void addWargearGroup(ArmylistWargearGroup group){
        this.wargearGroups.add(group);
    }
    
    public void removeargearGroup(ArmylistWargearGroup group){
        this.wargearGroups.remove(group);
    }
    
    public Collection getWargearGroups(){
        return Collections.unmodifiableCollection(this.wargearGroups);
    }
    
    public ArmylistWargearItem getWargearItemByName(String name){
        Iterator iterator = this.getWargearGroups().iterator();
        
        while(iterator.hasNext()){
            Iterator grpIterator = ((ArmylistWargearGroup)iterator.next()).getItems().iterator();
            while(grpIterator.hasNext()){
                ArmylistWargearItem item = (ArmylistWargearItem)grpIterator.next();
                if(item.getName().equals(name))
                    return item;
            }
        }
        return null;
    }
    
    public void emptyWargearGroups(){
        this.wargearGroups.clear();
    }
    
    public ArmylistWargearItem getWargearItem(String group, String itemName){
        Iterator iterator = this.getWargearGroupbyName(group).getItems().iterator();
        
        while(iterator.hasNext()){
            ArmylistWargearItem item = (ArmylistWargearItem)iterator.next();
            if(item.getName().equals(itemName))
                return item;
        }
        return null;
    }
    
    public ArmylistWargearGroup getWargearGroupbyName(String name){
        Iterator iterator = this.wargearGroups.iterator();
        while(iterator.hasNext()){
            ArmylistWargearGroup temp = (ArmylistWargearGroup)iterator.next();
            if(temp.getName().equalsIgnoreCase(name))
                return temp;
        }
        return null;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void removeUnit(ArmylistUnit unit){
        this.units.remove(unit);
        
        LinkedList list = ((LinkedList)this.unitsByType.get(unit.getUnitType()));
        if(list != null)
            list.remove(unit);
    }
    
    public void addUnit(ArmylistUnit unit){
        this.units.add(unit);
        LinkedList list = (LinkedList)this.unitsByType.get(unit.getUnitType());
        list.add(unit);
    }
    
    
    protected void unitTypeChanged(String fromType, ArmylistUnit unit){
        
        LinkedList list = ((LinkedList)this.unitsByType.get(fromType));
        if(list.remove(unit)){
            list = ((LinkedList)this.unitsByType.get(unit.getUnitType()));
            list.add(unit);
        }
    }
    
    public Collection getUnits(){
        return Collections.unmodifiableCollection(this.units);
    }
    
    public Collection getUnitsByType(String type){
        LinkedList list = ((LinkedList)this.unitsByType.get(type));
        if(list == null)
            return null;
        return Collections.unmodifiableCollection(list);
    }
    
    public Set getAllUnitUpdates(){
        HashSet set = new HashSet();
        Iterator iterator = this.allUnitUpdates.keySet().iterator();
        
        while(iterator.hasNext()){
            set.add(this.allUnitUpdates.get(iterator.next()));
        }
        return set;
        
    }
    
    public GameSystem getGameSystem(){
        return this.gameSystem;
    }
    
    public void setGameSystem(GameSystem gameSystem){
        if(gameSystem == null)
            return;
        this.gameSystem = gameSystem;
        Iterator iterator = this.gameSystem.getWeaponProfiles().iterator();
        while(iterator.hasNext()){
            WeaponProfile profile = (WeaponProfile)iterator.next();
            this.weaponsByProfileName.put(profile.getName(), new LinkedList());
        }
        this.initUnitByType();
    }
    
    public int getIdForModel(ArmylistModel model){
        Iterator iterator = this.allModels.keySet().iterator();
        while(iterator.hasNext()){
            Integer key = (Integer)iterator.next();
            if(model == this.allModels.get(key))
                return key.intValue();
        }
        return -1;
    }
    
    public int getIdForModelUpdate(ArmylistModelUpdate modelUpdate){
        Iterator iterator = this.allModelUpdates.keySet().iterator();
        while(iterator.hasNext()){
            Integer key = (Integer)iterator.next();
            if(modelUpdate == this.allModelUpdates.get(key))
                return key.intValue();
        }
        return -1;
    }    
    

    public int getIdForUnitUpdate(ArmylistUnitUpdate unitUpdate){
        Iterator iterator = this.allUnitUpdates.keySet().iterator();
        while(iterator.hasNext()){
            Integer key = (Integer)iterator.next();
            if(unitUpdate == this.allUnitUpdates.get(key))
                return key.intValue();
        }
        return -1;
    }     
    
    public void addWeapon(String profileName, ArmylistWeapon weapon){
        LinkedList list = (LinkedList)this.weaponsByProfileName.get(profileName);
        int index = 0;
        Iterator iterator = list.iterator();
        while(iterator.hasNext()){
            if( ((ArmylistWeapon)iterator.next()).getName().compareTo(weapon.getName()) < 0 ){
                ++index;
            }else{
                break;
            }
        }
        list.add(index, weapon);
        
        
        index = 0;
        iterator = allWeapons.iterator();
        while(iterator.hasNext()){
            if( ((ArmylistWeapon)iterator.next()).getName().compareTo(weapon.getName()) < 0 ){
                ++index;
            }else{
                break;
            }
        }
        this.allWeapons.add(index, weapon);
    }
    
    public Collection getWeapons(String profileName){
        LinkedList ret = ((LinkedList)this.weaponsByProfileName.get(profileName));
        if(ret != null)
            return Collections.unmodifiableCollection(ret);
        return null;
    }
    
    public void emptyWeapons(String profileName){
        LinkedList list = ((LinkedList)this.weaponsByProfileName.get(profileName));
        if(list != null){
            this.allWeapons.removeAll(list);
            list.clear();
        }
    }
    
    public ArmylistWeapon getWeaponByName(String name){
        Iterator iterator = this.allWeapons.iterator();
        while(iterator.hasNext()){
            ArmylistWeapon wep = (ArmylistWeapon)iterator.next();
            if(wep.getName().compareToIgnoreCase(name) == 0)
                return wep;
        }
        return null;
    }
    
    public String getComments(){
         return this.comments;
    }
    
    public void setComments(String newComment){
        this.comments = newComment;
    }
    
    public String getWriter(){
         return this.writer;
    }
    
    public void setWriter(String writer){
        this.writer = writer;
    }
    
    public String getEmail(){
         return this.email;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getWeb(){
         return this.web;
    }
    
    public void setWeb(String web){
        this.web = web;
    }
}
