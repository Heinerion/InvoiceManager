package de.heinerion.betriebe.fileoperations.io;

import de.heinerion.betriebe.exceptions.HeinerionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
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

  public static <T> List<T> load(T element, String path) {
    Object content = new ArrayList<>();

    File source = new File(path);
    if (source.exists()) {
      content = loadFromFile(path);
    } else {
      logger.warn("Nothing to load in {}", path);
    }

    return asList(element, content);
  }

  private static Object loadFromFile(String path) {
    Object content = new ArrayList<>();

    try (FileInputStream inFile = new FileInputStream(path);
         ObjectInputStream inObject = new ObjectInputStream(inFile)) {
      content = inObject.readObject();
    } catch (IOException | ClassNotFoundException e) {
      logger.error(e);
    }

    return content;
  }

  public static void writeObject(Object obj, String path) {
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
        logger.error(e);
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
