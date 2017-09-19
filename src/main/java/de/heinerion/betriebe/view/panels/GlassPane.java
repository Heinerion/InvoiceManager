package de.heinerion.betriebe.view.panels;

import javax.swing.*;
import java.awt.*;

/**
 * A Panel which covers the active window, decorates it with a gray overlay and effectively disables it.
 * <br>
 * The mouse pointer turns into an hourglass or the systems wait cursor respectively.
 *
 * @author heiner
 */
class GlassPane extends JPanel {
  private static final long serialVersionUID = -7600133123674600410L;
  private static final int ALPHA = 150;

  /**
   * Creates a new GlassPane with wait cursor, invisible for now
   */
  GlassPane() {
    setLayout(new BorderLayout());
    setVisible(false);
    setOpaque(false);

    // "hour glass"
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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
