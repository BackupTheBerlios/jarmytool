/*
 * NamedUnitsListAccessBean.java
 *
 * Created on 17 January 2004, 00:24
 */

package core_src.src.org.jArmyTool.data.database;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.jArmyTool.data.dataBeans.army.Army;
import org.jArmyTool.util.Config;

/**
 *
 * @author  pasi
 */
public class NamedUnitsListAccessBean {
    
    public static final String ARMY_NAME_FOR_NAMED = "NAMED UNITS STORAGE";
    
    private static NamedUnitsListAccessBean instance = null;
    
    private static Config config = Config.getInstance("namedUnits");
    private Logger logger = Logger.getLogger(NamedUnitsListAccessBean.class);
    
    private static ArmyAccessBean aab = ArmyAccessBean.getInstance();
    
    private HashMap namedUnitsByGameSystem;
    
    /** Singleton*/
    private NamedUnitsListAccessBean() {
        this.namedUnitsByGameSystem = new HashMap();
        this.loadSaved();
    }
    
    public static NamedUnitsListAccessBean getInstance(){
        if(instance == null)
            instance = new NamedUnitsListAccessBean();
        return instance;
    }
    
    
    
    private void loadSaved(){
        String location = this.config.getProperty("NAMED_UNITS_ARMY_DIRECTORY");
        File[] files = null;
        try{
            files = new File(location).listFiles();
        }catch(Exception e){
            logger.warn("Couldn't find namedUnits dir");
            new File(location).mkdir();
            return;
        }
        
        if(files == null){
            logger.warn("Couldn't find namedUnits dir");
            new File(location).mkdir();
            return;
        }
        
        for(int i = 0; i < files.length; ++i){
            if(files[i].getName().endsWith(".xml")){
               logger.debug("Starting parse file: "+files[i]); 

               Army tempArmy = null;
               try{
                    tempArmy = this.aab.readArmyXMLByFile(files[i]);
               }catch(Exception e){
                    logger.warn("failed to read Army for named storage: "+files[i]);
                    continue;
               }
                
               if(tempArmy  == null)
                   continue;
               if(!tempArmy.getName().equalsIgnoreCase(ARMY_NAME_FOR_NAMED))
                   continue;
               
               HashMap namedUnitStorages = (HashMap)this.namedUnitsByGameSystem.get(tempArmy.getArmylistArmy().getGameSystem().getName());
               if(namedUnitStorages == null){
                    namedUnitStorages = new HashMap();
                    this.namedUnitsByGameSystem.put(tempArmy.getArmylistArmy().getGameSystem().getName(), namedUnitStorages);
               }
               namedUnitStorages.put(tempArmy.getArmylistArmy().getName(), tempArmy);
            }               
        }
        
    }
    
    
    public void saveNamed(){
        logger.debug("Starting to writen named units to disk");
        
        String location = this.config.getProperty("NAMED_UNITS_ARMY_DIRECTORY");
        
        Iterator gameSystems = this.namedUnitsByGameSystem.keySet().iterator();
        while(gameSystems.hasNext()){
            String gameSystem = (String)gameSystems.next();
            Iterator storages = ((HashMap)this.namedUnitsByGameSystem.get(gameSystem)).values().iterator();
            while(storages.hasNext()){
                Army army = (Army)storages.next();
                this.aab.writeArmyXMLByFile(army, new File(location+ this.config.getProperty("NAMED_UNITS_FILE_PREFIX")+gameSystem + "_" + army.getArmylistArmy().getName() +".xml"));
                
            }
        }
    }
    
    public Army getNamedUnits(String armylistName, String gameSystemName){
        logger.debug("getNamedUnits() army:"+armylistName + "game system: "+gameSystemName);
        
        HashMap map = (HashMap)this.namedUnitsByGameSystem.get(gameSystemName);
        if(map == null){
            map = new HashMap();
            this.namedUnitsByGameSystem.put(gameSystemName, map);
        }
         
        Army temp = (Army)map.get(armylistName);
        if(temp == null){
            temp = new Army(ArmylistAccessBean.getInstance().getArmyByName(armylistName, gameSystemName));
            temp.setName(ARMY_NAME_FOR_NAMED);
            map.put(armylistName, temp);
        }
        return temp;
    }
    
    public void setNamedStorage(Army army){
        HashMap map = (HashMap)this.namedUnitsByGameSystem.get(army.getArmylistArmy().getGameSystem().getName());
        if(map == null){
            map = new HashMap();
            this.namedUnitsByGameSystem.put(army.getArmylistArmy().getGameSystem().getName(), map);
        }
        
        Army army_ = (Army)map.get(army.getArmylistArmy().getName());
        if(army_ == null){
            map.put(army.getArmylistArmy().getName(), army);
        }else{
            map.remove(army_);
            map.put(army.getArmylistArmy().getName(), army);
        }
    } 
}
