package de.heinerion.betriebe.loading;

import java.io.IOException;
import java.util.Map;

interface Reader {
  void closeFile() throws IOException;

  void prepareFile(String path) throws IOException;

  Map<String, String> read() throws IOException;
}
