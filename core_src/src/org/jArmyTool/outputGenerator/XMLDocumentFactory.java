/*
 * XMLDocumentFactory.java
 *
 * Created on 20 March 2003, 00:42
 */

package org.jArmyTool.outputGenerator;

import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.data.dataBeans.armylist.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.util.*;
/**
 *
 * @author  pasi
 */
public class XMLDocumentFactory {
    
    private Army army;
    private ArmylistArmy armylistArmy;
    
    private DocumentBuilder builder;
    
    private Document document;
    private Element rootElement;
    
    /** Creates a new instance of XMLDocumentFactory */
    public XMLDocumentFactory(Army army) {
        this.army = army;
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            this.builder = factory.newDocumentBuilder();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public Document createDOM(){
        
        this.document = this.builder.newDocument();
        
        this.rootElement = this.document.createElement("armyData");
        this.document.appendChild(rootElement);
        
        
        Element general = this.document.createElement("generalInfo");
        this.rootElement.appendChild(general);
        this.addGeneralInfo(general);
        
        Element units = this.document.createElement("units");
        this.rootElement.appendChild(units);
        this.addUnits(units);
        
        return this.document;
    }
    
    private void addGeneralInfo(Node node){
        
        //armylist
        Element armylist = this.document.createElement("armylist");
        armylist.setAttribute("name", this.army.getArmylistArmy().getName());
        armylist.setAttribute("gameSystem", this.army.getArmylistArmy().getGameSystem().getName());
        node.appendChild(armylist);
        //army
        Element army = this.document.createElement("army");
        army.setAttribute("name", this.army.getName());
        
        
        Element points = this.document.createElement("points");
        points.setAttribute("targetPointcost", ""+this.army.getTargetPointcost());
        
        node.appendChild(army);        
        node.appendChild(points);        
    }
    
    private void addUnits(Element node){
        Iterator iterator = this.army.getSelectedUnits().iterator();
        
        while(iterator.hasNext()){
            Unit temp = (Unit)iterator.next();
            this.addUnit(temp, node);
        }
    }
    
    private void addUnit(Unit unit, Element node){
        Element unitEl = this.document.createElement("unit");
        
        //armylist
        ArmylistUnit armylistUnit = unit.getArmylistUnit();
        
        Element listunitElement = this.document.createElement("armylistUnit"); 
        listunitElement.setAttribute("name", armylistUnit.getName());
        unitEl.appendChild(listunitElement);        
        
        //army
        Element unitElement = this.document.createElement("armyUnit"); 
        unitElement.setAttribute("name", unit.getName());
        unitEl.appendChild(unitElement);
        
        Element models = this.document.createElement("models");
        
        Iterator iterator = unit.getModels().iterator();
        while(iterator.hasNext()){
            addModel((Model)iterator.next(), models);
        }
        
        
        unitEl.appendChild(models);
        
        Element updates = this.document.createElement("unitUpdates");
        
        Iterator iterator2 = unit.getUpdates().iterator();
        
        while(iterator2.hasNext()){
            this.addUnitUpdate((UnitUpdate)iterator2.next(), updates);
        }
        unitEl.appendChild(updates);
        
        
        node.appendChild(unitEl);
        
    }
    
    
    //WARGEAR NOT HANDLED YET!!!
    private void addModel(Model model, Element node){
        Element modelEl = this.document.createElement("model");
        
        //armylist
        ArmylistModel armylistModel = model.getArmylistModel();
        
        Element listModel = this.document.createElement("armylistModel");
        listModel.setAttribute("name", armylistModel.getName());
        listModel.setAttribute("id", ""+armylistModel.getArmylistArmy().getIdForModel(armylistModel));
        
        modelEl.appendChild(listModel);
        
        //army
        Element armyModel = this.document.createElement("armyModel");
       
        armyModel.setAttribute("count", ""+model.getModelCount());
        
       //wargear
        Iterator groupIterator = armylistModel.getArmylistArmy().getWargearGroups().iterator();
        while(groupIterator.hasNext()){            
            String groupName = ((ArmylistWargearGroup)groupIterator.next()).getName();
            if(model.getSelectedWargear(groupName) == null || model.getSelectedWargear(groupName).isEmpty())
                continue;
            this.addWargearGroup(groupName, model.getSelectedWargear(groupName), armyModel);
        }
            

        
        
        modelEl.appendChild(armyModel);
  
        Element updates = this.document.createElement("modelUpdates");
        
        Iterator iterator = model.getUpdates().iterator();
        
        while(iterator.hasNext()){
            this.addModelUpdate((ModelUpdate)iterator.next(), updates);
        }
        
        modelEl.appendChild(updates);
        node.appendChild(modelEl);
        
    }
    
    
    private void addWargearGroup(String groupName, Collection col, Element node){
        Element wgGroupEl = this.document.createElement("wargearGroup");
        wgGroupEl.setAttribute("name", groupName);
        String items = "";
        Iterator iterator = col.iterator();
        while(iterator.hasNext()){
            if(items.length() > 0)
                items = items + ";";
            items = items + ((ArmylistWargearItem)(iterator.next())).getName();
        }
        wgGroupEl.setAttribute("selectedItems", items);
        
        node.appendChild(wgGroupEl);
    }
    
    private void addUnitUpdate(UnitUpdate update, Element node){
        Element updateEl = this.document.createElement("unitUpdate");
        
        //armylist
        ArmylistUnitUpdate armylistUpdate= update.getArmylistUnitUpdate();
        
        Element armylistUpdateEl = this.document.createElement("armylistUnitUpdate");
        armylistUpdateEl.setAttribute("name", armylistUpdate.getName());
        
        
        
        //army
        Element armyUpdateEl = this.document.createElement("armyUnitUpdate");
        armyUpdateEl.setAttribute("count",""+ update.getSelectedCount());
       
        
        updateEl.appendChild(armylistUpdateEl);
        updateEl.appendChild(armyUpdateEl);
        
        node.appendChild(updateEl);
    }

    private void addModelUpdate(ModelUpdate update, Element node){
        Element updateEl = this.document.createElement("modelUpdate");
        
        //armylist
        ArmylistModelUpdate armylistUpdate= update.getArmylistModelUpdate();
        
        Element armylistUpdateEl = this.document.createElement("armylistModelUpdate");
        armylistUpdateEl.setAttribute("name", armylistUpdate.getName());
        
        
        
        //army
        Element armyUpdateEl = this.document.createElement("armyModelUpdate");
        armyUpdateEl.setAttribute("name", update.getName());
        armyUpdateEl.setAttribute("count", ""+update.getSelectedCount());
        
        updateEl.appendChild(armylistUpdateEl);
        updateEl.appendChild(armyUpdateEl);
        
        node.appendChild(updateEl);
    }    
    
}
