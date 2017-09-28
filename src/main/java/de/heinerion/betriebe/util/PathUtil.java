package de.heinerion.betriebe.util;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.services.ConfigurationService;

import java.io.File;

public class PathUtil {
  private PathUtil() {
  }

  public static String getSystemPath() {
    return buildPath(getSystemFolderName());
  }

  private static String getSystemFolderName() {
    return ConfigurationService.get("folder.system");
  }

  public static String getTemplatePath() {
    return buildPath(getSystemPath(), getTemplateFolderName());
  }

  public static String getTemplateFolderName() {
    return ConfigurationService.get("folder.templates");
  }

  public static String getTexTemplatePath() {
    return buildPath(getTexTemplateFolderName());
  }

  private static String getTexTemplateFolderName() {
    return ConfigurationService.get("folder.texTemplates");
  }

  public static String determinePath(Class<? extends Letter> itemClass) {
    return buildPath(determineFolderName(itemClass));
  }

  public static String getBaseDir() {
    return java.lang.System.getProperty("user.home") + File.separator + ConfigurationService.get("folder.data");
  }

  private static String determineFolderName(Class<? extends Letter> itemClass) {
    String folder;

    if (itemClass.isAssignableFrom(Letter.class)) {
      folder = ConfigurationService.get("folder.letters");
    } else if (itemClass.isAssignableFrom(Invoice.class)) {
      folder = ConfigurationService.get("folder.invoices");
    } else {
      throw new NoValidLetterException(itemClass);
    }

    return folder;
  }

  private static String buildPath(String folderName) {
    return buildPath(getBaseDir(), folderName);
  }

  private static String buildPath(String baseDir, String folderName) {
    return baseDir + File.separator + folderName + (Session.isDebugMode() ? File.separator + "Debug" : "");
  }

  public static String getTemplateFileName(String descriptiveName) {
    return generateFileName(getTemplatePath(), descriptiveName, "sav");
  }

  private static String generateFileName(String path, String name, String ending) {
    return path + File.separator + name + "." + ending;
  }

  static class NoValidLetterException extends RuntimeException {
    NoValidLetterException(Class<?> clazz) {
      super(clazz.getSimpleName() + " is no valid letter extending class");
    }
  }
}
