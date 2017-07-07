package de.heinerion.betriebe.listener;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.fileoperations.MainOperations;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class PrintAction implements ActionListener {
  private static final Logger logger = LogManager.getLogger(PrintAction.class);
  private Conveyable letter;

  private MainOperations mainOperations = new MainOperations();

  @Override
  public void actionPerformed(ActionEvent arg0) {
    letter = Session.getActiveConveyable();

    if (letter != null) {
      createLetter();
    }
  }

  private void createLetter() {
    if (letter instanceof Invoice) {
      Company company = letter.getCompany();

      company.increaseInvoiceNumber();
      if (logger.isDebugEnabled()) {
        logger.debug("Rechnungsnummer von {} auf {} erhöht.",
            company.getDescriptiveName(), company.getInvoiceNumber());
      }
    }

    if (letter.isPrintable()) {
      mainOperations.createDocument(letter);
    }
  }
}