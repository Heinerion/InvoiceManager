package de.heinerion.betriebe.listener;

import de.heinerion.betriebe.classes.file_operations.MainOperations;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class DruckAction implements ActionListener {
  private static final Logger logger = LogManager.getLogger(DruckAction.class);

  @Override
  public void actionPerformed(ActionEvent arg0) {
    final Conveyable letter = Session.getActiveConveyable();

    if (letter != null) {
      createLetter(letter);
    }
  }

  /**
   * @param letter
   */
  private void createLetter(final Conveyable letter) {
    if (letter instanceof Invoice) {
      final Company company = letter.getCompany();

      company.increaseInvoiceNumber();
      if (logger.isDebugEnabled()) {
        logger.debug("Rechnungsnummer von {} auf {} erhöht.",
            company.getDescriptiveName(), company.getInvoiceNumber());
      }
    }

    if (letter.isPrintable()) {
      MainOperations.createDocument(letter);
    }
  }
}