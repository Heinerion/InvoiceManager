package de.heinerion.invoice.repositories.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Flogger
@RequiredArgsConstructor
abstract class Loader<T> {
  private static final String VALID = "File: %s, compatible with %s";
  private static final String INVALID = "File: %s, not compatible with %s";

  private List<Path> files;
  private final Path loadDirectory;

  public abstract String getDescriptiveName();

  final int getFileNumber() {
    if (files != null) {
      return files.size();
    }

    return 0;
  }

  private List<Path> getFiles() {
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
    log.atFine().log("loadDirectory %s", loadDirectory.toAbsolutePath());
    if (!Files.isDirectory(loadDirectory)) {
      log.atInfo().log("%s is no directory", loadDirectory.toAbsolutePath());
      return;
    }

    try (Stream<Path> filesInDirectory = Files.list(loadDirectory)) {
      files = filesInDirectory
          .filter(file -> matchFiles(file, getPattern()))
          .toList();
      log.atFine().log("%d documents found", getFileNumber());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public final List<T> load() {
    log.atFine().log("load %s.%nFiles found: %s", getDescriptiveName(), getFiles());

    List<T> resultList = getFiles().stream()
        .map(this::loopAction)
        .toList();

    log.atInfo().log("%d documents loaded (%s)", getFileNumber(), getDescriptiveName());
    return resultList;
  }

  abstract T loopAction(Path file);

  private boolean matchFiles(Path file, Pattern fileNamePattern) {
    String filename = file.toAbsolutePath().toString();
    Matcher matcher = fileNamePattern.matcher(filename);

    boolean result = matcher.matches();

    if (result) {
      log.atFine().log(VALID, filename, getClass().getSimpleName());
    } else {
      log.atInfo().log(INVALID, filename, getClass().getSimpleName());
    }

    return result;
  }
}
