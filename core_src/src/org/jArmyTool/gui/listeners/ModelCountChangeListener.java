/*
 * ModelCountChangeListener.java
 *
 * Created on 26 December 2002, 14:34
 */

package org.jArmyTool.gui.listeners;

import java.awt.event.*;
import javax.swing.event.*;
import org.jArmyTool.gui.components.*;
import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.gui.engine.*;
/**
 *
 * @author  pasi
 */
public class ModelCountChangeListener implements ChangeListener, ActionListener {
    
    ModelPanel modelPanel;
    Model model;
    
    /** Creates a new instance of ModelCountChangeListener */
    public ModelCountChangeListener(ModelPanel modelPanel, Model model) {
        this.modelPanel = modelPanel;
        this.model = model;
    }
    
    public void stateChanged(ChangeEvent e){
        this.handleEvent();
    }
    
    /** Invoked when an action occurs.
     *
     */
    public void actionPerformed(ActionEvent e) {
        this.handleEvent();
    }
    
    private void handleEvent(){
        this.model.setModelCount(this.modelPanel.getModelCount());
        this.modelPanel.refreshPointcost();     
    }
    
}
