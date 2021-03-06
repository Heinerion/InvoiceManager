package de.heinerion.betriebe.util;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.services.ConfigurationService;

import java.io.File;

import static de.heinerion.betriebe.services.ConfigurationService.PropertyKey.*;

public class PathUtilNG {

  private String getSystemFolderName() {
    return ConfigurationService.get(FOLDER_SYSTEM);
  }

  public String getTemplateFolderName() {
    return ConfigurationService.get(FOLDER_TEMPLATES);
  }

  private String getTexTemplateFolderName() {
    return ConfigurationService.get(FOLDER_TEX_TEMPLATES);
  }

  public String getSystemPath() {
    return buildPath(getSystemFolderName());
  }

  public String getTemplatePath() {
    return buildPath(getSystemPath(), getTemplateFolderName());
  }

  public String getTexTemplatePath() {
    return buildPath(getTexTemplateFolderName());
  }

  public String determinePath(Class<? extends Letter> itemClass) {
    return buildPath(determineFolderName(itemClass));
  }

  public String getBaseDir() {
    return java.lang.System.getProperty("user.home") + File.separator + ConfigurationService.get(FOLDER_DATA);
  }

  private String determineFolderName(Class<? extends Letter> itemClass) {
    String folder;

    if (itemClass.isAssignableFrom(Letter.class)) {
      folder = ConfigurationService.get(FOLDER_LETTERS);
    } else if (itemClass.isAssignableFrom(Invoice.class)) {
      folder = ConfigurationService.get(FOLDER_INVOICES);
    } else {
      throw new NoValidLetterException(itemClass);
    }

    return folder;
  }

  private String buildPath(String folderName) {
    return buildPath(getBaseDir(), folderName);
  }

  private String buildPath(String baseDir, String folderName) {
    return baseDir + File.separator + folderName + (Session.isDebugMode() ? File.separator + "Debug" : "");
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
