package de.heinerion.invoice.storage.loading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class Loader implements LoadListenable {
  private static final Logger logger = LogManager.getLogger(Loader.class);

  private static final String VALID = "File: {}, compatible with {}";
  private static final String INVALID = "File: {}, not compatible with {}";

  private List<File> files;
  private final List<LoadListener> listeners;
  private final File loadDirectory;

  Loader(File aLoadDirectory) {
    loadDirectory = aLoadDirectory;
    listeners = new ArrayList<>();
  }

  @Override
  public final void addListener(LoadListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  public abstract String getDescriptiveName();

  final int getFileNumber() {
    int number = 0;
    if (files != null) {
      number = files.size();
    }

    return number;
  }

  private List<File> getFiles() {
    List<File> result = files;
    if (result == null) {
      result = new ArrayList<>();
    }

    return result;
  }

  protected abstract Pattern getPattern();

  public final void init() {
    if (logger.isDebugEnabled()) {
      logger.debug("initialize {}", getClass().getSimpleName());
    }
    listFiles();
  }

  private void listFiles() {
    if (logger.isDebugEnabled()) {
      logger.debug("loadDirectory {}", loadDirectory.getAbsolutePath());
    }
    if (loadDirectory.isDirectory()) {
      File[] fileArray = loadDirectory.listFiles((File file) -> matchFiles(file, getPattern()));
      if (null != fileArray) {
        files = Arrays.asList(fileArray);
      }
    }
    if (logger.isDebugEnabled()) {
      logger.debug("{} documents found", getFileNumber());
    }
  }

  public final List<Loadable> load() {
    if (logger.isDebugEnabled()) {
      logger.debug("load {}", getDescriptiveName());
    }
    List<File> fileList = getFiles();
    List<Loadable> resultList = new ArrayList<>();

    for (File file : fileList) {
      Loadable item = loopAction(file);
      resultList.add(item);
      notifyLoadListeners(getDescriptiveName(), item);
    }

    if (logger.isInfoEnabled()) {
      logger.info("{} documents loaded ({})", getFileNumber(), getDescriptiveName());
    }

    return resultList;
  }

  abstract Loadable loopAction(File file);

  private boolean matchFiles(File file, Pattern fileNamePattern) {
    try {
      String filename = file.getCanonicalPath();
      Matcher matcher = fileNamePattern.matcher(filename);

      boolean result = matcher.matches();

      if (logger.isDebugEnabled() && result) {
        logger.debug(VALID, filename, getClass().getSimpleName());
      }
      if (logger.isInfoEnabled() && !result) {
        logger.info(INVALID, filename, getClass().getSimpleName());
      }

      return result;
    } catch (IOException e) {
      if (logger.isErrorEnabled()) {
        logger.error(e);
      }
      throw new Loader.MatchFilesException(e);
    }
  }

  @Override
  public final void notifyLoadListeners(String message, Loadable loadable) {
    for (LoadListener listener : listeners) {
      listener.notifyLoading(message, loadable);
    }
  }

  private static class MatchFilesException extends RuntimeException {
    MatchFilesException(Throwable cause) {
      super(cause);
    }
  }
}
