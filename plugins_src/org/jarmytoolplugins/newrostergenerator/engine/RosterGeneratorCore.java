/*
 * RosterGeneratorCore.java
 *
 * Created on 24 December 2003, 02:13
 */

package plugins_src.org.jarmytoolplugins.newrostergenerator.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.apache.log4j.Logger;
import org.jArmyTool.data.dataBeans.army.Army;
import org.jArmyTool.internaldata.GUICommands;
import org.jArmyTool.internaldata.UserPreferencesForPlugins;
import org.jarmytoolplugins.newrostergenerator.generators.BBCodeGenerator;
import org.jarmytoolplugins.newrostergenerator.generators.CSSGenerator;
import org.jarmytoolplugins.newrostergenerator.generators.HTMLHeadersGenerator;
import org.jarmytoolplugins.newrostergenerator.generators.HeaderGenerator;
import org.jarmytoolplugins.newrostergenerator.generators.TextGenerator;
import org.jarmytoolplugins.newrostergenerator.generators.UnitGenerator;
import org.jarmytoolplugins.newrostergenerator.generators.WeaponsGenerator;
import org.jarmytoolplugins.newrostergenerator.guiComponents.RosterGeneratorMainWindow;
import org.jarmytoolplugins.newrostergenerator.util.BrowserControl;

/**
 *
 * @author  pasi
 */
public class RosterGeneratorCore {
    
    private static final String PLUGIN_NAME = "rosterGenerator";
    private static final String FILE_DIR = "fileDir";
    
    private static final String TEMP_FILE = "tempData/tempRoster.html";
    
    private static Logger logger = Logger.getLogger(RosterGeneratorCore.class);
    
    private Army army;
    
    private RosterGeneratorMainWindow mainWindow;
    
    private GUICommands guiCommands;
    
    private StringBuffer htmlBuffer;
    private StringBuffer bbCodeBuffer;
    private StringBuffer textBuffer;
    private StringBuffer textPrewievBuffer;
    
    private UnitGenerator unitGenerator;
    private CSSGenerator cssGenerator;
    private WeaponsGenerator weaponsGenerator;
    
    private UserPreferencesForPlugins userPreferences;
    
    private File lastHTML = null;
    
    /** Creates a new instance of RosterGeneratorCore */
    public RosterGeneratorCore(Army army, GUICommands guiCommands) {
        this.guiCommands = guiCommands;
        this.army = army;
        
        this.userPreferences = new UserPreferencesForPlugins(PLUGIN_NAME);
        
        this.unitGenerator = new UnitGenerator(this.army);
        this.cssGenerator = new CSSGenerator();
        this.weaponsGenerator = new WeaponsGenerator(this.army);
        
        
        
        //Show window
        this.mainWindow = new RosterGeneratorMainWindow(this);
        this.mainWindow.setSize(this.guiCommands.getMainWindowDimension());
        
        this.mainWindow.setTitle("jArmyTool v. "+this.guiCommands.getVersion()+" Roster Generator: "+this.army.getName());
        
        this.mainWindow.setIconImage(this.guiCommands.getProgramIcon());
        this.mainWindow.show();
        
        
        this.regenerateHTML();
    }
    
    
  /// HTML  
    public StringBuffer generateHTML(){
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(HTMLHeadersGenerator.generateHTMLTop(this.army, this.cssGenerator));
        buffer.append(HeaderGenerator.generateHeader(this.army));
        
        buffer.append(this.unitGenerator.generateUnits());
        
        buffer.append(this.weaponsGenerator.generateAllWeapos());
        
        buffer.append(HeaderGenerator.generateBottomInfo(this.army, this.guiCommands.getVersion()));
        buffer.append(HTMLHeadersGenerator.generateHTMLBottom(this.army));
        
        return buffer;
    }
    
    public void regenerateHTML(){
        this.htmlBuffer = this.generateHTML();
        this.mainWindow.setDocument(this.htmlBuffer);
    }
    
    public void writeHTML(){
       this.regenerateHTML();
       JFileChooser chooser; 
       if(this.userPreferences.getProperty(FILE_DIR) != null){
           chooser = new JFileChooser(this.userPreferences.getProperty(FILE_DIR));
       }else{
           chooser = new JFileChooser();
       }
       chooser.setDialogTitle("Generate HTML");
       chooser.setFileFilter(new FileFilter(){
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }

                String extension = f.getName().substring(f.getName().lastIndexOf(".") + 1);
                if (extension != null) {
                    if (extension.compareToIgnoreCase("html") == 0 || extension.compareToIgnoreCase("htm") == 0){
                            return true;
                    } else {
                        return false;
                    }
                }

                return false;
            }    
            
            public String getDescription() {
                return "HTML";
            }            
       });
       
       chooser.setSelectedFile(new File(this.army.getName()+ "_"+ (int)this.army.getTargetPointcost() +  ".html"));
       
       int returnVal = chooser.showDialog(null, "Write");
       File file = null;
       if(returnVal == JFileChooser.APPROVE_OPTION) {
           try{
               file = chooser.getSelectedFile();
               PrintWriter out = new PrintWriter(new FileOutputStream(file),true);
               out.print(this.htmlBuffer.toString());
               out.flush();
               this.lastHTML = file;
           }catch(Exception ex){
                logger.error("Exception while writing roster to disk", ex);
           }
       }
       if(file != null){
            this.userPreferences.setProperty(FILE_DIR, file.getAbsolutePath());
       }
        
    }
    
    
    public void launchBrowser(){
        File file = null;
        try{
            file = new File(TEMP_FILE);
            PrintWriter out = new PrintWriter(new FileOutputStream(file),true);
            out.print(this.htmlBuffer.toString());
            out.flush();
        }catch(Exception e){
            return;
        }        
        
        String path = file.getAbsolutePath();   
        
        System.out.println("path: "+path);
        if(path.lastIndexOf("\\") != -1){
           System.out.println("lastI true");
           StringBuffer buf = new StringBuffer(path); 
           int index = 0;
           while(index < buf.length() && index != -1){
                System.out.println("while index:"+index);
                index = buf.indexOf("\\", index);
                if(index != -1){
                    buf.replace(index, index+1, "\\\\");
                    ++index;
                    ++index;
                }
           }
           path = buf.toString();
           //System.out.println("new Path: "+path);
            
        }
        
        //System.out.println("path: "+path);
        //System.out.println("absolute: "+file.getAbsolutePath());
     
        BrowserControl.displayURL("file://"+path);
    }
    
   /// BB CODE
    
    public void generateBBCode(){
        this.bbCodeBuffer = BBCodeGenerator.generateBBCode(this.army);
        this.mainWindow.setDocument(this.bbCodeBuffer);
    }
    
    public void copyBBCode(){
        this.generateBBCode();
        this.mainWindow.copyAll();
    }
    
    
   /// Text 
    public void generateText(){
        this.textPrewievBuffer = TextGenerator.generateText(this.army, true);
        this.textBuffer = TextGenerator.generateText(this.army, false);
        this.mainWindow.setDocument(this.textPrewievBuffer);
    }
    
    public void writeText(){
        this.generateText();
       JFileChooser chooser; 
       if(this.userPreferences.getProperty(FILE_DIR) != null){
           chooser = new JFileChooser(this.userPreferences.getProperty(FILE_DIR));
       }else{
           chooser = new JFileChooser();
       }
       chooser.setDialogTitle("Generate Text");
       chooser.setFileFilter(new FileFilter(){
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }

                String extension = f.getName().substring(f.getName().lastIndexOf(".") + 1);
                if (extension != null) {
                    if (extension.compareToIgnoreCase("txt") == 0 || extension.compareToIgnoreCase("text") == 0){
                            return true;
                    } else {
                        return false;
                    }
                }

                return false;
            }    
            
            public String getDescription() {
                return "Text";
            }            
       });
       
       chooser.setSelectedFile(new File(this.army.getName()+ "_"+ (int)this.army.getTargetPointcost() +  ".txt"));
       
       int returnVal = chooser.showDialog(null, "Write");
       File file = null;
       if(returnVal == JFileChooser.APPROVE_OPTION) {
           try{
               file = chooser.getSelectedFile();
               PrintWriter out = new PrintWriter(new FileOutputStream(file),true);
               out.print(this.textBuffer.toString());
               out.flush();
               this.lastHTML = file;
           }catch(Exception ex){
                logger.error("Exception while writing roster to disk", ex);
           }
       }
       if(file != null){
            this.userPreferences.setProperty(FILE_DIR, file.getAbsolutePath());
       }        
        
    }
    
    public void copyText(){
        this.generateText();
        this.mainWindow.copyAll();
    }
}
