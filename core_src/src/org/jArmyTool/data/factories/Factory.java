package core_src.src.org.jArmyTool.data.factories;

import org.jArmyTool.data.dataBeans.army.*;
import java.util.*;
/**
 * Singelton factory class
 *
 * @author  Pasi Lehtimäki
 */
public class Factory {
    
    //The only instance
    private static Factory instance;
    
    /** Not alloved to create instances outside this class*/
    private Factory() {
    }
    
    /**
     * Call this method to get instance of this class
     */
    public static Factory getInstance(){
        if(instance == null)
            instance = new Factory();
        return instance;
    }
    
    public Army createArmy(org.jArmyTool.data.dataBeans.armylist.ArmylistArmy armylistArmy){
        Army army = new Army(armylistArmy);
        
        return army;
    }
    
    public Unit createUnit(org.jArmyTool.data.dataBeans.armylist.ArmylistUnit armylistUnit){
        Unit unit = new Unit(armylistUnit);
              
        return unit;
    }
    
    public Model createModel(org.jArmyTool.data.dataBeans.armylist.ArmylistModel armylistModel){
        Model model = new Model(armylistModel);
        return model;
    }
 
    public UnitUpdate createUnitUpdate(org.jArmyTool.data.dataBeans.armylist.ArmylistUnitUpdate armylistUnitUpdate, Unit unit){
        UnitUpdate unitUpdate = new UnitUpdate(armylistUnitUpdate, unit);
        
        return unitUpdate;
    }    
    
    public ModelUpdate createModelUpdate(org.jArmyTool.data.dataBeans.armylist.ArmylistModelUpdate armylistModelUpdate, Model model){
        ModelUpdate modelUpdate = new ModelUpdate(armylistModelUpdate, model);
        
        return modelUpdate;
    }
    

}
