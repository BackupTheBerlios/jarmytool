/*
 * UnitType.java
 *
 * Created on 01 July 2003, 21:30
 */

package org.jArmyTool.data.dataBeans.gameSystem;

import java.awt.Image;
import java.awt.Toolkit;
import org.jArmyTool.util.Config;

/**
 *
 * @author  pasi
 */
public class UnitType {
    private static final String SELECTED_IMAGE_POSTFIX = "_selected";
    
    private String name;
    private String smallImagePath;
    private String largeImagePath;
    private String largeImagePathSelected;
    
    private Image smallImage;
    private Image largeImage;
    private Image largeImageSelected;
    
    private Config config;
    
    /** Creates a new instance of UnitType */
    public UnitType(String name) {
        this.name = name;
        this.config = Config.getInstance("gameSystems");
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setSmallImagePath(String path, String postfix){
        String pathPrefix = this.config.getProperty("unitTypeImageStorageDirectory");
        this.smallImagePath = pathPrefix + path + postfix;
        
        this.smallImage = Toolkit.getDefaultToolkit().getImage(this.smallImagePath);
    }    
    
    public void setLargeImagePath(String path, String postfix){
        String pathPrefix = this.config.getProperty("unitTypeImageStorageDirectory");
        this.largeImagePath = pathPrefix + path + postfix;
        this.largeImagePathSelected = pathPrefix + path + SELECTED_IMAGE_POSTFIX + postfix;
        
        this.largeImage = Toolkit.getDefaultToolkit().getImage(this.largeImagePath);
        this.largeImageSelected = Toolkit.getDefaultToolkit().getImage(this.largeImagePathSelected);
        //System.out.println(this.largeImagePath);
    }
    
    public String getLargeImagePath(){
        return this.largeImagePath;
    }
    
    public String getSmallImagePath(){
        return this.smallImagePath;
    }    
    
    public String getLargeImagePathSelected(){
        if(this.largeImagePathSelected == null)
            return this.largeImagePath;
        return this.largeImagePathSelected;
    }    

    public Image getSmallImage(){
        return this.smallImage;
    }
    
    public Image getLargeImage(){
        return this.largeImage;
    }
    
    public Image getLargeImageSelected(){
        if(this.largeImageSelected == null)
            return this.largeImage;
        return this.largeImageSelected;
    }    
}
