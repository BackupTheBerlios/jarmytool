/*
 * GameSystemAccessBean.java
 *
 * Created on 01 July 2003, 13:15
 */

package core_src.src.org.jArmyTool.data.database;


import org.jArmyTool.data.dataBeans.gameSystem.*;

import org.jArmyTool.util.*;
import java.util.*;

import java.io.*;
/**
 *
 * @author  pasi
 */
public class GameSystemAccessBean {

    private static GameSystemAccessBean instance = new GameSystemAccessBean();
    
    private Config gameSystemsConfig;
    private String directory; 
    
    private HashMap gameSystems;
    private HashMap gameSystemFiles;
    
    private String[] gameSystemNames;
    
    /** Creates a new instance of GameSystemAccessBean */
    private GameSystemAccessBean() {
        this.gameSystemsConfig = Config.getInstance("gameSystems");
        
        this.loadGameSystems();
    }
    
    public static GameSystemAccessBean getInstance(){
        return instance;
    }
    
    private void loadGameSystems(){
        this.gameSystems = new HashMap();
        this.gameSystemFiles = new HashMap();
        String location = this.gameSystemsConfig.getProperty("gameSystemsLocation");
        
        File[] listFiles = new File(location).listFiles();
        
        for(int i = 0; i < listFiles.length; ++i){
            if(listFiles[i].getName().endsWith(".xml")){
               GameSystem gameSystem = this.parseFile(listFiles[i]);
               if(gameSystem != null){
                    this.gameSystems.put(gameSystem.getName(), gameSystem);
                    this.gameSystemFiles.put(gameSystem.getName(), listFiles[i]);
               }
            }
        }
        
        this.gameSystemNames = new String[this.gameSystems.size()];
        Iterator iterator = this.gameSystems.keySet().iterator();
        int i = 0;
        while(iterator.hasNext()){
            this.gameSystemNames[i] = (String)iterator.next();
            ++i;
        }
        
    }
    
    public String[] getGameSystemNames(){
        return this.gameSystemNames;
    }
    
    public GameSystem getGameSystemByName(String name){
        return (GameSystem)this.gameSystems.get(name);
    }
    
    private GameSystem parseFile(File file){
        DOMGameSystemDocumentHandler handler = new DOMGameSystemDocumentHandler();
        return handler.parseFile(file);
    }    
}
