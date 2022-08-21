package de.heinerion.invoice.view.swing.menu;

import de.heinerion.betriebe.repositories.AddressRepository;
import de.heinerion.betriebe.repositories.CompanyRepository;
import de.heinerion.betriebe.repositories.InvoiceRepository;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.print.PrintOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
@RequiredArgsConstructor
public class MenuFactory {
  private final AddressRepository addressRepository;
  private final InvoiceRepository invoiceRepository;
  private final CompanyRepository companyRepository;
  private final PrintOperations printOperations;
  private final PathUtilNG pathUtil;

  public JMenuBar createMenuBar(JFrame frame) {
    return new MenuBar(frame, pathUtil, addressRepository, invoiceRepository, companyRepository, printOperations);
  }
}
