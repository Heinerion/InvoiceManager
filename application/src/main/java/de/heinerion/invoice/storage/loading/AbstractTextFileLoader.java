package de.heinerion.invoice.storage.loading;

import lombok.extern.flogger.Flogger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Flogger
public abstract class AbstractTextFileLoader extends Loader {
  private final Reader reader = new Reader();

  protected AbstractTextFileLoader(File aLoadDirectory) {
    super(aLoadDirectory);
  }

  @Override
  protected abstract Pattern getPattern();

  @Override
  public final Loadable loopAction(final File file) {
    final Map<String, String> attributes = this.readAttributes(file);
    final Loadable data = this.parse(attributes);

    log.atFine().log("add(%s)", data);

    return data;
  }

  protected abstract Loadable parse(Map<String, String> attributes);

  private Map<String, String> readAttributes(File path) {
    final Map<String, String> attributes = new HashMap<>();

    try {
      this.readAttributesFromFile(path.getCanonicalPath(), attributes);
    } catch (final IOException e) {
      throw new ParseAttributeException(e);
    }

    return attributes;
  }

  private void readAttributesFromFile(String path, Map<String, String> attributes) throws IOException {
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
