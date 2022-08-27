package de.heinerion.invoice.util;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.Conveyable;
import de.heinerion.invoice.models.Invoice;
import de.heinerion.invoice.models.Letter;
import de.heinerion.invoice.services.ConfigurationService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static de.heinerion.invoice.services.ConfigurationService.PropertyKey.*;

@Service
public class PathUtilNG {
  private final Session session = Session.getInstance();

  private String getSystemFolderName() {
    return ConfigurationService.get(FOLDER_SYSTEM);
  }

  private String getTemplateFolderName() {
    return ConfigurationService.get(FOLDER_TEMPLATES);
  }

  public Path getSystemPath() {
    return ensureDirectory(getBasePath().resolve(getSystemFolderName()));
  }

  public Path getWorkingDirectory() {
    return ensureDirectory(getSystemPath().resolve("workspace"));
  }

  public Path getLogPath(String folderName) {
    return ensureDirectory(getSystemPath().resolve("logs").resolve(folderName));
  }

  Path ensureDirectory(Path path) {
    if (Files.exists(path) &&
        Files.isDirectory(path)) {
      return path;
    }

    try {
      return Files.createDirectories(path);
    } catch (IOException e) {
      throw new RuntimeException(
          String.format("%s is no directory and could not be created", path), e);
    }
  }

  private Path getTemplatePath() {
    return ensureDirectory(getSystemPath().resolve(getTemplateFolderName()));
  }

  public Path determinePath(Class<? extends Conveyable> itemClass) {
    return ensureDirectory(buildPath(determineFolderName(itemClass)));
  }

  public Path getBasePath() {
    return ensureDirectory(Path.of(java.lang.System.getProperty("user.home"), ConfigurationService.get(FOLDER_DATA)));
  }

  private String determineFolderName(Class<? extends Conveyable> itemClass) {
    if (itemClass.isAssignableFrom(Letter.class)) {
      return ConfigurationService.get(FOLDER_LETTERS);
    }

    if (itemClass.isAssignableFrom(Invoice.class)) {
      return ConfigurationService.get(FOLDER_INVOICES);
    }

    throw new NoValidLetterException(itemClass);
  }

  private Path buildPath(String folderName) {
    return buildPath(getBasePath(), folderName);
  }

  private Path buildPath(Path baseDir, String folderName) {
    Path prodPath = baseDir.resolve(folderName);
    return session.isDebugMode()
        ? prodPath.resolve("Debug")
        : prodPath;
  }

  public Path getTemplateFilePath(String descriptiveName) {
    return ensureFile(getTemplatePath().resolve(descriptiveName + ".sav"));
  }

  Path ensureFile(Path path) {
    if (Files.exists(path) &&
        Files.isRegularFile(path)) {
      return path;
    }

    try {
      Files.createDirectories(path.getParent());
      return Files.createFile(path);
    } catch (IOException e) {
      throw new RuntimeException(
          String.format("%s is no directory and could not be created", path), e);
    }
  }

  private static class NoValidLetterException extends RuntimeException {
    NoValidLetterException(Class<?> clazz) {
      super(clazz.getSimpleName() + " is no valid letter extending class");
    }
  }
}
