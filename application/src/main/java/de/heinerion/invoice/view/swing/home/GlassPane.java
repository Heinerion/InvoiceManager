package de.heinerion.invoice.view.swing.home;

import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;

/**
 * A Panel which covers the active window, decorates it with a gray overlay and effectively disables it.
 * <br>
 * The mouse pointer turns into an hourglass or the systems wait cursor respectively.
 *
 * @author heiner
 */
@Service
class GlassPane {
  private static final int ALPHA = 150;
  private final JPanel basePanel;

  /**
   * Creates a new GlassPane with wait cursor, invisible for now
   */
  GlassPane() {
    basePanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        Color bgColor = getBackground();

        g.setColor(getTransparentColor(bgColor));
        g.fillRect(0, 0, getWidth(), getHeight());
      }
    };

    basePanel.setLayout(new BorderLayout());
    basePanel.setVisible(false);
    basePanel.setOpaque(false);

    // "hour glass"
    basePanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
  }

  private Color getTransparentColor(Color bgColor) {
    int red = bgColor.getRed();
    int blue = bgColor.getBlue();
    int green = bgColor.getGreen();
    return new Color(red, blue, green, ALPHA);
  }

  JComponent getComponent() {
    return basePanel;
  }
}
