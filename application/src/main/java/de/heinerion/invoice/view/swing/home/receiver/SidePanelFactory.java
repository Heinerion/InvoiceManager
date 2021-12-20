package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.storage.xml.jaxb.Migrator;
import de.heinerion.invoice.view.formatter.Formatter;
import de.heinerion.invoice.view.swing.home.Refreshable;

import javax.swing.*;

public class SidePanelFactory {
  private SidePanelFactory() {
  }

  public static JPanel createCalculatorSidePanel() {
    return new CalculatorSidePanel().getPanel();
  }

  public static Refreshable createAddressPanel(Formatter formatter) {
    return new AddressPanel(formatter);
  }

  public static Refreshable createCompanyChooserPanel(Migrator migrator) {
    return new CompanyChooserPanel(new CompanyCreateDialog(migrator));
  }
}
