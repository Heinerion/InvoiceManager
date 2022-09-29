package de.heinerion.invoice.print;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.*;
import de.heinerion.invoice.repositories.company.CompanyXmlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.awt.event.*;

@Flogger
@Service
@RequiredArgsConstructor
public class PrintAction implements ActionListener {
  private final CompanyXmlRepository companyRepository;
  private final PrintOperations printOperations;
  private final Session session = Session.getInstance();

  private Conveyable letter;

  @Override
  public void actionPerformed(ActionEvent arg0) {
    letter = session.getActiveConveyable();

    if (letter != null) {
      createLetter();
    }
  }

  private void createLetter() {
    if (letter instanceof Invoice) {
      Company company = letter.getCompany();

      company.increaseInvoiceNumber();
      session.notifyCompany();
      log.atFine().log("raise invoice number of %s to %d.",
          company.getDescriptiveName(), company.getInvoiceNumber());
      companyRepository.save(company);
    }

    if (letter.isPrintable()) {
      printOperations.createDocument(letter);
    }
  }
}