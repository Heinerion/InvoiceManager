package de.heinerion.invoice.view.swing.home;

import javax.swing.*;
import java.awt.*;

public enum ComponentSize {
  COMPANY_PANEL(230, 40),
  CALCULATOR(200, 150),
  ADDRESS_AREA(230, 60);

  private final Dimension dimension;

  ComponentSize(int width, int height) {
    this.dimension = new Dimension(width, height);
  }

  public void applyTo(JComponent component) {
    component.setPreferredSize(dimension);
    component.setMinimumSize(dimension);
    component.setMaximumSize(dimension);
  }
}
