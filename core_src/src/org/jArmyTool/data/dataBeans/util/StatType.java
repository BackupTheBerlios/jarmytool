/*
 * StatType.java
 *
 * Created on 28 May 2003, 15:39
 */

package org.jArmyTool.data.dataBeans.util;

import java.util.*;

/**
 *
 * @author  pasleh
 */
public class StatType {
    
    LinkedList stats;
    
    /** Creates a new instance of StatType */
    public StatType() {
        this.stats = new LinkedList();
    }
    
    public void addStat(ModelStat stat){
        this.stats.add(stat);
    }
    
    public Collection getAllStats(){
        return Collections.unmodifiableCollection(this.stats);
    }
    
    public boolean equals(Object another){
        if(another instanceof StatType){
            return this.stats.equals(((StatType)another).stats);
        }
        return false;
    }
    
}
