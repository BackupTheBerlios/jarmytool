/*
 * UnitDeleteListener.java
 *
 * Created on 27 December 2002, 01:56
 */

package org.jArmyTool.gui.listeners;

import java.awt.event.*;
import org.jArmyTool.gui.engine.*;
import org.jArmyTool.data.dataBeans.army.*;
/**
 *
 * @author  pasi
 */
public class UnitDeleteListener implements ActionListener {
    
    private GUICore guiCore;
    private Unit unit;
    
    /** Creates a new instance of UnitDeleteListener */
    public UnitDeleteListener(GUICore core, Unit unit) {
        this.guiCore = core;
        this.unit = unit;
    }
    
    /** Invoked when an action occurs.
     *
     */
    public void actionPerformed(ActionEvent e) {
        this.guiCore.removeUnitFromArmy(this.unit);
    }
    
}
