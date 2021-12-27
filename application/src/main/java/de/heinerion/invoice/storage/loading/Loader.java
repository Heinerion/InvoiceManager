package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.repositories.AddressRepository;
import lombok.extern.flogger.Flogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Flogger
abstract class Loader implements LoadListenable {
  private static final String VALID = "File: %s, compatible with %s";
  private static final String INVALID = "File: %s, not compatible with %s";

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
    log.atFine().log("initialize %s", getClass().getSimpleName());
    listFiles();
  }

  private void listFiles() {
    log.atFine().log("loadDirectory %s", loadDirectory.getAbsolutePath());
    if (loadDirectory.isDirectory()) {
      File[] fileArray = loadDirectory.listFiles((File file) -> matchFiles(file, getPattern()));
      if (null != fileArray) {
        files = Arrays.asList(fileArray);
      }
    }
    log.atFine().log("%d documents found", getFileNumber());
  }

  public final List<Loadable> load(AddressRepository addressRepository) {
    log.atFine().log("load %s", getDescriptiveName());
    List<File> fileList = getFiles();
    log.atFine().log("files found: %s", fileList);
    List<Loadable> resultList = new ArrayList<>();

    for (File file : fileList) {
      Loadable item = loopAction(file, addressRepository);
      resultList.add(item);
      notifyLoadListeners(getDescriptiveName(), item);
    }

    log.atInfo().log("%d documents loaded (%s)", getFileNumber(), getDescriptiveName());

    return resultList;
  }

  abstract Loadable loopAction(File file, AddressRepository addressRepository);

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
