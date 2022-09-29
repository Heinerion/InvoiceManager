package de.heinerion.invoice.view.swing.menu;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.print.PrintOperations;
import de.heinerion.invoice.repositories.address.AddressXmlRepository;
import de.heinerion.invoice.repositories.company.CompanyXmlRepository;
import de.heinerion.invoice.repositories.invoice.InvoiceXmlRepository;
import de.heinerion.invoice.repositories.letter.LetterXmlRepository;
import de.heinerion.invoice.util.PathUtilNG;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
@RequiredArgsConstructor
public class MenuFactory {
  private final AddressXmlRepository addressRepository;
  private final InvoiceXmlRepository invoiceRepository;
  private final LetterXmlRepository letterRepository;
  private final CompanyXmlRepository companyRepository;
  private final PrintOperations printOperations;
  private final PathUtilNG pathUtil;
  private final Session session = Session.getInstance();

  public JMenuBar createMenuBar(JFrame frame) {
    return new MenuBar(frame, session, pathUtil, addressRepository, invoiceRepository, letterRepository, companyRepository, printOperations);
  }
}
