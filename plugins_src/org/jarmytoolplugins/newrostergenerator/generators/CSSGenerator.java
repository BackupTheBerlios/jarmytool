/*
 * CSSGenerator.java
 *
 * Created on 28 December 2003, 23:30
 */

package plugins_src.org.jarmytoolplugins.newrostergenerator.generators;

/**
 *
 * @author  pasi
 */
public class CSSGenerator {
    
    public static final String CSS_STYLE_NAME_TABLE = "table";
    public static final String CSS_STYLE_NAME_H1 = "h1";
    public static final String CSS_STYLE_NAME_H2 = "h2";
    public static final String CSS_STYLE_NAME_UNIT_NAME = "unit_name";
    public static final String CSS_STYLE_NAME_UNIT_BOX = "unit_box";
    public static final String CSS_STYLE_NAME_UNIT_BOX_BORDER_TOP = "unit_box_border_top";
    public static final String CSS_STYLE_NAME_UNIT_STAT_NAME_BOX = "stat_name_box";
    
    public static final String CSS_STYLE_NAME_UNIT_BG_1 = "unit_bg1";
    public static final String CSS_STYLE_NAME_UNIT_BG_2 = "unit_bg2";
    
    private String tableFamily = "Tahoma, Verdana";
    private String tableSize = "12px";    
    
    private String h1Family = "Tahoma, Verdana";
    private String h1Size = "25px";
    private String h1Weight = "bold";
    
    private String h2Family = "Tahoma, Verdana";
    private String h2Size = "18px";
    private String h2Weight = "bold";    
    
    private String unit_nameFamily = "Tahoma, Verdana";
    private String unit_nameSize = "12px";
    private String unit_nameWeight = "bold";     
 
    private String unit_boxBorder = "1px solid black";
    private String unit_boxBGColor = "#f5f5f5";
    
    private String unit_box_border_topBorderTop = "1px solid black";
    private String unit_box_border_topBGColor = "#f5f5f5";   
    
    private String stat_name_boxBorderBottom = "1px solid black";
    private String stat_name_boxBGColor = "#c0c0c0";      
    
    private String unit_bg_color_1 = "#f5f5f5";     
    private String unit_bg_color_2 = "#e5e5e5";
    
    
    
    
    /** Creates a new instance of CSSGenerator */
    public CSSGenerator() {
    }
    
    public StringBuffer generateCSS(){
        StringBuffer ret = new StringBuffer();
        ret.append("<STYLE TYPE=\"text/css\">\n");
        ret.append(this.generateTable());
        
        ret.append(this.generateH1());
        ret.append(this.generateH2());
        ret.append(this.generateStat_name_box());
        ret.append(this.generateUnit_box());
        ret.append(this.generateUnit_box_border_top());
        ret.append(this.generateUnit_box_border_top());
        ret.append(this.generateUnit_name());
        ret.append(this.generateUnit_bg1());
        ret.append(this.generateUnit_bg2());
        
        
        ret.append("</STYLE>\n");
        return ret;
    }
    
    private StringBuffer generateTable(){
        StringBuffer ret = new StringBuffer();
        ret.append("table {\nfont-family:"+ this.tableFamily +";\n font-size:"+ this.tableSize +";\n}\n\n");
        return ret;        
    }

    private StringBuffer generateH1(){
        StringBuffer ret = new StringBuffer();
        ret.append("h1 {\nfont-family:"+ this.h1Family +";\n font-size:"+ this.h1Size +";\nfont-weight:"+ this.h1Weight +";\n}\n\n");
        return ret;        
    }    
    
    private StringBuffer generateH2(){
        StringBuffer ret = new StringBuffer();
        ret.append("h2 {\nfont-family:"+ this.h2Family +";\n font-size:"+ this.h2Size +";\nfont-weight:"+ this.h2Weight +";\n}\n\n");
        return ret;        
    }    

    private StringBuffer generateUnit_name(){
        StringBuffer ret = new StringBuffer();
        ret.append(".unit_name {\nfont-family:"+ this.unit_nameFamily +";\n font-size:"+ this.unit_nameSize +";\nfont-weight:"+ this.unit_nameWeight +";\n}");
        return ret;        
    }
    
    private StringBuffer generateUnit_box(){
        StringBuffer ret = new StringBuffer();
        ret.append(".unit_box {\nborder:"+ this.unit_boxBorder +";\n background-color:"+ this.unit_boxBGColor +";}\n\n");
        return ret;        
    }    

    private StringBuffer generateUnit_box_border_top(){
        StringBuffer ret = new StringBuffer();
        ret.append(".unit_box_border_top {\nborder-top:"+ this.unit_box_border_topBorderTop +";\n background-color:"+ this.unit_box_border_topBGColor +";}\n\n");
        return ret;        
    }    

    private StringBuffer generateStat_name_box(){
        StringBuffer ret = new StringBuffer();
        ret.append(".stat_name_box {\nborder-bottom:"+ this.stat_name_boxBorderBottom +";\n background-color:"+ this.stat_name_boxBGColor +";}\n\n");
        return ret;        
    }        
 
    
    private StringBuffer generateUnit_bg1(){
        StringBuffer ret = new StringBuffer();
        ret.append(".unit_bg1 {\nbackground-color:"+ this.unit_bg_color_1 +";}\n\n");
        return ret;        
    }      
    
    private StringBuffer generateUnit_bg2(){
        StringBuffer ret = new StringBuffer();
        ret.append(".unit_bg2 {\nbackground-color:"+ this.unit_bg_color_2 +";}\n\n");
        return ret;        
    }          
}
