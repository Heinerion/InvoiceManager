package de.heinerion.betriebe.gui.panels;

import javax.swing.*;

public class SidePanelFactory {
  private SidePanelFactory() {
  }

  public static JPanel createPrintButtonPanel() {
    return new PrintButtonPanel();
  }

  public static JPanel createCalculatorPanel() {
    return new CalculatorPanel();
  }
}
