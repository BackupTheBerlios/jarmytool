/*
 * XMLWriter.java
 *
 * Created on 21 March 2003, 00:26
 */

package core_src.src.org.jArmyTool.outputGenerator;

import org.w3c.dom.*;
import java.io.*;
/**
 *
 * @author  pasi
 */
public class XMLWriter {
 
    private PrintWriter out = null;
    
    private String doctypeString = "";
    //private static PrintStream out = System.out;
	/**
	*Writes the XML document to disk.
	*Call this method when exiting the program.
	*/
    
    public void setDoctypeString(String typeString){
        this.doctypeString = typeString;
    }
    
    public void writeToDisk(Node doc, File file){
            try{
                    out = new PrintWriter(new FileOutputStream(file),true);
                    writeNode(doc);
            }catch(Exception e){
                    System.out.println("file printer init failed");
            }
    }


  private void writeNode(Node node) {
    if (node == null) {
      return;
    }
    int type = node.getNodeType();

    switch (type) {

      case Node.DOCUMENT_NODE: {

        out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");  
          
        //out.println("<?xml version=\"1.0\"?>");
        out.println(this.doctypeString);
        writeNode( ((Document)node).getDocumentElement() );
        out.flush();
        break;
      }
      case Node.ELEMENT_NODE: {
        out.print('<');
        out.print(node.getNodeName());

        Attr attrs[] = getAttrArray(node.getAttributes());

        for (int i = 0; i < attrs.length; i++) {
          Attr attr = attrs[i];
          out.print(' ');
          out.print(attr.getNodeName());
          out.print("=\"");
          out.print(strToXML(attr.getNodeValue()));
          out.print('"');
        }
        out.println('>');
        NodeList children = node.getChildNodes();
        if (children != null) {
          int len = children.getLength();
          for (int i = 0; i < len; i++) {
          writeNode(children.item(i));
          }
        }
        break;
      }
      case Node.ENTITY_REFERENCE_NODE: {
        out.print('&');
        out.print(node.getNodeName());
        out.println(';');
        break;
      }
      case Node.CDATA_SECTION_NODE:
      case Node.TEXT_NODE: {
        out.println(strToXML(node.getNodeValue()));
        break;
      }

      case Node.PROCESSING_INSTRUCTION_NODE: {
        out.print("<?");
        out.print(node.getNodeName());
        String data = node.getNodeValue();
        if (data != null && data.length() > 0) {
          out.print(' ');
          out.print(data);
        }
        out.println("?>");
        break;
      }
    }

    if (type == Node.ELEMENT_NODE) {
      out.print("</");
      out.print(node.getNodeName());
      out.println('>');
    }
  }

  private String strToXML(String s) {
    //return java.net.URLEncoder.encode(s);
    StringBuffer str = new StringBuffer();
    int len = (s != null) ? s.length() : 0;

    for (int i = 0; i < len; i++) {
      char ch = s.charAt(i);
      switch (ch) {
        case '<': {
          str.append("&lt;");
          break;
        }
        case '>': {
          str.append("&gt;");
          break;
        }
        case '&': {
          str.append("&amp;");
          break;
        }
        case '"': {
          str.append("&quot;");
          break;
        }
        
  /*      case 'Ä': {
          str.append("&Auml;");
          break;
        }
        case 'ä': {
          str.append("&auml;");
          break;
        }
        case 'Ö': {
          str.append("&Ouml;");
          break;
        }
        case 'ö': {
          str.append("&ouml;");
          break;
        }
        case 'Ü': {
          str.append("&Uuml;");
          break;
        }
        case 'ü': {
          str.append("&uuml;");
          break;
        }
        case 'å': {
          str.append("&aring;");
          break;
        }
        case 'Å': {
          str.append("&Aring;");
          break;
        }*/
        
        default: {
          str.append(ch);
        }
      }
    }

    return str.toString();
  }
  
   private Attr[] getAttrArray(NamedNodeMap attrs) {
        int len = (attrs != null) ? attrs.getLength() : 0;
        Attr array[] = new Attr[len];
        for (int i = 0; i < len; i++) {
          array[i] = (Attr)attrs.item(i);
        }
        return array;
  }
    
}
