/**
 * Main.java
 * heiner 27.03.2012
 */
package de.heinerion.betriebe;

import de.heinerion.betriebe.classes.file_operations.IO;
import de.heinerion.betriebe.classes.file_operations.loading.JProgressBarIndicator;
import de.heinerion.betriebe.classes.file_operations.loading.ProgressIndicator;
import de.heinerion.betriebe.classes.gui.RechnungFrame;
import de.heinerion.betriebe.enums.Utilities;
import de.heinerion.betriebe.exceptions.HeinerionException;
import de.heinerion.betriebe.tools.gui.LookAndFeel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author heiner
 */
final class BetriebeOriginal {
  private static final Logger logger = LogManager.getLogger(BetriebeOriginal.class);

  private static final long ONE_SECOND = 1000L;

  private BetriebeOriginal() {
  }

  public static void main(String... args) {
    setDebugMode(args);

    if (logger.isInfoEnabled()) {
      logger.info("Setting up application.");
    }
    setup();

    if (logger.isInfoEnabled()) {
      logger.info("Entering application.");
    }
    start();
  }

  private static void setDebugMode(String... args) {
    for (String string : args) {
      if (string.contains("debug")) {
        Utilities.setDebugMode(true);
      }
    }
    if (logger.isWarnEnabled() && !Utilities.isDebugMode()) {
      logger.warn("\n\tPRODUCTION MODE");
    }
  }

  private static void setup() {
    LookAndFeel.setNimbus();
  }

  private static void start() {
    final RechnungFrame rechnungFrame = RechnungFrame.getInstance();

    rechnungFrame.setLocationRelativeTo(null);
    rechnungFrame.setVisible(true);

    startDataThread(rechnungFrame);
  }

  private static void startDataThread(RechnungFrame rechnungFrame) {
    new Thread(() -> collectData(rechnungFrame)).start();
  }

  private static void collectData(RechnungFrame rechnungFrame) {
    ProgressIndicator progress = getProgressBarIndicator(rechnungFrame);

    IO.load(progress);
    rechnungFrame.refresh();

    waitASecond();
    progress.setEnabled(false);
  }

  private static JProgressBarIndicator getProgressBarIndicator(RechnungFrame rechnungFrame) {
    return new JProgressBarIndicator(rechnungFrame.getProgressBar());
  }

  private static void waitASecond() {
    try {
      Thread.sleep(ONE_SECOND);
    } catch (final InterruptedException e) {
      HeinerionException.handleException(BetriebeOriginal.class, e);
    }
  }
}
