/*
 * ModelStatHolder.java
 *
 * Created on 09 June 2003, 14:11
 */

package core_src.src.org.jArmyTool.data.dataBeans.util;

/**
 *
 * @author  pasleh
 */
public class ModelStatHolder {
    
    private ModelStat stat;
    
    private String value;
    /** Creates a new instance of ModelStatHolder */
    public ModelStatHolder(ModelStat stat) {
        this.stat = stat;
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
}
