package de.heinerion.betriebe.classes.fileOperations.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class FileHandler {
  private static Logger logger = LogManager.getLogger(FileHandler.class);

  private FileHandler() {
  }

  /**
   * @param element
   * @param ergebnis
   * @param obj
   * @return
   */
  @SuppressWarnings("unchecked")
  private static <T> List<T> asList(T element, final Object obj) {
    List<T> ergebnis = new ArrayList<>();
    if (isListOf(obj, element.getClass())) {
      ergebnis = (List<T>) obj;
    }
    return ergebnis;
  }

  private static boolean isListOf(Object obj, Class<?> clazz) {
    boolean ret = false;
    if (obj instanceof List<?>) {
      final List<?> objAsList = (List<?>) obj;

      if (objAsList.size() > 0) {
        final Object firstElem = objAsList.get(0);
        final Class<?> objClass = firstElem.getClass();
        ret = objClass.equals(clazz);
      }
    }

    return ret;
  }

  /**
   * @param element
   * @param pfad
   * @param ergebnis
   * @param quelle
   * @return
   */
  public static <T> List<T> load(T element, String pfad) {
    Object content = new ArrayList<>();

    try {
      final FileInputStream inFile = new FileInputStream(pfad);
      final ObjectInputStream inObject = new ObjectInputStream(inFile);
      content = inObject.readObject();
      inObject.close();
      inFile.close();
    } catch (FileNotFoundException e) {
      // Keine Speicherdaten ODER Datenklasse geändert
      logger.warn("Keine Speicherdaten in {}", pfad);
    } catch (IOException | ClassNotFoundException e) {
      logger.error(e);
    }

    return asList(element, content);
  }

  public static void writeObject(Object obj, String path) {
    try {
      File pathToFile = new File(path);
      pathToFile.getParentFile().mkdirs();
      final FileOutputStream fOut = new FileOutputStream(path);
      final ObjectOutputStream objOut = new ObjectOutputStream(fOut);
      objOut.writeObject(obj);
      objOut.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
