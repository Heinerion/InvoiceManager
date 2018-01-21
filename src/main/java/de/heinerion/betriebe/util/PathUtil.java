package de.heinerion.betriebe.util;

import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.services.ConfigurationService;

/**
 * Only a wrapper for transition to PathUtilNG
 *
 * @deprecated use PathUtilNG instead
 */
@Deprecated
public class PathUtil {
  private static PathUtilNG pathUtil = ConfigurationService.getBean(PathUtilNG.class);

  private PathUtil() {
  }

  /**
   * @deprecated use pathUtil#getTexTemplatePath instead
   */
  /*
   * still used in TexTemplate constructor
   */
  @Deprecated
  public static String getTexTemplatePath() {
    return pathUtil.getTexTemplatePath();
  }


  /**
   * @deprecated use PathUtilNG#determinePath instead
   */
  /*
   * still used in Company#getFolderFile()
   */
  @Deprecated
  public static String determinePath(Class<? extends Letter> itemClass) {
    return pathUtil.determinePath(itemClass);
  }
}
