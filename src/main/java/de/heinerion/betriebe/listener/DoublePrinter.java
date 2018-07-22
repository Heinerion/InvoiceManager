package de.heinerion.betriebe.listener;

import de.heinerion.betriebe.models.Letter;

import java.io.File;

/**
 * uses two printers
 */
public class DoublePrinter implements Printer {
  private final Printer masterPrinter;
  private final Printer backupPrinter;

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
