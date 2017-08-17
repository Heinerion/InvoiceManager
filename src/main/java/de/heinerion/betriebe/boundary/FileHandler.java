package de.heinerion.betriebe.boundary;

import de.heinerion.exceptions.HeinerionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class FileHandler {
  @Autowired
  FileHandler() {
  }

  File writeToFile(String path, String content) {
    File target = new File(path);

    try (BufferedWriter out = new BufferedWriter(new FileWriter(target))) {
      out.write(content);

      out.flush();
      out.close();
    } catch (IOException e) {
      HeinerionException.rethrow(e);
    }

    return target;
  }
}
