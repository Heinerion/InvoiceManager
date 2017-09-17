package de.heinerion.betriebe.loading;

import java.io.IOException;

interface Writer {
  void closeFile() throws IOException;

  void prepareFile(String path) throws IOException;

  void write(String key, String value) throws IOException;
}
