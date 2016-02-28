package de.heinerion.betriebe.loader;

import java.io.IOException;

public interface Writer {
  void closeFile() throws IOException;

  void prepareFile(String path) throws IOException;

  void write(String key, String value) throws IOException;
}
