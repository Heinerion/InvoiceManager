package de.heinerion.betriebe.classes.file_operations.loading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractLoader<T> implements Loader<T> {
  private static final Logger logger = LogManager.getLogger(AbstractLoader.class);

  private List<File> files;
  private final List<LoadListener> listeners;
  private final File loadDirectory;

  protected AbstractLoader(File aLoadDirectory) {
    loadDirectory = aLoadDirectory;
    listeners = new ArrayList<>();
  }

  @Override
  public final void addListener(LoadListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  @Override
  public final int getFileNumber() {
    int number = 0;
    if (files != null) {
      number = files.size();
    }

    return number;
  }

  protected final List<File> getFiles() {
    return files;
  }

  protected final File getLoadDirectory() {
    return loadDirectory;
  }

  protected abstract Pattern getPattern();

  @Override
  public final void init() {
    if (logger.isDebugEnabled()) {
      logger.debug("Initialisiere {}", getClass().getSimpleName());
    }
    listFiles();
  }

  private void listFiles() {
    if (loadDirectory.isDirectory()) {
      File[] fileArray = loadDirectory.listFiles((File file) -> matchFiles(file, getPattern()));
      if (null != fileArray) {
        files = Arrays.asList(fileArray);
      }
    }
  }

  @Override
  public final List<Loadable> load() {
    if (logger.isDebugEnabled()) {
      logger.debug("Lade {}", getDescriptiveName());
    }
    List<File> fileList = getFiles();
    List<Loadable> resultList = new ArrayList<>();
    if (fileList != null) {
      for (File file : fileList) {
        Loadable item = loopAction(file);
        resultList.add(item);
        notifyLoadListeners(getDescriptiveName(), item);
      }
    }

    logger.info("{} Dokumente geladen", getFileNumber());
    return resultList;
  }

  public abstract Loadable loopAction(File file);

  private boolean matchFiles(File file, Pattern fileNamePattern) {
    boolean result = false;
    try {
      String filename = file.getCanonicalPath();
      Matcher matcher = fileNamePattern.matcher(filename);
      if (logger.isDebugEnabled()) {
        logger.debug("{}::{}", filename, matcher.matches());
      }
      result = matcher.matches();
    } catch (IOException e) {
      if (logger.isErrorEnabled()) {
        logger.error(e);
      }
    }

    return result;
  }

  @Override
  public final void notifyLoadListeners(String message, Loadable loadable) {
    for (LoadListener listener : listeners) {
      listener.notifyLoading(message, loadable);
    }
  }
}
