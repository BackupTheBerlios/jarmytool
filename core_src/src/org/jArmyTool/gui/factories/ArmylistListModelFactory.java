/*
 * ListModelFactory.java
 *
 * Created on 20 December 2002, 23:39
 */

package core_src.src.org.jArmyTool.gui.factories;

import javax.swing.*;
import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.data.dataBeans.armylist.*;
import java.util.*;

/**
 *
 * @author  Pasi Lehtimäki
 */
public class ArmylistListModelFactory {
    
    private static ArmylistListModelFactory instance;
    
    /** Creates a new instance of ListModelFactory */
    private ArmylistListModelFactory() {
    }
    
    public static ArmylistListModelFactory getInstance(){
        if(instance == null)
            instance = new ArmylistListModelFactory();
        return instance;
    }
    
    
    public DefaultListModel getUnitChoises(ArmylistArmy armylistArmy){
        DefaultListModel model = new DefaultListModel();
        
        String[] types = armylistArmy.getGameSystem().getUnitTypeNames();
        for(int i = 0; i < types.length; ++i){
            Iterator iterator = armylistArmy.getUnitsByType(types[i]).iterator();
            while(iterator.hasNext()){
                ArmylistUnit unit = (ArmylistUnit)iterator.next();
                model.addElement(unit);
            }
        }

      return model;
    }

    public DefaultListModel getUnitChoises(ArmylistArmy armylistArmy, String unitType){
        DefaultListModel model = new DefaultListModel();
        
        Iterator iterator = armylistArmy.getUnitsByType(unitType).iterator();
        
        while(iterator.hasNext()){
            ArmylistUnit unit = (ArmylistUnit)iterator.next();
            model.addElement(unit);
        }
        
        /*while(iterator.hasNext()){
            ArmylistUnit unit = (ArmylistUnit)iterator.next();
            if(unit.getUnitType().equalsIgnoreCase(unitType))
               model.addElement(unit);
        }*/
      return model;
    }
    
}
