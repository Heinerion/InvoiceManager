package de.heinerion.invoice.repositories.migration;

import lombok.extern.flogger.Flogger;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Flogger
public
class FileHandler {
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
    return obj instanceof List<?> list
        && isListOfClass(clazz, list);
  }

  private static boolean isListOfClass(Class<?> clazz, List<?> objAsList) {
    if (objAsList.isEmpty()) {
      return false;
    }

    Object firstElem = objAsList.get(0);
    Class<?> objClass = firstElem.getClass();
    return objClass.equals(clazz);
  }

  public static <T> List<T> load(T element, Path path) {
    Object content = new ArrayList<>();

    if (Files.exists(path)) {
      content = loadFromFile(path);
    } else {
      log.atWarning().log("Nothing to load in %s", path);
    }

    return asList(element, content);
  }

  private static Object loadFromFile(Path path) {
    Object content = new ArrayList<>();

    try (InputStream inFile = Files.newInputStream(path);
         ObjectInputStream inObject = new ObjectInputStream(inFile)) {
      content = inObject.readObject();
    } catch (IOException e) {
      log.atSevere().withCause(e).log("load from %s", path);
    } catch (ClassNotFoundException e) {
      log.atWarning().withCause(e).log("loading old template from %s", path);
      content = convertLegacyTemplates(path);
    }

    return content;
  }

  private static Object convertLegacyTemplates(Path path) {
    String backupPath = path + "_backup_" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ").format(new Date());

    log.atInfo().log("backup legacy template to %s", backupPath);

    try {
      Files.copy(path, Paths.get(backupPath));
    } catch (IOException e) {
      throw new FileCouldNotBeCopiedException(e);
    }

    Object content = new ArrayList<>();

    try (InputStream inFile = Files.newInputStream(path);
         ObjectInputStream inObject = new LegacyTemplateReader(inFile)) {
      log.atInfo().log("try to read %s as old Template", path);
      content = inObject.readObject();

      log.atInfo().log("write back to %s as new Template", path);
      writeObject(content, path);
    } catch (IOException | ClassNotFoundException e2) {
      log.atSevere().withCause(e2).log("could not repair template %s", path);
    }

    return content;
  }

  static void writeObject(Object obj, Path path) {
    try {
      Files.createDirectories(path.getParent());
      if (Files.exists(path)) {
        writeToFile(path, obj);
      } else {
        throw new FileCouldNotBeCreatedException(path);
      }
    } catch (IOException e) {
      log.atSevere().withCause(e).log("could not write to %s", path);
      throw new CouldNotWriteObjectToFileException(e);
    }
  }

  private static void writeToFile(Path path, Object obj) throws IOException {
    try (OutputStream fOut = Files.newOutputStream(path);
         ObjectOutputStream objOut = new ObjectOutputStream(fOut)) {
      objOut.writeObject(obj);
    }
  }

  private static class CouldNotWriteObjectToFileException extends RuntimeException {
    CouldNotWriteObjectToFileException(Throwable e) {
      super(e);
    }
  }

  private static class FileCouldNotBeCopiedException extends RuntimeException {
    FileCouldNotBeCopiedException(Throwable e) {
      super(e);
    }
  }

  private static class FileCouldNotBeCreatedException extends RuntimeException {
    FileCouldNotBeCreatedException(Path path) {
      super("File " + path + " could not be created.");
    }
  }
}
