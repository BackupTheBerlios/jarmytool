/*
 * WargearChangelistener.java
 *
 * Created on 18 January 2003, 23:28
 */

package org.jArmyTool.gui.listeners;

import java.awt.event.*;
import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.gui.components.*;
/**
 *
 * @author  pasi
 */
public class WargearChangelistener implements ActionListener {
    
    private ArmylistWargearItem item;
    private Model model;
    private ModelPanel modelPanel;
    private String groupName;
    private WargearPopup wargearPopup;
    
    /** Creates a new instance of WargearChangelistener */
    public WargearChangelistener(Model model, String groupName, ArmylistWargearItem item, WargearPopup wargearPopup) {
        this.model = model;
        this.item = item;
        this.groupName = groupName;
        this.wargearPopup = wargearPopup;
    }
    
    /** Invoked when an action occurs.
     *
     */
    public void actionPerformed(ActionEvent e) {
        this.model.changeWargearSelection(this.groupName, this.item);
        this.wargearPopup.refreshPointcost();
    }
    
}
