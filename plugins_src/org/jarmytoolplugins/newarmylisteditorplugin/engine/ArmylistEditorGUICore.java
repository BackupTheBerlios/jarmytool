/*
 * ArmylistEditorGUICore.java
 *
 * Created on 27 October 2003, 19:09
 */

package org.jarmytoolplugins.newarmylisteditorplugin.engine;

import java.awt.Dimension;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import org.jArmyTool.data.dataBeans.armylist.ArmylistArmy;
import org.jArmyTool.data.dataBeans.armylist.ArmylistUnit;
import org.jArmyTool.internaldata.GUICommands;
import org.jarmytoolplugins.newarmylisteditorplugin.components.ArmylistEditorMainWindow;

/**
 *
 * @author  pasi
 */
public class ArmylistEditorGUICore {
    
    private ArmylistArmy army;
    private ArmylistEditorMainWindow mainWindow;
    
    private GUICommands guiCommands;
    
    /** Creates a new instance of ArmylistEditorGUICore */
    public ArmylistEditorGUICore(ArmylistArmy army, GUICommands guiCommands) {
        this.guiCommands = guiCommands;
        this.army = army;
        this.mainWindow = new ArmylistEditorMainWindow(this, army);
        this.mainWindow.setUnitsListModel(this.getUnitChoises());
        
        this.mainWindow.setTitle("Armylist Editor - " + this.army.getName() + " - jArmyTool v."+guiCommands.getVersion());
    }
    
    
    public void saveData(){
        this.mainWindow.saveData();
    }
    
    public void editorExit(){
        this.saveData();
        this.mainWindow.dispose();
        this.guiCommands.fireArmylistDataChange();
    }
    
    public void displayEditor(Dimension size){
        this.mainWindow.setSize(size);
        this.mainWindow.setIconImage(GUICommands.getInstance().getProgramIcon());
        this.mainWindow.show();
    }
    
    public void unitFilterSelected(String type){
        this.mainWindow.setUnitsListModel(this.getUnitChoises(type));
    }
    
    public void unitFilterCleared(){
        this.mainWindow.setUnitsListModel(this.getUnitChoises());
    }
    
    private DefaultListModel getUnitChoises(){
        DefaultListModel model = new DefaultListModel();
        
        String[] types = this.army.getGameSystem().getUnitTypeNames();
        for(int i = 0; i < types.length; ++i){
            Iterator iterator = this.army.getUnitsByType(types[i]).iterator();
            while(iterator.hasNext()){
                ArmylistUnit unit = (ArmylistUnit)iterator.next();
                model.addElement(unit);
            }
        }

      return model;
    }

    private DefaultListModel getUnitChoises(String unitType){
        DefaultListModel model = new DefaultListModel();
        
        Iterator iterator = this.army.getUnitsByType(unitType).iterator();
        
        while(iterator.hasNext()){
            ArmylistUnit unit = (ArmylistUnit)iterator.next();
            model.addElement(unit);
        }
      return model;
    }    
    
    public void newUnit(String type){
        ArmylistUnit newUnit = new ArmylistUnit("no name", this.army);
        if(type != null){
            newUnit.setUnitType(type);
        }else{
            newUnit.setUnitType(this.army.getGameSystem().getUnitTypeNames()[0]);
        }
        this.addUnit(newUnit);
    }
    
    public void addUnit(ArmylistUnit unit){
        this.army.addUnit(unit);
       
        this.mainWindow.setUnit(unit);
        
        if(this.mainWindow.isFiltered()){
            this.unitFilterSelected(unit.getUnitType());
        }else{
            this.unitFilterCleared();
        }        
    }
    
    public void removeUnit(ArmylistUnit unit){
        this.army.removeUnit(unit);
        if(this.mainWindow.isFiltered()){
            this.unitFilterSelected(unit.getUnitType());
        }else{
            this.unitFilterCleared();
        }        
    }
}
