package de.heinerion.betriebe.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
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

  public static <T> T getBean(Class<T> requiredType) {
    return getContext()
        .orElseGet(() -> context = createContext())
        .getBean(requiredType);
  }

  private static ClassPathXmlApplicationContext createContext() {
    LOGGER.info("create new application context");
    return new ClassPathXmlApplicationContext("de/heinerion/invoice/applicationContext.xml");
  }

  public static String get(PropertyKey key) {
    return getConfig().getProperty(Objects.requireNonNull(key).key);
  }

  private static Optional<AbstractApplicationContext> getContext() {
    return Optional.ofNullable(context);
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

    getContext().ifPresent(AbstractApplicationContext::registerShutdownHook);
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
        LOGGER.error(e);
      }
    }
  }

  private static void close() {
    getContext().ifPresent(AbstractApplicationContext::close);
  }

  public static void exitApplication() {
    close();
    LOGGER.info("successfully shut down.");
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
    REVISION("git.commit.id.describe-short");

    final String key;

    PropertyKey(String key) {
      this.key = key;
    }
  }
}
