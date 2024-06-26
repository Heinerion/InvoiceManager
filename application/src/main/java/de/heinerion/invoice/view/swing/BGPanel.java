package de.heinerion.invoice.view.swing;

import de.heinerion.invoice.view.swing.laf.LookAndFeelUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serial;
import java.util.*;

import static de.heinerion.invoice.view.swing.PanelSides.*;

public class BGPanel extends JPanel {
  private static final int CORNER_SIZE = 15;

  @Serial
  private static final long serialVersionUID = 1L;

  private final LookAndFeelUtil lookAndFeelUtil;

  private boolean drawTop;
  private boolean drawBottom;
  private boolean drawRight;
  private boolean drawLeft;

  private int widthTop;
  private int widthLeft;
  private int widthBottom;
  private int widthRight;

  private Color borderColor;
  private Color backgroundColor;
  private Color[] colors;
  private static final float[] fractions = new float[]{0f, 1f};

  private transient RadialGradientPaint topLeftCorner;
  private transient RadialGradientPaint topRightCorner;
  private transient RadialGradientPaint bottomLeftCorner;
  private transient RadialGradientPaint bottomRightCorner;

  private transient GradientPaint backgroundPaint;
  private transient GradientPaint leftPaint;
  private transient GradientPaint rightPaint;
  private transient GradientPaint topPaint;
  private transient GradientPaint bottomPaint;

  private BGPanel(LookAndFeelUtil lookAndFeelUtil, Collection<PanelSides> sides) {
    this.lookAndFeelUtil = lookAndFeelUtil;
    determineSidesToDraw(sides);

    Border panelBorder = createPanelBorder();
    setBorder(panelBorder);
  }

  public static BGPanel createWithColoredSides(LookAndFeelUtil lookAndFeelUtil, PanelSides sideA, PanelSides sideB) {
    return new BGPanel(lookAndFeelUtil, Arrays.asList(sideA, sideB));
  }

  public static BGPanel createWithAllSidesColored(LookAndFeelUtil lookAndFeelUtil) {
    return new BGPanel(lookAndFeelUtil, Arrays.asList(TOP, RIGHT, BOTTOM, LEFT));
  }

  private void determineSidesToDraw(Collection<PanelSides> panelSides) {
    drawTop = panelSides.contains(TOP);
    drawBottom = panelSides.contains(BOTTOM);
    drawLeft = panelSides.contains(LEFT);
    drawRight = panelSides.contains(RIGHT);
  }

  private Border createPanelBorder() {
    widthTop = getBorderWidth(drawTop);
    widthLeft = getBorderWidth(drawLeft);
    widthBottom = getBorderWidth(drawBottom);
    widthRight = getBorderWidth(drawRight);

    return BorderFactory.createEmptyBorder(widthTop, widthLeft, widthBottom, widthRight);
  }

  private int getBorderWidth(boolean draw) {
    int borderWidth = 0;

    if (draw) {
      borderWidth = CORNER_SIZE;
    }

    return borderWidth;
  }

  private void determineColorSettings() {
    borderColor = (new JPanel()).getBackground();
    backgroundColor = lookAndFeelUtil.adjustColorByTheme(borderColor);
    colors = new Color[]{backgroundColor, borderColor};
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D drawer = (Graphics2D) g;
    setupRenderingHints(drawer);

    // Panel:
    //
    // []|......|[]
    // --+------+--
    // ..|......|..
    // ..|......|..
    // --+------+--
    // []|......|[]
    //
    //  []  -> drawCorners
    // |..| -> drawAreas
    //
    // horizontal Areas fade to the left and right side
    // vertical Areas fade to the top and bottom side
    // Corners fade diagonally

    PositionCoordinates position = determineCoordinates();

    determineColorSettings();
    initColors(position);
    initPaint(position);

    drawCorners(drawer, position);
    drawAreas(drawer, position);
  }

  private void setupRenderingHints(Graphics2D g2) {
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  }

  private PositionCoordinates determineCoordinates() {
    return PositionCoordinates.builder()
        .withHeight(getHeight() - CORNER_SIZE)
        .withWidth(getWidth() - CORNER_SIZE)
        .build();
  }

  private void initColors(PositionCoordinates position) {
    int leftX = CORNER_SIZE;
    int rightX = position.getWidth();
    int topY = CORNER_SIZE;
    int bottomY = position.getHeight();

    topLeftCorner = getRadialGradientPaint(leftX, topY);
    topRightCorner = getRadialGradientPaint(rightX, topY);
    bottomLeftCorner = getRadialGradientPaint(leftX, bottomY);
    bottomRightCorner = getRadialGradientPaint(rightX, bottomY);
  }

  private RadialGradientPaint getRadialGradientPaint(int posX, int posY) {
    return new RadialGradientPaint(posX, posY, CORNER_SIZE, fractions, colors);
  }

  private void initPaint(PositionCoordinates position) {
    int width = position.getWidth();
    int height = position.getHeight();
    int x = position.getPosX();
    int y = position.getPosY();

    backgroundPaint = new GradientPaint(x, y, backgroundColor, CORNER_SIZE, y, backgroundColor);
    leftPaint = new GradientPaint(x, y, borderColor, CORNER_SIZE, y, backgroundColor);
    rightPaint = new GradientPaint(width, y, backgroundColor, getWidth(), y, borderColor);
    topPaint = new GradientPaint(x, y, borderColor, x, CORNER_SIZE, backgroundColor);
    bottomPaint = new GradientPaint(x, height, backgroundColor, x, getHeight(), borderColor);
  }

  private void drawCorners(Graphics2D g2, PositionCoordinates position) {
    int topY = position.getPosY();
    int bottomY = position.getHeight();

    int leftX = position.getPosX();
    int rightX = position.getWidth();

    if (drawTop && drawLeft) {
      drawCorner(g2, topLeftCorner, leftX, topY);
    }

    if (drawTop && drawRight) {
      drawCorner(g2, topRightCorner, rightX, topY);
    }

    if (drawBottom && drawLeft) {
      drawCorner(g2, bottomLeftCorner, leftX, bottomY);
    }

    if (drawBottom && drawRight) {
      drawCorner(g2, bottomRightCorner, rightX, bottomY);
    }
  }

  private void drawCorner(Graphics2D g2, Paint paint, int startX, int startY) {
    g2.setPaint(paint);
    g2.fillRect(startX, startY, CORNER_SIZE, CORNER_SIZE);
  }

  private void drawAreas(Graphics2D g2, PositionCoordinates position) {
    int x = position.getPosX();
    int y = position.getPosY();

    int halfWidth = getWidth() / 2;
    int halfHeight = getHeight() / 2;

    g2.setPaint(getPaint(drawLeft, leftPaint));
    g2.fill(createHorizontalArea(x, halfWidth));

    g2.setPaint(getPaint(drawRight, rightPaint));
    g2.fill(createHorizontalArea(halfWidth, getWidth()));

    g2.setPaint(getPaint(drawTop, topPaint));
    g2.fill(createVerticalArea(y, halfHeight));

    g2.setPaint(getPaint(drawBottom, bottomPaint));
    g2.fill(createVerticalArea(halfHeight, getHeight()));
  }

  private GradientPaint getPaint(boolean draw, GradientPaint paint) {
    GradientPaint resultPaint = backgroundPaint;

    if (draw) {
      resultPaint = paint;
    }

    return resultPaint;
  }

  private Rectangle2D.Double createHorizontalArea(int xStart, int xEnd) {
    return new Rectangle2D.Double(xStart, widthTop, xEnd, getEffectiveHeight());
  }

  private Rectangle2D.Double createVerticalArea(int yStart, int yEnd) {
    return new Rectangle2D.Double(widthLeft, yStart, getEffectiveWidth(), yEnd);
  }

  private int getEffectiveHeight() {
    return getHeight() - widthBottom - widthTop;
  }

  private int getEffectiveWidth() {
    return getWidth() - widthRight - widthLeft;
  }
}
