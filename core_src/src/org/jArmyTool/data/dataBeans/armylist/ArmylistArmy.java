package org.jArmyTool.data.dataBeans.armylist;

import java.util.*;
import java.io.Serializable;

import org.jArmyTool.data.dataBeans.gameSystem.*;

import org.jArmyTool.data.dataBeans.util.*;
/**
 * This class is the base of single armylist in jArmyTool.
 * @author Pasi Lehtimäki
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
    
    /**
     * Creates a new instance of Army
     * @param name Name of this armylist
     * @param gameSystem This list's game system
     */
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
     * @param name Name of cloned list
     * @param clone The list from where the data is copied
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
    

    
    /**
     * Use this to get all unit update IDs in use
     * @return Collection containing Integer objects
     */    
    public Collection getAllUnitUpdateIds(){
        return this.allUnitUpdates.keySet();
    }

    /**
     * Use this to obtain unit update using it's ID
     * @param id ID of the unit update
     * @return UnitUpdate corresponding the ID. If the ID is not found then <I>null</I>
     * is returned.
     */    
    public ArmylistUnitUpdate getUnitUpdate(int id){
        return (ArmylistUnitUpdate)this.allUnitUpdates.get(new Integer(id));
    }
    
    /**
     * This method will add unit update to this list
     * @param update Update to add
     * @return ID which is assosiated to thi update from here on.
     */    
    public int mapUnitUpdate(ArmylistUnitUpdate update){
        int assosiatedId = this.nextUnitUpdateId ++;
        
        this.allUnitUpdates.put(new Integer(assosiatedId), update);
        
        return assosiatedId;
    }
    
    /**
     * Used to add unit update to armylist using selected ID.
     *
     * CAUTION! id might already be in use and this method
     * does not check it.
     * Use mapUnitUpdate(ArmylistUnitUpdate) instead in normal case.
     *
     * Use only if you know what you're doing.
     * @param update Update to be added
     * @param id Selected id
     * @return ID assosiated to this update
     */
    public int mapUnitUpdate(ArmylistUnitUpdate update, int id){
        if(this.nextUnitUpdateId <= id)
            this.nextUnitUpdateId = id + 1;
        
        this.allUnitUpdates.put(new Integer(id), update);
        return id;
    }
    
    /**
     * Use this to get model update usind ID
     * @param id Id of the  model update
     * @return ArmylistModelUpdate assosiated to the ID. <I>null</I> if no update is found by
     * that id.
     */    
    public ArmylistModelUpdate getModelUpdate(int id){
        return (ArmylistModelUpdate)this.allModelUpdates.get(new Integer(id));
    }
    
    /**
     * This method adds a model update to this list
     * @param update Update to be added
     * @return ID assosiated to this update from now on.
     */    
    public int mapModelUpdate(ArmylistModelUpdate update){
        int assosiatedId = this.nextModelUpdateId ++;
        
        this.allModelUpdates.put(new Integer(assosiatedId), update);
        
        return assosiatedId;
    }
    
    /**
     * Used to add model update to armylist using selected ID.
     *
     * CAUTION! id might already be in use and this method
     * does not check it.
     * Use mapModelUpdate(ArmylistModelUpdate) instead in normal case.
     *
     * Use only if you know what you're doing.
     * @param update Update to be added
     * @param id ID to be used
     * @return Id assosiated to this update from now on
     */    
    public int mapModelUpdate(ArmylistModelUpdate update, int id){
        if(this.nextModelUpdateId <= id)
            this.nextModelUpdateId = id + 1;
        
        this.allModelUpdates.put(new Integer(id), update);
        return id;
    }
    
    /**
     * Use thios to get all model updates
     * @return Collection containing Integer objects presenting all IDs of model updates.
     */    
    public Collection getAllModelUpdateIds(){
        return this.allModelUpdates.keySet();
    }


    /**
     * GEt model using id
     * @param id Id of model
     * @return ArmylistModel for that id. <I>null</I> if no model is found by that id
     */    
    public ArmylistModel getModel(int id){
        return (ArmylistModel)this.allModels.get(new Integer(id));
    }
    
    /**
     * Add model to this list
     * @param update update to be added
     * @return Id assoiated to this model from now on.
     */    
    public int mapModel(ArmylistModel update){
        int assosiatedId = this.nextModelId ++;
        
        this.allModels.put(new Integer(assosiatedId), update);
        
        return assosiatedId;
    }
    
    /**
     * Used to add model to armylist using selected ID.
     *
     * CAUTION! id might already be in use and this method
     * does not check it.
     * Use mapModel(ArmylistModel) instead in normal case.
     *
     * Use only if you know what you're doing.
     * @param update update to be added
     * @param id id to be used
     * @return id assosiated to this model from now on
     */    
    public int mapModel(ArmylistModel update, int id){
        if(this.nextModelId <= id)
            this.nextModelId = id + 1;
        
        this.allModels.put(new Integer(id), update);
        return id;
    }    
    
    /**
     * Use this to get all models in this list
     * @return Collection containing Integer objects presenting IDs of all models in this list
     */    
    public Collection getAllModelIds(){
        return this.allModels.keySet();
    }
    
    
    /**
     * This method adds a root wargear group to the list
     * @param group Group to be added
     */    
    public void addWargearGroup(ArmylistWargearGroup group){
        this.wargearGroups.add(group);
    }
    
    /**
     * This method removes a root wargear group from armylist
     * @param group group to be removed
     */    
    public void removeargearGroup(ArmylistWargearGroup group){
        this.wargearGroups.remove(group);
    }
    
    /**
     * Get all root wargear groups.
     * @return Collection containing ArmylistWargearGroup objects.
     */    
    public Collection getWargearGroups(){
        return Collections.unmodifiableCollection(this.wargearGroups);
    }
    
    /**
     * This method returns first matching item entry in root wargear groups.
     * @param name Item name
     * @return First matching wargear item
     * @deprecated This method does not use new wargear hierarchy
     */    
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
    
    /**
     * This method clears all root wargear groups.
     */    
    public void emptyWargearGroups(){
        this.wargearGroups.clear();
    }
    
    /**
     * returns wargear item with matching name in specified root wargear group
     * @param group Name of the group
     * @param itemName Name of the item
     * @return found wargear item. <I>null</I> if none found
     * @deprecated This method does not use new wargear hierarchy
     */    
    public ArmylistWargearItem getWargearItem(String group, String itemName){
        Iterator iterator = this.getWargearGroupbyName(group).getItems().iterator();
        
        while(iterator.hasNext()){
            ArmylistWargearItem item = (ArmylistWargearItem)iterator.next();
            if(item.getName().equals(itemName))
                return item;
        }
        return null;
    }
    
    /**
     * Returns root wargear group with matching name
     * @param name name of the group
     * @return wargear group with matching name. <I>null</I> if none found.
     * @deprecated This method does not use the new wargear hierarchy.
     */    
    public ArmylistWargearGroup getWargearGroupbyName(String name){
        Iterator iterator = this.wargearGroups.iterator();
        while(iterator.hasNext()){
            ArmylistWargearGroup temp = (ArmylistWargearGroup)iterator.next();
            if(temp.getName().equalsIgnoreCase(name))
                return temp;
        }
        return null;
    }
    
    /**
     * Change name of this list.
     *
     * Use carefully. Changing name of armylist can render saved armies
     * invalid since they look armylist by name.
     * @param name New name
     */    
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Name of this list
     * @return name of this list
     */    
    public String getName(){
        return this.name;
    }
    
    /**
     * Remove unit form this list
     * @param unit unit to remove
     */    
    public void removeUnit(ArmylistUnit unit){
        this.units.remove(unit);
        
        LinkedList list = ((LinkedList)this.unitsByType.get(unit.getUnitType()));
        if(list != null)
            list.remove(unit);
    }
    
    /**
     * Add new unit to this list
     * @param unit unti to be added
     */    
    public void addUnit(ArmylistUnit unit){
        this.units.add(unit);
        LinkedList list = (LinkedList)this.unitsByType.get(unit.getUnitType());
        list.add(unit);
    }
    
    
    /**
     * This method is used to correct intrernal data structure once unit's type gets
     * changed
     * @param fromType old type
     * @param unit the unit
     */    
    protected void unitTypeChanged(String fromType, ArmylistUnit unit){
        
        LinkedList list = ((LinkedList)this.unitsByType.get(fromType));
        if(list.remove(unit)){
            list = ((LinkedList)this.unitsByType.get(unit.getUnitType()));
            list.add(unit);
        }
    }
    
    /**
     * Get all units in this list
     * @return Collection containing ArmylistUnit objects
     */    
    public Collection getUnits(){
        return Collections.unmodifiableCollection(this.units);
    }
    
    /**
     * This method returns all units in this army which have the selected type
     * @param type wanted type
     * @return Collection ArmylitUnit objects. <I>null</I> if provided type is not found.
     */    
    public Collection getUnitsByType(String type){
        LinkedList list = ((LinkedList)this.unitsByType.get(type));
        if(list == null)
            return null;
        return Collections.unmodifiableCollection(list);
    }
    
    /**
     * Get all unit updates of this list
     * @return Set containing ArmylistUnitUpdate objects
     */    
    public Set getAllUnitUpdates(){
        HashSet set = new HashSet();
        Iterator iterator = this.allUnitUpdates.keySet().iterator();
        
        while(iterator.hasNext()){
            set.add(this.allUnitUpdates.get(iterator.next()));
        }
        return set;
        
    }
    
    /**
     * Get gamesystem used by this list
     * @return This list's game system
     */    
    public GameSystem getGameSystem(){
        return this.gameSystem;
    }
    
    /**
     * Set the game system.
     * Use carefully. Changing the system once list already contain
     * data can break internal data sructure.
     * @param gameSystem new game system
     */    
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
    
    /**
     * This method tells which id is assosiated to given model
     * @param model model
     * @return id assosiated to given model
     */    
    public int getIdForModel(ArmylistModel model){
        Iterator iterator = this.allModels.keySet().iterator();
        while(iterator.hasNext()){
            Integer key = (Integer)iterator.next();
            if(model == this.allModels.get(key))
                return key.intValue();
        }
        return -1;
    }
    
    /**
     * This method tells which id is assosiated to given model update
     * @param modelUpdate model update
     * @return id assosiated to given model update
     */    
    public int getIdForModelUpdate(ArmylistModelUpdate modelUpdate){
        Iterator iterator = this.allModelUpdates.keySet().iterator();
        while(iterator.hasNext()){
            Integer key = (Integer)iterator.next();
            if(modelUpdate == this.allModelUpdates.get(key))
                return key.intValue();
        }
        return -1;
    }    
    

    /**
     * This method tells which id is assosiated to given unit update
     * @param unitUpdate unit update
     * @return id assosiated to given unti update
     */    
    public int getIdForUnitUpdate(ArmylistUnitUpdate unitUpdate){
        Iterator iterator = this.allUnitUpdates.keySet().iterator();
        while(iterator.hasNext()){
            Integer key = (Integer)iterator.next();
            if(unitUpdate == this.allUnitUpdates.get(key))
                return key.intValue();
        }
        return -1;
    }     
    
    /**
     * Add new weapon to this list
     * @param profileName Weapon profile name. This should be one of the names used in the game system of this
     * list.
     * @param weapon weapon to be added
     */    
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
    
    /**
     * Obtain weapons by profile name
     * @param profileName Name of profile
     * @return Collection of ArmylistWeapon objects
     */    
    public Collection getWeapons(String profileName){
        LinkedList ret = ((LinkedList)this.weaponsByProfileName.get(profileName));
        if(ret != null)
            return Collections.unmodifiableCollection(ret);
        return null;
    }
    
    /**
     * Empties all weapons whit given profile
     * @param profileName name of the profile
     */    
    public void emptyWeapons(String profileName){
        LinkedList list = ((LinkedList)this.weaponsByProfileName.get(profileName));
        if(list != null){
            this.allWeapons.removeAll(list);
            list.clear();
        }
    }
    
    /**
     * Get first weapon with matching name
     * @param name name of weaapon
     * @return weapon with given name. <I>null</I> if none found
     */    
    public ArmylistWeapon getWeaponByName(String name){
        Iterator iterator = this.allWeapons.iterator();
        while(iterator.hasNext()){
            ArmylistWeapon wep = (ArmylistWeapon)iterator.next();
            if(wep.getName().compareToIgnoreCase(name) == 0)
                return wep;
        }
        return null;
    }
    
    /**
     * list comment field
     * @return comment of this list
     */    
    public String getComments(){
         return this.comments;
    }
    
    /**
     * change comment
     * @param newComment new comment
     */    
    public void setComments(String newComment){
        this.comments = newComment;
    }
    
    /**
     * Writer of this list
     * @return writer of this list
     */    
    public String getWriter(){
         return this.writer;
    }
    
    /**
     * change writer
     * @param writer new writer
     */    
    public void setWriter(String writer){
        this.writer = writer;
    }
    
    /**
     * e-mail info of writer
     * @return e-mail
     */    
    public String getEmail(){
         return this.email;
    }
    
    /**
     * change e-mail
     * @param email new e-mail
     */    
    public void setEmail(String email){
        this.email = email;
    }
    
    /**
     * URL of this list
     * @return URL of this list web site
     */    
    public String getWeb(){
         return this.web;
    }
    
    /**
     * change URL
     * @param web new URL
     */    
    public void setWeb(String web){
        this.web = web;
    }
}
