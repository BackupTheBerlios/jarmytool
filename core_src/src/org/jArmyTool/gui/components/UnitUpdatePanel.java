/*
 * UnitUpdatePanel.java
 *
 * Created on 29 December 2002, 02:09
 */

package core_src.src.org.jArmyTool.gui.components;

import java.awt.Color;
import org.jArmyTool.data.dataBeans.army.*;
import org.jArmyTool.gui.engine.*;
import org.jArmyTool.gui.listeners.*;
/**
 *
 * @author  pasi
 */
public class UnitUpdatePanel extends javax.swing.JPanel {
    
    private static final int MAX_NAME_LENGTH = 25;
    private static final int SEARCH_START_INDEX = 10;
    
    private UnitUpdate unitUpdate;
    private GUICore guiCore;
    private UnitUpdateCountChangeListener unitUpdateCountChangeListener;
    
    private boolean isSingular;
    
    private Color ilegalCountBox = Color.red;
    private Color legalCountBox;
    
    private Color ilegalCountSpinner = Color.red;
    private Color legalCountSpinner;
    
    /** Creates new form UnitUpdatePanel */
    public UnitUpdatePanel(UnitUpdate unitUpdate, GUICore guiCore) {
        this.unitUpdate = unitUpdate;
        this.guiCore = guiCore;
        initComponents();
        this.legalCountBox = this.updateCountCheckBox.getBackground();
        this.legalCountSpinner = this.updateCountSpinner.getBackground();
        this.refreshPointcost();
        this.refreshArmylistData();
        this.unitUpdateCountChangeListener = new UnitUpdateCountChangeListener(this, this.unitUpdate, this.guiCore);
        this.initCount();
        this.refreshColor();
    }
    
    
    
    
    
    private void initCount(){
        if(this.unitUpdate.getArmylistUnitUpdate().getMaxCount() > 1 || this.unitUpdate.getArmylistUnitUpdate().getMaxCount() == -1){
            this.isSingular = false;
            this.countPanel.remove(this.updateCountCheckBox);
             
            
            this.updateCountSpinner.addChangeListener(this.unitUpdateCountChangeListener);
            this.updateCountSpinner.setValue(new Integer(this.unitUpdate.getSelectedCount()));            
        }else{
            this.isSingular = true;
            
            this.countPanel.remove(this.updateCountSpinner); 
            this.remove(this.updateNameLabel);
            this.updateCountCheckBox.setText(this.unitUpdate.getName());
            this.updateCountCheckBox.addActionListener(this.unitUpdateCountChangeListener);
            if(this.unitUpdate.getSelectedCount() == 1){
                this.updateCountCheckBox.setSelected(true);
            }
        }
    }    
    
    
    public void refreshPointcost(){
        double cost = this.unitUpdate.getPointcost();
        if(cost == (int)cost){
            this.updatePointcost.setText(""+(int)cost);
        }else{
            this.updatePointcost.setText(""+cost);
        }
    }
    
    public void refreshArmylistData(){
        this.refreshName();
        String listCostText;
        double cost = this.unitUpdate.getArmylistUnitUpdate().getPointcost();
        if(cost == (int)cost){
            listCostText = "(" +  (int)cost;
        }else{
            listCostText = "(" +  cost;
        }
        if(this.unitUpdate.getArmylistUnitUpdate().isPointcostPerModel()){
            listCostText = listCostText + "/model";
        }
        
        this.listCost.setText(listCostText + ")");
    }
    
    private void refreshName(){
        String name;

        if(this.unitUpdate.getName().length() > MAX_NAME_LENGTH){
            int space = this.unitUpdate.getName().substring(SEARCH_START_INDEX, MAX_NAME_LENGTH).lastIndexOf(" ") + SEARCH_START_INDEX;
            if(space != -1){
                name = "<html>"+ this.unitUpdate.getName().substring(0, space) +"<p>" +this.unitUpdate.getName().substring(space+1, this.unitUpdate.getName().length()) + "</html>";
            }else{
                if( !this.unitUpdate.getArmylistUnitUpdate().isPointcostPerModel() && this.unitUpdate.getName().length() > MAX_NAME_LENGTH+6){
                    name = this.unitUpdate.getName().substring(0, MAX_NAME_LENGTH+6) + "..";
                }else{
                    name = this.unitUpdate.getName().substring(0, MAX_NAME_LENGTH) + "..";
                }
            }
            
        }else{
            name = this.unitUpdate.getName();
        }        
        
        if(this.isSingular){
            this.updateCountCheckBox.setText(name);
            this.updateCountCheckBox.setToolTipText(this.unitUpdate.getName());
        }else{
            this.updateNameLabel.setText(name);
            this.updateNameLabel.setToolTipText(this.unitUpdate.getName());
        }
    }

    public int getUpdateCount(){
        if(this.isSingular){
            if(this.updateCountCheckBox.getSelectedObjects() == null)
                return 0;
            return 1;
        }else{
            return ((Integer)this.updateCountSpinner.getValue()).intValue();
        }
    }
    
    public void refreshColor(){
        int count = this.unitUpdate.getSelectedCount();
        
        if( (this.unitUpdate.getArmylistUnitUpdate().getMaxCount() != -1 && count > this.unitUpdate.getArmylistUnitUpdate().getMaxCount() ) || count < this.unitUpdate.getArmylistUnitUpdate().getMinCount() ){
            this.updateCountCheckBox.setBackground(this.ilegalCountBox);
            this.updateCountSpinner.setBackground(this.ilegalCountSpinner);
            
        }else{
            this.updateCountCheckBox.setBackground(this.legalCountBox);
            this.updateCountSpinner.setBackground(this.legalCountSpinner);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        countPanel = new javax.swing.JPanel();
        updateCountSpinner = new javax.swing.JSpinner();
        updateCountCheckBox = new javax.swing.JCheckBox();
        updateNameLabel = new javax.swing.JLabel();
        listCost = new javax.swing.JLabel();
        updatePointcost = new javax.swing.JLabel();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 0));

        countPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        countPanel.add(updateCountSpinner);

        countPanel.add(updateCountCheckBox);

        add(countPanel);

        updateNameLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        updateNameLabel.setText("name");
        add(updateNameLabel);

        listCost.setFont(new java.awt.Font("Dialog", 0, 12));
        listCost.setText("jLabel1");
        add(listCost);

        updatePointcost.setFont(new java.awt.Font("Dialog", 1, 14));
        updatePointcost.setText("cost");
        add(updatePointcost);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel countPanel;
    private javax.swing.JLabel listCost;
    private javax.swing.JCheckBox updateCountCheckBox;
    private javax.swing.JSpinner updateCountSpinner;
    private javax.swing.JLabel updateNameLabel;
    private javax.swing.JLabel updatePointcost;
    // End of variables declaration//GEN-END:variables
    
}
