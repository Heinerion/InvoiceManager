package de.heinerion.betriebe.view.swing;

import de.heinerion.betriebe.view.common.StatusComponent;

import javax.swing.*;

public class PanelFactory {
  private PanelFactory() {
  }

  public static JPanel createBackgroundPanel(PanelSides... coloredSides) {
    return BGPanel.createBackgroundPanel(coloredSides);
  }

  public static StatusComponent<JPanel> createStatusComponent() {
    return new StatusComponentSwingImpl();
  }
}
