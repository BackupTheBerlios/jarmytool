/*
 * ArmyAccessBean.java
 *
 * Created on 15 March 2003, 20:46
 */

package core_src.src.org.jArmyTool.data.database;

import org.jArmyTool.util.*;
import org.jArmyTool.data.dataBeans.army.*;
import java.io.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jArmyTool.outputGenerator.*;
/**
 *
 * @author  pasi
 */
public class ArmyAccessBean {
    
    private XMLWriter armyXMLWriter;
    
    private static ArmyAccessBean instance;
    
    private Config armyStorageConfig;
    private String storageDirectory;
    private String filePrefix;
    
    /** Creates a new instance of ArmyAccessBean */
    private ArmyAccessBean() {
        this.armyStorageConfig = Config.getInstance("armyStorage");
        this.storageDirectory = this.armyStorageConfig.getProperty("directory");
        this.filePrefix = this.armyStorageConfig.getProperty("armyPrefix");
        this.armyXMLWriter = new XMLWriter();
    }
    
    public static ArmyAccessBean getInstance(){
        if(instance == null){
            instance = new ArmyAccessBean();
        }
        
        return instance;
    }
    
    public Army readArmyByName(String name) throws IOException, FileNotFoundException {
        String filename = "";
        File file = new File(filename);
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        
        throw new UnsupportedOperationException();
        
        //return new Army(null);
    }
    
    
    public Army readArmyXMLByFile(File file){
        ArmyXMLReader reader = new ArmyXMLReader(file);
        return reader.getArmy();
    }
    
    public void writeArmyXMLByFile(Army army, File file){
        XMLDocumentFactory documentFactory = new XMLDocumentFactory(army);
        
        try{
            javax.xml.transform.TransformerFactory.newInstance().newTransformer().transform(new DOMSource(documentFactory.createDOM()), new StreamResult(file));
        }catch(Exception e){
            //LOG!!!
            e.printStackTrace();
        }
        
        //this.armyXMLWriter.writeToDisk(documentFactory.createDOM(), file);
    }
    
    
    //LOGGING!!!
    //Class cat Ex!!!
 /*   public Army readArmyByFilename(String filename) throws IOException, FileNotFoundException {
        File file = new File(this.storageDirectory + filename);
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        Object object = null;
        try{
            object = ois.readObject();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Army ret = null;
        if(object != null){
            ret = (Army)object;
        }
        
        return ret;
    }
 
    
    //LOGGING!!!
    //Class cat Ex!!!
    public Army readArmyByFile(File file) throws IOException, FileNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        Object object = null;
        try{
            object = ois.readObject();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Army ret = null;
        if(object != null){
            ret = (Army)object;
        }
        
        return ret;
    }    
    
    
    public void writeArmyByFilename(String filename, Army army) throws IOException{
        File file = new File(this.storageDirectory + filename);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(army);
    }
    
    public void writeArmyByFile(File file, Army army) throws IOException{
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(army);
    }    */
}
