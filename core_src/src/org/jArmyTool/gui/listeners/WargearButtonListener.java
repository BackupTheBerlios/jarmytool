/*
 * WargearButtonListener.java
 *
 * Created on 19 January 2003, 00:14
 */

package core_src.src.org.jArmyTool.gui.listeners;

import java.awt.event.*;
import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.gui.components.*;
/**
 *
 * @author  pasi
 */
public class WargearButtonListener implements ActionListener{
 
    //private ArmylistArmy army;
    //private Model model;
    //private ModelPanel modelPanel;
    private WargearPopup popup;
    
    /** Creates a new instance of WargearButtonListener */
    public WargearButtonListener(ArmylistArmy army, Model model, ModelPanel modelPanel) {
        //this.army = army;
        //this.model = model;
        //this.modelPanel = modelPanel;
        
        this.popup = new WargearPopup(army, model, modelPanel);
    }
    
    /** Invoked when an action occurs.
     *
     */
    public void actionPerformed(ActionEvent e) {
        popup.show();
    }
    
}
