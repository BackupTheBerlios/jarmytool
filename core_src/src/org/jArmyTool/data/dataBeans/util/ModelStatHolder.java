/*
 * ModelStatHolder.java
 *
 * Created on 09 June 2003, 14:11
 */

package org.jArmyTool.data.dataBeans.util;

import org.jArmyTool.data.util.statCalc;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author  pasleh
 */
public class ModelStatHolder {
    
    private ModelStat stat;
    private Vector statcalc;
    
    private String value;
    private String modifiableValue;
    /** Creates a new instance of ModelStatHolder */
    public ModelStatHolder(ModelStat stat) {
        this.stat = stat;
        this.statcalc = new Vector();
    }
    
    public ModelStatHolder(ModelStatHolder toClone) {
        this.stat = new ModelStat(toClone.stat);
        this.value = new String(toClone.value);
        this.statcalc = new Vector(); // <--- possibly duplicate statCalcs? probably not
        this.modifiableValue = new String(this.value);
    }
    
    public void setValue(String value){
        this.value = value;
        this.modifiableValue = this.value;
    }
    
    public String getValue(){
        return this.value;
    }
    
    public ModelStat getStat(){
        return this.stat;
    }
    
    public void setCalc(statCalc s)
    {
        this.statcalc.add(s);
        //System.out.println("setCalc called");
    }
    
    public void calculate()
    {
        this.modifiableValue = new String(this.value);
        Iterator i = this.statcalc.iterator();
        while(i.hasNext())
        {
            statCalc s = (statCalc)i.next();
            HashMap m = new HashMap();
            m.put("oldvalue", this.modifiableValue);
            s.replaceStats(m);
            s.calculate();
            this.modifiableValue = s.getStat();
        }
    }
    
    public String calcValue()
    {
        //System.out.println("calcValue called");
        this.calculate();
        return this.modifiableValue;
    }
    
    public void removeCalc(statCalc s)
    {
        while(statcalc.remove(s)){}  //<-- if we happen to have more then one of this update..
    }
    
    public void clearCalc()
    {
        this.statcalc.clear();
        this.modifiableValue = new String(this.value);
    }
    
}
