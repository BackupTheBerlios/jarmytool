/*
 * Config.java
 *
 * Created on 30 December 2002, 18:11
 */

package core_src.src.org.jArmyTool.util;


import java.util.*;
import java.io.*;
import org.apache.log4j.Logger;

/**
 *
 * @author  pasi
 */
public class Config {
    
    private static Properties confCore = new Properties();
    private static Logger logger;
    private static HashMap configsMap = new HashMap();
    
    private Properties props;
    
    static{
        logger = Logger.getLogger(Config.class);
        try{
            confCore.load(new FileInputStream("conf/confCore.properties"));
        }catch(Exception e){
            logger.error("Error reading coreConf file", e);
            e.printStackTrace();
        }
    }
    
    /** Creates a new instance of Config */
    private Config(String conf) {
        String filename = confCore.getProperty(conf);
        this.props = new Properties();
        try{
            this.props.load(new FileInputStream(filename));
        }catch(Exception e){
            logger.error("Error reading configuration file", e);
            e.printStackTrace();
        }
    }
    
    public static Config getInstance(String conf){
        if(configsMap.get(conf) != null){
            return (Config)configsMap.get(conf);
        }
        
        Config config = new Config(conf);
        configsMap.put(conf, config);
        
        return config;
    }
    
    
    public String getProperty(String key){
        return this.props.getProperty(key);
    }
    
}
