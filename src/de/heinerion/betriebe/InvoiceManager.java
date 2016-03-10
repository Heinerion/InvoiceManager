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
import de.heinerion.betriebe.tools.LookAndFeelUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author heiner
 */
final class InvoiceManager {
  private static final Logger LOGGER = LogManager.getLogger(InvoiceManager.class);

  private static final long ONE_SECOND = 1000L;

  private InvoiceManager() {
  }

  public static void main(String... args) {
    parseArguments(args);
    setup();
    start();
  }

  private static void parseArguments(String... args) {
    boolean debug = false;
    for (String string : args) {
      if (string.contains("debug")) {
        debug = true;
      }
    }

    Utilities.setDebugMode(debug);

    if (LOGGER.isWarnEnabled() && !Utilities.isDebugMode()) {
      LOGGER.warn("\n\tPRODUCTION MODE");
    }
  }

  private static void setup() {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Setting up application.");
    }

    LookAndFeelUtil.setNimbus();
  }

  private static void start() {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Entering application.");
    }

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
      HeinerionException.handleException(InvoiceManager.class, e);
    }
  }
}
