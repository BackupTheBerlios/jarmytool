/*
 * DeletedUnitListListener.java
 *
 * Created on 27 December 2002, 03:03
 */

package core_src.src.org.jArmyTool.gui.listeners;

import java.awt.event.*;
import org.jArmyTool.data.dataBeans.army.Unit;
import org.jArmyTool.gui.engine.*;
import org.jArmyTool.gui.components.*;
/**
 *
 * @author  pasi
 */
public class DeletedUnitListListener implements ActionListener {
    
    private MainWindow mainWindow;
    private GUICore guiCore;
    
    /** Creates a new instance of DeletedUnitListListener */
    public DeletedUnitListListener(GUICore core, MainWindow mainWindow){
        this.mainWindow = mainWindow;
        this.guiCore = core;
    }
    
    public void actionPerformed(ActionEvent e) {
        if(this.mainWindow.getDeletedUnitListSelected() == null)
            return;
        
        Unit unit = (Unit)this.mainWindow.getDeletedUnitListSelected();
        this.guiCore.addDeletedUnitToArmy(unit);
    }
}
