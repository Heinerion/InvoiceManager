package de.heinerion.betriebe.util;

import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.services.ConfigurationService;

public class PathUtil {
  private static PathUtilNG pathUtil = ConfigurationService.getBean(PathUtilNG.class);

  private PathUtil() {
  }

  /*
   * still used in TexTemplate constructor
   */
  public static String getTexTemplatePath() {
    return pathUtil.getTexTemplatePath();
  }

  /*
   * still used in Company#getFolderFile()
   */
  public static String determinePath(Class<? extends Letter> itemClass) {
    return pathUtil.determinePath(itemClass);
  }
}
