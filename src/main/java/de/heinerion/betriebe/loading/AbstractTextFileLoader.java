package de.heinerion.betriebe.loading;

import de.heinerion.betriebe.HeinerionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

abstract class AbstractTextFileLoader extends AbstractLoader {
  private static final Logger logger = LogManager.getLogger(AbstractTextFileLoader.class);

  private final Reader reader = new TextFileReader();

  AbstractTextFileLoader(File aLoadDirectory) {
    super(aLoadDirectory);
  }

  @Override
  protected abstract Pattern getPattern();

  @Override
  public final Loadable loopAction(final File file) {
    final Map<String, String> attributes = this.readAttributes(file);
    final Loadable data = this.parse(attributes);

    if (logger.isDebugEnabled()) {
      logger.debug("add({})", data);
    }

    return data;
  }

  protected abstract Loadable parse(Map<String, String> attributes);

  private Map<String, String> readAttributes(File path) {
    final Map<String, String> attributes = new HashMap<>();

    try {
      this.readAttributesFromFile(path.getCanonicalPath(), attributes);
    } catch (final IOException e) {
      HeinerionException.rethrow(e);
    }

    return attributes;
  }

  /**
   * @param path
   * @param attributes
   * @throws IOException
   */
  private void readAttributesFromFile(String path,
                                      Map<String, String> attributes) throws IOException {
    this.reader.prepareFile(path);

    Map<String, String> current;
    while (true) {
      current = this.reader.read();

      if (current != null) {
        current.forEach(attributes::put);
      } else {
        break;
      }
    }

    this.reader.closeFile();
  }
}
