/*
 * ModelStatHolder.java
 *
 * Created on 09 June 2003, 14:11
 */

package org.jArmyTool.data.dataBeans.util;

import org.jArmyTool.data.util.statCalc;
/**
 *
 * @author  pasleh
 */
public class ModelStatHolder {
    
    private ModelStat stat;
    private statCalc statcalc;
    
    private String value;
    /** Creates a new instance of ModelStatHolder */
    public ModelStatHolder(ModelStat stat) {
        this.stat = stat;
        this.statcalc = null;
    }
    
    public ModelStatHolder(ModelStatHolder toClone) {
        this.stat = new ModelStat(toClone.stat);
        this.value = toClone.value;
    }
    
    public void setValue(String value){
        this.value = value;
    }
    
    public String getValue(){
        return this.value;
    }
    
    public ModelStat getStat(){
        return this.stat;
    }
    
    public void setCalc(statCalc s)
    {
        this.statcalc = s;
        System.out.println("setCalc called");
    }
    
    public void calculate()
    {
        if(statcalc != null)
        {
            this.statcalc.calculate();
            this.value = this.statcalc.getStat();
        }
    }
    
    public String calcValue()
    {
        //System.out.println("calcValue called");
        if(statcalc != null)
        {
            this.statcalc.calculate();
            return this.statcalc.getStat();
        }
        return getValue();
    }
    
    public void clearCalc()
    {
        this.statcalc = null;
    }
    
}
