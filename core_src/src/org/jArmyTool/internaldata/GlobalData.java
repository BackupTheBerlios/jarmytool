/*
 * GlobalData.java
 *
 * Created on 22 September 2003, 12:48
 */

package org.jArmyTool.internaldata;

/**
 * This class contains global data used by core. 
 * 
 * This class is a singleton. So only one instance can be created. Use getInstance().
 * @author  Pasi Lehtimäki
 */
public class GlobalData {
    
    private static GlobalData instance;
    
    /** Creates a new instance of CurrentData */
    private GlobalData() {
    }
    
    public static GlobalData getInstance(){
        if(instance == null)
            instance = new GlobalData();
        
        return instance;
    }
    
}
