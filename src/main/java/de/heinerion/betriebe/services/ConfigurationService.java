package de.heinerion.betriebe.services;

import de.heinerion.exceptions.HeinerionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationService {
  private static final Logger LOGGER = LogManager.getLogger(ConfigurationService.class);

  private static AbstractApplicationContext context;

  private static Properties config;

  /**
   * Hides the default public Constructor
   */
  private ConfigurationService() {
  }

  public static <T> T getBean(String classnameOrId) {
    return (T) getContext().getBean(classnameOrId);
  }

  public static String get(String key) {
    return getConfig().getProperty(key);
  }

  private static AbstractApplicationContext getContext() {
    if (context == null) {
      context = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    return context;
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

    getContext().registerShutdownHook();
  }

  private static void loadPropertiesFile(String propFileName) {
    InputStream inputStream = ConfigurationService.class.getClassLoader().getResourceAsStream(propFileName);

    try {
      getConfig().load(inputStream);
    } catch (IOException ex) {
      throw new HeinerionException("property file '" + propFileName + "' could not be read", ex);
    } finally {
      closeInputStream(inputStream);
    }
  }

  private static void closeInputStream(InputStream inputStream) {
    if (inputStream != null) {
      try {
        inputStream.close();
      } catch (IOException e) {
        LOGGER.error(e);
      }
    }
  }
}
