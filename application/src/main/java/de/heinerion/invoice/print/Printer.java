package de.heinerion.invoice.print;

import de.heinerion.invoice.models.Conveyable;

import java.nio.file.Path;

public interface Printer {
  void writeFile(Conveyable conveyable, Path parentFolder, String title);
}
