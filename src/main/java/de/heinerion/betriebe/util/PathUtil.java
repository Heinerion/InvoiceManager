package de.heinerion.betriebe.util;

import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.services.ConfigurationService;

public class PathUtil {
  private static PathUtilNG pathUtil = ConfigurationService.getBean(PathUtilNG.class);

  private PathUtil() {
  }

  public static String getSystemPath() {
    return pathUtil.getSystemPath();
  }

  public static String getTemplatePath() {
    return pathUtil.getTemplatePath();
  }

  public static String getTemplateFolderName() {
    return pathUtil.getTemplateFolderName();
  }

  public static String getTexTemplatePath() {
    return pathUtil.getTexTemplatePath();
  }

  public static String determinePath(Class<? extends Letter> itemClass) {
    return pathUtil.determinePath(itemClass);
  }

  public static String getBaseDir() {
    return pathUtil.getBaseDir();
  }

  public static String getTemplateFileName(String descriptiveName) {
    return pathUtil.getTemplateFileName(descriptiveName);
  }

  static class NoValidLetterException extends RuntimeException {
    NoValidLetterException(Class<?> clazz) {
      super(clazz.getSimpleName() + " is no valid letter extending class");
    }
  }
}
