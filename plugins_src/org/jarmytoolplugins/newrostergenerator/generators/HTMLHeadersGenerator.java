/*
 * HTMLHeadersGenerator.java
 *
 * Created on 24 December 2003, 02:32
 */

package plugins_src.org.jarmytoolplugins.newrostergenerator.generators;

import org.jArmyTool.data.dataBeans.army.Army;

/**
 *
 * @author  pasi
 */
public class HTMLHeadersGenerator {
        
    /** Creates a new instance of HTMLHeadersGenerator */
    private HTMLHeadersGenerator() {
    }
    
    public static StringBuffer generateHTMLTop(Army army, CSSGenerator css){
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html>\n<head>\n");
        
        buffer.append(css.generateCSS());
        
        if(army.getTargetPointcost() == (int)army.getTargetPointcost()){
            buffer.append("<title>"+army.getName() + " " + army.getArmylistArmy().getName() + " " + (int)army.getTargetPointcost());
        }else{
            buffer.append("<title>"+army.getName() + " " + army.getArmylistArmy().getName() + " " + army.getTargetPointcost());
        }
        
        buffer.append("</title>\n</head>\n<body>");
        return buffer;
    }
    
    public static StringBuffer generateHTMLBottom(Army army){
        StringBuffer buffer = new StringBuffer();
        buffer.append("</body>\n</html>");
        return buffer;
    }
    
}
