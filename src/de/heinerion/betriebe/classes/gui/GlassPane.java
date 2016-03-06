package de.heinerion.betriebe.classes.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Ein Panel, das sich vor das aktive Fenster legt, es unnutzbar macht und
 * "ausgraut". <br>
 * Der Mauszeiger wird über diesem Panel zu einer Sanduhr bzw. zum
 * entsprechenden "Wait-Cursor" des Systems
 *
 * @author heiner
 */
public final class GlassPane extends JPanel {
  /**
   * Generierte UID
   */
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
    final Color bgColor = this.getBackground();

    // Hintergrund 60% Transparent
    g.setColor(new Color(bgColor.getRed(), bgColor.getBlue(), bgColor
        .getGreen(), ALPHA));

    // Komplett füllen
    g.fillRect(0, 0, this.getWidth(), this.getHeight());
  }
}
