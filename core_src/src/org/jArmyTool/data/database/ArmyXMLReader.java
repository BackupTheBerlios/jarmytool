/*
 * ArmyXMLReader.java
 *
 * Created on 25 March 2003, 16:08
 */

package org.jArmyTool.data.database;

import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.data.dataBeans.armylist.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.util.*;
import java.io.*;
import org.apache.log4j.Logger;

/**
 *
 * @author  pasleh
 */
public class ArmyXMLReader {
    
    private ArmylistAccessBean aab;
    
    private DocumentBuilder documentBuilder;
    
    private Document document;
    
    private HashMap armylistUnits;
    
    private boolean initFailed = false;
    
    private static Logger logger = Logger.getLogger(ArmyXMLReader.class);
    
    /** Creates a new instance of ArmyXMLReader */
    protected ArmyXMLReader(File file) {
        this.aab = ArmylistAccessBean.getInstance();
        this.armylistUnits = new HashMap();
        
        try{
            this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.document = this.documentBuilder.parse(file);
        }catch(Exception e){
            logger.error("Exception while reading army file", e);
            this.initFailed = true;
        }
    }
    
    protected Army getArmy(){
        if(this.initFailed)
            return null;
        //System.out.println("getArmy()");
        
        Army army = null;
        
        NodeList list = this.document.getElementsByTagName("generalInfo");
        if(list.getLength() > 0){
            army = this.parseGeneralData((Element)list.item(0));
        }else{
            //LOG!!
            System.out.println("Crappy file 1");
        }
        
        
        this.parseUnits(this.document.getElementsByTagName("unit"), army);
        
        return army;
    }
    
    private Army parseGeneralData(Element generalData){
        Army ret = null;
        
        NodeList list = generalData.getElementsByTagName("armylist");
        if(list.getLength() > 0){
            Element armylistEl = (Element)list.item(0);
            
            ArmylistArmy armylistArmy = this.aab.getArmyByName(armylistEl.getAttribute("name"), armylistEl.getAttribute("gameSystem")); 
            if (armylistArmy == null){
               //LOG!!
                logger.error("Malfunctioned army file at point 1");
            }
            try{
                ret = new Army(armylistArmy);
                this.createUnitHashMap(armylistArmy);
                
            }catch (Exception e){
                logger.error("failed to create Army instance", e);
            }
        }else{
            logger.error("Malfunctioned army file at point 2");
        }        
        
        list = generalData.getElementsByTagName("points");
        if(list.getLength() > 0){
            Element armyEl = (Element)list.item(0);
            try{
                ret.setTargetPointcost((Double.parseDouble(armyEl.getAttribute("targetPointcost"))));
            }catch(NumberFormatException ex){
                ret.setTargetPointcost(ret.getArmylistArmy().getGameSystem().getDefaultTargetPointcost());
            }
        }else{
            ret.setTargetPointcost(ret.getArmylistArmy().getGameSystem().getDefaultTargetPointcost());
        }
        
        
        list = generalData.getElementsByTagName("army");
        if(list.getLength() > 0){
            Element armyEl = (Element)list.item(0);
            ret.setName(armyEl.getAttribute("name"));
        }else{
            logger.error("Malfunctioned army file at point 3");
        }
        
        return ret;
    }
    
    private void createUnitHashMap(ArmylistArmy armylistArmy){
        Iterator iterator = armylistArmy.getUnits().iterator();
        while(iterator.hasNext()){
            ArmylistUnit unit = (ArmylistUnit)iterator.next();
            this.armylistUnits.put(unit.getName(), unit);
        }
    }
    
    
    private void parseUnits(NodeList units, Army army){
        for(int i = 0; i < units.getLength(); ++i){
            this.parseUnit((Element)units.item(i), army);
        }
    }
    
    private void parseUnit(Element unit, Army army){
        NodeList list = unit.getElementsByTagName("armylistUnit");
        Element armylistUnitEl = (Element)list.item(0);
        
        ArmylistUnit armylistUnit = (ArmylistUnit)this.armylistUnits.get(armylistUnitEl.getAttribute("name"));
        if(armylistUnit == null){
            //LOG!!!
            return;
        }
        Unit armyUnit = new Unit(armylistUnit);
        
        list = unit.getElementsByTagName("armyUnit");
        Element armyUnitEl = (Element)list.item(0);
        
        armyUnit.setName(armyUnitEl.getAttribute("name")); 
        
        list = unit.getElementsByTagName("model");
        for(int i = 0; i < list.getLength(); ++i){
            this.parseModel((Element)list.item(i), armyUnit);
        }
        
        this.parseUnitUpdates(unit.getElementsByTagName("unitUpdate"), armyUnit);
        
        army.addUnit(armyUnit);
    }
    
    private void parseModel(Element modelEl, Unit unit){
        NodeList list = modelEl.getElementsByTagName("armylistModel");
        Element armylistModelEl = (Element)list.item(0);
        String name = armylistModelEl.getAttribute("name");        
        int id = Integer.parseInt(armylistModelEl.getAttribute("id"));  
        
        Iterator iterator = unit.getModels().iterator();
        Model model = null;
        while(iterator.hasNext()){
            Model tmp = (Model)iterator.next();
            if( unit.getArmylistUnit().getArmylistArmy().getIdForModel(tmp.getArmylistModel()) == id ){
                model = tmp;
                break;
            }
        }
        
        list = modelEl.getElementsByTagName("armyModel");
        Element armyModelEl = (Element)list.item(0);
        model.setModelCount(Integer.parseInt(armyModelEl.getAttribute("count")));
        
        NodeList wgNodes = armyModelEl.getElementsByTagName("wargearGroup");
        for(int i = 0; i < wgNodes.getLength(); ++i){
            this.parseWGGroup((Element)wgNodes.item(i), model);
        }
        
        
        this.parseModelUpdates(modelEl.getElementsByTagName("modelUpdate"), model);
        
    }
    
    private void parseWGGroup(Element wgGroupNode, Model model){
        String name = wgGroupNode.getAttribute("name");
        System.out.println("Name: "+name);
        StringTokenizer st  = new StringTokenizer(wgGroupNode.getAttribute("selectedItems"), ";");
        while(st.hasMoreTokens()){
            String itemName = st.nextToken();
            ArmylistWargearItem item = model.getArmylistModel().getArmylistArmy().getWargearItem(name, itemName);
            model.selectWargear(name, item);
        }
    }
    
    private void parseUnitUpdates(NodeList updates, Unit unit){
        for(int i = 0; i < updates.getLength(); ++i){
            this.parseUnitUpdate((Element)updates.item(i), unit);
        }
    }
    
    private void parseUnitUpdate(Element updateEl, Unit unit){
        NodeList list = updateEl.getElementsByTagName("armylistUnitUpdate");
        if(list.getLength() <= 0)
            return;
        Element armylistUnitUpdateEl = (Element)list.item(0);
        String name = armylistUnitUpdateEl.getAttribute("name");
        
        Iterator iterator = unit.getUpdates().iterator();
        
        UnitUpdate update = null;
        while(iterator.hasNext()){
            UnitUpdate tmp = (UnitUpdate)iterator.next();
            if( tmp.getName().equalsIgnoreCase(name) ){
                update = tmp;
                break;
            }
        }
        
        
        list = updateEl.getElementsByTagName("armyUnitUpdate");
        Element armyUnitUpdateEl = (Element)list.item(0);
        update.setSelectedCount(Integer.parseInt(armyUnitUpdateEl.getAttribute("count")));
        
    }

    private void parseModelUpdates(NodeList updates, Model model){
        for(int i = 0; i < updates.getLength(); ++i){
            this.parseModelUpdate((Element)updates.item(i), model);
        }        
    }
    
    private void parseModelUpdate(Element updateEl, Model model){
        NodeList list = updateEl.getElementsByTagName("armylistModelUpdate");
        if(list.getLength() <= 0)
            return;
        
        Element armylistModelUpdateEl = (Element)list.item(0);
        String name = armylistModelUpdateEl.getAttribute("name");
        
        Iterator iterator = model.getUpdates().iterator();
        
        ModelUpdate update = null;
        while(iterator.hasNext()){
            ModelUpdate tmp = (ModelUpdate)iterator.next();
            if( tmp.getName().equalsIgnoreCase(name) ){
                update = tmp;
                break;
            }
        }
        
        
        list = updateEl.getElementsByTagName("armyModelUpdate");
        Element armyModelUpdateEl = (Element)list.item(0);
        update.setSelectedCount(Integer.parseInt(armyModelUpdateEl.getAttribute("count")));
        
    }    
    
}
