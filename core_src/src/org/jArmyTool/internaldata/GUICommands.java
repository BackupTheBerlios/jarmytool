/*
 * GUICommands.java
 *
 * Created on 22 September 2003, 14:50
 */

package org.jArmyTool.internaldata;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import org.jArmyTool.gui.components.MainWindow;
import org.jArmyTool.gui.components.UnitPanel;
import org.jArmyTool.gui.engine.GUICore;

/**
 * This class does not actually contain any internal data. This class acts as
 * a handler between plugins and core GUI. 
 *
 * @author  Pasi Lehtimäki
 */
public class GUICommands {
    private static GUICommands instance;
    
    private GUICore core;
    
    /** Singleton */
    private GUICommands() {
    }
    
    /**
     * This class is singleton. Use this method to get instance.
     * @return the only instance
     */    
    public static GUICommands getInstance(){
        if(instance == null)
            instance = new GUICommands();
        
        return instance;
    }
    
    /**
     * Used by Core. Can be used only once.
     *
     * @throws IllegalArgumentException if core is already set.
     */
    public void setGUICore(GUICore core){
        if(this.core != null)
            throw new IllegalArgumentException("Illegal setGUICore() call. Core allready set");
        this.core = core;
    }
    
    /**
     * This method will add a JManuItem to main UI.
     * Path defines the menu hierarchy. Use '.' separator in path.<br>
     * Example: path: menu1.menu2.menu3<br>
     * <pre>
     *  menu1
     *   menu2
     *    menu3 - menuItem
     * </pre>
     *
     * If path (or part of it) can allready be found those menus are used.
     *
     * @param menuItem the JMenuItem to be added
     * @param path String - separated by '.'
     */    
    public void addMenuItem(JMenuItem menuItem, String path){
        this.core.addMenuItem(menuItem, path);
    }
    
    /**
     * This method causes GUI to refres armylist data
     */
    public void fireArmylistDataChange(){
        this.core.refreshArmylistData();
    }
    
    /**
     * @return Dimension - Size of the main window
     */    
    public Dimension getMainWindowDimension(){
        return this.core.getMainwindow().getSize();
    }
    
    /**
     * @return String - version of jArmyTool
     */    
    public String getVersion(){
        return this.core.getVersion();
    }
    
    /**
     * @return Cursor - cursor for delete
     */   
    public Cursor getXCursor(){
        return MainWindow.DEL_CURSOR;
    }
    
    public Image getProgramIcon(){
        return MainWindow.PROGRAM_ICON;
    }
    
    public int getWheelSteps(){
        return MainWindow.WHEEL_STEPS;
    }
    
    public ImageIcon getXIcon(){
        return UnitPanel.CLOSE_X;
    }
    
    public ImageIcon getMoveUpArrow(){
        return UnitPanel.UP_ARROW;
    }
    
    public ImageIcon getMovedDownArrow(){
        return UnitPanel.DOWN_ARROW;
    }    
}
