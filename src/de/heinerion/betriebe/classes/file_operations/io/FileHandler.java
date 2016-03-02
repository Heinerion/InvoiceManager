package de.heinerion.betriebe.classes.file_operations.io;

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

      if (objAsList.size() > 0) {
        Object firstElem = objAsList.get(0);
        Class<?> objClass = firstElem.getClass();
        ret = objClass.equals(clazz);
      }
    }

    return ret;
  }

  public static <T> List<T> load(T element, String path) {
    Object content = new ArrayList<>();

    try (FileInputStream inFile = new FileInputStream(path);
         ObjectInputStream inObject = new ObjectInputStream(inFile)) {
      content = inObject.readObject();
    } catch (FileNotFoundException e) {
      // Keine Speicherdaten ODER Datenklasse geändert
      logger.warn("Keine Speicherdaten in {}", path);
    } catch (IOException | ClassNotFoundException e) {
      logger.error(e);
    }

    return asList(element, content);
  }

  public static void writeObject(Object obj, String path) {
    try {
      File pathToFile = new File(path);
      pathToFile.getParentFile().mkdirs();
      try (FileOutputStream fOut = new FileOutputStream(path);
           ObjectOutputStream objOut = new ObjectOutputStream(fOut)) {
        objOut.writeObject(obj);
        objOut.close();
      }
    } catch (IOException e) {
      if (logger.isErrorEnabled()) {
        logger.error(e);
      }
      HeinerionException.rethrow(e);
    }
  }
}
