package de.heinerion.betriebe.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class TextFileReader implements Reader {
  private static final String PROPERTY_DEVIDER = ":";

  private java.io.BufferedReader reader = null;
  private boolean isOpen = false;

  public TextFileReader() {
  }

  @Override
  public void closeFile() throws IOException {
    if (this.isOpen) {
      this.reader.close();
    }
  }

  @Override
  public void prepareFile(String path) throws IOException {
    final File file = new File(path);

    this.reader = new BufferedReader(new FileReader(file));
    this.isOpen = true;
  }

  @Override
  public Map<String, String> read() throws IOException {
    Map<String, String> ret;

    final String line = this.reader.readLine();
    if (line != null) {
      final String[] token = line.split(PROPERTY_DEVIDER, 2);
      ret = new HashMap<>();
      ret.put(token[0].trim(), token[1].trim());
    } else {
      ret = null;
    }

    return ret;
  }

}
