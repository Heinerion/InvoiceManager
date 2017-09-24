package de.heinerion.betriebe.loading;

import de.heinerion.betriebe.HeinerionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class FileHandler {
  private static final Logger logger = LogManager.getLogger(FileHandler.class);

  private FileHandler() {
  }

  @SuppressWarnings("unchecked")
  private static <T> List<T> asList(T element, Object obj) {
    List<T> result = new ArrayList<>();
    if (isListOf(obj, element.getClass())) {
      result = (List<T>) obj;
    }
    return result;
  }

  private static boolean isListOf(Object obj, Class<?> clazz) {
    boolean ret = false;
    if (obj instanceof List<?>) {
      List<?> objAsList = (List<?>) obj;

      ret = isListOfClass(clazz, objAsList);
    }

    return ret;
  }

  private static boolean isListOfClass(Class<?> clazz, List<?> objAsList) {
    boolean ret = false;
    if (!objAsList.isEmpty()) {
      Object firstElem = objAsList.get(0);
      Class<?> objClass = firstElem.getClass();
      ret = objClass.equals(clazz);
    }
    return ret;
  }

  static <T> List<T> load(T element, String path) {
    Object content = new ArrayList<>();

    File source = new File(path);
    if (source.exists()) {
      content = loadFromFile(path);
    } else {
      if (logger.isWarnEnabled()) {
        logger.warn("Nothing to load in {}", path);
      }
    }

    return asList(element, content);
  }

  private static Object loadFromFile(String path) {
    Object content = new ArrayList<>();

    try (FileInputStream inFile = new FileInputStream(path);
         ObjectInputStream inObject = new ObjectInputStream(inFile)) {
      content = inObject.readObject();
    } catch (IOException e) {
      logger.error("load from " + path, e);
    } catch (ClassNotFoundException e) {
      if (logger.isWarnEnabled()) {
        logger.warn("loading old template from " + path, e);
      }
      content = convertLegacyTemplates(path);
    }

    return content;
  }

  private static Object convertLegacyTemplates(String path) {
    String backupPath = path + "_backup_" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ").format(new Date());

    if (logger.isInfoEnabled()) {
      logger.info("backup legacy template to {}", backupPath);
    }

    try {
      Files.copy(Paths.get(path), Paths.get(backupPath));
    } catch (IOException e) {
      throw new HeinerionException(e);
    }

    Object content = new ArrayList<>();

    try (FileInputStream inFile = new FileInputStream(path);
         ObjectInputStream inObject = new LegacyTemplateReader(inFile)) {
      if (logger.isInfoEnabled()) {
        logger.info("try to read {} as old Template", path);
      }
      content = inObject.readObject();

      if (logger.isInfoEnabled()) {
        logger.info("write back to {} as new Template", path);
      }
      writeObject(content, path);
    } catch (IOException | ClassNotFoundException e2) {
      logger.error("could not repair template " + path, e2);
    }

    return content;
  }

  static void writeObject(Object obj, String path) {
    try {
      File pathToFile = new File(path);
      boolean dirsCreated = pathToFile.getParentFile().mkdirs();
      if (dirsCreated || pathToFile.exists()) {
        writeToFile(path, obj);
      } else {
        throw new HeinerionException("File could " + path + " not be created");
      }
    } catch (IOException e) {
      if (logger.isErrorEnabled()) {
        logger.error("write to " + path, e);
      }
      HeinerionException.rethrow(e);
    }
  }

  private static void writeToFile(String path, Object obj) throws IOException {
    try (FileOutputStream fOut = new FileOutputStream(path);
         ObjectOutputStream objOut = new ObjectOutputStream(fOut)) {
      objOut.writeObject(obj);
      objOut.close();
    }
  }
}
