/*
 * JArmyToolPluginInterface.java
 *
 * Created on 22 September 2003, 12:39
  * Distributed under GPL
 */

package core_src.src.org.jArmyTool.plugininterface;

/**
 * This interface defines methods for jArmyTool plugins
 *
 * All plugins have to implement this interface.
 *
 * (c) Pasi Lehtimäki
 * 
 * @author  Pasi Lehtimäki 
 */

import org.jArmyTool.internaldata.*;

public interface JArmyToolPluginInterface {
    
    
    /**
     * This method is called before main window is shown to user.
     *
     * All parameter objects are initialized to use same internal data which is 
     * used by the core.
     */
    public void initialize(GUICommands gui, GlobalData gd, CurrentData cd);    
    
    /**
     * This method is called after main all plugin and core initialization is ready.
     */
    public void start();
    
    
    /**
     * This method is called when program is shutting down.
     */
    public void stop();
    
    /**
     * This method is called when program is shutting down adn all stop methods are called.
     * After exit method the core writes internal data to disk.
     */
    public void exit();
    
    /**
     * @return String containing text displayed in splash when this plugin is initialized. Can be null.
     */
    public String getSplasText();
}
