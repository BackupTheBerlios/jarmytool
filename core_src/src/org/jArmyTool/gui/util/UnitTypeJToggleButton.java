/*
 * UnitTypeJToggleButton.java
 *
 * Created on 11 July 2003, 02:12
 */

package core_src.src.org.jArmyTool.gui.util;

import java.awt.Image;
import java.awt.Insets;
import javax.swing.ImageIcon;

/**
 *
 * @author  pasi
 */
public class UnitTypeJToggleButton extends javax.swing.JToggleButton {
    private String unitType;
    
    private ImageIcon untoggled;
    private ImageIcon toggled;
    
    /** Creates a new instance of UnitTypeJToggleButton */
    public UnitTypeJToggleButton(String unitType, Image untoggled, Image toggled) {
        super(new ImageIcon(untoggled));
        this.untoggled = new ImageIcon(untoggled);
        this.toggled = new ImageIcon(toggled);
        this.unitType = unitType;
        this.setMargin(new Insets(0,0,0,0));
    }
    
    public String getUnitType(){
        return this.unitType;
    }
    
    public void setSelected(boolean selected){
        super.setSelected(selected);
        if(selected){
            this.setIcon(this.toggled);
        }else{
            this.setIcon(this.untoggled);
        }
    }
    
    
}
