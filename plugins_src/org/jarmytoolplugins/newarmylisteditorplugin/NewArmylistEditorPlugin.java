/*
 * ArmylistEditorPlugin.java
 *
 * Created on 06 October 2003, 16:26
 */

package plugins_src.org.jarmytoolplugins.newarmylisteditorplugin;

import javax.swing.JMenuItem;
import org.jArmyTool.plugininterface.JArmyToolPluginInterface;
import org.jArmyTool.internaldata.*;
import org.jarmytoolplugins.newarmylisteditorplugin.engine.ArmylistEditorGUICore;

//import org.jarmytoolplugins.armylisteditorplugin.engine.*;
/**
 *
 * @author  pasleh
 */
public class NewArmylistEditorPlugin implements JArmyToolPluginInterface{
    
    private GUICommands guiCommands;
    private GlobalData globalData;
    private CurrentData currentData;
    
    /** Creates a new instance of ArmylistEditorPlugin */
    public NewArmylistEditorPlugin() {
    }
    
    public void exit() {
    }
    
    public String getSplasText() {
        return "Initializing new armylist editor plugin";
    }
    
    public void initialize(GUICommands gui, GlobalData gd, CurrentData cd) {
        this.guiCommands = gui;
        this.globalData = gd;
        this.currentData = cd;
        
        JMenuItem editArmylist = new JMenuItem("edit armylist");
        editArmylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ArmylistEditorGUICore(currentData.getCurrentArmylistArmy(), guiCommands).displayEditor(guiCommands.getMainWindowDimension());
            }
        });
        
        this.guiCommands.addMenuItem(editArmylist, "Armylist");
    }
    
    public void start() {
    }
    
    public void stop() {
    }
    
}
