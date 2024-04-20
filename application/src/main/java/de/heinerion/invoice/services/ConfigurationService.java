package de.heinerion.invoice.services;

import lombok.extern.flogger.Flogger;

import java.io.*;
import java.util.*;

@Flogger
public class ConfigurationService {
  private static Properties config;

  /**
   * Hides the default public Constructor
   */
  private ConfigurationService() {
  }

  public static String get(PropertyKey key) {
    return getConfig().getProperty(Objects.requireNonNull(key).key);
  }

  private static Properties getConfig() {
    if (config == null) {
      config = new Properties();

      loadConfigurations();
    }

    return config;
  }

  private static void loadConfigurations() {
    loadPropertiesFile("configuration.properties");
    loadPropertiesFile("git.properties");
  }

  private static void loadPropertiesFile(String propFileName) {
    InputStream inputStream = ConfigurationService.class.getClassLoader().getResourceAsStream(propFileName);

    try {
      getConfig().load(inputStream);
    } catch (IOException ex) {
      throw new PropertiesCouldNotBeReadException(propFileName, ex);
    } finally {
      closeInputStream(inputStream);
    }
  }

  private static void closeInputStream(InputStream inputStream) {
    if (inputStream != null) {
      try {
        inputStream.close();
      } catch (IOException e) {
        log.atSevere().withCause(e).log();
      }
    }
  }

  public static void exitApplication() {
    log.atInfo().log("successfully shut down.");
  }

  private static class PropertiesCouldNotBeReadException extends RuntimeException {
    PropertiesCouldNotBeReadException(String fileName, Throwable t) {
      super("property file '" + fileName + "' could not be read", t);
    }
  }

  public enum PropertyKey {
    FOLDER_DATA("folder.data"),
    FOLDER_INVOICES("folder.invoices"),
    FOLDER_LETTERS("folder.letters"),
    FOLDER_SOURCES("folder.sources"),
    FOLDER_SYSTEM("folder.system"),
    FOLDER_TEMPLATES("folder.templates"),
    FOLDER_TEX_TEMPLATES("folder.texTemplates"),
    REVISION("git.commit.id.describe-short"),
    LATEX_COMMAND("latex.command");

    final String key;

    PropertyKey(String key) {
      this.key = key;
    }
  }
}
