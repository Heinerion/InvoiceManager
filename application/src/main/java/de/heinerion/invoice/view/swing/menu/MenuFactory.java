package de.heinerion.invoice.view.swing.menu;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.repositories.address.AddressRepository;
import de.heinerion.betriebe.repositories.company.CompanyRepository;
import de.heinerion.betriebe.repositories.invoice.InvoiceRepository;
import de.heinerion.betriebe.repositories.letter.LetterRepository;
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
  private final LetterRepository letterRepository;
  private final CompanyRepository companyRepository;
  private final PrintOperations printOperations;
  private final PathUtilNG pathUtil;
  private final Session session = Session.getInstance();

  public JMenuBar createMenuBar(JFrame frame) {
    return new MenuBar(frame, session, pathUtil, addressRepository, invoiceRepository, letterRepository, companyRepository, printOperations);
  }
}
