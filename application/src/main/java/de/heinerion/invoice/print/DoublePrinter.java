package de.heinerion.invoice.print;

import de.heinerion.betriebe.models.Letter;
import de.heinerion.invoice.print.pdf.latex.LatexPrinter;
import de.heinerion.invoice.print.xml.XmlPrinter;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * uses two printers
 */
@Service("Double")
@Primary
public class DoublePrinter implements Printer {
  private final Printer masterPrinter;
  private final Printer backupPrinter;

  DoublePrinter(LatexPrinter masterPrinter, XmlPrinter backupPrinter) {
    this.masterPrinter = masterPrinter;
    this.backupPrinter = backupPrinter;
  }

  @Override
  public void writeFile(Letter letter, File parentFolder, String title) {
    masterPrinter.writeFile(letter, parentFolder, title);
    backupPrinter.writeFile(letter, parentFolder, title);
  }
}
