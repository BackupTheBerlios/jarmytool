/*
 * ArmylistsSelectListener.java
 *
 * Created on 19 January 2003, 02:51
 */

package org.jArmyTool.gui.listeners;

import java.awt.event.*;
import org.jArmyTool.gui.engine.*;
import org.jArmyTool.gui.components.*;
/**
 *
 * @author  pasi
 */
public class ArmylistsSelectListener implements ActionListener{
    
    private GUICore core;
    private MainWindow mainWindow;
    
    /** Creates a new instance of ArmylistsSelectListener */
    public ArmylistsSelectListener(GUICore core, MainWindow mainWindow) {
        this.core = core;
        this.mainWindow = mainWindow;
    }
    
    /** Invoked when an action occurs.
     *
     */
    public void actionPerformed(ActionEvent e) {
        this.core.userChangedArmylist(mainWindow.getSelectedArmylist());
    }
    
}
