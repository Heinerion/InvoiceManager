package de.heinerion.invoice.view.swing;

import de.heinerion.invoice.view.common.StatusComponent;

import javax.swing.*;

public class PanelFactory {
  private PanelFactory() {
  }

  public static JPanel createBackgroundPanel(PanelSides... coloredSides) {
    return BGPanel.createBackgroundPanel(coloredSides);
  }

  public static StatusComponent createStatusComponent() {
    return new StatusComponent();
  }
}
