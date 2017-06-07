package de.heinerion.betriebe.tools;

import de.heinerion.betriebe.enums.SystemAndPathsEnum;
import de.heinerion.betriebe.exceptions.HeinerionException;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.services.ConfigurationService;

import java.io.File;

public class PathUtil {
  private PathUtil() {
  }

  public static String getSystemPath() {
    String baseDir = getBaseDir();
    String folderName = getSystemFolderName();

    return buildPath(baseDir, folderName);
  }

  private static String getSystemFolderName() {
    return ConfigurationService.get("folder.system");
  }

  public static String determinePath(Class<? extends Conveyable> itemClass) {
    String baseDir = getBaseDir();
    String folderName = determineFolderName(itemClass);

    return buildPath(baseDir, folderName);
  }

  public static String getBaseDir() {
    return System.getProperty("user.home") + File.separator + ConfigurationService.get("folder.data")
        + File.separator;
  }

  private static String determineFolderName(Class<? extends Conveyable> itemClass) {
    String folder;

    if (itemClass.isAssignableFrom(Invoice.class)) {
      folder = ConfigurationService.get("folder.invoices");
    } else if (itemClass.isAssignableFrom(Letter.class)) {
      folder = ConfigurationService.get("folder.letters");
    } else {
      throw new HeinerionException("The given itemClass is no valid letter or invoice class");
    }

    return folder;
  }

  private static String buildPath(String baseDir, String folderName) {
    return baseDir + File.separator + folderName + (SystemAndPathsEnum.isDebugMode() ? File.separator + "Debug" : "");
  }
}
