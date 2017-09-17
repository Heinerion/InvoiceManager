package de.heinerion.betriebe.gui.panels;

import javax.swing.*;

public class SidePanelFactory {
  private SidePanelFactory() {
  }

  static JPanel createPrintButtonPanel() {
    return new PrintButtonPanel();
  }

  static JPanel createCalculatorSidePanel() {
    return new CalculatorSidePanel();
  }

  static CalculatorPanel createCalculatorPanel() {
    return new CalculatorPanel();
  }

  static Refreshable createAddressPanel() {
    return new AddressPanel();
  }

  static Refreshable createCompanyChooserPanel() {
    return new CompanyChooserPanel();
  }
}
