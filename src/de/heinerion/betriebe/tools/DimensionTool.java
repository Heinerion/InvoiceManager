package de.heinerion.betriebe.tools;

import java.awt.Dimension;

public final class DimensionTool {

  public static final int ADDRESSFIELD_W = 230;
  public static final int ADDRESSFIELD_H = 60;

  public static final int DEAFULT_HEIGHT = 600;
  public static final int DEAFULT_WIDTH = 800;

  public static final Dimension FRAME = new Dimension(DEAFULT_WIDTH,
      DEAFULT_HEIGHT);
  public static final Dimension PROGRESS_BAR = new Dimension(400, 30);
  public static final Dimension CALCULATOR = new Dimension(200, 150);
  public static final Dimension ADDRESS_AREA = new Dimension(ADDRESSFIELD_W,
      ADDRESSFIELD_H);
  public static final Dimension MENU = new Dimension(DEAFULT_WIDTH,
      DEAFULT_HEIGHT);

  private DimensionTool() {
  }
}
