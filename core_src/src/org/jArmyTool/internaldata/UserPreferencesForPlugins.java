/*
 * UserPreferencesForPlugins.java
 *
 * Created on 04 January 2004, 01:24
 */

package core_src.src.org.jArmyTool.internaldata;

import org.jArmyTool.gui.engine.GUICore;

/**
 *
 * @author  pasi
 */
public class UserPreferencesForPlugins {
    
    private String plugin;
    
    private static GUICore core;
    
    
    /** Creates a new instance of UserPreferencesForPlugins */
    public UserPreferencesForPlugins(String plugin) {
        this.plugin = plugin;
    }
    
    public static void setCore(GUICore core_){
        core = core_;
    }
    
    public void setProperty(String key, String value){
        core.setUserProperty(this.plugin+"."+key, value);
    }
    
    public String getProperty(String key){
        return core.getUserProperty(this.plugin+"."+key);
    }
    
}
