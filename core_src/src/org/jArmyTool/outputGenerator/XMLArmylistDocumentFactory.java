/*
 * XMLArmylistDocumentFactory.java
 *
 * Created on 25 March 2003, 18:02
 */

package org.jArmyTool.outputGenerator;

import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.data.dataBeans.util.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.util.*;
import java.io.*;
import java.lang.StringBuffer;
import org.jArmyTool.data.dataBeans.util.WeaponProfile;

/**
 *
 * @author  pasleh
 */
public class XMLArmylistDocumentFactory {

    private static final String MODEL_ID_PREFIX = "model";
    private int modelIdCounter;
   
    private static final String MODEL_UPDATE_ID_PREFIX = "modelUpdate";
    private int modelUpdateIdCounter;

    private static final String UNIT_UPDATE_ID_PREFIX = "unitUpdate";
    private int unitUpdateIdCounter;    
    
    private ArmylistArmy armylistArmy;
    
    private DocumentBuilder builder;
    
    private Document document;
    private Element rootElement;
    
    
    private HashMap modelUpdateIds;
    private HashMap unitUpdateIds;
    private HashMap modelIds;
    
    
    /** Creates a new instance of XMLArmylistDocumentFactory */
    public XMLArmylistDocumentFactory(ArmylistArmy armylistArmy) {
        this.modelUpdateIds = new HashMap();
        this.unitUpdateIds = new HashMap();
        this.modelIds = new HashMap();
        
        this.modelIdCounter = 0;
        this.modelUpdateIdCounter = 0;
        this.unitUpdateIdCounter = 0;
        
        this.armylistArmy = armylistArmy;
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            this.builder = factory.newDocumentBuilder();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Document createDOM(){
        
        this.document = this.builder.newDocument();
   
        this.rootElement = this.document.createElement("armylist");
        
        this.document.appendChild(this.rootElement);
        
        this.createHeader();
        this.createWargear();
        this.createWeapons();
        this.createUnits();
        
        this.createUnitUpdates();
        this.createModels();
        this.createModelUpdates();

        return this.document;
    }
    
    private void createHeader(){
        Element header = this.document.createElement("armyHeader");
        header.setAttribute("id", "header");
        header.setAttribute("gameSystem", this.armylistArmy.getGameSystem().getName());
        Element name = this.document.createElement("name");
        name.setAttribute("value", this.armylistArmy.getName());
        
        header.appendChild(name);
        
        Element comment = this.document.createElement("comment");
        comment.setAttribute("value", this.armylistArmy.getComments());
        comment.setAttribute("writer", this.armylistArmy.getWriter());
        comment.setAttribute("email", this.armylistArmy.getEmail());
        comment.setAttribute("web", this.armylistArmy.getWeb());
        
        
        
        header.appendChild(comment);
        
        this.rootElement.appendChild(header);
    }
    
 
    
    private void createWargear(){
        Element wargear = this.document.createElement("wargear");
        
        Iterator iterator = this.armylistArmy.getWargearGroups().iterator();
        while(iterator.hasNext()){
            this.createWargearGroup(wargear, (ArmylistWargearGroup)iterator.next());
        }
        
        this.rootElement.appendChild(wargear);
    }
    
    private void createWargearGroup(Element node, ArmylistWargearGroup group){
        Element wgGroup = this.document.createElement("wargearGroup");
        wgGroup.setAttribute("name", group.getName());
        
        Iterator required = group.getRequiredItems().iterator();
        String requiredSt = "";
        while(required.hasNext()){
            requiredSt = requiredSt + (String)required.next();  
            if(required.hasNext())
                requiredSt = requiredSt + ",";
        }
        wgGroup.setAttribute("requiredItems", requiredSt);
        
        Iterator iterator = group.getSubGroups().iterator();
        while(iterator.hasNext()){
            ArmylistWargearGroup subGroup = (ArmylistWargearGroup)iterator.next();
            this.createWargearGroup(wgGroup, subGroup);
        }
        
        
        iterator = group.getItems().iterator();
        while(iterator.hasNext()){
            ArmylistWargearItem item = (ArmylistWargearItem)iterator.next();
            Element itemEl = this.document.createElement("wargearItem");
            itemEl.setAttribute("name", item.getName());
            itemEl.setAttribute("pointcost", ""+item.getPointcost());
            
            StringBuffer sb = new StringBuffer();
            Iterator wgIterator = item.getWeapons().iterator();
            while(wgIterator.hasNext()){
                if(sb.length() > 0 )
                    sb.append(",");
                sb.append( ((ArmylistWeapon)wgIterator.next()).getName() );
            }

            itemEl.setAttribute("weapon", sb.toString());            
            
            wgGroup.appendChild(itemEl);
        }
        node.appendChild(wgGroup);
    }
    
    
    private void createWeapons(){
        Iterator iterator = this.armylistArmy.getGameSystem().getWeaponProfiles().iterator();
        
        if(!iterator.hasNext())
            return;
        
        Element weapons = this.document.createElement("weapons");

        while(iterator.hasNext()){
            String profileName = ((WeaponProfile)iterator.next()).getName();
            Iterator weaponIterator = this.armylistArmy.getWeapons(profileName).iterator();
            
            while(weaponIterator.hasNext()){
               ArmylistWeapon weapon = (ArmylistWeapon)weaponIterator.next();
               Element weaponEl = this.document.createElement("weapon");
               weaponEl.setAttribute("name", weapon.getName());
               weaponEl.setAttribute("profileName", profileName);
               
               StringBuffer sb = new StringBuffer();
               Iterator profileIterator = weapon.getStats().iterator();
               while(profileIterator.hasNext()){
                    if(sb.length() > 0)
                        sb.append(",");
                    sb.append((String)profileIterator.next());
               }
               
               weaponEl.setAttribute("profile", sb.toString());
               weapons.appendChild(weaponEl);
            }
        }
        this.rootElement.appendChild(weapons);
    }
    
    
    
    private void createUnits(){
        Iterator iterator = this.armylistArmy.getUnits().iterator();
        while(iterator.hasNext()){
            this.createUnit((ArmylistUnit)iterator.next());
        }
    }
    
    private void createUnit(ArmylistUnit unit){
       Element unitEl = this.document.createElement("unit"); 
       unitEl.setAttribute("name", unit.getName());
       unitEl.setAttribute("minCount", ""+unit.getMinCount());
       unitEl.setAttribute("maxCount", ""+unit.getMaxCount());
       unitEl.setAttribute("maxUnitSize", ""+unit.getMaxUnitSize());
       unitEl.setAttribute("minUnitSize", ""+unit.getMinUnitSize());
       unitEl.setAttribute("unitType", ""+unit.getUnitType());
       
       Element allowedModels = this.document.createElement("allowedModels");
       
       String allowedModelsStr = "";
       
       Iterator iterator = unit.getModelIds().iterator();
       
       while(iterator.hasNext()){
         if(allowedModelsStr.length() > 0)
             allowedModelsStr = allowedModelsStr + ",";
         
         allowedModelsStr = allowedModelsStr + MODEL_ID_PREFIX +((Integer)iterator.next()).toString();
         
       }
       allowedModels.setAttribute("value", allowedModelsStr);
       
       unitEl.appendChild(allowedModels);
       
       /////
       Element allowedUpdates = this.document.createElement("allowedUpdates");
       
       String allowedUpdatesStr = "";
       
       iterator = unit.getUpdateIds().iterator();
        while(iterator.hasNext()){
         if(allowedUpdatesStr.length() > 0)
             allowedUpdatesStr = allowedUpdatesStr + ",";
         
          allowedUpdatesStr = allowedUpdatesStr + UNIT_UPDATE_ID_PREFIX+((Integer)iterator.next()).toString();
       }
       allowedUpdates.setAttribute("value", allowedUpdatesStr);
       unitEl.appendChild(allowedUpdates);
       
       HashMap exlMap = unit.getExclusivesHashMap();
       Iterator exclIterator = exlMap.keySet().iterator();
       while(exclIterator.hasNext()){
           Integer key = (Integer)exclIterator.next();
           unitEl.appendChild(createExclusiveGroup(key.intValue(), (Collection)exlMap.get(key), false ));
       }

       HashMap inlMap = unit.getInclusivesHashMap();
       Iterator inclIterator = inlMap.keySet().iterator();
       while(inclIterator.hasNext()){
           Integer key = (Integer)inclIterator.next();
           unitEl.appendChild(createExclusiveGroup(key.intValue(), (Collection)inlMap.get(key), true ));
       }       
       
       this.rootElement.appendChild(unitEl);
    }
    
    
    private Element createExclusiveGroup(int thisId, Collection otherIds, boolean inclusive){
        Element exclEl = this.document.createElement("exclusiveModels");
        exclEl.setAttribute("modelId", ""+thisId);
        
        StringBuffer otherModels = new StringBuffer();
        Iterator iterator = otherIds.iterator();
        while(iterator.hasNext()){
            if(otherModels.length() > 0)
                otherModels.append(",");
            otherModels.append(((Integer)iterator.next()).intValue());
        }
        exclEl.setAttribute("otherModelIds", otherModels.toString());
        
        if(inclusive)
            exclEl.setAttribute("inclusive", "TRUE");
        
        return exclEl;
    }
    
    private void createModels(){
        Iterator iterator = this.armylistArmy.getAllModelIds().iterator();
        
        while(iterator.hasNext()){
            int id  = ((Integer)iterator.next()).intValue();
            this.createModel(this.armylistArmy.getModel(id), MODEL_ID_PREFIX + id);
        }
    }
    
    
    private void createModel(ArmylistModel model, String id){
       Element modelEl = this.document.createElement("model"); 
       modelEl.setAttribute("id", id);
       modelEl.setAttribute("name", model.getName());
       modelEl.setAttribute("pointcost", ""+model.getPointCostPerModel());
       modelEl.setAttribute("minCount", ""+model.getMinCount());
       modelEl.setAttribute("maxCount", ""+model.getMaxCount());
       modelEl.setAttribute("defaultSelectedAmount", ""+model.getDefaultSelectedAmount());
       modelEl.setAttribute("counted", ""+model.isCounted());
       modelEl.setAttribute("isLinked", ""+model.isLinked());
       modelEl.setAttribute("statType", model.getStatTypeName());
       modelEl.setAttribute("statValues", model.getStatValues(","));
       
       Element allowedUpdates = this.document.createElement("allowedUpdates");
       
       String allowedUpdatesStr = "";
       
       Iterator iterator = model.getUpdateIds().iterator();
       while(iterator.hasNext()){
         if(allowedUpdatesStr.length() > 0)
             allowedUpdatesStr = allowedUpdatesStr + ",";
         allowedUpdatesStr = allowedUpdatesStr + MODEL_UPDATE_ID_PREFIX+((Integer)iterator.next()).toString();
       }
       allowedUpdates.setAttribute("value", allowedUpdatesStr);
       modelEl.appendChild(allowedUpdates);
       
       //Wargear
       
       if(model.getAllowedWargear() > 0){
           Element wg = this.document.createElement("allowedWargear");
           wg.setAttribute("value", ""+ model.getAllowedWargear());
           this.createSubWGGroupAllowances(wg, model);
           modelEl.appendChild(wg);
           
            String wgGroups = "";
            iterator = model.getAllowedWargearGroups().iterator();
            while(iterator.hasNext()){
                if(wgGroups.length() > 0)
                    wgGroups = wgGroups + ",";
                wgGroups = wgGroups + (String)iterator.next();
            }
            Element wgGroupsEl = this.document.createElement("allowedWargearGroups");
            wgGroupsEl.setAttribute("value", wgGroups);
            
            modelEl.appendChild(wgGroupsEl);
            
       }
       
       this.rootElement.appendChild(modelEl);
    }
    
    private void createSubWGGroupAllowances(Element wgGroupsEl, ArmylistModel model){
        Iterator subGroups = model.getSubWGGroupAllowedGroups().iterator();
        while(subGroups.hasNext()){
            String name = (String)subGroups.next();
            Element subGroupAllowed = this.document.createElement("subWGGroupsAllowed");
            subGroupAllowed.setAttribute("groupName", name);
            subGroupAllowed.setAttribute("allowed", ""+model.getSubWGGroupAllowedAmount(name));
            wgGroupsEl.appendChild(subGroupAllowed);
        }
        
    }
 
    private void createModelUpdates(){
        Iterator iterator = this.armylistArmy.getAllModelUpdateIds().iterator();
        
        while(iterator.hasNext()){
            int id  = ((Integer)iterator.next()).intValue();
            this.createModelUpdate(this.armylistArmy.getModelUpdate(id), MODEL_UPDATE_ID_PREFIX + id);
        }
    }    
    
    private void createModelUpdate(ArmylistModelUpdate update, String id){
        Element updateEl = this.document.createElement("modelUpdate");
        updateEl.setAttribute("id", id);
        updateEl.setAttribute("name", update.getName());
        
        updateEl.setAttribute("pointcostPerModel", ""+update.isPointcostPerModel());
        updateEl.setAttribute("pointcost", ""+update.getPointcost());
        updateEl.setAttribute("minCount", ""+update.getMinCount());
        updateEl.setAttribute("maxCount", ""+update.getMaxCount());
        updateEl.setAttribute("defaultSelectedAmount", ""+update.getDefaultCount());
        
        StringBuffer sb = new StringBuffer();
        Iterator iterator = update.getWeapons().iterator();
        while(iterator.hasNext()){
            if(sb.length() > 0 )
                sb.append(",");
            sb.append( ((ArmylistWeapon)iterator.next()).getName() );
        }
        
        updateEl.setAttribute("weapon", sb.toString());
        this.rootElement.appendChild(updateEl);
    }
 
    private void createUnitUpdates(){
        Iterator iterator = this.armylistArmy.getAllUnitUpdateIds().iterator();
        
        while(iterator.hasNext()){
            int id  = ((Integer)iterator.next()).intValue();
            this.createUnitUpdate(this.armylistArmy.getUnitUpdate(id), UNIT_UPDATE_ID_PREFIX + id);
        }
    }      
    
    private void createUnitUpdate(ArmylistUnitUpdate update, String id){
        Element updateEl = this.document.createElement("unitUpdate");
        updateEl.setAttribute("id", id);
        updateEl.setAttribute("name", update.getName());
        
        updateEl.setAttribute("pointcostPerModel", ""+update.isPointcostPerModel());
        updateEl.setAttribute("pointcost", ""+update.getPointcost());
        updateEl.setAttribute("minCount", ""+update.getMinCount());
        updateEl.setAttribute("maxCount", ""+update.getMaxCount());
        updateEl.setAttribute("defaultSelectedAmount", ""+update.getDefaultCount());
 
        StringBuffer sb = new StringBuffer();
        Iterator iterator = update.getWeapons().iterator();
        while(iterator.hasNext()){
            if(sb.length() > 0 )
                sb.append(",");
            sb.append( ((ArmylistWeapon)iterator.next()).getName() );
        }
        
        updateEl.setAttribute("weapon", sb.toString());
        
        
        this.rootElement.appendChild(updateEl);
    }    
}
