/*
 * GUICore.java
 *
 * Created on 21 December 2002, 00:07
 */

package org.jArmyTool.gui.engine;

import com.l2fprod.gui.plaf.skin.*;


import org.jArmyTool.gui.components.*;
import org.jArmyTool.gui.listeners.*;
import org.jArmyTool.gui.factories.*;
import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.data.dataBeans.gameSystem.*;
import org.jArmyTool.data.database.*;
import org.jArmyTool.outputGenerator.*;
import org.jArmyTool.gui.user.*;
import org.jArmyTool.gui.splashScreen.*;

import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import org.jArmyTool.gui.components.CloneArmylistNameDialog;
import org.jArmyTool.gui.components.NewArmylistNameDialog;
import org.jArmyTool.internaldata.CurrentData;
import org.jArmyTool.internaldata.GUICommands;
import org.jArmyTool.pluginLoader.PluginLoader;

import org.apache.log4j.*;
import org.jArmyTool.internaldata.UserPreferencesForPlugins;
import org.jArmyTool.util.Config;


/**
 *
 * @author  pasi
 */
public class GUICore {
    private static final String SOFTWARE_NAME = "jArmyTool";
    private static final String VERSION = "0.5 dev5";
    
    public static final String USER_SAVE_DIRECTORY_KEY = "save_directory";
    //private static String AUTOSAVE_ARMY_DIRECTORY= "autosave";
    //private static String AUTOSAVE_ARMY_FILE_NAME= "autosavedArmy.xml";
    
    private MainWindow mainWindow;
    private ArmylistListModelFactory armylistListModelFactory;
    private ComponentFactory componentFactory;
    private org.jArmyTool.data.factories.Factory dataFactory;    
    //private Army army;
    //private ArmylistArmy armylistArmy;
    private LinkedList unitPanels;
//    private LinkedList deletedUnitPanels;
    private DefaultListModel deletedUnitsModel;
    private DefaultListModel currentFilterDeletedUnitsModel;
    private DefaultListModel namedUnitsModel;
    private DefaultListModel currentFilterNamedUnitsModel;    
    private Army currentNamedUnitsStorage;
    
    private DefaultComboBoxModel armylistsModel;
    
    private ArmyAccessBean armyAccessBean;    
    private ArmylistAccessBean aab;
    private NamedUnitsListAccessBean namedAccessBean;
    
    private UserPreferences userPreferences;
    
    private SplashScreen splashScreen;
    
    private JMenu pluginMenu;
    
    private CurrentData currentData;
    
    private HashMap menus;
    private JMenuBar menuBar;
    
    private String activeFilter;
    
    private Config autosaveConfig = Config.getInstance("autosave");
    
    private static Logger logger = Logger.getLogger(GUICore.class);
    
    /** Creates a new instance of GUICore */
    public GUICore() { 
        this.logger.debug("---- Starting jArmyTool ----");
        this.logger.debug("version: "+VERSION);
        
        this.activeFilter = "";
        
        this.splashScreen = new SplashScreen(this);
        
        splashScreen.show();
        
        this.splashScreen.setStatus("Loading user preferences");
        this.userPreferences = UserPreferences.getInstance();
        
        this.splashScreen.setStatus("Init look and feel");
        initLookAndFeel(this.userPreferences.getProperty("skinPackLocation"));  
        
        this.splashScreen.setStatus("Init internal data");
        this.currentData = CurrentData.getInstance();
        GUICommands.getInstance().setGUICore(this);

        this.splashScreen.setStatus("Loading armylists");
        this.armyAccessBean = ArmyAccessBean.getInstance();
        
        this.splashScreen.setStatus("Initializing UI");
        this.deletedUnitsModel = new DefaultListModel();
        this.unitPanels = new LinkedList();
//        this.deletedUnitPanels = new LinkedList();
        this.mainWindow = new MainWindow(this);
        this.armylistListModelFactory = ArmylistListModelFactory.getInstance();
        this.componentFactory = ComponentFactory.getInstance();
        this.dataFactory = org.jArmyTool.data.factories.Factory.getInstance();
        
        UnitListListener unitListListener = new UnitListListener(this, this.mainWindow);
        this.mainWindow.setUnitListListener(unitListListener);
        DeletedUnitListListener deletedUnitListener = new DeletedUnitListListener(this, this.mainWindow);
        this.mainWindow.setDeletedUnitListListener(deletedUnitListener);
        
        this.mainWindow.setDeletedUnitListModel(this.deletedUnitsModel);
        
        this.splashScreen.setStatus("Initializing armylists");
        this.initArmylists();

        this.splashScreen.setStatus("Initializing named units");
        this.namedAccessBean = NamedUnitsListAccessBean.getInstance();

        this.splashScreen.setStatus("Restoring user preferences");
        this.restoreUserPreferences();
        
        this.splashScreen.setStatus("Initializing menus");
        this.initMenus();
        
        this.splashScreen.setStatus("Initializing plugins");
        UserPreferencesForPlugins.setCore(this);
        this.initPlugins();
        
        this.splashScreen.setStatus("Initializing help");
        this.initHelp();
        
        this.splashScreen.setStatus("Starting main UI");
        this.showMainWindow();
        
        this.splashScreen.setStatus("Disposing splash screen");
        this.splashScreen.setVisible(false);
        this.splashScreen.dispose();
        
        this.logger.debug("GUICore contructor ready");
    }
    
    private void initHelp(){
        JMenuItem helpMenu = new JMenuItem("Help");

        helpMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               JFrame helpFrame = new net.sourceforge.helpgui.gui.MainFrame("/help/","java");
               helpFrame.setVisible(true);
               helpFrame.setTitle("jArmyTool "+VERSION+" - Help");
               helpFrame.setIconImage(MainWindow.PROGRAM_ICON);
            }
        });
        this.addMenuItem(helpMenu, "Help");         
        
    }
    
    
    private void initLookAndFeel(String themepack){
        try{
              if (themepack != null && themepack.length() > 0) {
              
                  if (themepack.endsWith(".xml")) {
                    SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePackDefinition(SkinUtils.toURL(new File(themepack))));
                    SkinLookAndFeel.enable();
                  } else if (themepack.startsWith("class:")) {
                    String classname = themepack.substring("class:".length());
                    SkinLookAndFeel.setSkin((Skin)Class.forName(classname).newInstance());
                    SkinLookAndFeel.enable();
                  } else if (themepack.startsWith("theme:")) {
                    String classname = themepack.substring("theme:".length());
                    MetalTheme theme = (MetalTheme)Class.forName(classname).newInstance();
                    MetalLookAndFeel metal = new MetalLookAndFeel();
                    metal.setCurrentTheme(theme);
                    UIManager.setLookAndFeel(metal);
                  } else {
                    SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack(themepack));
                    SkinLookAndFeel.enable();
                  }
            }
        }catch(Exception e){
            this.logger.warn("Failed to load Look and Feel" ,e);
        }
    }
    
    private void initPlugins(){
        PluginLoader pluginLoader = new PluginLoader();
        pluginLoader.loadPlugins();
        pluginLoader.initPlugins();
        pluginLoader.startPlugins();
    }
    
    private void initMenus(){
        this.menus = new HashMap();
        this.menuBar = new JMenuBar();
        this.mainWindow.setMenuBar(this.menuBar);
        
        //Some of these sould be replaced by plugins!!! :
        JMenuItem newArmylist = new JMenuItem("new armylist");
        newArmylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewArmylistNameDialog dialog = new NewArmylistNameDialog(mainWindow, true, mainWindow);
                dialog.setSize(250, 200);
                dialog.show();
                
            }
        });
        this.addMenuItem(newArmylist, "Armylist");
        
 /*       JMenuItem cloneArmylist = new JMenuItem("clone armylist");
        cloneArmylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               new CloneArmylistNameDialog(mainWindow, true, mainWindow).show();
            }
        });
        this.addMenuItem(cloneArmylist, "Armylist");   */
 
    }
    
    public void addMenuItem(JMenuItem item, String path){
        this.logger.debug("Adding menuitem: "+item.getText()+" for path: "+path);

        JMenu menu = null;
        LinkedList menuList = new LinkedList();
        String beginPath = path;
        boolean atRoot = false;
        do{
            menu = (JMenu)this.menus.get(beginPath);
            if(menu != null){
                if(!menuList.isEmpty()){
                    menuList.removeLast();
                }
            }else{
                if(beginPath.lastIndexOf('.') != -1) {
                    menuList.add(beginPath.substring(beginPath.lastIndexOf(".") +1));;
                    beginPath = beginPath.substring(0, beginPath.lastIndexOf("."));  
                }else{
                    menuList.add(beginPath);
                    if(menu != null){
                    }else{
                        atRoot = true;
                    }
                }
            }
        }while(!atRoot && menu == null);
        
        Collections.reverse(menuList);
        Iterator iterator = menuList.iterator();
        if(atRoot && menu == null){
            if(!iterator.hasNext()){
                this.logger.error("Error parsing menu paths.");
            }
            menu = new JMenu((String)iterator.next());
            this.menuBar.add(menu);
            this.menus.put(beginPath, menu);
        }
        
        while(iterator.hasNext()){
            String menuName = (String)iterator.next();
            beginPath = beginPath + "." + menuName;
            JMenu temp = new JMenu(menuName);
            menu.add(temp);
            this.menus.put(beginPath, temp);
            menu = temp;
        }
        menu.add(item);
    }
    
    
    public String getVersion(){
        return VERSION;
    }
    
    private void initArmylists(){
        this.armylistsModel = new DefaultComboBoxModel();
        this.aab = ArmylistAccessBean.getInstance();
        String[] names = GameSystemAccessBean.getInstance().getGameSystemNames();
        for(int i = 0; i < names.length; ++i){
            
            this.armylistsModel.addElement(names[i]);
            
            Iterator iterator = this.aab.getArmylistsForGameSystem(names[i]).iterator();
            while(iterator.hasNext()){
                this.armylistsModel.addElement(iterator.next());
            }
        }
        
        
        /*String[] temp = this.aab.getArmylistNames();
        for(int i = 0; i < temp.length; ++i){
            this.armylistsModel.addElement(temp[i]);
        } */ 
        this.mainWindow.setArmylistsModel(this.armylistsModel);
    }
    
    private void restoreUserPreferences(){
        try{
            int width = Integer.parseInt(this.userPreferences.getProperty(UserPreferences.WINDOW_SIZE_WIDTH));
            int height = Integer.parseInt(this.userPreferences.getProperty(UserPreferences.WINDOW_SIZE_HEIGHT));
            
            this.mainWindow.setSize(width, height);
        }catch(NumberFormatException e){
            this.logger.warn("Can't set window dimensions", e);
        }
        
        try{
            int first= Integer.parseInt(this.userPreferences.getProperty(UserPreferences.FIRST_SPLIT_LOCATION));
            int second= Integer.parseInt(this.userPreferences.getProperty(UserPreferences.SECOND_SPLIT_LOCATION));
            
            this.mainWindow.setFirstSplitLocation(first);
            this.mainWindow.setSecondSplitLocation(second);
        }catch(NumberFormatException e){
            this.logger.warn("Can't set split location", e);
        }        
        
        String lastUsedArmylist = this.userPreferences.getProperty(UserPreferences.LAST_USED_ARMYLIST);
        String lastUsedGamesystem = this.userPreferences.getProperty(UserPreferences.LAST_USED_GAMESYSTEM);
        if(lastUsedArmylist != null && lastUsedArmylist.length() > 0  && lastUsedGamesystem != null && lastUsedGamesystem.length() > 0){
            ArmylistArmy listArmy = aab.getArmyByName(lastUsedArmylist, lastUsedGamesystem);
            if(listArmy != null){
                this.setArmylistArmy(listArmy);
                /*try{
                    this.setArmy(this.loadAutoSavedArmy(listArmy.getName()));
                    //this.loadArmy(new File(this.autosaveConfig.getProperty("AUTOSAVE_ARMY_DIRECTORY")+"/"+this.autosaveConfig.getProperty("AUTOSAVE_ARMY_FILE_NAME")));
                }catch(Exception e){
                    //LOG!
                    this.setArmy(new Army(this.currentData.getCurrentArmylistArmy()));
                }*/
            }
        }
        
    }
    
    public void showMainWindow(){
        this.mainWindow.show();
    }
    
    public void closeMainWindow(){
        try{
            this.aab.saveArmylist(this.getArmylistArmy());
        
            try{
                this.userPreferences.setProperty(UserPreferences.LAST_USED_ARMYLIST, this.currentData.getCurrentArmylistArmy().getName());
                this.userPreferences.setProperty(UserPreferences.LAST_USED_GAMESYSTEM, this.currentData.getCurrentArmylistArmy().getGameSystem().getName());
            }catch(Exception e){
                logger.warn("Could not save last used armylist");
            }
            this.userPreferences.setProperty(UserPreferences.WINDOW_SIZE_HEIGHT, ""+this.mainWindow.getSize().height);
            this.userPreferences.setProperty(UserPreferences.WINDOW_SIZE_WIDTH, ""+this.mainWindow.getSize().width);
            
            this.userPreferences.setProperty(UserPreferences.FIRST_SPLIT_LOCATION, ""+this.mainWindow.getFirstSplitLocation());
            this.userPreferences.setProperty(UserPreferences.SECOND_SPLIT_LOCATION, ""+this.mainWindow.getSecondSplitLocation());
            
            
            this.autoSaveArmy();
            
            
            //this.saveArmy(new File(this.autosaveConfig.getProperty("AUTOSAVE_ARMY_DIRECTORY")+"/"+this.autosaveConfig.getProperty("AUTOSAVE_ARMY_FILE_NAME")));            
        }catch(Exception e){
            this.logger.error("error while doing closeup saving", e);
        }
        this.userPreferences.writePropertiesToDisk();
        this.namedAccessBean.saveNamed();
        
        this.logger.debug("---- Exiting jArmyTool ----");
        System.exit(0);
    }
    
    public void setArmylistArmy(ArmylistArmy armylistArmy){
        if(armylistArmy == null)
            return;
        
        
        if(this.currentData.getCurrentArmylistArmy() != null)
            this.aab.saveArmylist(this.currentData.getCurrentArmylistArmy());
        if(this.currentData.getCurrentArmy() != null &&  armylistArmy != this.currentData.getCurrentArmy().getArmylistArmy()){
            this.autoSaveArmy();
        }
        this.currentData.setCurrentArmylistArmy(armylistArmy);
        this.mainWindow.setUnitListModel(this.armylistListModelFactory.getUnitChoises(armylistArmy));
        this.mainWindow.setUnitListsCellRenderer(armylistArmy.getGameSystem());
        
        this.mainWindow.clearUnits();
        this.mainWindow.initUnitPanels(armylistArmy.getGameSystem());
        this.currentData.setCurrentArmy(null);
        this.mainWindow.setPointcostTotal(0, 0);
        this.unitPanels.clear();
        this.mainWindow.setTitle(SOFTWARE_NAME + " v." + VERSION + " - " + armylistArmy.getName()); 
        this.mainWindow.refreshUnitTypeButtons(armylistArmy.getGameSystem());
        this.mainWindow.setArmylistsComboSelected(armylistArmy);
        
        
        
        this.mainWindow.setInfo(getInfoForArmy(armylistArmy));
        
        Army autoLoad = this.loadAutoSavedArmy(armylistArmy.getName());
        if(autoLoad != null){
            this.setArmy(autoLoad);
        }else{
            this.setArmy(new Army(this.currentData.getCurrentArmylistArmy()));
        }
        this.unitTypeFilterUnselected();
        this.clearDeletedUnits();      
        this.setNamedStorage(armylistArmy);
        
    }
    
    private void setNamedStorage(ArmylistArmy army){
        this.currentNamedUnitsStorage = this.namedAccessBean.getNamedUnits(army.getName(), army.getGameSystem().getName());
        this.namedUnitsModel = new DefaultListModel();
        Iterator units = this.currentNamedUnitsStorage.getSelectedUnits().iterator();
        while(units.hasNext()){
            Unit unit = (Unit)units.next();
            this.namedUnitsModel.addElement(unit);
        }
        this.mainWindow.setNamedUnitsListModel(this.namedUnitsModel);
    }
    
    public void addNamedUnit(Unit unit){
        if(this.currentNamedUnitsStorage.getSelectedUnits().contains(unit)){
            this.mainWindow.refreshNamedList();
            return;
        }
        if(unit.getName().equalsIgnoreCase(unit.getArmylistUnit().getName()))
            return;
        if(this.currentFilterDeletedUnitsModel != null && this.activeFilter.equals(unit.getArmylistUnit().getUnitType()))
            this.currentFilterNamedUnitsModel.addElement(unit);
        this.currentNamedUnitsStorage.addUnit(unit);
        this.namedUnitsModel.addElement(unit);
        this.mainWindow.refreshNamedList();
    }
    
    public void removeNamedUnitFromList(){
        Unit unit = (Unit)this.mainWindow.getNamedUnitListSelected();
        if(unit == null)
            return;
        
        this.currentNamedUnitsStorage.removeUnit(unit);
        if(this.currentFilterDeletedUnitsModel != null && this.activeFilter.equals(unit.getArmylistUnit().getUnitType()))
            this.currentFilterNamedUnitsModel.removeElement(unit);
        this.namedUnitsModel.removeElement(unit);
    }
    
    public void addNamedUnitToArmy(){
        if(this.mainWindow.getNamedUnitListSelected() == null)
            return;
        this.addUnitToArmy( new Unit((Unit)this.mainWindow.getNamedUnitListSelected()) );
    }
    
    
    public static String getInfoForArmy(ArmylistArmy army){
        String ret ="<html>";
        int startLength = ret.length();
        
        if(army.getWriter() != null && army.getWriter().length() > 0){
            ret = ret + "<b>Rules by: "+army.getWriter()+"</b>";
        }
       
        if(army.getEmail() != null && army.getEmail().length() > 0){
            ret = ret + "<br><a href=\"mailto:"+army.getEmail()+"\">"+army.getEmail()+"</a>";
        }
        
        if(army.getWeb() != null && army.getWeb().length() > 0){
            ret = ret + "<br><a href=\""+army.getWeb()+"\">"+army.getWeb()+"</a>";
        }
        
        if(army.getComments() != null && army.getComments().length() > 0){
            ret = ret + "<hr>"+army.getComments();
        }        
        
        if(ret.length() <= startLength)
            return null;
        return ret;
    }
    
    public void setArmy(Army army){
        this.logger.debug("set army: "+ army.getName());
        
        this.mainWindow.clearUnits();
        this.currentData.setCurrentArmy(army);
        this.mainWindow.setPointcostTotal(this.currentData.getCurrentArmy().getPointcostTotal(), this.currentData.getCurrentArmy().getTargetPointcost() - this.currentData.getCurrentArmy().getPointcostTotal() );
        
        this.deletedUnitsModel.clear();
        
        Iterator iterator = this.currentData.getCurrentArmy().getSelectedUnits().iterator();
        while(iterator.hasNext()){
            this.insertUnitToMainWindow((Unit) iterator.next());
        }
        
        this.mainWindow.setArmyNameLabelText(this.currentData.getCurrentArmy().getName());
        this.mainWindow.setArmylistNameLabelText("("+this.currentData.getCurrentArmy().getArmylistArmy().getName()+")");
        double targetPointcost = this.currentData.getCurrentArmy().getTargetPointcost();
        if(targetPointcost == (int)targetPointcost){
            this.mainWindow.setTargetPointcostLabelText("" + (int)targetPointcost);
        }else{
            this.mainWindow.setTargetPointcostLabelText("" + targetPointcost);
        } 
    }
    
    public void newArmy(){
        this.mainWindow.clearUnits();
        this.setArmy(new Army(this.currentData.getCurrentArmylistArmy()));
        this.clearDeletedUnits();
    }
    
    private void autoSaveArmy(){
        if(this.currentData.getCurrentArmy() == null)
            return;
        this.logger.debug("auto saving: " + this.autosaveConfig.getProperty("AUTOSAVE_ARMY_DIRECTORY")+"/"+this.autosaveConfig.getProperty("indivisualAutoSavePrefix") + this.currentData.getCurrentArmylistArmy().getName() + this.autosaveConfig.getProperty("indivisualAutoSavePostfix"));
        this.saveArmy(new File(this.autosaveConfig.getProperty("AUTOSAVE_ARMY_DIRECTORY")+"/"+this.autosaveConfig.getProperty("indivisualAutoSavePrefix") + this.currentData.getCurrentArmylistArmy().getName() + this.autosaveConfig.getProperty("indivisualAutoSavePostfix")), false);
    }
    
    private Army loadAutoSavedArmy(String armylistName){
        File file = new File(this.autosaveConfig.getProperty("AUTOSAVE_ARMY_DIRECTORY")+"/"+this.autosaveConfig.getProperty("indivisualAutoSavePrefix") + armylistName + this.autosaveConfig.getProperty("indivisualAutoSavePostfix"));
        if(!file.exists()){
            logger.debug("Tried to auto load army but didn't find the file");
            return null;
        }
        try{
            return this.armyAccessBean.readArmyXMLByFile(file);
        }catch(Exception e){
            logger.warn("Failed to read autosaved armylist",e);
            return null;
        }
    }
    
    
    public void refreshArmylistData(){
        this.aab.saveArmylist(this.getArmylistArmy());
        //this.aab.refreshArmylistNames();
        this.mainWindow.refreshArmylistData();
        
        
        this.initArmylists();
        /*this.armylistsModel = new DefaultComboBoxModel();
        String[] temp = this.aab.getArmylistNames();
        for(int i = 0; i < temp.length; ++i){
            this.armylistsModel.addElement(temp[i]);
        }*/
        
        this.mainWindow.setArmylistsModel(this.armylistsModel); 
        this.mainWindow.setArmylistsComboSelected(this.currentData.getCurrentArmylistArmy());
    }
    
    public void refresUnitTree(){
        this.mainWindow.refreshUnitTree();
    }
    
    public void addUnitToArmy(Unit unit){
        // removed due user request. This would index similary named units
      /*  int index = 0;
        while(this.isNameDublicate(unit.getName(), index)){
            ++index;
        }
        if(index > 0){
            unit.setName(unit.getName() + "[" + (index+1) + "]");
        }*/
        
        
        this.currentData.getCurrentArmy().addUnit(unit);
        this.insertUnitToMainWindow(unit);
    }
    
    private void insertUnitToMainWindow(Unit unit){
        UnitPanel temp = this.componentFactory.createUnitPanel(unit, this);
        this.unitPanels.add(temp);
        this.mainWindow.addUnit(temp);
        this.mainWindow.setPointcostTotal(this.currentData.getCurrentArmy().getPointcostTotal(), this.currentData.getCurrentArmy().getTargetPointcost() - this.currentData.getCurrentArmy().getPointcostTotal());        
        this.mainWindow.scrollTo(temp);
    }
    
    public Army getArmy(){
        return this.currentData.getCurrentArmy();
    }
    
    public ArmylistArmy getArmylistArmy(){
        return this.currentData.getCurrentArmylistArmy();
    }
    
    public void removeUnitFromArmy(Unit unit){
        this.currentData.getCurrentArmy().removeUnit(unit);
        
        Iterator iterator = this.unitPanels.iterator();
        UnitPanel tempPanel = null;
        while(iterator.hasNext()){
            UnitPanel temp = (UnitPanel)iterator.next();
            if( temp.getUnit().equals(unit) ){
                tempPanel = temp;
                break;
            }
        } 
        if(tempPanel == null)
            return;
        //this.deletedUnitPanels.add(tempPanel);
        this.mainWindow.removeUnit(tempPanel);
        this.unitPanels.remove(tempPanel);
        this.mainWindow.setPointcostTotal(this.currentData.getCurrentArmy().getPointcostTotal(), this.currentData.getCurrentArmy().getTargetPointcost() - this.currentData.getCurrentArmy().getPointcostTotal());
        this.deletedUnitsModel.addElement(unit);
        if(this.currentFilterDeletedUnitsModel != null && unit.getArmylistUnit().getUnitType().equalsIgnoreCase(this.activeFilter))
            this.currentFilterDeletedUnitsModel.addElement(unit);
        
    }
    
    public void addDeletedUnitToArmy(Unit unit){
        this.deletedUnitsModel.remove(this.mainWindow.getDeletedUnitListSelectedIndex());
        if(this.currentFilterDeletedUnitsModel != null)
            this.currentFilterDeletedUnitsModel.remove(this.mainWindow.getDeletedUnitListSelectedIndex());
        
        //this.deletedUnitPanels.remove(unitPanel);
        this.currentData.getCurrentArmy().addUnit(unit);
        UnitPanel unitPanel = new UnitPanel(unit, this);
        this.unitPanels.add(unitPanel);
        this.mainWindow.addUnit(unitPanel);
        this.mainWindow.setPointcostTotal(this.currentData.getCurrentArmy().getPointcostTotal(), this.currentData.getCurrentArmy().getTargetPointcost() - this.currentData.getCurrentArmy().getPointcostTotal());
    }
    
    public void clearDeletedUnits(){
//        this.deletedUnitPanels.clear();
        this.deletedUnitsModel.clear();
        if(this.currentFilterDeletedUnitsModel != null)
            this.currentFilterDeletedUnitsModel.clear();
    }
    
/*    private boolean isNameDublicate(String name, int index){
        if(index > 0){
            name = name + "["+(index+1)+"]";
        }
        
        Iterator iterator = this.currentData.getCurrentArmy().getSelectedUnits().iterator();
        while(iterator.hasNext()){
             if(name.equalsIgnoreCase( ((Unit)iterator.next()).getName() ))
                return true;
        }
        
        iterator = this.deletedUnitPanels.iterator();
        while(iterator.hasNext()){
             if(name.equalsIgnoreCase( ((UnitPanel)iterator.next()).getUnit().getName() ))
                return true;
        }
        
        return false;
    }*/

    public void userChangedArmylist(ArmylistArmy army){
        if(army == null)
            return;
        this.mainWindow.endArmyNameEdit();
        this.mainWindow.endTargetPointcostEdit();
        this.mainWindow.endTargetPointcosteditWithoutSaving();
        
        this.setArmylistArmy(army);    
    }
    
    public void refreshPointCosts(){
        this.mainWindow.setPointcostTotal(this.currentData.getCurrentArmy().getPointcostTotal(), this.currentData.getCurrentArmy().getTargetPointcost() - this.currentData.getCurrentArmy().getPointcostTotal());
        Iterator iterator = this.unitPanels.iterator();
        while(iterator.hasNext()){
            ((UnitPanel)iterator.next()).refreshPointcost();
        }
    }
    
    
    public void loadArmy(File file){
        this.logger.debug("load File: "+file.getName());
        this.setUserProperty(GUICore.USER_SAVE_DIRECTORY_KEY, file.getAbsolutePath());
        try{
            this.setArmy( this.armyAccessBean.readArmyXMLByFile(file) );
        }catch(Exception e){
            e.printStackTrace();
        }
    }
 
    public void saveArmy(File file, boolean remember){
        this.logger.debug("save File: "+file.getName());
        if(remember)
            this.setUserProperty(GUICore.USER_SAVE_DIRECTORY_KEY, file.getAbsolutePath());
        try{
            this.armyAccessBean.writeArmyXMLByFile(this.currentData.getCurrentArmy(), file);
        }catch(Exception e){
            this.logger.error("error saving army", e);
        }        
    }    
    
    
    public MainWindow getMainwindow(){
        return this.mainWindow;
    }
    
    
    public void removeArmylistUnit(Object selectedListValue){
        ArmylistArmy armylist = this.getArmylistArmy();
        armylist.removeUnit((ArmylistUnit)selectedListValue);
        this.mainWindow.setUnitListModel(this.armylistListModelFactory.getUnitChoises(this.currentData.getCurrentArmylistArmy())); 
    }

/*    public void cloneNewArmylist(String name, String toClone){
        ArmylistArmy newList = null;
        try{
            newList = this.aab.cloneArmylist(name, toClone);
        }catch(Exception e){
            e.printStackTrace();
        }
        this.armylistsModel.addElement(newList.getName());
        this.setArmylistArmy(newList);
    }    */
    
    
    public void createNewArmylist(String name, String gameSystemName){
        ArmylistArmy newList = null;
        GameSystem gameSystem = GameSystemAccessBean.getInstance().getGameSystemByName(gameSystemName);
        try{
            newList = this.aab.createNewArmylist(name, gameSystem);
        }catch(Exception e){
            logger.error("Failed to create new armylist", e);
            return;
        }
        
        this.initArmylists();
        this.setArmylistArmy(newList);
    }
    
    public void unitTypeSelectedInUnitList(String unitType){
        this.activeFilter = unitType;
        this.mainWindow.setUnitListModel(this.armylistListModelFactory.getUnitChoises(this.currentData.getCurrentArmylistArmy(), unitType));
        this.currentFilterDeletedUnitsModel = this.filerListModelByType(this.deletedUnitsModel, unitType);
        this.mainWindow.setDeletedUnitListModel(this.currentFilterDeletedUnitsModel);
        
        this.currentFilterNamedUnitsModel = this.filerListModelByType(this.namedUnitsModel, unitType);
        this.mainWindow.setNamedUnitsListModel(this.currentFilterNamedUnitsModel);
    }
    
    public void unitTypeFilterUnselected(){
        this.activeFilter = "";
        this.mainWindow.setUnitListModel(this.armylistListModelFactory.getUnitChoises(this.currentData.getCurrentArmylistArmy()));
        this.mainWindow.setDeletedUnitListModel(this.deletedUnitsModel);
        this.currentFilterDeletedUnitsModel = null;
        
        if(this.namedUnitsModel != null)
            this.mainWindow.setNamedUnitsListModel(this.namedUnitsModel);
        this.currentFilterNamedUnitsModel = null;
    }
    
    private DefaultListModel filerListModelByType(DefaultListModel model, String unitType){ 
        DefaultListModel ret = new DefaultListModel();
        for(int i = 0; i < model.getSize(); ++i){
            Unit temp = null;
            if(model.getElementAt(i) instanceof Unit){
                temp = (Unit)model.getElementAt(i);
            }if(model.getElementAt(i) instanceof UnitPanel){
                temp = ((UnitPanel)model.getElementAt(i)).getUnit();
            }
            if(temp == null)
                continue;
            if(temp.getArmylistUnit().getUnitType().equalsIgnoreCase(unitType)){
                ret.addElement(model.getElementAt(i));
            }
        }
        return ret;
    }
    
    public void setArmyName(String newName){
        this.currentData.getCurrentArmy().setName(newName);
    }
    
    public boolean setTargetPointcost(String cost){
        try{
            this.currentData.getCurrentArmy().setTargetPointcost(Double.parseDouble(cost));
        }catch(NumberFormatException ex){
            return false;
        }
        this.refreshPointCosts();
        return true;
    }
    
    public String getUserProperty(String name){
        return this.userPreferences.getProperty(name);
    }
    
    public void setUserProperty(String name, String value){
        this.userPreferences.setProperty(name, value);
    }
  
    public void moveUnitUp(UnitPanel panel, Unit unit){
        if(this.currentData.getCurrentArmy().moveUnitUp(unit)){
            this.mainWindow.moveUnitPanelUp(panel, unit.getArmylistUnit().getUnitType());
        }
    }

    public void moveUnitDown(UnitPanel panel, Unit unit){
        if(this.currentData.getCurrentArmy().moveUnitDown(unit)){
            this.mainWindow.moveUnitPanelDown(panel, unit.getArmylistUnit().getUnitType());
        }
    }
    
    
    public static void main(String args[]){
        PropertyConfigurator.configure("conf/log4j.properties");
        //System.setProperty("log4j.configuration", "config/log4j.properties");
        try{
            GUICore core = new GUICore();   
        }catch(Exception e){
            logger.fatal("Something failed", e);
            try{
                Thread.sleep(5000);
            }catch(Exception ex){}
            System.exit(1);
        }
    }
}
