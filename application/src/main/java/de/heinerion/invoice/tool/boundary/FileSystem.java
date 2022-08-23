package de.heinerion.invoice.tool.boundary;

import java.io.File;
import java.io.IOException;

/**
 * Handles task like creating or deleting files
 * <p>
 * Main purpose is to decouple file system works from unit tests
 * </p>
 */
public class FileSystem {
  public void ensureFileExists(File texDestination) throws IOException {
    if (!texDestination.exists()) {
      boolean dirsCreated = texDestination
          .getAbsoluteFile()
          .getParentFile()
          .mkdirs();

      if (dirsCreated) {
        log("created directory: " + texDestination.getAbsoluteFile().getParent());
      }

      boolean fileCreated = texDestination.createNewFile();

      if (fileCreated) {
        log("created file: " + texDestination.getAbsolutePath());
      }
    }
  }

  public void deleteFile(String directory, String baseName, String ending) {
    File tempFile = new File(directory, baseName + "." + ending);

    if (tempFile.delete()) {
      log("removed: " + tempFile.getAbsolutePath());
    } else {
      log("could not be removed: " + tempFile.getAbsolutePath());
    }
  }

  private void log(String message) {
    System.out.println(message);
  }
}
