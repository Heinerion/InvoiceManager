package de.heinerion.invoice.view.swing;

import javax.swing.*;

public class PanelFactory {
  private PanelFactory() {
  }

  public static JPanel createBackgroundPanel(PanelSides... coloredSides) {
    return BGPanel.createBackgroundPanel(coloredSides);
  }
}
