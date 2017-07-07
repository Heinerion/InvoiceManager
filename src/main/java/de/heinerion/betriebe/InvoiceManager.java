/**
 * Main.java
 * heiner 27.03.2012
 */
package de.heinerion.betriebe;

import de.heinerion.aspects.annotations.LogBefore;
import de.heinerion.aspects.annotations.LogMethod;
import de.heinerion.betriebe.data.System;
import de.heinerion.exceptions.HeinerionException;
import de.heinerion.betriebe.fileoperations.IO;
import de.heinerion.betriebe.fileoperations.loading.JProgressBarIndicator;
import de.heinerion.betriebe.fileoperations.loading.ProgressIndicator;
import de.heinerion.betriebe.gui.ApplicationFrame;
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

  @LogMethod
  public static void main(String... args) {
    parseArguments(args);
    setup();
    start();
  }

  private static void parseArguments(String... args) {
    for (String argument : args) {
      evaluateArgument(argument);
    }

    if (!System.isDebugMode()) {
      LOGGER.warn("PRODUCTION MODE");
    }
  }

  private static void evaluateArgument(String string) {
    switch (string) {
      case "debug":
        System.activateDebugMode();
        break;
    }
  }

  @LogBefore
  private static void setup() {
    LookAndFeelUtil.setNimbus();
  }

  @LogBefore
  private static void start() {
    final ApplicationFrame applicationFrame = getApplicationFrame();

    prepareApplicationFrame(applicationFrame);
    startDataThread(applicationFrame);
  }

  private static ApplicationFrame getApplicationFrame() {
    return ApplicationFrame.getInstance();
  }

  private static void prepareApplicationFrame(ApplicationFrame applicationFrame) {
    applicationFrame.setLocationRelativeTo(null);
    applicationFrame.setVisible(true);
  }

  @LogBefore
  private static void startDataThread(ApplicationFrame applicationFrame) {
    new Thread(() -> collectData(applicationFrame)).start();
  }

  private static void collectData(ApplicationFrame applicationFrame) {
    ProgressIndicator progress = getProgressBarIndicator(applicationFrame);

    IO.load(progress);
    applicationFrame.refresh();

    waitASecond();
    progress.setEnabled(false);
  }

  private static JProgressBarIndicator getProgressBarIndicator(ApplicationFrame applicationFrame) {
    return new JProgressBarIndicator(applicationFrame.getProgressBar());
  }

  private static void waitASecond() {
    try {
      Thread.sleep(ONE_SECOND);
    } catch (final InterruptedException e) {
      HeinerionException.handleException(InvoiceManager.class, e);
    }
  }
}
