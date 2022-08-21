package de.heinerion.invoice.print.pdf.boundary;

import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Flogger
@Service
class FileHandler {
  File writeToFile(String path, String content) {
    File target = new File(path);

    try (BufferedWriter out = new BufferedWriter(new FileWriter(target))) {
      out.write(content);

      out.flush();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return target;
  }

  void moveFile(String destinationPath, File output) {
    File destination = prepareFile(destinationPath);
    try {
      Files.move(output.toPath(), destination.toPath(), StandardCopyOption.ATOMIC_MOVE);
      log.atFine().log("File moved to %s", destination.getAbsolutePath());
    } catch (IOException ioe) {
      log.atWarning()
          .withCause(ioe)
          .log("File could not be moved to %s.", destination.getAbsolutePath());
    }
  }

  private File prepareFile(String texPath) {
    File texDestination = new File(texPath);

    if (!texDestination.exists()) {
      createDestinationFile(texDestination);
    }

    return texDestination;
  }

  private void createDestinationFile(File texDestination) {
    try {
      boolean dirsCreated = texDestination
          .getAbsoluteFile()
          .getParentFile()
          .mkdirs();

      boolean fileCreated = texDestination.createNewFile();
      log.atFine().log(createLogMessage(texDestination, dirsCreated, fileCreated));
    } catch (IOException e) {
      log.atSevere()
          .withCause(e)
          .log("Could not create destination '%s'", texDestination.getAbsolutePath());
    }
  }

  private String createLogMessage(File texDestination, boolean dirsCreated, boolean fileCreated) {
    List<String> messages = new ArrayList<>();
    if (fileCreated) {
      messages.add("File '%s' created.".formatted(texDestination.getAbsolutePath()));
    }
    if (dirsCreated) {
      messages.add("Directories created.");
    }
    return String.join(" ", messages);
  }

  void deleteFile(String filename) {
    File tempFile = new File(filename);

    if (tempFile.delete()) {
      log.atFine().log("%s removed", filename);
    } else {
      log.atWarning().log("%s could not be removed", filename);
    }
  }
}
