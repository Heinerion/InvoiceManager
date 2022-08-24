package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.repositories.address.AddressRepository;
import de.heinerion.invoice.repositories.company.CompanyRepository;
import de.heinerion.invoice.view.formatter.Formatter;
import de.heinerion.invoice.view.swing.home.Refreshable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
@RequiredArgsConstructor
public class SidePanelFactory {
  private final AddressRepository addressRepository;
  private final CompanyRepository companyRepository;
  private final Session session = Session.getInstance();

  public JPanel createCalculatorSidePanel() {
    return new CalculatorSidePanel(session).getPanel();
  }

  public Refreshable createAddressPanel(Formatter formatter) {
    return new AddressPanel(formatter, addressRepository, session);
  }

  public Refreshable createCompanyChooserPanel() {
    return new CompanyChooserPanel(new CompanyCreateDialog(session, companyRepository), session);
  }
}
