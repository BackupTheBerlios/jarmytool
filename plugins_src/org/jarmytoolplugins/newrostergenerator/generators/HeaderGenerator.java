/*
 * HeaderGenerator.java
 *
 * Created on 24 December 2003, 02:30
 */

package plugins_src.org.jarmytoolplugins.newrostergenerator.generators;

import org.jArmyTool.data.dataBeans.army.Army;

/**
 *
 * @author  pasi
 */
public class HeaderGenerator {
    
    /** Creates a new instance of HeaderGenerator */
    private HeaderGenerator() {
    }
    
    public static StringBuffer generateHeader(Army army){
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("<table><tr>");
        
        if(army.getArmylistArmy().getName().compareTo(army.getName()) != 0 ){
            buffer.append("<td><h1>" + army.getName() + "</h1>");
            buffer.append("(" + army.getArmylistArmy().getName() + ")</td>");
            
        }else{
            buffer.append("<td><h1>" + army.getName() + "</h1></td>");            
         
        }
        
        if( (int)army.getPointcostTotal() == army.getPointcostTotal() ){
                buffer.append("<td valign=\"bottom\">" + (int)army.getPointcostTotal() + " Points</td>");
            }else{
                buffer.append("<td valign=\"bottom\">" + army.getPointcostTotal() + " Points</td>");
            }   
        
        buffer.append("</tr></table>");
        
        buffer.append("<hr>");
        
        return buffer;
    }
    
    public static StringBuffer generateBottomInfo(Army army, String version){
        StringBuffer buffer = new StringBuffer();
        
        StringBuffer comments = new StringBuffer();
        boolean anyComments = false;
        comments.append("<hr>");
        
        if(army.getArmylistArmy().getWriter() != null && army.getArmylistArmy().getWriter().length() > 0){
            anyComments = true;
            comments.append("Rules by: "+army.getArmylistArmy().getWriter());
        }
        
        if(army.getArmylistArmy().getEmail() != null && army.getArmylistArmy().getEmail().length() > 0){
            anyComments = true;
            comments.append("<br><a href=\"mailto:"+army.getArmylistArmy().getEmail()+"\""+army.getArmylistArmy().getEmail() +"</a>");
        }
        
        if(army.getArmylistArmy().getWeb() != null && army.getArmylistArmy().getWeb().length() > 0){
            anyComments = true;
            comments.append("<br><a href=\""+army.getArmylistArmy().getWeb()+"\">"+army.getArmylistArmy().getWeb()+"</a>");
        }
        
        if(army.getArmylistArmy().getComments() != null && army.getArmylistArmy().getComments().length() > 0){
            anyComments = true;
            comments.append("<br>"+army.getArmylistArmy().getComments());
        }
        
        if(anyComments)
            buffer.append(comments);
        
        buffer.append("<hr>\n");
        buffer.append("This roster was generated using jArmyTool v."+version+"<br>\n");
        buffer.append("<a href=\"http://www.cs.helsinki.fi/u/pjlehtim/jArmyTool\">http://www.cs.helsinki.fi/u/pjlehtim/jArmyTool</a>");
        return buffer;
    }
    
}
