/*
 * UnitListListener.java
 *
 * Created on 22 December 2002, 03:43
 */

package core_src.src.org.jArmyTool.gui.listeners;

import java.awt.event.*;
import org.apache.log4j.Logger;
import org.jArmyTool.gui.engine.*;
import org.jArmyTool.gui.components.*;
import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.data.factories.*;
/**
 *
 * @author  pasi
 */
public class UnitListListener implements ActionListener {
 
    private Logger logger;
    private GUICore core;
    private MainWindow mainWindow;
    private Factory factory;
    
    /** Creates a new instance of UnitListListener */
    public UnitListListener(GUICore core, MainWindow mainWindow) {
        this.logger = Logger.getLogger(UnitListListener.class);
        this.core = core;
        this.mainWindow = mainWindow;
        this.factory = Factory.getInstance();
    }
    
    /** Invoked when an action occurs.
     *
     */
    public void actionPerformed(ActionEvent e) {
        if(this.mainWindow.getUnitListSelected() == null)
            return;
        ArmylistUnit unit = (ArmylistUnit)this.mainWindow.getUnitListSelected();
        core.addUnitToArmy(this.factory.createUnit(unit));
    }
    
}
