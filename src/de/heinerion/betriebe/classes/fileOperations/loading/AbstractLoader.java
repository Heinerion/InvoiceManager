package de.heinerion.betriebe.classes.fileOperations.loading;

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
  private static Logger logger = LogManager.getLogger(AbstractLoader.class);

  private List<File> files;
  private final List<LoadListener> listeners;
  private final File loadDirectory;

  protected AbstractLoader(File aLoadDirectory) {
    this.loadDirectory = aLoadDirectory;
    this.listeners = new ArrayList<>();
  }

  @Override
  public final void addListener(LoadListener listener) {
    if (!this.listeners.contains(listener)) {
      this.listeners.add(listener);
    }
  }

  @Override
  public final int getFileNumber() {
    int number = 0;
    if (this.files != null) {
      number = this.files.size();
    }

    return number;
  }

  protected final List<File> getFiles() {
    return this.files;
  }

  protected final File getLoadDirectory() {
    return this.loadDirectory;
  }

  protected abstract Pattern getPattern();

  @Override
  public final void init() {
    if (logger.isDebugEnabled()) {
      logger.debug("Initialisiere {}", this.getClass().getSimpleName());
    }
    this.listFiles();
  }

  private void listFiles() {
    if (this.loadDirectory.isDirectory()) {
      final File[] fileArray = this.loadDirectory.listFiles((File file) -> this
          .matchFiles(file, this.getPattern()));
      if (null != fileArray) {
        this.files = Arrays.asList(fileArray);
      }
    }
  }

  @Override
  public final List<Loadable> load() {
    if (logger.isDebugEnabled()) {
      logger.debug("Lade {}", this.getDescriptiveName());
    }
    final List<File> fileList = this.getFiles();
    final List<Loadable> resultList = new ArrayList<>();
    if (fileList != null) {
      for (final File file : fileList) {
        final Loadable item = this.loopAction(file);
        resultList.add(item);
        this.notifyLoadListeners(this.getDescriptiveName(), item);
      }
    }

    logger.info("{} Dokumente geladen", this.getFileNumber());
    return resultList;
  }

  public abstract Loadable loopAction(File file);

  /**
   * TODO aus dem anderen AbstractLoader kopiert
   *
   * @param file
   * @return
   */
  private boolean matchFiles(File file, Pattern fileNamePattern) {
    try {
      final String filename = file.getCanonicalPath();
      final Matcher matcher = fileNamePattern.matcher(filename);
      if (logger.isDebugEnabled()) {
        logger.debug("{}::{}", filename, matcher.matches());
      }
      return matcher.matches();
    } catch (final IOException e) {
      return false;
    }
  }

  @Override
  public final void notifyLoadListeners(String message, Loadable loadable) {
    for (final LoadListener listener : this.listeners) {
      listener.notifyLoading(message, loadable);
    }
  }
}
