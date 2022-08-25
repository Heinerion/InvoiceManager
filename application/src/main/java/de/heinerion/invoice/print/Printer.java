package de.heinerion.invoice.print;

import de.heinerion.invoice.models.Letter;

import java.nio.file.Path;

public interface Printer {
  void writeFile(Letter letter, Path parentFolder, String title);
}
