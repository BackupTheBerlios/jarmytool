/*
 * ImagePanel.java
 *
 * Created on 10 July 2003, 23:15
 */

package core_src.src.org.jArmyTool.gui.util;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class ImagePanel extends JPanel{
	private Image image;

	public ImagePanel(Image image){
		this.image = image;
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(image,0);
		try{ tracker.waitForID(0); }
		catch(Exception e){}
	}
        

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(this.image, 0, 0, null);
	}


}	