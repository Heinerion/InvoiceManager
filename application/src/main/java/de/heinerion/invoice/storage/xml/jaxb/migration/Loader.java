package de.heinerion.invoice.storage.xml.jaxb.migration;

import lombok.extern.flogger.Flogger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Flogger
abstract class Loader<T> {
  private static final String VALID = "File: %s, compatible with %s";
  private static final String INVALID = "File: %s, not compatible with %s";

  private List<File> files;
  private final File loadDirectory;

  Loader(File aLoadDirectory) {
    loadDirectory = aLoadDirectory;
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

  public final List<T> load() {
    log.atFine().log("load %s.%nFiles found: %s", getDescriptiveName(), getFiles());

    List<T> resultList = getFiles().stream()
        .map(this::loopAction)
        .toList();

    log.atInfo().log("%d documents loaded (%s)", getFileNumber(), getDescriptiveName());
    return resultList;
  }

  abstract T loopAction(File file);

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

  private static class MatchFilesException extends RuntimeException {
    MatchFilesException(Throwable cause) {
      super(cause);
    }
  }
}
