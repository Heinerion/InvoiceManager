package de.heinerion.invoice.print;

import de.heinerion.betriebe.models.Letter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * uses two printers
 */
@Service("Double")
public class DoublePrinter implements Printer {
  private final Printer masterPrinter;
  private final Printer backupPrinter;

  DoublePrinter(@Qualifier("Latex") Printer masterPrinter, @Qualifier("XML") Printer backupPrinter) {
    this.masterPrinter = masterPrinter;
    this.backupPrinter = backupPrinter;
  }

  @Override
  public void writeFile(Letter letter, File parentFolder, String title) {
    masterPrinter.writeFile(letter, parentFolder, title);
    backupPrinter.writeFile(letter, parentFolder, title);
  }
}
