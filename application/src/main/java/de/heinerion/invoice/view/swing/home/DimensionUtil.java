package de.heinerion.invoice.view.swing.home;

import java.awt.*;

public final class DimensionUtil {

  private static final int ADDRESS_FIELD_W = 230;
  private static final int ADDRESS_FIELD_H = 60;

  private static final int DEFAULT_HEIGHT = 600;
  private static final int DEFAULT_WIDTH = 800;

  public static final Dimension FRAME = new Dimension(DEFAULT_WIDTH,
      DEFAULT_HEIGHT);
  public static final Dimension PROGRESS_BAR = new Dimension(400, 30);
  public static final Dimension CALCULATOR = new Dimension(200, 150);
  public static final Dimension ADDRESS_AREA = new Dimension(ADDRESS_FIELD_W,
      ADDRESS_FIELD_H);
  public static final Dimension MENU = new Dimension(DEFAULT_WIDTH,
      DEFAULT_HEIGHT);

  private DimensionUtil() {
  }
}
