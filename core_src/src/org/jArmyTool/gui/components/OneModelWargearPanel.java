/*
 * OneModelWargearPanel.java
 *
 * Created on 18 November 2003, 22:37
 */

package core_src.src.org.jArmyTool.gui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import org.jArmyTool.data.dataBeans.army.Model;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearGroup;
import org.jArmyTool.data.dataBeans.armylist.ArmylistWargearItem;
import org.jArmyTool.gui.layout.VerticalFlowLayout;

/**
 *
 * @author  pasi
 */
public class OneModelWargearPanel extends javax.swing.JPanel {
    private static final int MAX_NAME_LENGTH = 24;
    private static final int SEARCH_START_INDEX = 15;
    
    private OneModelUpdatesExpanded updatesPanel;
    private Model model;
    
    /** Creates new form OneModelWargearPanel */
    public OneModelWargearPanel(OneModelUpdatesExpanded updates, Model model) {
        this.updatesPanel = updates;
        this.model = model;
        initComponents();
        this.allWG.setLayout(new VerticalFlowLayout());
        this.selectedWG.setLayout(new VerticalFlowLayout());
        this.initData();
        
        this.jScrollPane1.addMouseWheelListener(new MouseWheelListener(){
            public void mouseWheelMoved(MouseWheelEvent e){
                
                JScrollBar sb = jScrollPane1.getVerticalScrollBar();
                sb.setValue(sb.getValue() + (e.getWheelRotation() * MainWindow.WHEEL_STEPS) );
            }
        });
        this.jScrollPane2.addMouseWheelListener(new MouseWheelListener(){
            public void mouseWheelMoved(MouseWheelEvent e){
                
                JScrollBar sb = jScrollPane2.getVerticalScrollBar();
                sb.setValue(sb.getValue() + (e.getWheelRotation() * MainWindow.WHEEL_STEPS) );
            }
        });        
    }
    
    private void initData(){
        if(this.model.getArmylistModel().getAllowedWargear() == 0)
            return;
        
        Iterator groups = this.model.getArmylistModel().getAllowedWargearGroups().iterator();
        while(groups.hasNext()){
            final ArmylistWargearGroup group = this.model.getArmylistModel().getArmylistArmy().getWargearGroupbyName((String)groups.next());
            if(group == null)
                continue;
            JPanel groupHeaderPanelAll = new JPanel();
            groupHeaderPanelAll.add(new JLabel(group.getName()));
            groupHeaderPanelAll.setBackground(MainWindow.TITLE_BACKGROUND);
            groupHeaderPanelAll.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
            
            JPanel groupHeaderPanelSelected = new JPanel();
            groupHeaderPanelSelected.add(new JLabel(group.getName()));
            groupHeaderPanelSelected.setBackground(MainWindow.TITLE_BACKGROUND);
            groupHeaderPanelSelected.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
            
            this.allWG.add(groupHeaderPanelAll);
            
            final JPanel selectedPanelGroup = new JPanel();
            selectedPanelGroup.setLayout(new VerticalFlowLayout());
            selectedPanelGroup.add(groupHeaderPanelSelected);
            this.selectedWG.add(selectedPanelGroup);
            
            
            Collection selectedInGroup = this.model.getSelectedWargear(group.getName());
            
            Iterator itemsInGroup = group.getItems().iterator();
            while(itemsInGroup.hasNext()){
                final ArmylistWargearItem item = (ArmylistWargearItem)itemsInGroup.next();
                String name;
                
                
                if(item.getName().length() > MAX_NAME_LENGTH){
                    int space = item.getName().substring(SEARCH_START_INDEX, MAX_NAME_LENGTH).lastIndexOf(" ") + SEARCH_START_INDEX;
                    if(space != -1){
                        name = "<html>"+ item.getName().substring(0, space) +"<p>" +item.getName().substring(space+1, item.getName().length()) + "</html>";
                    }else{
                        name = item.getName().substring(0, MAX_NAME_LENGTH) + "..";
                    }
                }else{
                    name = item.getName();
                }
                final JCheckBox box;
                final JLabel selectedLabel;
                
                if(item.getPointcost() == (int)item.getPointcost()){
                    box = new JCheckBox(name+ " " + ((int)item.getPointcost()));
                    selectedLabel = new JLabel(name+ " " + ((int)item.getPointcost()));
                }else{
                    box = new JCheckBox(name+ " " + item.getPointcost());
                    selectedLabel = new JLabel(name + " " + item.getPointcost());
                }
                box.setToolTipText(item.getName());
                selectedLabel.setToolTipText(item.getName());
                
               // new JCheckBox(item.getName()+ " " + (item.getPointcost() == ((int)item.getPointcost()) ? ((int)item.getPointcost()) : (int)item.getPointcost())  );
                 //= new JLabel(item.getName() + " " + (item.getPointcost()== ((int)item.getPointcost()) ? ((int)item.getPointcost()) : (int)item.getPointcost())  );
                if(selectedInGroup.contains(item)){
                    selectedPanelGroup.add(selectedLabel);
                    selectedLabel.addMouseListener(new MouseAdapter(){
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                removeWG(selectedPanelGroup, selectedLabel, group.getName(), item, box);
                            }       
                            
                            public void mouseEntered(java.awt.event.MouseEvent evt) {
                                selectedLabel.setFont(new Font(selectedLabel.getFont().getName(), Font.BOLD, selectedLabel.getFont().getSize()));
                            }        
                            public void mouseExited(java.awt.event.MouseEvent evt) {
                                selectedLabel.setFont(new Font(selectedLabel.getFont().getName(), Font.PLAIN, selectedLabel.getFont().getSize()));
                            }   
                    });         
                    selectedLabel.setCursor(MainWindow.DEL_CURSOR);
                    box.setSelected(true);
                }
                box.addActionListener(new ActionListener(){
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        if(box.isSelected()){
                            addWG(group.getName(), item, selectedPanelGroup, box);
                        }else{
                            removeWG(selectedPanelGroup, selectedLabel, group.getName(), item, box);
                        }
                    }                    
                });
                
                this.allWG.add(box);
                
            }
        }
       
    }
    
    private void addWG(final String group, final ArmylistWargearItem item, final JPanel selectedGroupPanel, final JCheckBox box){
        this.model.selectWargear(group, item);
        final JLabel selectedLabel;
        
        String name;
        if(item.getName().length() > MAX_NAME_LENGTH){
            int space = item.getName().substring(SEARCH_START_INDEX, MAX_NAME_LENGTH).lastIndexOf(" ") + SEARCH_START_INDEX;
            if(space != -1){
                name = "<html>"+ item.getName().substring(0, space) +"<p>" +item.getName().substring(space+1, item.getName().length()) + "</html>";
            }else{
                name = item.getName().substring(0, MAX_NAME_LENGTH) + "..";
            }
        }else{
            name = item.getName();
        }
        
        if(item.getPointcost() == (int)item.getPointcost()){
            selectedLabel = new JLabel(name+ " " + ((int)item.getPointcost()));
        }else{
            selectedLabel = new JLabel(name+ " " + item.getPointcost());
        }
        selectedLabel.setToolTipText(item.getName());
        selectedLabel.setCursor(MainWindow.DEL_CURSOR);
        selectedLabel.addMouseListener(new MouseAdapter(){
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    removeWG(selectedGroupPanel, selectedLabel, group, item, box);
                }    
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    selectedLabel.setFont(new Font(selectedLabel.getFont().getName(), Font.BOLD, selectedLabel.getFont().getSize()));
                }        
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    selectedLabel.setFont(new Font(selectedLabel.getFont().getName(), Font.PLAIN, selectedLabel.getFont().getSize()));
                }
        });
        selectedGroupPanel.add(selectedLabel);
        selectedGroupPanel.updateUI();
        box.addActionListener(new ActionListener(){
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        if(box.isSelected()){
                            //addWG(group, item, selectedGroupPanel, box);
                        }else{
                            removeWG(selectedGroupPanel, selectedLabel, group, item, box);
                        }
                    }                    
         });
         this.updatesPanel.refreshPointcost();
    }
    
    private void removeWG(JPanel selectedGroupPanel, JLabel itemLabel, String group, ArmylistWargearItem item, final JCheckBox box ){
        this.model.deSelectWargear(group, item);
        selectedGroupPanel.remove(itemLabel);
        box.setSelected(false);
        selectedGroupPanel.updateUI();
        this.updatesPanel.refreshPointcost();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        selectedWG = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        allWG = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(selectedWG);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.WEST);

        jScrollPane2.setMaximumSize(new java.awt.Dimension(300, 300));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(300, 300));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 300));
        jScrollPane2.setViewportView(allWG);

        jPanel1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel allWG;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel selectedWG;
    // End of variables declaration//GEN-END:variables
    
}
