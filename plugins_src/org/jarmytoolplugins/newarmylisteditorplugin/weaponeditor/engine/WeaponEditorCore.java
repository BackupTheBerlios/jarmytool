/*
 * WeaponEditorCore.java
 *
 * Created on 14 October 2003, 16:12
 */

package org.jarmytoolplugins.newarmylisteditorplugin.weaponeditor.engine;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.LinkedList;
import org.jArmyTool.data.dataBeans.armylist.ArmylistArmy;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWeapon;
import org.jArmyTool.data.dataBeans.gameSystem.GameSystem;
//import org.jarmytoolplugins.newarmylisteditorplugin.engine.ArmylistEditorGUICore;
import org.jArmyTool.data.dataBeans.util.WeaponProfile;
import org.jArmyTool.internaldata.GUICommands;
import org.jarmytoolplugins.newarmylisteditorplugin.weaponeditor.components.WeaponEditorMainWindow;
import org.jarmytoolplugins.newarmylisteditorplugin.weaponeditor.components.WeaponTypePanel;



/**
 *
 * @author  pasleh
 */
public class WeaponEditorCore {
    
   // private ArmylistEditorGUICore core;
    private GameSystem gameSystem;
    private ArmylistArmy armylistArmy;
    private WeaponEditorMainWindow mainWindow;
    private LinkedList panels;
    
    
    /** Creates a new instance of WeaponEditorCore */
    public WeaponEditorCore(ArmylistArmy armylistArmy) {
        this.panels = new LinkedList();
        this.armylistArmy = armylistArmy;
        this.gameSystem = this.armylistArmy.getGameSystem();
     //   this.core = core;
        this.mainWindow = new WeaponEditorMainWindow(this);
        this.initPanels();
    }
    
    private void initPanels(){
        
        Iterator iterator = this.gameSystem.getWeaponProfiles().iterator();
        while(iterator.hasNext()){
            WeaponProfile profile = (WeaponProfile)iterator.next();
            WeaponTypePanel panel = new WeaponTypePanel(profile, this.armylistArmy.getWeapons(profile.getName()));
            this.mainWindow.addWeaponTypePanel(panel);
            this.panels.add(panel);
        }
    }
    
    public void saveOnExit(){
        Iterator iterator = this.panels.iterator();
        
        while(iterator.hasNext()){
            WeaponTypePanel panel = (WeaponTypePanel)iterator.next();
            String profileName = panel.getWeaponProfile().getName();
            this.armylistArmy.emptyWeapons(profileName);
            Iterator weapons = panel.getWeapons().iterator();
            while(weapons.hasNext()){
                this.armylistArmy.addWeapon(profileName, (ArmylistWeapon)weapons.next());
            }
        }
    }
    
    public void displayEditor(Dimension size){
        this.mainWindow.setSize(size);
        this.mainWindow.setIconImage(GUICommands.getInstance().getProgramIcon());
        this.mainWindow.show();
    }
    
}
