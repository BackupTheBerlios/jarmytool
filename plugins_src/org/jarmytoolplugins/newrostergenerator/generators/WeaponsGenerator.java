/*
 * WeaponsGenerator.java
 *
 * Created on 03 January 2004, 02:06
 */

package org.jarmytoolplugins.newrostergenerator.generators;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.jArmyTool.data.dataBeans.army.Army;
import org.jArmyTool.data.dataBeans.army.Model;
import org.jArmyTool.data.dataBeans.army.ModelUpdate;
import org.jArmyTool.data.dataBeans.army.Unit;
import org.jArmyTool.data.dataBeans.army.UnitUpdate;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearItem;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWeapon;
import org.jArmyTool.data.dataBeans.util.WeaponProfile;

/**
 *
 * @author  pasi
 */
public class WeaponsGenerator {
    
    private Army army;
    HashMap weapons;
    
    /** Creates a new instance of WeaponsGenerator */
    public WeaponsGenerator(Army army) {
        this.army = army;
        this.init();
    }
    
    private void init(){
        this.weapons = new HashMap();
        
        Iterator weaponProfiles = this.army.getArmylistArmy().getGameSystem().getWeaponProfiles().iterator();
        while(weaponProfiles.hasNext()){
            WeaponProfile profile = (WeaponProfile)weaponProfiles.next();
            weapons.put(profile, new LinkedList());
            
        }
        
        Iterator units = army.getSelectedUnits().iterator();
        
        while(units.hasNext()){
            Unit unit = (Unit)units.next();
            Iterator unitUpdates = unit.getUpdates().iterator();
            while(unitUpdates.hasNext()){
                UnitUpdate unitUpdate = (UnitUpdate)unitUpdates.next();
                if(unitUpdate.getSelectedCount() > 0 && unitUpdate.getArmylistUnitUpdate().isWeapon()){
                    Iterator updateWeapons = unitUpdate.getArmylistUnitUpdate().getWeapons().iterator();
                    while(updateWeapons.hasNext()){
                        ArmylistWeapon weapon = (ArmylistWeapon)updateWeapons.next();
                        LinkedList list = (LinkedList)weapons.get(weapon.getProfile());
                        if(!list.contains(weapon))
                            list.add(weapon);
                    }
                }
            }
            
            Iterator models = unit.getModels().iterator();
            while(models.hasNext()){
                Model model = (Model)models.next();
                
                Iterator modelUpdates = model.getUpdates().iterator();
                while(modelUpdates.hasNext()){
                    
                    ModelUpdate modelUpdate = (ModelUpdate)modelUpdates.next();
                    if(modelUpdate.getSelectedCount() > 0 && modelUpdate.getArmylistModelUpdate().isWeapon()){
                        Iterator updateWeapons = modelUpdate.getArmylistModelUpdate().getWeapons().iterator();
                        while(updateWeapons.hasNext()){
                            ArmylistWeapon weapon = (ArmylistWeapon)updateWeapons.next();
                            LinkedList list = (LinkedList)weapons.get(weapon.getProfile());
                            if(!list.contains(weapon)){
                                list.add(weapon);
                            }
                        }
                    }
                }   
                
                if(model.getArmylistModel().getAllowedWargear() > 0){
                    Iterator wgGroups = model.getArmylistModel().getAllowedWargearGroups().iterator();
                    while(wgGroups.hasNext()){
                        Iterator wargear = model.getSelectedWargear((String)wgGroups.next()).iterator();
                        while(wargear.hasNext()){
                            ArmylistWargearItem item = (ArmylistWargearItem)wargear.next();
                            if(item.isWeapon()){
                                Iterator weaponIterator = item.getWeapons().iterator();
                                while(weaponIterator.hasNext()){
                                    ArmylistWeapon weapon = (ArmylistWeapon)weaponIterator.next();
                                    LinkedList list = (LinkedList)weapons.get(weapon.getProfile());
                                    if(!list.contains(weapon))
                                        list.add(weapon);                                
                                }
                            }
                        }
                        
                    }
                }
                   
            }
        }        
        
    }
    
    
    public StringBuffer generateAllWeapos(){
        
        StringBuffer ret = new StringBuffer();
        ret.append("<h2>Weapons</h2>\n");
        
        ret.append("<table class=\""+ CSSGenerator.CSS_STYLE_NAME_UNIT_BOX +"\" width=\"500\" border=\"0\" cellspacing=\"0\" cellpadding=\"3\">\n");
        
        int lineCount = 0;
        Iterator profiles = this.weapons.keySet().iterator();
        while(profiles.hasNext()){
            StringBuffer oneType = new StringBuffer();
            boolean hasWeapons = false;
            lineCount = 0;
            WeaponProfile profile = (WeaponProfile)profiles.next();
            oneType.append("<tr>\n<td class=\""+ CSSGenerator.CSS_STYLE_NAME_UNIT_NAME +"\">"+ profile.getName() +"</td>\n</tr>\n");
            oneType.append("<tr class=\""+ CSSGenerator.CSS_STYLE_NAME_UNIT_STAT_NAME_BOX +"\"\n><td width=\"30%\">&nbsp;</td>");
            Iterator headers = profile.getHeaders().iterator();
            while(headers.hasNext()){
                oneType.append("<td width=\"7%\" align=\"right\"><b>"+(String)headers.next()+"</b></td>");
            }
            oneType.append("</tr>\n");
            
            Iterator weaponIterator = ((LinkedList)this.weapons.get(profile)).iterator();
            while(weaponIterator.hasNext()){
                hasWeapons = true;
                if(lineCount % 2 == 0){
                    oneType.append("<tr class=\""+CSSGenerator.CSS_STYLE_NAME_UNIT_BG_1+"\">");
                }else{
                    oneType.append("<tr class=\""+CSSGenerator.CSS_STYLE_NAME_UNIT_BG_2+"\">");
                }
                
                ArmylistWeapon weapon = (ArmylistWeapon)weaponIterator.next();
                oneType.append("<td align=\"left\" valign=\"top\"><b>" + weapon.getName() + "</b></td>\n");
                Iterator weaponProfile = weapon.getStats().iterator();
                while(weaponProfile.hasNext()){
                    oneType.append("<td align=\"right\" valign=\"top\">" + (String)weaponProfile.next() + "</td>\n");
                }
                oneType.append("</tr>");
                ++lineCount;
            }
            if(hasWeapons)
                ret.append(oneType);
            
        }
        
        
        
        
        ret.append("</table>");
        return ret;
    }
    
}
