/*
 * XMLGameSystemDocumentFactory.java
 *
 * Created on 01 July 2003, 14:10
 */

package core_src.src.org.jArmyTool.outputGenerator;

import org.jArmyTool.data.dataBeans.util.*;
import org.jArmyTool.data.dataBeans.gameSystem.*;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.util.*;
import java.io.*;
/**
 *
 * @author  pasi
 */
public class XMLGameSystemDocumentFactory {
    
    private GameSystem gameSystem;
    
    private DocumentBuilder builder;
    
    private Document document;
    private Element rootElement;    
    
    /** Creates a new instance of XMLGameSystemDocumentFactory */
    public XMLGameSystemDocumentFactory(GameSystem gameSystem) {
        this.gameSystem = gameSystem;
        
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            this.builder = factory.newDocumentBuilder();
        }catch(Exception e){
            e.printStackTrace();
        }        
        
    }
    
    public Document createDOM(){
        this.document = this.builder.newDocument();
   
        this.rootElement = this.document.createElement("gameSystem");
        
        this.document.appendChild(this.rootElement);   
        
        this.createHeader();
        this.createModelStatGroups();
        
        return this.document;
    }
    
    private void createHeader(){
        Element header = this.document.createElement("gameSystemHeader");
        header.setAttribute("id", "header");
        Element name = this.document.createElement("name");
        name.setAttribute("complete", this.gameSystem.getName());
        name.setAttribute("short", this.gameSystem.getShortName());
        
        header.appendChild(name);
        this.rootElement.appendChild(header);
    }    
    
   private void createModelStatGroups(){
        Element groups = this.document.createElement("modelStatGroups");
        Iterator names = this.gameSystem.getStatTypeNames().iterator();
        while(names.hasNext()){
            String name = (String)names.next();
            StatType type = this.gameSystem.getStatType(name);
            Element el = this.document.createElement("modelStatGroup");
            el.setAttribute("name", name);
            Iterator stats = type.getAllStats().iterator();
            while(stats.hasNext()){
                Element statEl = this.document.createElement("modelStat");
                ModelStat stat = (ModelStat)stats.next();
                statEl.setAttribute("symbol",stat.getSymbol());
                statEl.setAttribute("tooltip",stat.getTooltip());
                statEl.setAttribute("type",stat.getType());
                el.appendChild(statEl);
            }
            groups.appendChild(el);
        }
        this.rootElement.appendChild(groups);
    }    
    
}


