package de.heinerion.invoice.boundary;

import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

@Flogger
@Service
class FileHandler {
  void writeToFile(Path path, String content) {
    try (BufferedWriter out = new BufferedWriter(new FileWriter(path.toFile()))) {
      out.write(content);

      out.flush();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
