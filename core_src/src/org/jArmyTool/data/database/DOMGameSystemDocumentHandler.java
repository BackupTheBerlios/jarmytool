/*
 * DOMGameSystemDocumentHandler.java
 *
 * Created on 01 July 2003, 13:34
 */

package core_src.src.org.jArmyTool.data.database;

import org.jArmyTool.data.dataBeans.gameSystem.*;
import org.jArmyTool.data.dataBeans.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.io.*;
import java.util.*;
import org.jArmyTool.data.dataBeans.util.WeaponProfile;

/**
 *
 * @author  pasi
 */
public class DOMGameSystemDocumentHandler {
    private static final String HEADER_ID = "header";
    
    private DocumentBuilder documentBuilder;
    
    private Document document;
    
    /** Creates a new instance of DOMGameSystemDocumentHandler */
    public DOMGameSystemDocumentHandler() {
        try{
            this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }catch(Exception e){
            e.printStackTrace();
            //LOG!!!
        }        
    }
    
    public GameSystem parseFile(File file){ 
        document = null;
        try{
            document = this.documentBuilder.parse(file);
        }catch(Exception e){
            e.printStackTrace();
            //LOG!!
        }
        
        GameSystem gameSystem = new GameSystem("error reading name");
        
        this.parseHeader(document.getElementById(HEADER_ID), gameSystem);
        this.parseModelStats((Element)document.getElementsByTagName("modelStatGroups").item(0), gameSystem);
        Element weaponProfiles = (Element)document.getElementsByTagName("weaponProfiles").item(0); 
        if(weaponProfiles != null)
            this.parseWeaponProfiles(weaponProfiles, gameSystem);
        this.parseUnitTypes((Element)document.getElementsByTagName("unitTypes").item(0), gameSystem);
        
        return gameSystem;
    }
    
    private void parseHeader(Element header, GameSystem gameSystem){
        NodeList list = header.getElementsByTagName("name");
        Node name = list.item(0);
        gameSystem.setName( ((Element)name).getAttribute("complete") );
        gameSystem.setShortName( ((Element)name).getAttribute("short") );
        
        list = header.getElementsByTagName("points");
        Node points = list.item(0);
        gameSystem.setDefaultTargetPointcost( Double.parseDouble(((Element)points).getAttribute("defaultTargetPointcost")) );
    }   
    
    private void parseWeaponProfiles(Element profilesElement, GameSystem gameSystem){
        NodeList list = profilesElement.getElementsByTagName("weaponProfile");
        for(int i = 0; i < list.getLength(); ++i){
            Element el = (Element)list.item(i);
            this.parseWeaponProfile(el, gameSystem);
        }
    }
    
    private void parseWeaponProfile(Element profileElement, GameSystem gameSystem){
        String name = profileElement.getAttribute("name");
        WeaponProfile profile = new WeaponProfile(name);
        NodeList list = profileElement.getElementsByTagName("weaponStatHeader");
        for(int i = 0; i < list.getLength(); ++i){
            profile.addHeader( ((Element)list.item(i)).getAttribute("name") );
        }
        
        gameSystem.addWeaponProfile(profile);
    }
    
    
    private void parseModelStats(Element statEl, GameSystem gameSystem){
        
        NodeList list = statEl.getElementsByTagName("modelStatGroup");
        for(int i = 0; i < list.getLength(); ++i){
            Element el = (Element)list.item(i);  
            gameSystem.addStatType(this.parseStatGroup(el), el.getAttribute("name"));
        }
    }
    
    private StatType parseStatGroup(Element groupElement){
        StatType ret = new StatType();
        NodeList list = groupElement.getElementsByTagName("modelStat");
        for(int i = 0; i < list.getLength(); ++i){
            Element el = (Element)list.item(i);
            String symbol = el.getAttribute("symbol");
            String tooltip = el.getAttribute("tooltip");
            String type = el.getAttribute("type");
            ModelStat temp = new ModelStat(symbol, tooltip, type);
            ret.addStat(temp);
        }
        return ret;
    }    
    
    private void parseUnitTypes(Element typeEl, GameSystem gameSystem){
        
        NodeList list = typeEl.getElementsByTagName("unitType");
        for(int i = 0; i < list.getLength(); ++i){
            Element el = (Element)list.item(i);  
            UnitType type = new UnitType(el.getAttribute("name"));
            type.setLargeImagePath(el.getAttribute("largePic"), el.getAttribute("postfix"));
            type.setSmallImagePath(el.getAttribute("smallPic"), el.getAttribute("postfix"));
            
            gameSystem.addUnitType( type );
        }
    }
}
