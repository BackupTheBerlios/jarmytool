/*
 * ModelUpdateListItem.java
 *
 * Created on 19 May 2003, 20:12
 */

package plugins_src.org.jarmytoolplugins.newarmylisteditorplugin.util;

import org.jArmyTool.data.dataBeans.armylist.ArmylistModelUpdate;
/**
 *
 * @author  pasi
 */
public class ModelUpdateListItem {
    
    private int key;
    private ArmylistModelUpdate armylistModelUpdate;
    
    /** Creates a new instance of  */
    public ModelUpdateListItem(int key, ArmylistModelUpdate armylistModelUpdate) {
        this.key = key;
        this.armylistModelUpdate = armylistModelUpdate;
    }
    
    public int getKey(){
        return this.key;
    }
    
    public ArmylistModelUpdate getUpdate(){
        return this.armylistModelUpdate;
    }
    
    public String toString(){
        return this.armylistModelUpdate.getName();
    }
    
}
