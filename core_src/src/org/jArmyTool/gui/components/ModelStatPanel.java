/*
 * ModelStatPanel.java
 *
 * Created on 25 June 2003, 22:43
 */

package core_src.src.org.jArmyTool.gui.components;

import org.jArmyTool.data.dataBeans.army.*;
//import org.jArmyTool.data.dataBeans.armylist.*;
import org.jArmyTool.data.dataBeans.util.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
/**
 *
 * @author  pasi
 */
public class ModelStatPanel extends JPanel {
    private static final int FIRST_LINE = 10;
    private static final int LINE_SPACING = 10;
    
    private Model model;
    private int statSpacing;
    private boolean displayHeaders;
    
    /** Creates a new instance of ModelStatPanel */
    public ModelStatPanel(Model model, int statSpacing, boolean displayHeaders) {
        this.model = model;
        this.displayHeaders = displayHeaders;
        this.statSpacing = statSpacing;
        
        int statCount = this.model.getArmylistModel().getStats().size();
        int height = FIRST_LINE;
        if(this.displayHeaders)
            height = height + LINE_SPACING;
        
        this.setPreferredSize(new Dimension(statCount * this.statSpacing, height));
    }
    
    
    public void paint(Graphics g){
        super.paint(g);
        Collection c = this.model.getArmylistModel().getStats();
        
        
        Iterator iterator = c.iterator();
        LinkedList temp = new LinkedList();
        Font basicFont = g.getFont();
        g.setFont(new Font( basicFont.getName(), Font.BOLD, basicFont.getSize() ));
        
        int count = 0;
        while(iterator.hasNext()){
            ModelStatHolder holder = (ModelStatHolder)iterator.next();
            
            temp.add(holder.getValue());
            
            if(this.displayHeaders){
               g.drawString(holder.getStat().getSymbol(), count * this.statSpacing, FIRST_LINE); 
            }
            ++count;
        }
        
        count = 0;
        iterator = temp.iterator();
        
        //g.setFont(basicFont);
        g.setFont(new Font( basicFont.getName(), Font.ITALIC, basicFont.getSize() ));
        int line = FIRST_LINE;
        if(this.displayHeaders)
            line = line + LINE_SPACING;
        while(iterator.hasNext()){
            String str = (String)iterator.next();
            g.drawString(str, count * this.statSpacing, line);
            ++count;
        }
    }
    
}
