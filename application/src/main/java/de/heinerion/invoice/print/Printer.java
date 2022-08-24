package de.heinerion.invoice.print;

import de.heinerion.invoice.models.Letter;

import java.io.File;

public interface Printer {
  void writeFile(Letter letter, File parentFolder, String title);
}
