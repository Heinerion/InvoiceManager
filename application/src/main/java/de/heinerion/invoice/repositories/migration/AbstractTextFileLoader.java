package de.heinerion.invoice.repositories.migration;

import lombok.extern.flogger.Flogger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Flogger
public abstract class AbstractTextFileLoader<T> extends Loader<T> {
  private final Reader reader = new Reader();

  protected AbstractTextFileLoader(Path aLoadDirectory) {
    super(aLoadDirectory);
  }

  @Override
  protected abstract Pattern getPattern();

  @Override
  public final T loopAction(Path file) {
    final Map<String, String> attributes = this.readAttributes(file);
    final T data = this.parse(attributes);

    log.atFine().log("add(%s)", data);

    return data;
  }

  protected abstract T parse(Map<String, String> attributes);

  private Map<String, String> readAttributes(Path path) {
    final Map<String, String> attributes = new HashMap<>();

    try {
      this.readAttributesFromFile(path, attributes);
    } catch (final IOException e) {
      throw new ParseAttributeException(e);
    }

    return attributes;
  }

  private void readAttributesFromFile(Path path, Map<String, String> attributes) throws IOException {
    this.reader.prepareFile(path);

    Map<String, String> current;
    while (true) {
      current = this.reader.read();

      if (current != null) {
        attributes.putAll(current);
      } else {
        break;
      }
    }

    this.reader.closeFile();
  }

  private static class ParseAttributeException extends RuntimeException {
    ParseAttributeException(Throwable cause) {
      super(cause);
    }
  }
}
