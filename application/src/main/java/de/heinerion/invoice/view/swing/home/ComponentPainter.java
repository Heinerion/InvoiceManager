package de.heinerion.invoice.view.swing.home;

import lombok.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public final class ComponentPainter {
  private ComponentPainter() {
    // avoid instantiation
  }

  public static void paintComponent(Graphics2D g, JComponent component, Details details) {
    Graphics2D g2 = initGraphic(g);

    g2.setPaint(Color.gray);

    Color decor = component.getBackground().darker();
    Color base = component.getBackground();

    double componentHeight = component.getHeight();
    double componentWidth = component.getWidth();

    int decorHeight = details.getDecorHeight();
    int decorWidth = details.getDecorWidth();

    int x = details.getOffsetX();
    int y = details.getOffsetY();

    GradientPaint baseToDecorGradient = new GradientPaint(x, y, decor, decorWidth,
        decorHeight, base);
    g2.setPaint(baseToDecorGradient);
    g2.fill(new Rectangle2D.Double(x, y, componentWidth - x, componentHeight - y));
  }

  private static Graphics2D initGraphic(Graphics2D g) {
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    return g;
  }

  @Builder
  @Getter
  public static class Details {
    @Builder.Default
    private int decorWidth = 0;
    @Builder.Default
    private int decorHeight = 0;
    @Builder.Default
    private int offsetX = 0;
    @Builder.Default
    private int offsetY = 0;
  }
}
