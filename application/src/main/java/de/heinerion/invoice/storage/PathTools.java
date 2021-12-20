package de.heinerion.invoice.storage;

import de.heinerion.betriebe.models.Storable;
import de.heinerion.betriebe.util.PathUtilNG;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PathTools {

  private static final String FOLDER = "folder";

  private static final String FILE_ENDING = "fileEnding";

  private static final Map<String, Map<String, String>> FILE_INFOS = Collections.unmodifiableMap(generateInfoMap());

  private static Map<String, Map<String, String>> generateInfoMap() {
    Map<String, Map<String, String>> infoMap = new HashMap<>();

    infoMap.put("Address", createEntries("Addresses", "address"));
    infoMap.put("Client", createEntries("Clients", "client"));
    infoMap.put("Company", createEntries("Companies", "company"));
    infoMap.put("Invoice", createEntries("Invoices", "invoice"));
    infoMap.put("Letter", createEntries("Letters", "letter"));

    return infoMap;
  }

  private PathTools() {
  }

  /**
   * Generiert Mapeinträge
   *
   * @param folder     Ordnername
   * @param fileEnding Dateiendung
   * @return Eine Map mit den korrekten Einträgen
   */
  private static Map<String, String> createEntries(String folder, String fileEnding) {
    final Map<String, String> ret = new HashMap<>();
    ret.put(FOLDER, folder);
    ret.put(FILE_ENDING, fileEnding);

    return ret;
  }

  public static String determineFileEnding(Class<?> clazz) {
    return get(clazz, FILE_ENDING);
  }

  public static String determineFolderName(Class<?> clazz) {
    return get(clazz, FOLDER);
  }

  private static String generatePath(String rootPath, Storable storable) {
    StringBuilder path = new StringBuilder(rootPath);

    for (String classification : storable.getClassification()) {
      path.append(File.separator)
          .append(classification);
    }

    path.append(File.separator)
        .append(storable.getEntryName())
        .append(".")
        .append(storable.getIdentification());

    return path.toString();
  }

  private static String get(Class<?> clazz, String key) {
    final Map<String, String> details = FILE_INFOS.get(clazz.getSimpleName());

    if (details == null) {
      throw new NoFileInfoException(clazz);
    } else {
      return details.get(key);
    }
  }

  public static String getPath(Class<?> clazz, PathUtilNG pathUtil) {
    if (FILE_INFOS.containsKey(clazz.getSimpleName())) {
      return pathUtil.getSystemPath() + File.separator + determineFolderName(clazz);
    } else {
      throw new NoFileInfoException(clazz);
    }
  }

  public static String getPath(Storable storable, PathUtilNG pathUtil) {
    return generatePath(pathUtil.getSystemPath(), storable);
  }

  static class NoFileInfoException extends RuntimeException {
    NoFileInfoException(Class<?> clazz) {
      super("no file info has been defined for " + clazz.getSimpleName());
    }
  }
}
