package de.heinerion.betriebe.listener;

import de.heinerion.betriebe.models.Letter;

import java.io.File;

public interface Printer {
  void writeFile(Letter letter, File parentFolder, String title);
}
