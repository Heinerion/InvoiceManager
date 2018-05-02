package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.betriebe.listener.Printer;
import de.heinerion.betriebe.models.Letter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * uses two printers
 */
public class DoublePrinter implements Printer {
  private final Printer masterPrinter;
  private final Printer backupPrinter;

  @Autowired
  DoublePrinter(Printer masterPrinter, Printer backupPrinter) {
    this.masterPrinter = masterPrinter;
    this.backupPrinter = backupPrinter;
  }

  @Override
  public void writeFile(Letter letter, File parentFolder, String title) {
    masterPrinter.writeFile(letter, parentFolder, title);
    backupPrinter.writeFile(letter, parentFolder, title);
  }
}
