/*
 * ModelListItem.java
 *
 * Created on 19 May 2003, 19:13
 */

package plugins_src.org.jarmytoolplugins.newarmylisteditorplugin.util;

import org.jArmyTool.data.dataBeans.armylist.ArmylistModel;
/**
 *
 * @author  pasi
 */
public class ModelListItem {
    
    private int key;
    private ArmylistModel armylistModel;
    
    private static String LINKED_POSTFIX = "(linked)";
    
    /** Creates a new instance of  */
    public ModelListItem(int key, ArmylistModel armylistModel) {
        this.key = key;
        this.armylistModel = armylistModel;
    }
    
    public int getKey(){
        return this.key;
    }
    
    public ArmylistModel getModel(){
        return this.armylistModel;
    }
    
    public String toString(){
        if(this.armylistModel.isLinked()){
            return this.armylistModel.getName() + LINKED_POSTFIX;
        }
        return this.armylistModel.getName();
        
    }    
}
