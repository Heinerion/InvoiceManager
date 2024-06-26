package de.heinerion.invoice.view.swing.menu;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.print.PrintOperations;
import de.heinerion.invoice.repositories.*;
import de.heinerion.invoice.util.PathUtilNG;
import de.heinerion.invoice.view.swing.laf.LookAndFeelUtil;
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
  private final Session session;
  private final LookAndFeelUtil lookAndFeelUtil;

  public JMenuBar createMenuBar(JFrame frame) {
    return new MenuBar(frame, session, pathUtil, addressRepository, invoiceRepository, letterRepository, companyRepository, printOperations, lookAndFeelUtil);
  }
}
