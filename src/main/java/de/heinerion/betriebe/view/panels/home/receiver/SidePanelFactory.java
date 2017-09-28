package de.heinerion.betriebe.view.panels.home.receiver;

import de.heinerion.betriebe.view.formatter.Formatter;
import de.heinerion.betriebe.view.panels.home.Refreshable;

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
