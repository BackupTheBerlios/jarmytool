/*
 * UserPreferences.java
 *
 * Created on 30 June 2003, 16:30
 */

package core_src.src.org.jArmyTool.gui.user;

import java.util.*;
import java.io.*;
/**
 *
 * @author  pasi
 */
public class UserPreferences {
    public static String LAST_USED_ARMYLIST = "lastUsedArmylist";
    public static String LAST_USED_GAMESYSTEM = "lastUsedGameSystem";
    
    public static String WINDOW_SIZE_WIDTH = "windowSize_width";
    public static String WINDOW_SIZE_HEIGHT = "windowSize_height";
    
    public static String FIRST_SPLIT_LOCATION = "firstSplitLocation";
    public static String SECOND_SPLIT_LOCATION = "secondSplitLocation";
    
    private static String USER_PREFERENCES_FILE_NAME = "user/userPreferences.properties";
    private static String PROPERTY_HEADER = "User Preferences";
    
    
    
    private Properties props;
    private static UserPreferences instance = new UserPreferences();
    
    /** Use getInstance() instead */
    private UserPreferences() {
        try{
            this.props = new Properties();
            this.props.load(new FileInputStream(new File(USER_PREFERENCES_FILE_NAME)));
        }catch(Exception e){
            //LOG!!!
            System.out.println("Can't init user preferences: ");
            e.printStackTrace();
        }
    }
    
    public static UserPreferences getInstance(){
        return instance;
    }
    
    public String getProperty(String key){
        return this.props.getProperty(key);
    }
    
    public void setProperty(String key, String value){
        this.props.setProperty(key, value);
    }
    
    public void writePropertiesToDisk(){
        try{
            this.props.store(new FileOutputStream(new File(USER_PREFERENCES_FILE_NAME)), PROPERTY_HEADER);
        }catch(Exception e){
            //LOG!!!
            System.out.println("Can't store user preferences: ");
            e.printStackTrace();            
        }
    }
    
}
