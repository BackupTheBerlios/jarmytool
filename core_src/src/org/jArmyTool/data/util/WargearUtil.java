/*
 * WargearUtil.java
 *
 * Created on 12 July 2004, 03:50
 */

package org.jArmyTool.data.util;

import java.util.Iterator;
import org.jArmyTool.data.dataBeans.army.Model;

import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearGroup;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearItem;

/**
 *
 * @author  pasi
 */
public class WargearUtil {
    
    /** No need to create an instance of this class */
    private WargearUtil() {
    }
    
    public static boolean checkIfAllowed(Model model, ArmylistWargearGroup group){
        Iterator requiredItems = group.getRequiredItems().iterator();
        while(requiredItems.hasNext()){
            String item = (String)requiredItems.next();
            String groupName = item.substring(0, item.lastIndexOf("."));
            String itemName = item.substring(item.lastIndexOf(".")+1);
            
            System.out.println("Found required item. Group: "+ groupName + " item: "+ itemName);
            
            Iterator selected = model.getSelectedWargear(groupName).iterator();
            boolean found = false;
            while(selected.hasNext()){
                String name = ((ArmylistWargearItem)selected.next()).getName();
                if(name.equalsIgnoreCase(itemName)){
                    found = true;
                    break;
                }
            }
            if(!found){
                System.out.println("Not found!!! -FALSE");
                return false;
            }
            
        }
        
        return true;
    }
    
}
