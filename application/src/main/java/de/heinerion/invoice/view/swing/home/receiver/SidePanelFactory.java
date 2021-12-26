package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.repositories.AddressRepository;
import de.heinerion.invoice.storage.loading.TextFileLoader;
import de.heinerion.invoice.storage.xml.jaxb.Migrator;
import de.heinerion.invoice.view.formatter.Formatter;
import de.heinerion.invoice.view.swing.home.Refreshable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
@RequiredArgsConstructor
public class SidePanelFactory {
  private final DataBase dataBase;
  private final Migrator migrator;
  private final TextFileLoader fileLoader;
  private final AddressRepository addressRepository;

  public JPanel createCalculatorSidePanel() {
    return new CalculatorSidePanel().getPanel();
  }

  public Refreshable createAddressPanel(Formatter formatter) {
    return new AddressPanel(formatter, addressRepository);
  }

  public Refreshable createCompanyChooserPanel() {
    return new CompanyChooserPanel(new CompanyCreateDialog(migrator, dataBase, fileLoader));
  }
}
