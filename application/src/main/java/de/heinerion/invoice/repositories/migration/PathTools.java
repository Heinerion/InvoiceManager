package de.heinerion.invoice.repositories.migration;

import de.heinerion.invoice.util.PathUtilNG;

import java.io.File;
import java.util.Map;
import java.util.Optional;

public final class PathTools {

  private static final String FOLDER = "folder";

  private static final String FILE_ENDING = "fileEnding";

  private static final Map<String, Map<String, String>> FILE_INFOS = Map.of(
      "Address", createEntries("Addresses", "address"),
      "Client", createEntries("Clients", "client"),
      "Company", createEntries("Companies", "company"),
      "Invoice", createEntries("Invoices", "invoice"),
      "Letter", createEntries("Letters", "letter")
  );

  private PathTools() {
  }

  private static Map<String, String> createEntries(String folder, String fileEnding) {
    return Map.of(FOLDER, folder, FILE_ENDING, fileEnding);
  }

  public static String determineFolderName(Class<?> clazz) {
    return Optional.ofNullable(FILE_INFOS.get(clazz.getSimpleName()))
        .map(infos -> infos.get(FOLDER))
        .orElseThrow(() -> new NoFileInfoException(clazz));
  }

  public static String getPath(Class<?> clazz, PathUtilNG pathUtil) {
    if (FILE_INFOS.containsKey(clazz.getSimpleName())) {
      return pathUtil.getSystemPath() + File.separator + determineFolderName(clazz);
    } else {
      throw new NoFileInfoException(clazz);
    }
  }

  static class NoFileInfoException extends RuntimeException {
    NoFileInfoException(Class<?> clazz) {
      super("no file info has been defined for " + clazz.getSimpleName());
    }
  }
}
