package de.heinerion.betriebe.view.swing.home.receiver;

import de.heinerion.betriebe.view.formatter.Formatter;
import de.heinerion.betriebe.view.swing.home.Refreshable;

import javax.swing.*;

public class SidePanelFactory {
  private SidePanelFactory() {
  }

  static JPanel createCalculatorSidePanel() {
    return new CalculatorSidePanel().getPanel();
  }

  static Refreshable createAddressPanel(Formatter formatter) {
    return new AddressPanel(formatter);
  }

  static Refreshable createCompanyChooserPanel() {
    return new CompanyChooserPanel();
  }
}
