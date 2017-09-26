package de.heinerion.betriebe.view.panels;

import de.heinerion.betriebe.view.formatter.Formatter;

import javax.swing.*;

public class SidePanelFactory {
  private SidePanelFactory() {
  }

  static JPanel createCalculatorSidePanel() {
    return new CalculatorSidePanel();
  }

  static CalculatorPanel createCalculatorPanel() {
    return new CalculatorPanel();
  }

  static Refreshable createAddressPanel(Formatter formatter) {
    return new AddressPanel(formatter);
  }

  static Refreshable createCompanyChooserPanel() {
    return new CompanyChooserPanel();
  }
}
