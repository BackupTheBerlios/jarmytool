/*
 * WargearEditorGUICore.java
 *
 * Created on 20 May 2003, 16:24
 */

package plugins_src.org.jarmytoolplugins.newarmylisteditorplugin.wargearEditor.engine;

import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.internaldata.GUICommands;

import org.jarmytoolplugins.newarmylisteditorplugin.wargearEditor.components.WargearEditorMainWindow;
//import org.jarmytoolplugins.newarmylisteditorplugin.engine.ArmylistEditorGUICore;
/**
 *
 * @author  pasleh
 */
public class WargearEditorGUICore {
    
    private ArmylistArmy armylistArmy;
    
    private WargearEditorMainWindow mainWindow;
    
    //private ArmylistEditorGUICore core;
    
    /** Creates a new instance of WargearEditorGUICore */
    public WargearEditorGUICore(ArmylistArmy armylistArmy) {
       // this.core = core;
        this.armylistArmy = armylistArmy;
        
        this.mainWindow = new WargearEditorMainWindow(this.armylistArmy, this);
    }
    
    public void closeWindow(){
        this.mainWindow.saveData();
        this.mainWindow.dispose();
    }
    
    public void displayEditor(){
        this.mainWindow.setSize(GUICommands.getInstance().getMainWindowDimension());
        this.mainWindow.setIconImage(GUICommands.getInstance().getProgramIcon());
        this.mainWindow.show();
    }
    
}
