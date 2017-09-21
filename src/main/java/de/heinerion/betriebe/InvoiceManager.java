package de.heinerion.betriebe;

import de.heinerion.aspects.annotations.LogBefore;
import de.heinerion.aspects.annotations.LogMethod;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.loading.IO;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.betriebe.view.panels.ApplicationFrame;
import de.heinerion.betriebe.view.panels.PanelFactory;
import de.heinerion.betriebe.view.panels.ProgressIndicator;
import de.heinerion.betriebe.util.LookAndFeelUtil;
import de.heinerion.exceptions.HeinerionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

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
    Session.isDebugMode(false);

    for (String argument : args) {
      evaluateArgument(argument);
    }

    if (!Session.isDebugMode()) {
      LOGGER.warn("PRODUCTION MODE");
    }
  }

  private static void evaluateArgument(String string) {
    switch (string) {
      case "debug":
        Session.isDebugMode(true);
        break;
    }
  }

  @LogBefore
  private static void setup() {
    LookAndFeelUtil.setNimbus();
  }

  @LogBefore
  private static void start() {
    prepareApplicationFrame(Session.getFrame());
    startDataThread(Session.getApplicationFrame());
  }

  private static void prepareApplicationFrame(JFrame frame) {
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  @LogBefore
  private static void startDataThread(ApplicationFrame applicationFrame) {
    new Thread(() -> collectData(applicationFrame)).start();
  }

  private static void collectData(ApplicationFrame applicationFrame) {
    ProgressIndicator progress = getProgressBarIndicator(applicationFrame);

    // TODO pass to constructor
    IO io = ConfigurationService.getBean(IO.class);
    // registerListenersAndLoaders needs to be called explicitly here
    // instead of in its constructor to avoid multiple registrations
    io.registerListenersAndLoaders();
    io.load(progress);
    applicationFrame.refresh();

    waitASecond();
    progress.setEnabled(false);
  }

  private static ProgressIndicator getProgressBarIndicator(ApplicationFrame applicationFrame) {
    return PanelFactory.getProgressIndicator(applicationFrame.getProgressBar());
  }

  private static void waitASecond() {
    try {
      Thread.sleep(ONE_SECOND);
    } catch (final InterruptedException e) {
      HeinerionException.handleException(InvoiceManager.class, e);
    }
  }
}
