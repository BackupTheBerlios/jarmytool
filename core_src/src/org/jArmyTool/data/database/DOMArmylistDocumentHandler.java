/*
 * DOMArmyDocumentHandler.java
 *
 * Created on 24 March 2003, 21:52
 */

package org.jArmyTool.data.database;

import org.jArmyTool.data.dataBeans.gameSystem.GameSystem;
import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.data.dataBeans.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;


/**
 *
 * @author  pasi
 */
public class DOMArmylistDocumentHandler {
    
    private static final String HEADER_ID = "header";
    
    private static final String MODEL_ID_PREFIX = "model";
    private static final String MODEL_UPDATE_ID_PREFIX = "modelUpdate";
    private static final String UNIT_UPDATE_ID_PREFIX = "unitUpdate";
    
    private DocumentBuilder documentBuilder;
    
    private HashMap unitUpdates;
    private HashMap modelUpdates;
    private HashMap models;
    
    private ArmylistArmy armyInProgress;
    
    private Document document;
    
    private static final Logger logger = Logger.getLogger(DOMArmylistDocumentHandler.class);
    
    
   
    
    /** Creates a new instance of DOMArmyDocumentHandler */
    public DOMArmylistDocumentHandler() {
        try{
            this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }catch(Exception e){
            e.printStackTrace();
            //LOG!!!
        }
    }
    
    public ArmylistArmy parseFile(File file){
        logger.debug("Startin parser for file: "+file);
        this.unitUpdates = new HashMap();
        this.modelUpdates = new HashMap();
        this.models = new HashMap();
        
        document = null;
        try{
            document = this.documentBuilder.parse(file);
        }catch(Exception e){
            e.printStackTrace();
            //LOG!!
        }
        
        ArmylistArmy army = new ArmylistArmy("error reading armylist name", (GameSystem)null);
        this.armyInProgress = army;
        
        
        this.parseHeader(document.getElementById(HEADER_ID), army);
        
        this.parseWeapons((Element)document.getElementsByTagName("weapons").item(0), army);
        
        
        NodeList wargear = document.getElementsByTagName("wargear");
        for(int i = 0; i < wargear.getLength(); ++i){
            NodeList wargearGroups = ((Element)wargear.item(i)).getChildNodes();
            
            
            for(int j = 0; j < wargearGroups.getLength(); ++j){
                if(! (wargearGroups.item(j) instanceof Element) )
                    continue;
                this.parseWargearGroup((Element)wargearGroups.item(j), army, null);
            }
        }
        
        this.parseUnits(army);
        
        Iterator iterator = this.unitUpdates.keySet().iterator();
        while(iterator.hasNext()){
            String s = (String)iterator.next();
            army.mapUnitUpdate( (ArmylistUnitUpdate)this.unitUpdates.get(s), Integer.parseInt(s));
        }
        
        iterator = this.modelUpdates.keySet().iterator();
        while(iterator.hasNext()){
            String s = (String)iterator.next();
            army.mapModelUpdate( (ArmylistModelUpdate)this.modelUpdates.get(s), Integer.parseInt(s));
        }
        
        iterator = this.models.keySet().iterator();
        while(iterator.hasNext()){
            String s = (String)iterator.next();
            army.mapModel( (ArmylistModel)this.models.get(s), Integer.parseInt(s));
        }
        
        logger.debug("Finished parsing armylist file for army: "+ army.getName());
        return army;
    }
    
    private void parseHeader(Element header, ArmylistArmy army){
        //System.out.println(header);
        NodeList list = header.getElementsByTagName("name");
        Node name = list.item(0);
        army.setName( ((Element)name).getAttribute("value") );
        logger.debug("Seting name for armylist: "+ army.getName());
        GameSystemAccessBean gsab = GameSystemAccessBean.getInstance();
        //System.out.println("gamesystem name read: "+ header.getAttribute("gameSystem"));
        
        army.setGameSystem(gsab.getGameSystemByName(header.getAttribute("gameSystem")));
        //System.out.println("gamesystem: "+army.getGameSystem());
        
        
        list = header.getElementsByTagName("comment");
        if(list.getLength() > 0){
            name = list.item(0);
            
            army.setComments( ((Element)name).getAttribute("value") );
            army.setWriter( ((Element)name).getAttribute("writer") );
            army.setWeb( ((Element)name).getAttribute("web") );
            army.setEmail( ((Element)name).getAttribute("email") );
        }
    }
    

    private void parseWeapons(Element weapons, ArmylistArmy army){
        if(weapons == null)
            return;
        NodeList list = weapons.getElementsByTagName("weapon");
        for(int i = list.getLength(); i > 0; --i){
            Element weapon = (Element)list.item(i-1);
            ArmylistWeapon wep = new ArmylistWeapon(weapon.getAttribute("name"), army.getGameSystem().getWeaponProfile(weapon.getAttribute("profileName")));
            StringTokenizer st = new StringTokenizer(weapon.getAttribute("profile"), ",");
            
            while(st.hasMoreTokens()){
                wep.addStat(st.nextToken());
            }
            
            
            army.addWeapon(wep.getProfile().getName(), wep);
        }
    }
    
    private void parseWargearGroup(Element group, ArmylistArmy army, ArmylistWargearGroup parent){
        ArmylistWargearGroup grp = new ArmylistWargearGroup(group.getAttribute("name"));
        
        String req = group.getAttribute("requiredItems");
        StringTokenizer required = new StringTokenizer(req, ",");
        while(required.hasMoreTokens()){
            grp.addRequiredItem(required.nextToken());
        }
        
        NodeList list = group.getChildNodes();

        for(int i = 0; i < list.getLength(); ++i){
            Element group_el;
            try{
                group_el = (Element)list.item(i);
            }catch(ClassCastException e){
                continue;
            }
            if(!group_el.getNodeName().equalsIgnoreCase("wargearGroup"))
                continue;
            
            this.parseWargearGroup(group_el, army, grp);
        }        
        
        
        //list = group.getElementsByTagName("wargearItem");
        list = group.getChildNodes();
        
        for(int i = 0; i < list.getLength(); ++i){
            Element item;
            try{
                item = (Element)list.item(i);
            }catch(ClassCastException e){
                continue;
            }
            if(!item.getNodeName().equalsIgnoreCase("wargearItem"))
                continue;
            
            ArmylistWargearItem aItem = new ArmylistWargearItem(item.getAttribute("name"), this.armyInProgress);
            aItem.setPointcost(Double.parseDouble(item.getAttribute("pointcost")));
            StringTokenizer st = new StringTokenizer(item.getAttribute("weapon"), ",");
            while(st.hasMoreTokens()){
                String name = st.nextToken();
                aItem.addWeapon(this.armyInProgress.getWeaponByName(name));
            }
            
            
            grp.addItem(aItem);
        }
        
        if(parent != null){
            parent.addSubGroup(grp);
            
        }else{
            army.addWargearGroup(grp);
        }
    }
    
    private void parseUnits(ArmylistArmy army){
        NodeList unitsList = this.document.getElementsByTagName("unit");
        for(int i = 0; i < unitsList.getLength(); ++i){
            army.addUnit( this.parseUnit((Element)unitsList.item(i), army) );
        }
    }
    
    private ArmylistUnit parseUnit(Element unit, ArmylistArmy army){
        
        ArmylistUnit listUnit = new ArmylistUnit(unit.getAttribute("name"), army);
        listUnit.setMinCount(Integer.parseInt(unit.getAttribute("minCount")));
        listUnit.setMaxCount(Integer.parseInt(unit.getAttribute("maxCount")));
        listUnit.setMaxUnitSize(Integer.parseInt(unit.getAttribute("maxUnitSize")));
        listUnit.setMinUnitSize(Integer.parseInt(unit.getAttribute("minUnitSize")));
        listUnit.setUnitType(unit.getAttribute("unitType"));
        
        NodeList list = unit.getElementsByTagName("allowedUpdates");
        if(list.getLength() > 0){
            Element allowedUpdates = (Element)list.item(0);
            String allowedUpdatesStr = allowedUpdates.getAttribute("value");
            StringTokenizer st = new StringTokenizer(allowedUpdatesStr, ",");
            while(st.hasMoreTokens()){
                String s = st.nextToken();
                String id = s.substring(s.lastIndexOf(UNIT_UPDATE_ID_PREFIX) + UNIT_UPDATE_ID_PREFIX.length());
                listUnit.addUpdate(Integer.parseInt(id));
                this.parseUnitUpdate(s, id);
            }
        }        
        
        list = unit.getElementsByTagName("allowedModels");
        if(list.getLength() > 0){
            Element allowedModels = (Element)list.item(0);
            String allowedModelsStr = allowedModels.getAttribute("value");
            StringTokenizer st = new StringTokenizer(allowedModelsStr, ",");
            while(st.hasMoreTokens()){
                String s = st.nextToken();
                String id = s.substring(s.lastIndexOf(MODEL_ID_PREFIX) + MODEL_ID_PREFIX.length());
                
                listUnit.addModel(Integer.parseInt(id));
                this.parseModel(s, id, army);
            }
        } 
        
        list = unit.getElementsByTagName("exclusiveModels");
        for(int i = 0; i < list.getLength() ; ++i){
            logger.debug("Found exclusive tag");
            Element exclElement = (Element)list.item(i);
            int id = Integer.parseInt(exclElement.getAttribute("modelId"));
            
            if(exclElement.getAttribute("inclusive") != null && exclElement.getAttribute("inclusive").compareToIgnoreCase("TRUE") == 0){
                StringTokenizer st = new StringTokenizer(exclElement.getAttribute("otherModelIds"), ",");
                while(st.hasMoreTokens()){
                    listUnit.addInclusiveModelForModel(id, Integer.parseInt(st.nextToken()));
                }
            }else{
                StringTokenizer st = new StringTokenizer(exclElement.getAttribute("otherModelIds"), ",");
                while(st.hasMoreTokens()){
                    listUnit.addExclusiveModelForModel(id, Integer.parseInt(st.nextToken()));
                }
            }
        }
        
        return listUnit;
    }
    
    private void parseUnitUpdate(String id, String idToSave){
        if(this.unitUpdates.containsKey(idToSave)){
            return;
        }
        
        Element update = this.document.getElementById(id);
        ArmylistUnitUpdate unitUpdate = new ArmylistUnitUpdate(update.getAttribute("name"), this.armyInProgress);
        
        unitUpdate.setPointcost(Double.parseDouble(update.getAttribute("pointcost")));
        if(update.getAttribute("pointcostPerModel").compareToIgnoreCase("false") == 0){
            unitUpdate.setPointcostPerModel(false);
        }else{
            unitUpdate.setPointcostPerModel(true);
        }
        
        unitUpdate.setMinCount(Integer.parseInt(update.getAttribute("minCount")));
        unitUpdate.setMaxCount(Integer.parseInt(update.getAttribute("maxCount")));
        unitUpdate.setDefaultCount(Integer.parseInt(update.getAttribute("defaultSelectedAmount")));
        
        StringTokenizer st = new StringTokenizer(update.getAttribute("weapon"), ",");
        while(st.hasMoreTokens()){
            String name = st.nextToken();
            unitUpdate.addWeapon(this.armyInProgress.getWeaponByName(name));
        }    
        
        this.unitUpdates.put(idToSave, unitUpdate);
        return;
    }
    

    private void parseModelUpdate(String id, String idToSave){
        if(this.modelUpdates.containsKey(idToSave)){
            return;
        }
        
        Element update = this.document.getElementById(id);
        ArmylistModelUpdate modelUpdate = new ArmylistModelUpdate(update.getAttribute("name"), this.armyInProgress);
        
        
        modelUpdate.setPointcost(Double.parseDouble(update.getAttribute("pointcost")));
        if(update.getAttribute("pointcostPerModel").compareToIgnoreCase("false") == 0){
            modelUpdate.setPointcostPerModel(false);
        }else{
            modelUpdate.setPointcostPerModel(true);
        }
        
        modelUpdate.setMinCount(Integer.parseInt(update.getAttribute("minCount")));
        modelUpdate.setMaxCount(Integer.parseInt(update.getAttribute("maxCount")));
        modelUpdate.setDefaultCount(Integer.parseInt(update.getAttribute("defaultSelectedAmount")));
        
        StringTokenizer st = new StringTokenizer(update.getAttribute("weapon"), ",");
        while(st.hasMoreTokens()){
            String name = st.nextToken();
            modelUpdate.addWeapon(this.armyInProgress.getWeaponByName(name));
        }
        
        StringTokenizer st2 = new StringTokenizer(update.getAttribute("stats"), ",");
        while(st2.hasMoreTokens()){
            String stat = st2.nextToken();
            String updatevalue = st2.nextToken();
            modelUpdate.addStatModification(stat, updatevalue);
        }
        
        this.modelUpdates.put(idToSave, modelUpdate);
        return;
    }

    
    private void parseModel(String id, String idToSave, ArmylistArmy army){
        if(this.models.containsKey(idToSave)){
            return;
        }
        
        Element modelEl = this.document.getElementById(id);
        ArmylistModel model = new ArmylistModel(modelEl.getAttribute("name"), army);
        model.setPointCostPerModel(Double.parseDouble(modelEl.getAttribute("pointcost")));
        model.setMinCount(Integer.parseInt(modelEl.getAttribute("minCount")));
        model.setMaxCount(Integer.parseInt(modelEl.getAttribute("maxCount")));
        model.setStatType(modelEl.getAttribute("statType"));
        model.setDefaultSelectedAmount(Integer.parseInt(modelEl.getAttribute("defaultSelectedAmount")));
        model.setStatValues(modelEl.getAttribute("statValues"), ",");
        if(modelEl.getAttribute("counted").equalsIgnoreCase("false")){
            model.setIsCounted(false);
        }else{
            model.setIsCounted(true);
        }
        
        if(modelEl.getAttribute("isLinked").equalsIgnoreCase("false")){
            model.setLinked(false);
        }else{
            model.setLinked(true);
        }
        
        NodeList list = modelEl.getElementsByTagName("allowedUpdates");
        if(list.getLength() > 0){
            Element allowedUpdates = (Element)list.item(0);
            String allowedUpdatesStr = allowedUpdates.getAttribute("value");
            StringTokenizer st = new StringTokenizer(allowedUpdatesStr, ",");
            while(st.hasMoreTokens()){
                String s = st.nextToken();
                String id_ = s.substring(s.lastIndexOf(MODEL_UPDATE_ID_PREFIX) + MODEL_UPDATE_ID_PREFIX.length());
                                
                
                model.addUpdate(Integer.parseInt(id_));
                this.parseModelUpdate(s, id_);
            }
        }
            
        list = modelEl.getElementsByTagName("allowedWargearGroups");
        if(list.getLength() > 0){
            Element allowedWG = (Element)list.item(0);
            String allowedWGStr = allowedWG.getAttribute("value");
            StringTokenizer st = new StringTokenizer(allowedWGStr, ",");
            while(st.hasMoreTokens()){
                model.addWargearGroup(st.nextToken());
            }
        }
        
        
        list = modelEl.getElementsByTagName("allowedWargear");
        if(list.getLength() > 0){
            Element allowedWG = (Element)list.item(0);
            model.setAllowedWargear(Double.parseDouble(allowedWG.getAttribute("value")));
            
            list = allowedWG.getElementsByTagName("subWGGroupsAllowed");
            for(int i = 0; i < list.getLength(); ++i){
                Element subGroup = (Element)list.item(i);
                model.setSubWGGroupAllowedAmount(subGroup.getAttribute("groupName"), Double.parseDouble(subGroup.getAttribute("allowed")) );
            }
            
            
        }
            
        this.models.put(idToSave, model);
        return;
    }    
    
    
}
