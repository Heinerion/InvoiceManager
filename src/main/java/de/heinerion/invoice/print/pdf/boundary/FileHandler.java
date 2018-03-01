package de.heinerion.invoice.print.pdf.boundary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FileHandler {
  private Logger logger = LogManager.getLogger(FileHandler.class);

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
      BoundaryException.rethrow(e);
    }

    return target;
  }

  void moveFile(String destinationPath, File output) {
    File destination = prepareFile(destinationPath);
    boolean success = output.renameTo(destination);

    if (success) {
      if (logger.isInfoEnabled()) {
        logger.info("File moved to {}", destination.getAbsolutePath());
      }
    } else {
      if (logger.isWarnEnabled()) {
        logger.warn("File could not be moved to {}", destination.getAbsolutePath());
      }
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

      if (logger.isInfoEnabled()) {
        List<String> messages = new ArrayList<>();
        if (fileCreated) {
          messages.add("File '" + texDestination.getAbsolutePath() + "' created.");
        }
        if (dirsCreated) {
          messages.add("Directories created.");
        }

        logger.info(Strings.join(messages, ' '));
      }
    } catch (IOException e) {
      if (logger.isErrorEnabled()) {
        logger.error("Could not create destination '" + texDestination.getAbsolutePath() + "'", e);
      }
    }
  }

  void deleteFile(String filename) {
    File tempFile = new File(filename);

    if (tempFile.delete()) {
      if (logger.isDebugEnabled()) {
        logger.debug("{} removed", filename);
      }
    } else {
      if (logger.isWarnEnabled()) {
        logger.warn("{} could not be removed", filename);
      }
    }
  }
}
