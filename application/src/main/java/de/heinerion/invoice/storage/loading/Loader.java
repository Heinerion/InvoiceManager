package de.heinerion.invoice.storage.loading;

import lombok.extern.flogger.Flogger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Flogger
abstract class Loader implements LoadListenable {
  private static final String VALID = "File: %s, compatible with %s";
  private static final String INVALID = "File: %s, not compatible with %s";

  private List<File> files;
  private final Set<LoadListener> listeners;
  private final File loadDirectory;

  Loader(File aLoadDirectory) {
    loadDirectory = aLoadDirectory;
    listeners = new HashSet<>();
  }

  @Override
  public final void addListener(LoadListener listener) {
    listeners.add(listener);
  }

  public abstract String getDescriptiveName();

  final int getFileNumber() {
    if (files != null) {
      return files.size();
    }

    return 0;
  }

  private List<File> getFiles() {
    return files == null
        ? Collections.emptyList()
        : files;
  }

  protected abstract Pattern getPattern();

  public final void init() {
    log.atFine().log("initialize %s", getClass().getSimpleName());
    listFiles();
  }

  private void listFiles() {
    log.atFine().log("loadDirectory %s", loadDirectory.getAbsolutePath());
    if (!loadDirectory.isDirectory()) {
      log.atInfo().log("%s is no directory", loadDirectory.getAbsolutePath());
      return;
    }

    File[] fileArray = loadDirectory.listFiles((File file) -> matchFiles(file, getPattern()));
    if (null != fileArray) {
      files = Arrays.asList(fileArray);
    }
    log.atFine().log("%d documents found", getFileNumber());
  }

  public final List<Loadable> load() {
    log.atFine().log("load %s.%nFiles found: %s", getDescriptiveName(), getFiles());

    List<Loadable> resultList = getFiles().stream()
        .map(this::loopAction)
        .map(this::notify)
        .toList();

    log.atInfo().log("%d documents loaded (%s)", getFileNumber(), getDescriptiveName());
    return resultList;
  }

  private Loadable notify(Loadable loadable) {
    notifyLoadListeners(getDescriptiveName(), loadable);
    return loadable;
  }

  abstract Loadable loopAction(File file);

  private boolean matchFiles(File file, Pattern fileNamePattern) {
    try {
      String filename = file.getCanonicalPath();
      Matcher matcher = fileNamePattern.matcher(filename);

      boolean result = matcher.matches();

      if (result) {
        log.atFine().log(VALID, filename, getClass().getSimpleName());
      } else {
        log.atInfo().log(INVALID, filename, getClass().getSimpleName());
      }

      return result;
    } catch (IOException e) {
      log.atSevere().withCause(e).log("could not match files");
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
