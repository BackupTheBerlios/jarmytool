/*
 * PluginLoader.java
 *
 * Created on 22 September 2003, 15:35
 */

package org.jArmyTool.pluginLoader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.jArmyTool.util.Config;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.jArmyTool.plugininterface.*;
import org.jArmyTool.internaldata.*;
import org.w3c.dom.Element;

/**
 * This class will load all plugins to memory 
 * and initialize and start them.
 *
 * Methods should be called in following order:<br>
 * 1) loadPlugins()<br>
 * 2) initPugins()<br>
 * 3) startPlugins()<br><br>
 *
 * @author  Pasi Lehtimäki
 */
public class PluginLoader {
    
    private LinkedList plugins;
    
    private Config conf = Config.getInstance("pluginConfig");
    
    private ConfigurationForPlugins conf4Plugins;
    
    private DocumentBuilder documentBuilder;
    
    private URLClassLoader classLoader;
    
    /** Creates a new instance of PluginLoader */
    public PluginLoader() {
        this.initClassLoader();
        
        this.plugins = new LinkedList();
        this.conf4Plugins = ConfigurationForPlugins.getInstance();
        try{
            this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }catch(Exception e){
            e.printStackTrace();
            //LOG!!!
        }
    }
    
    private void initClassLoader(){
        File pluginDir = new File(this.conf.getProperty("pluginsDirectory"));
        String[] files = pluginDir.list();
        LinkedList urls = new LinkedList();
        
        //If plugins dir not found -> nullpointer (files)  FIX this!
        
        for(int i = 0; i < files.length; ++i){
            if(files[i].endsWith(".jar") || files[i].endsWith(".JAR")){
                try{
                    URL url = new File(pluginDir, files[i]).toURL();
                    urls.add(url);
                    System.out.println("URL added to class loader: "+url);
                }catch(java.net.MalformedURLException ex){
                    System.out.println("found crappy plugin file");
                }
            }
        }
        URL[] urlArray = new URL[urls.size()];
        
        Iterator iterator = urls.iterator();
        int i = 0;
        while(iterator.hasNext()){
            urlArray[i] = (URL)iterator.next();
            ++i;
        }
        
        this.classLoader = new URLClassLoader(urlArray);
        
        
    }
    
    public void loadPlugins(){
        String pluginConfXMLFile = this.conf.getProperty("configXML");
        
        
        Document document = null;
        try{
            document = this.documentBuilder.parse(new File(pluginConfXMLFile));
        }catch(Exception e){
            e.printStackTrace();
            //LOG!!
        }
        
        NodeList plugins = document.getElementsByTagName("plugin");
        for(int i = plugins.getLength(); i > 0; --i){
          this.parsePlugin((Element)plugins.item(i-1));
        }
    }
    
    private void parsePlugin(Element plugin){
        System.out.println("found plugin: "+plugin.getAttribute("name") + " version: " +plugin.getAttribute("version") );
        Element mainClass = (Element)plugin.getElementsByTagName("mainPluginClass").item(0);
        String mainClassName = mainClass.getAttribute("name");
        System.out.println("Main class: " + mainClassName);
        try{
            this.plugins.add(this.classLoader.loadClass(mainClassName).newInstance());
        }catch(ClassNotFoundException e){
            try{
                this.plugins.add(Class.forName(mainClassName).newInstance());
            }catch(Exception ex2){
            //LOG!!
            ex2.printStackTrace();
            return;            
            }
        }catch(Exception ex){
            //LOG!!
            ex.printStackTrace();
            return;            
        }
        
        NodeList confs = plugin.getElementsByTagName("configuration");        
        if(confs.getLength() > 0){
            this.conf4Plugins.addPluginConf(plugin.getAttribute("name"), this.parseConfig((Element)confs.item(0)));
        }
    }
    
    private Properties parseConfig(Element confElement){
        Properties ret = new Properties();
        NodeList pluginConfs = confElement.getElementsByTagName("property");
        for(int i = 0; i < pluginConfs.getLength(); ++i){
            Element conf = (Element)pluginConfs.item(i);
            ret.setProperty(conf.getAttribute("name"), conf.getAttribute("value")); 
        }
        
        return ret;
    }
    
    
    
    public void initPlugins(){
        
        Iterator iterator = this.plugins.iterator();
        while(iterator.hasNext()){
           ((JArmyToolPluginInterface)iterator.next()).initialize(GUICommands.getInstance(), GlobalData.getInstance(), CurrentData.getInstance());
        }
        
    }
    
    public void startPlugins(){
        
    }
}
