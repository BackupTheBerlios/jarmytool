/*
 * WargearGroupTextField.java
 *
 * Created on 12 January 2004, 14:40
 */

package plugins_src.org.jarmytoolplugins.newarmylisteditorplugin.wargearEditor.util;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearGroup;
import org.jarmytoolplugins.newarmylisteditorplugin.wargearEditor.components.WargearEditorMainWindow;

/**
 *
 * @author  pasleh
 */
public class WargearGroupTextField extends JTextField{
    
    private ArmylistWargearGroup group;
    private WargearEditorMainWindow parent;
    
    /** Creates a new instance of WargearGroupTextField */
    public WargearGroupTextField(ArmylistWargearGroup group_, WargearEditorMainWindow parent_) {
        this.group = group_;
        this.parent = parent_;
        this.setText(group.getName());
        this.addFocusListener(new FocusAdapter(){
            public void focusLost(FocusEvent e){
                performAction();
            }
        }); 
        this.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                performAction();
            }
        });
    }
    
    private void performAction(){
        if(this.getText().length() <= 0){
            this.parent.removeGroup(this);
            return;
        }
        group.setName(getText());
        parent.refreshGroups();
    }
    
    public ArmylistWargearGroup getGroup(){
        return this.group;   
    }
    
}
