package org.jArmyTool.data.dataBeans.util;

/*
 * ModelStat.java
 *
 * Created on 04 December 2002, 22:53
 */

/**
 * 
 * @author  pasi
 */
public class ModelStat {
    
    private String type;
    private String symbol;
    private String tooltip;    
    
    /** Creates a new instance of ModelStat */
    public ModelStat(String symbol, String tooltip, String type) {
        this.type = type;
        this.symbol = symbol;
        this.tooltip = tooltip;
    }
    
    public ModelStat(ModelStat toClone){
        this.symbol = toClone.symbol;
        this.tooltip = toClone.tooltip;
        this.type = toClone.type;
    }
    
    public String getSymbol(){
        return this.symbol;
    }
    
    public String getTooltip(){
        return this.tooltip;
    }

    public String getType(){
        return this.type;
    }    
}
