package de.heinerion.invoice.util;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.Invoice;
import de.heinerion.invoice.models.Letter;
import de.heinerion.invoice.services.ConfigurationService;
import org.springframework.stereotype.Service;

import java.io.File;
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

  public String getSystemPath() {
    return buildPath(getSystemFolderName());
  }

  public Path getWorkingDirectory() {
    return ensureDirectory(Path.of(getSystemPath(), "workspace"));
  }

  public Path getLogPath(String folderName) {
    return ensureDirectory(Path.of(getSystemPath(), "logs", folderName));
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

  private String getTemplatePath() {
    return buildPath(getSystemPath(), getTemplateFolderName());
  }

  public String determinePath(Class<? extends Letter> itemClass) {
    return buildPath(determineFolderName(itemClass));
  }

  public String getBaseDir() {
    return java.lang.System.getProperty("user.home") + File.separator + ConfigurationService.get(FOLDER_DATA);
  }

  private String determineFolderName(Class<? extends Letter> itemClass) {
    if (itemClass.isAssignableFrom(Letter.class)) {
      return ConfigurationService.get(FOLDER_LETTERS);
    }

    if (itemClass.isAssignableFrom(Invoice.class)) {
      return ConfigurationService.get(FOLDER_INVOICES);
    }

    throw new NoValidLetterException(itemClass);
  }

  private String buildPath(String folderName) {
    return buildPath(getBaseDir(), folderName);
  }

  private String buildPath(String baseDir, String folderName) {
    return baseDir
        + File.separator
        + folderName
        + (session.isDebugMode() ? File.separator + "Debug" : "");
  }

  public String getTemplateFileName(String descriptiveName) {
    return generateSavFileName(getTemplatePath(), descriptiveName);
  }

  private String generateSavFileName(String path, String name) {
    return path + File.separator + name + ".sav";
  }

  private static class NoValidLetterException extends RuntimeException {
    NoValidLetterException(Class<?> clazz) {
      super(clazz.getSimpleName() + " is no valid letter extending class");
    }
  }
}
