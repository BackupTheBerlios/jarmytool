/*
 * ComponentFactory.java
 *
 * Created on 22 December 2002, 00:41
 */

package core_src.src.org.jArmyTool.gui.factories;



import org.jArmyTool.gui.components.*;
import org.jArmyTool.gui.engine.*;
import org.jArmyTool.data.dataBeans.army.*;
import javax.swing.*;
/**
 *
 * @author  pasi
 */
public class ComponentFactory {
    
    private static ComponentFactory instance;
    
    private org.jArmyTool.data.factories.Factory dataFactory;
    
    /** Creates a new instance of ComponentFactory */
    private ComponentFactory() {
        this.dataFactory = org.jArmyTool.data.factories.Factory.getInstance();
    }
    
    public static ComponentFactory getInstance(){
        if(instance == null)
            instance = new ComponentFactory();
        return instance;
    }
    
    
    public UnitPanel createUnitPanel(Unit unit, GUICore guiCore){
        UnitPanel unitPanel = new UnitPanel(unit, guiCore);
        return unitPanel;
    }

    public ModelPanel createModelPanel(Model model, GUICore guiCore, int statStartingPoint, int statSpacing, boolean displayStatHeaders){
        ModelPanel modelPanel = new ModelPanel(model, guiCore, statStartingPoint, statSpacing, displayStatHeaders);
        return modelPanel;
    }    

    public UnitUpdatePanel createUnitUpdatePanel(UnitUpdate unitUpdate, GUICore guiCore){
        UnitUpdatePanel unitUpdatePanel = new UnitUpdatePanel(unitUpdate, guiCore);
        return unitUpdatePanel;
    }
    
    /*
     * @deprecated
     */
    public ModelUpdatePanel createModelUpdatePanel(ModelUpdate modelUpdate, ModelPanel modelPanel, GUICore guiCore){
        /*ModelUpdatePanel modelUpdatePanel = new ModelUpdatePanel(modelUpdate, modelPanel, guiCore);
        return modelUpdatePanel;*/ return null;
    }    
}
