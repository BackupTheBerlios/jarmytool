/*
 * WeaponProfile.java
 *
 * Created on 09 October 2003, 00:53
 */

package org.jArmyTool.data.dataBeans.util;

import java.util.*;
/**
 *
 * @author  pasi
 */
public class WeaponProfile {
    
    private LinkedList statHeaders;
    private String name;
    
    /** Creates a new instance of WeaponProfile */
    public WeaponProfile(String name) {
        this.statHeaders = new LinkedList();
        this.name = name;
    }
    
    public void addHeader(String header){
        this.statHeaders.add(header);
        
    }
    
    public Collection getHeaders(){
        return Collections.unmodifiableCollection(this.statHeaders);
    }
    
    public String getName(){
        return this.name;
    }
    
}
