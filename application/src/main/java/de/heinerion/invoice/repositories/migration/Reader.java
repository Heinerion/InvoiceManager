package de.heinerion.invoice.repositories.migration;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Reader {
  private static final String PROPERTY_DIVIDER = ":";

  private BufferedReader reader = null;
  private boolean isOpen = false;

  void closeFile() throws IOException {
    if (this.isOpen) {
      this.reader.close();
    }
  }

  void prepareFile(Path path) throws IOException {
    this.reader = Files.newBufferedReader(path);
    this.isOpen = true;
  }

  public Map<String, String> read() throws IOException {
    Map<String, String> ret;

    final String line = this.reader.readLine();
    if (line != null) {
      final String[] token = line.split(PROPERTY_DIVIDER, 2);
      ret = new HashMap<>();
      ret.put(token[0].trim(), token[1].trim());
    } else {
      ret = null;
    }

    return ret;
  }
}
