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
public class ModelUpdateCountChangeListener implements ChangeListener, ActionListener {
    
    private ModelUpdate modelUpdate;
    private ModelUpdatePanel modelUpdatePanel;
    private ModelPanel modelPanel;
    private GUICore guiCore;
    
    /** Creates a new instance of UnitUpdateCountChangeListener */
    public ModelUpdateCountChangeListener(ModelUpdatePanel modelUpdatePanel, ModelUpdate modelUpdate, ModelPanel modelPanel, GUICore core) {
        this.modelUpdatePanel = modelUpdatePanel;
        this.modelUpdate = modelUpdate;
        this.modelPanel = modelPanel;
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
       // this.modelUpdate.setSelectedCount(this.modelUpdatePanel.getUpdateCount());
        this.modelUpdatePanel.refreshPointcost();
        this.modelPanel.refreshPointcost();
        this.guiCore.refreshPointCosts();    
    }
    
    /** Invoked when an action occurs.
     *
     */
    public void actionPerformed(ActionEvent e) {
        this.handleEvent();
    }
    
}
