/*
 * UnitUpdateCountChangeListener.java
 *
 * Created on 29 December 2002, 02:23
 */

package core_src.src.org.jArmyTool.gui.listeners;

import javax.swing.event.*;
import java.awt.event.*;
import org.jArmyTool.gui.components.*;
import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.gui.engine.*;

/**
 *
 * @author  pasi
 */
public class UnitUpdateCountChangeListener implements ChangeListener, ActionListener {
    
    private UnitUpdate unitUpdate;
    private UnitUpdatePanel unitUpdatePanel;
    private GUICore guiCore;
    
    /** Creates a new instance of UnitUpdateCountChangeListener */
    public UnitUpdateCountChangeListener(UnitUpdatePanel unitUpdatePanel, UnitUpdate unitUpdate, GUICore core) {
        this.unitUpdatePanel = unitUpdatePanel;
        this.unitUpdate = unitUpdate;
        this.guiCore = core;
    }
    
    /** Invoked when the target of the listener has changed its state.
     *
     * @param e  a ChangeEvent object
     *
     */
    public void stateChanged(ChangeEvent e) {
        this.handleEvent();
    }
    
    private void handleEvent(){
        this.unitUpdate.setSelectedCount(this.unitUpdatePanel.getUpdateCount());
        this.unitUpdatePanel.refreshPointcost();
        this.unitUpdatePanel.refreshColor();
        this.guiCore.refreshPointCosts();
    }
    
    /** Invoked when an action occurs.
     *
     */
    public void actionPerformed(ActionEvent e) {
        this.handleEvent();
    }
    
}
