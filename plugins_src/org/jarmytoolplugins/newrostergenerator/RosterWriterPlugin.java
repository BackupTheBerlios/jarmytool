/*
 * RosterWriterPlugin.java
 *
 * Created on 06 October 2003, 17:36
 */

package org.jarmytoolplugins.newrostergenerator;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import org.jArmyTool.data.dataBeans.army.Army;
import org.jArmyTool.internaldata.*;
import org.jArmyTool.plugininterface.JArmyToolPluginInterface;
import org.jarmytoolplugins.newrostergenerator.engine.RosterGeneratorCore;

/**
 *
 * @author  pasleh
 */
public class RosterWriterPlugin implements JArmyToolPluginInterface{
    
    private GUICommands guiCommands;
    private GlobalData globalData;
    private CurrentData currentData;
    
    
    /** Creates a new instance of RosterWriterPlugin */
    public RosterWriterPlugin() {
    }
    
    public void exit() {
    }
    
    public String getSplasText() {
        return "Initializing roster plugin";
    }
    
    public void initialize(GUICommands gui, GlobalData gd, CurrentData cd) {
        this.guiCommands = gui;
        this.globalData = gd;
        this.currentData = cd;
        
        
        
        JMenuItem roster = new JMenuItem("Generate Roster");
        roster.addActionListener(new java.awt.event.ActionListener() {
           
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RosterGeneratorCore core = new RosterGeneratorCore(currentData.getCurrentArmy(), guiCommands);
            }
            });
        
        this.guiCommands.addMenuItem(roster, "Roster");
    }
    
    public void start() {
    }
    
    public void stop() {
    }
    
}
