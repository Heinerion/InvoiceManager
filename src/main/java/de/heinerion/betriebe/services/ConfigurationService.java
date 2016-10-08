package de.heinerion.betriebe.services;

import de.heinerion.betriebe.exceptions.HeinerionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationService {
  private static final Logger LOGGER = LogManager.getLogger(ConfigurationService.class);

  private static Properties config;

  private static InputStream inputStream;

  public static void loadConfigurations() throws IOException {
    config = new Properties();
    String propFileName = "configuration.properties";

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
