package de.heinerion.invoice.boundary;

import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

@Flogger
@Service
class FileHandler {
  void writeToFile(Path path, String content) {
    try {
      Files.write(path, Collections.singleton(content));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
