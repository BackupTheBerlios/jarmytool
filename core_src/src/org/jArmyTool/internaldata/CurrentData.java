/*
 * CurrentData.java
 *
 * Created on 22 September 2003, 12:42
 */

package core_src.src.org.jArmyTool.internaldata;

import org.jArmyTool.data.dataBeans.army.Army;
import org.jArmyTool.data.dataBeans.armylist.ArmylistArmy;

/**
 * This class is designed to contain current internal data of the program. 
 * An instance of this class is used by GUI Core and plugins to handle internal
 * data.
 *
 * This class is a singleton. So only one instance can be created. Use getInstance(). 
 * @author  Pasi Lehtimäki
 */
public class CurrentData {
    
    private static CurrentData instance;
    
    private ArmylistArmy armylistArmy;
    private Army army;
    
    /** Private for singleton */
    private CurrentData() {
    }
    
    /**
     * Singleton
     * @return the only instance of this class
     */    
    public static CurrentData getInstance(){
        if(instance == null)
            instance = new CurrentData();
        
        return instance;
    }
    
    /**
     * This method returns current armylist in use by UI.
     * @return ArmylistArmy
     */    
    public ArmylistArmy getCurrentArmylistArmy(){
        return this.armylistArmy;
    }
    
    /** 
     * This method is used by Core. Settin armylist will confuse the UI.
     * <br>
     * <br>
     * This will be fixed later  
     *   
     * @param army ArmylistArmy
     */    
    public void setCurrentArmylistArmy(ArmylistArmy army){
        this.armylistArmy = army;
    }
    
    /**
     * This method returns Army currently in use by UI
     * @return Army
     */    
    public Army getCurrentArmy(){
        return this.army;
    }
    
    /**
     * This method is used by Core. Settin army will confuse the UI.
     * <br>
     * <br>
     * This will be fixed later
     *
     * @param army
     */    
    public void setCurrentArmy(Army army){
        this.army = army;
    }
    
}
