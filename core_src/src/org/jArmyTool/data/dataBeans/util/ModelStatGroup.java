package org.jArmyTool.data.dataBeans.util;

/*
 * ModelStatGroup.java
 *
 * Created on 04 December 2002, 22:52
 */

import java.util.*;

/**
 *
 * @author  pasi
 */
public class ModelStatGroup {
    
    private String name;
    private LinkedList stats;
    
    /** Creates a new instance of ModelStatGroup */
    public ModelStatGroup(String name) {
        this.name = name;
        this.stats = new LinkedList();
    }
    
    
    /**
     * @return Iterator containing ModelStat-objects
     */
    public Iterator getStats(){
        return this.stats.iterator();
    }
    
    public void addStat(ModelStat stat){
        this.stats.add(stat);
    }
}
