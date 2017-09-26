package de.heinerion.betriebe.view.panels;

import javax.swing.*;

public class PanelFactory {
  private PanelFactory() {
  }

  public static JPanel createBackgroundPanel(PanelSides... coloredSides) {
    return BGPanel.createBackgroundPanel(coloredSides);
  }

  public static ProgressIndicator getProgressIndicator(JProgressBar progressBar) {
    return new ProgressIndicatorImpl(progressBar);
  }
}
