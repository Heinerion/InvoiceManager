package de.heinerion.invoice.print;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;
import lombok.extern.flogger.Flogger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Flogger
public class PrintAction implements ActionListener {
  private Letter letter;

  private PrintOperations printOperations;

  PrintAction(PrintOperations printOperations) {
    this.printOperations = printOperations;
  }

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
      log.atFine().log("Rechnungsnummer von %s auf %d erhöht.",
          company.getDescriptiveName(), company.getInvoiceNumber());
    }

    if (letter.isPrintable()) {
      printOperations.createDocument(letter);
    }
  }
}