/*
 * VerticalFlowLayout.java
 *
 * Created on 10 March 2003, 22:46
 */

package org.jArmyTool.gui.layout;

import java.awt.*;

/**
 * 
 */
public class VerticalFlowLayout implements LayoutManager {
  private int vgap = 0;

  /**
   * VerticalFlowLayout constructor comment.
   */
  public VerticalFlowLayout() {
    this(0);
  }

  /**
   * VerticalFlowLayout constructor comment.
   */
  public VerticalFlowLayout(int vgap) {
    if (vgap < 0) {
      this.vgap = 0;
    } else {
      this.vgap = vgap;
    } 
  }

  /**
   * addLayoutComponent method comment.
   */
  public void addLayoutComponent(String name, Component comp) {
  } 

  /**
   * layoutContainer method comment.
   */
  public void layoutContainer(Container parent) {
    Insets insets = parent.getInsets();
    int w = parent.getSize().width - insets.left - insets.right;
    // int h = parent.size().height - insets.top - insets.bottom;
    int numComponents = parent.getComponentCount();
    if (numComponents == 0) {
      return;
    } 
    int y = insets.top;
    int x = insets.left;
    for (int i = 0; i < numComponents; ++i) {
      Component c = parent.getComponent(i);
      if (c.isVisible()) {
        Dimension d = c.getPreferredSize();
        c.setBounds(x, y, w, d.height);
        y += d.height + vgap;
      } 
    } 
  } 

  /**
   * minimumLayoutSize method comment.
   */
  public Dimension minimumLayoutSize(Container parent) {
    Insets insets = parent.getInsets();
    int maxWidth = 0;
    int totalHeight = 0;
    int numComponents = parent.getComponentCount();
    for (int i = 0; i < numComponents; ++i) {
      Component c = parent.getComponent(i);
      if (c.isVisible()) {
        Dimension cd = c.getMinimumSize();
        maxWidth = Math.max(maxWidth, cd.width);
        totalHeight += cd.height;
      } 
    } 
    Dimension td = new Dimension(maxWidth + insets.left + insets.right, 
                                 totalHeight + insets.top + insets.bottom 
                                 + vgap * numComponents);
    return td;
  } 

  /**
   * preferredLayoutSize method comment.
   */
  public Dimension preferredLayoutSize(Container parent) {
    Insets insets = parent.getInsets();
    int maxWidth = 0;
    int totalHeight = 0;
    int numComponents = parent.getComponentCount();
    for (int i = 0; i < numComponents; ++i) {
      Component c = parent.getComponent(i);
      if (c.isVisible()) {
        Dimension cd = c.getPreferredSize();
        maxWidth = Math.max(maxWidth, cd.width);
        totalHeight += cd.height;
      } 
    } 
    Dimension td = new Dimension(maxWidth + insets.left + insets.right, 
                                 totalHeight + insets.top + insets.bottom 
                                 + vgap * numComponents);
    return td;
  } 

  /**
   * removeLayoutComponent method comment.
   */
  public void removeLayoutComponent(Component comp) {
  } 

}
