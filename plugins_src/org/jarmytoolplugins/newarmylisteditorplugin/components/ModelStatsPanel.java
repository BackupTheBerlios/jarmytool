/*
 * ModelStatsPanel.java
 *
 * Created on 07 January 2004, 01:32
 */

package plugins_src.org.jarmytoolplugins.newarmylisteditorplugin.components;

import java.awt.BorderLayout;
import java.lang.StringBuffer;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.jArmyTool.data.dataBeans.armylist.ArmylistModel;
import org.jArmyTool.data.dataBeans.util.ModelStat;
import org.jArmyTool.data.dataBeans.util.ModelStatHolder;
import org.jArmyTool.data.dataBeans.util.StatType;

/**
 *
 * @author  pasi
 */
public class ModelStatsPanel extends javax.swing.JPanel {
    
    private ArmylistModel model;
    private DefaultTableModel tableModel;
    
    
    /** Creates new form ModelStatsPanel */
    public ModelStatsPanel(ArmylistModel model) {
        this.model = model;
        initComponents();
        this.initTable();
    }
    
    private void initTable(){
        
        if (this.model.getStatTypeName() == null){
            this.model.setStatType((String)this.model.getArmylistArmy().getGameSystem().getStatTypeNames().iterator().next());
        }
        
        StatType type = this.model.getArmylistArmy().getGameSystem().getStatType(this.model.getStatTypeName());
        
        Iterator stats = type.getAllStats().iterator();
        
        int headersSize = type.getAllStats().size();
        Vector headers = new Vector();
        while(stats.hasNext()){
            headers.add( ((ModelStat)stats.next()).getSymbol() );
        }
        
        
        
        if(this.model.getStats() == null){
            StringBuffer buf = new StringBuffer();
            for(int j = 0; j < headersSize; ++j){
                buf.append(" ");
                if(j+1 < headersSize){
                    buf.append(",");
                }
            }
            
            this.model.setStatValues(buf.toString(), ",");
        }
        
        Iterator statsV = this.model.getStats().iterator();
        Object[] statValues = new Object[this.model.getStats().size()];
        int i = 0;
        while(statsV.hasNext()){
            statValues[i] = ((ModelStatHolder)statsV.next()).getValue();
            ++i;
        }
        
        this.tableModel = new DefaultTableModel(headers, 0);
        this.tableModel.addRow(statValues);
        
        //this.basePanel.add(this.table.getTableHeader(), BorderLayout.NORTH);
        
        
        
        this.table.setModel(this.tableModel);

    }
    
    public String getAsText(String separator){
        this.table.editCellAt(0,0);
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < this.tableModel.getColumnCount(); ++i){
            String next = " ";
            if(this.tableModel.getValueAt(0, i) != null && this.tableModel.getValueAt(0, i).toString() != null)
                next = this.tableModel.getValueAt(0, i).toString();
            
            buf.append(next);
            if(i+1 < this.tableModel.getColumnCount()){
                buf.append(separator);
            }
        }
        return buf.toString();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        basePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        basePanel.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setMaximumSize(new java.awt.Dimension(200, 50));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 50));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 50));
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(table);

        basePanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(basePanel, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel basePanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
    
}