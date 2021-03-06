package de.heinerion.invoice.storage.loading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

  void prepareFile(String path) throws IOException {
    final File file = new File(path);

    this.reader = new BufferedReader(new FileReader(file));
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
