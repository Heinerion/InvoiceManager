package de.heinerion.betriebe.loader;

import java.io.IOException;
import java.util.Map;

public interface Reader {
  void closeFile() throws IOException;

  void prepareFile(String path) throws IOException;

  Map<String, String> read() throws IOException;
}
