package de.heinerion.betriebe.services;

import de.heinerion.exceptions.HeinerionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationService {
  private static final Logger LOGGER = LogManager.getLogger(ConfigurationService.class);

  private static Properties config;

  private static InputStream inputStream;

  static {
    try {
      loadConfigurations();
    } catch (IOException e) {
      LOGGER.error("Error in loading the configuration file", e);
    }
  }

  /**
   * Hides the default public Constructor
   */
  private ConfigurationService(){}

  public static void loadConfigurations() throws IOException {
    config = new Properties();

    loadPropertiesFile("configuration.properties");
    loadPropertiesFile("git.properties");
  }

  private static void loadPropertiesFile(String propFileName) {
    inputStream = ConfigurationService.class.getClassLoader().getResourceAsStream(propFileName);

    try {
      config.load(inputStream);
    } catch (IOException ex) {
      throw new HeinerionException("property file '" + propFileName + "' could not be read", ex);
    } finally {
      closeInputStream();
    }
  }

  private static void closeInputStream() {
    if (inputStream != null) {
      try {
        inputStream.close();
      } catch (IOException e) {
        LOGGER.error(e);
      }
    }
  }

  public static String get(String key) {
    return config.getProperty(key);
  }
}
