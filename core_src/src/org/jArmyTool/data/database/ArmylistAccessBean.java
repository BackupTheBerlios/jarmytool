package core_src.src.org.jArmyTool.data.database;

import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.data.dataBeans.gameSystem.*;
import org.jArmyTool.util.*;
import org.jArmyTool.outputGenerator.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;

/**
 *
 * @author  Pasi Lehitmäki
 */
public class ArmylistAccessBean {
    
    private static ArmylistAccessBean instance;
    
    private XMLWriter armylistXMLWriter;
    
    //private SAXParser parser;
    private Config armylistConfig;
    
    private HashMap armylists;
    private String[] armylistNames;
    private HashMap armylistFiles;
    
    private HashMap armylistFilesByGameSystem;
    private HashMap armylistsByGameSystem;
    
    private static Logger logger = Logger.getLogger(ArmylistAccessBean.class);
    
    /** Creates a new instance of ArmylistAccessBean */
    private ArmylistAccessBean() {
        this.armylistsByGameSystem = new HashMap();
        this.armylistFilesByGameSystem = new HashMap();
        
        this.armylistConfig = Config.getInstance("armylists");
        
        
        this.armylistXMLWriter = new XMLWriter();
        this.armylistXMLWriter.setDoctypeString(this.armylistConfig.getProperty("armylistDoctypeString"));
        
        
        this.loadArmylists();
    }
    
    public static ArmylistAccessBean getInstance(){
        if(instance == null){
            instance = new ArmylistAccessBean();
        }
        return instance;
    }
    
    
/*    public ArmylistArmy cloneArmylist(String name, String toClone) throws Exception{
         
        String location = this.armylistConfig.getProperty("armylistsLocation");
        ArmylistArmy newList = new ArmylistArmy(name, this.getArmyByName(toClone));
        String[] newNames = new String[this.armylistNames.length + 1];
        int i = 0;
        for(i = 0; i < this.armylistNames.length; ++i){
            newNames[i] = this.armylistNames[i];
        }
        newNames[i] = name;
        this.armylists.put(name, newList);
        
        String filename = location + name+".xml";
        
        Set set = this.armylistFiles.entrySet();
        
        if(set.contains(new File(filename))){
            boolean foundValidFilename = false;
            int count = 1;
            while(!foundValidFilename){
                filename = location + name + count + ".xml";
                if(!set.contains(filename))
                    foundValidFilename = true;
            }
        }
        
        this.armylistFiles.put(name, new File(filename));
        this.armylistNames = newNames;
        
        return newList;        
    }*/
    
    
    
    public ArmylistArmy createNewArmylist(String name, GameSystem gameSystem) throws Exception{
        logger.debug("Starting new armylist: "+name); 
        
        String location = this.armylistConfig.getProperty("armylistsLocation")+gameSystem.getName()+"/";
        ArmylistArmy newList = new ArmylistArmy(name, gameSystem);
        
        String defaultName = location + name;
        
        HashMap fileNames = ((HashMap)this.armylistFilesByGameSystem.get(gameSystem.getName()));
        
        Collection files = fileNames.values();
        String newName = defaultName;
        boolean nameOK = false;
        int index = 2;
        while(nameOK){
            if(files.contains(new File(newName))){
                newName = defaultName.subSequence(0, defaultName.lastIndexOf(".")-1) + "_"+index+".xml";
                ++index;
                logger.debug("Filename allready in use. Adding index");
            }else{
                nameOK = true;
            }
        }
        if(!newName.endsWith(".xml"))
            newName = newName + ".xml";
        
        logger.debug("Found filename to use: "+newName);
        fileNames.put(newList.getName(), new File(newName));
        
        ((LinkedList)this.armylistsByGameSystem.get(gameSystem.getName())).add(newList);;
        
        return newList;
        
        
        /*
        
        String[] newNames = new String[this.armylistNames.length + 1];
        int i = 0;
        for(i = 0; i < this.armylistNames.length; ++i){
            newNames[i] = this.armylistNames[i];
        }
        newNames[i] = name;
        this.armylists.put(name, newList);
        
        String filename = location + name+".xml";
        
        Set set = this.armylistFiles.entrySet();
        
        if(set.contains(filename)){
            boolean foundValidFilename = false;
            int count = 1;
            while(!foundValidFilename){
                filename = location + name + count + ".xml";
                if(!set.contains(filename))
                    foundValidFilename = true;
            }
        }
        
        this.armylistFiles.put(name, new File(filename));
        this.armylistNames = newNames;
        return newList;*/
        
    }
    
    public void saveArmylist(ArmylistArmy listArmy, File file){
        XMLArmylistDocumentFactory factory = new XMLArmylistDocumentFactory(listArmy);
        
        /*try{
            javax.xml.transform.TransformerFactory.newInstance().newTransformer().transform(new DOMSource(factory.createDOM()), new StreamResult(file));
        }catch(Exception e){
            //LOG!!!
            e.printStackTrace();
        }*/
        
        this.armylistXMLWriter.writeToDisk(factory.createDOM(), file);
    }

    public void saveArmylist(ArmylistArmy listArmy){
        if(listArmy != null){ 
            try{
                File file = (File)((HashMap)this.armylistFilesByGameSystem.get(listArmy.getGameSystem().getName())).get(listArmy.getName());
                this.saveArmylist(listArmy, file);
                logger.debug("Saved: "+ listArmy.getName() + " at file: "+file);
            }catch(Exception e){
                logger.error("Failed to save armylist: "+ listArmy.getName(), e);
            }
            
        }else{
            logger.warn("Can't save null army");
        }
    }
    
    private void loadArmylists(){
        logger.debug("Starting to load armylists");
        GameSystemAccessBean gsab = GameSystemAccessBean.getInstance();
        String[] gameSystems = gsab.getGameSystemNames();
        
        //this.armylists = new HashMap();
        //this.armylistFiles = new HashMap();
        
        
        for(int j = 0; j < gameSystems.length; ++j){
            HashMap fileList = new HashMap();
            this.armylistFilesByGameSystem.put(gameSystems[j], fileList);
            
            LinkedList armiesList = new LinkedList();
            this.armylistsByGameSystem.put(gameSystems[j], armiesList);
            
            String location = this.armylistConfig.getProperty("armylistsLocation")+gameSystems[j]+"/";
            logger.debug("Starting search directory: "+location);
            File[] listFiles;
            try{
                listFiles = new File(location).listFiles();
            }catch(Exception e){
                logger.warn("Couldn't find armylist dir for game system: "+gameSystems[j]);
                new File(location).mkdir();
                continue;
            }
            
            if(listFiles == null){
                logger.warn("Couldn't find armylist dir for game system: "+gameSystems[j]);
                new File(location).mkdir();
                continue;
            }
            
            for(int i = 0; i < listFiles.length; ++i){
                if(listFiles[i].getName().endsWith(".xml")){
                   logger.debug("Starting parse file: "+listFiles[i]); 
                   ArmylistArmy army = this.parseFile(listFiles[i]);
                   if(army != null){
                       fileList.put(army.getName(), listFiles[i]);
                       armiesList.add(army);
                        /*this.armylists.put(army.getName(), army);
                        this.armylistFiles.put(army.getName(), listFiles[i]);*/
                   }
                }
            }
        
        }
        
        this.loadOldLists();
        /*this.armylistNames = new String[this.armylists.size()];
        Iterator iterator = this.armylists.keySet().iterator();
        int i = 0;
        while(iterator.hasNext()){
            this.armylistNames[i] = (String)iterator.next();
            ++i;
        }*/
        
    }   
    
    private void loadOldLists(){
        logger.debug("Starting to load OLD armylists");
        
        GameSystemAccessBean gsab = GameSystemAccessBean.getInstance();
        String[] gameSystems = gsab.getGameSystemNames();
        
        this.armylists = new HashMap();
        this.armylistFiles = new HashMap();
        
        
        String location = this.armylistConfig.getProperty("armylistsLocation");
        logger.debug("Starting search directory: "+location);
        File[] listFiles;
        try{
            listFiles = new File(location).listFiles();
        }catch(Exception e){
            logger.warn("no files ");
            return;
        }

        if(listFiles == null){
            logger.warn("no files ");
            return;
        }

        for(int i = 0; i < listFiles.length; ++i){
            if(listFiles[i].getName().endsWith(".xml")){
               logger.debug("Starting parse file: "+listFiles[i]); 
               ArmylistArmy army = this.parseFile(listFiles[i]);
               if(army != null){
                   ((HashMap)this.armylistFilesByGameSystem.get(army.getGameSystem().getName())).put(army.getName(), new File(this.armylistConfig.getProperty("armylistsLocation")+ army.getGameSystem().getName()+"/"+army.getName()+".xml"));
                   ((LinkedList)this.armylistsByGameSystem.get(army.getGameSystem().getName())).add(army);
                   
                   logger.debug("Renaming file: " + listFiles[i]);
                   boolean suc = listFiles[i].renameTo(new File(listFiles[i]+"_backup"));
                   logger.debug("Rename succesfull? "+suc);
                   
                   this.saveArmylist(army);
               }
            }
        }
        
    }
    
    public Collection getArmylistsForGameSystem(String gsName){
        return (Collection)this.armylistsByGameSystem.get(gsName);
    }
    
    /*public String[] getArmylistNames(){
        return this.armylistNames;
    }*/
    
    public ArmylistArmy getArmyByName(String name, String gsName){
        if(gsName == null){
            logger.error("Somebaody asked for army list for null game system... returning null.");
            return null;
        }
        Iterator iterator = null;
        try{
            iterator = ((Collection)this.armylistsByGameSystem.get(gsName)).iterator();
        }catch(Exception e){
            logger.error("Somebody asked for a amry list which game system can't be found: "+ gsName);
            return null;
        }
        
        
        while(iterator.hasNext()){
            ArmylistArmy army = (ArmylistArmy)iterator.next();
            if(army.getName().equalsIgnoreCase(name))
                return army;
        }
        return null;
    }
    
/*    public void refreshArmylistNames(){
        HashMap armylists_ = new HashMap();
        HashMap armylistFiles_ = new HashMap();
        
        for(int i = 0; i < this.armylistNames.length; ++i){
            ArmylistArmy army = this.getArmyByName(this.armylistNames[i]);
            armylists_.put(army.getName(), army);
            armylistFiles_.put(army.getName(), this.armylistFiles.get(this.armylistNames[i]));
        }
        
        this.armylists = armylists_;
        this.armylistFiles = armylistFiles_;
        
        this.armylistNames = new String[this.armylists.size()];
        Iterator iterator = this.armylists.keySet().iterator();
        int i = 0;
        while(iterator.hasNext()){
            this.armylistNames[i] = (String)iterator.next();
            ++i;
        }        
    }*/
    
    
    private ArmylistArmy parseFile(File file){
        DOMArmylistDocumentHandler handler = new DOMArmylistDocumentHandler();
        return handler.parseFile(file);
    }
    
    
/*    public Collection getArmylistsForGameSystem(GameSystem system){
        LinkedList ret = new LinkedList();
        for(int i = 0; i < this.armylistNames.length; ++i){
            ArmylistArmy army = this.getArmyByName(this.armylistNames[i]);
            if(army.getGameSystem() == system)
                ret.add(army);
        }
        
        return ret;
    }*/
 
}
