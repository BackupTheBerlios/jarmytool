/*
 * WargearPanelContainer.java
 *
 * Created on 12 July 2004, 04:06
 */

package org.jArmyTool.gui.components.util;

import javax.swing.JPanel;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearGroup;

/**
 *
 * @author  pasi
 */
public class WargearPanelContainer {
    
    private JPanel panel;
    private ArmylistWargearGroup group;
    
    /** Creates a new instance of WargearPanelContainer */
    public WargearPanelContainer(JPanel panel, ArmylistWargearGroup group) {
        this.panel = panel;
        this.group = group;
    }
    
    public JPanel getPanel(){
        return this.panel;
    }
    
    public ArmylistWargearGroup getGroup(){
        return this.group;
    }
    
}
