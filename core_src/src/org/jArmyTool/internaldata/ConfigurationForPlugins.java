/*
 * ConfigurationForPlugins.java
 *
 * Created on 06 October 2003, 20:48
 */

package core_src.src.org.jArmyTool.internaldata;

import java.util.*;

/**
 * This class is initialized by plugin loader. Each plugin which have configuration
 * tag in plugins.xml has Properties object stored in this class. 
 * This Property instance is got using getPluginConf(<name>).
 *
 * @author  Pasi Lehtimäki
 */
public class ConfigurationForPlugins {
    
    private static ConfigurationForPlugins instance;
    
    private HashMap plugins;
    
    /** Singleton*/
    private ConfigurationForPlugins() {
        this.plugins = new HashMap();
    }
    
    
    /**
     * This class is Singleton. This method is used to get the only instance.
     * @return instance of this class
     */    
    public static ConfigurationForPlugins getInstance(){
        if(instance == null)
            instance = new ConfigurationForPlugins();
        return instance;
    }
    
    /**
     * This method is used by the plugin loader. Plugins shouldn't mess with this one.
     *
     * @param name String - plugin name found in plugins.xml
     * @param pluginConf Property object to be stored
     */    
    public void addPluginConf(String name, Properties pluginConf){
        this.plugins.put(name, pluginConf);
    }
    
    /**
     * @param pluginName String - the name found in plugins.xml
     * @return
     */    
    public Properties getPluginConf(String pluginName){
        return (Properties)this.plugins.get(pluginName);
    }
    
}
