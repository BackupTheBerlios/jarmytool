/*
 * ExclusiveCheckBox.java
 *
 * Created on 07 December 2003, 13:09
 */

package org.jarmytoolplugins.newarmylisteditorplugin.util;

import javax.swing.JCheckBox;
import org.jArmyTool.data.dataBeans.armylist.ArmylistModel;

/**
 *
 * @author  pasi
 */
public class ExclusiveCheckBox extends JCheckBox{
    
    private int id;
    private ArmylistModel aModel;
    
    /** Creates a new instance of ExclusiveCheckBox */
    public ExclusiveCheckBox(ArmylistModel model, int id) {
        super(model.getName());
        this.id = id;
        this.aModel = model;
    }
    
    public int getId(){
        return this.id;
    }
    
    public ArmylistModel getArmylistModel(){
        return this.aModel;
    }
    
    public void refreshName(){
        this.setText(this.aModel.getName());
    }
    
}
