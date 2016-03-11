package de.heinerion.betriebe.classes.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Ein Panel, das sich vor das aktive Fenster legt, es unnutzbar macht und
 * "ausgraut". <br>
 * Der Mauszeiger wird über diesem Panel zu einer Sanduhr bzw. zum
 * entsprechenden "Wait-Cursor" des Systems
 *
 * @author heiner
 */
public final class GlassPane extends JPanel {
  private static final long serialVersionUID = -7600133123674600410L;
  private static final int ALPHA = 150;

  /**
   * Erzeugt ein neues GlassPane mit WaitCursor, zunächst unsichtbar
   */
  public GlassPane() {
    this.setLayout(new BorderLayout());
    this.setVisible(false);
    this.setOpaque(false);

    // "Sanduhr"
    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
  }

  @Override
  protected void paintComponent(Graphics g) {
    Color bgColor = getBackground();

    g.setColor(getTransparentColor(bgColor));
    g.fillRect(0, 0, getWidth(), getHeight());
  }

  private Color getTransparentColor(Color bgColor) {
    int red = bgColor.getRed();
    int blue = bgColor.getBlue();
    int green = bgColor.getGreen();
    return new Color(red, blue, green, ALPHA);
  }
}
