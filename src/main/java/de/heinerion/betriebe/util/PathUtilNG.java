package de.heinerion.betriebe.util;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.services.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class PathUtilNG {
  @Autowired
  public PathUtilNG() {

  }

  public String getSystemFolderName() {
    return ConfigurationService.get("folder.system");
  }

  public String getTemplateFolderName() {
    return ConfigurationService.get("folder.templates");
  }

  public String getTexTemplateFolderName() {
    return ConfigurationService.get("folder.texTemplates");
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
    return java.lang.System.getProperty("user.home") + File.separator + ConfigurationService.get("folder.data");
  }

  private String determineFolderName(Class<? extends Letter> itemClass) {
    String folder;

    if (itemClass.isAssignableFrom(Letter.class)) {
      folder = ConfigurationService.get("folder.letters");
    } else if (itemClass.isAssignableFrom(Invoice.class)) {
      folder = ConfigurationService.get("folder.invoices");
    } else {
      throw new PathUtil.NoValidLetterException(itemClass);
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
    return generateFileName(getTemplatePath(), descriptiveName, "sav");
  }

  private String generateFileName(String path, String name, String ending) {
    return path + File.separator + name + "." + ending;
  }
}
