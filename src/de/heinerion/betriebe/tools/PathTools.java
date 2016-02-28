package de.heinerion.betriebe.tools;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.heinerion.betriebe.enums.Utilities;
import de.heinerion.betriebe.models.interfaces.Storable;

public final class PathTools {

  private static final String FOLDER = "folder";

  private static final String FILE_ENDING = "fileEnding";

  @SuppressWarnings("serial")
  private static final Map<String, Map<String, String>> FILE_INFOS = Collections
  .unmodifiableMap(new HashMap<String, Map<String, String>>() {
    {
      this.put("Address", createEntries("Addresses", "address"));
      this.put("Client", createEntries("Clients", "client"));
      this.put("Company", createEntries("Companies", "company"));
      this.put("Invoice", createEntries("Invoices", "invoice"));
      this.put("Letter", createEntries("Letters", "letter"));
    }
  });

  private PathTools() {
  }

  /**
   * Generiert Mapeinträge
   *
   * @param folder
   *          Ordnername
   * @param fileEnding
   *          Dateiendung
   * @return Eine Map mit den korrekten Einträgen
   */
  private static Map<String, String> createEntries(String folder,
      String fileEnding) {
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
    String path = rootPath;
    for (final String classification : storable.getClassification()) {
      path += File.separator + classification;
    }
    path += File.separator + storable.getEntryName();
    path += "." + storable.getIdentification();

    return path;
  }

  private static String get(Class<?> clazz, String key) {
    final Map<String, String> details = FILE_INFOS.get(clazz.getSimpleName());

    if (details != null) {
      return details.get(key);
    } else {
      throw new RuntimeException();
    }
  }

  public static String getPath(Class<?> clazz) {
    if (FILE_INFOS.containsKey(clazz.getSimpleName())) {
      return Utilities.SYSTEM.getPath() + File.separator
          + determineFolderName(clazz);
    } else {
      throw new RuntimeException();
    }
  }

  public static String getPath(Storable storable) {
    return generatePath(Utilities.SYSTEM.getPath(), storable);
  }
}
