package de.heinerion.betriebe;

import de.heinerion.aspects.annotations.LogBefore;
import de.heinerion.aspects.annotations.LogMethod;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.loading.IO;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.betriebe.util.LookAndFeelUtil;
import de.heinerion.betriebe.view.panels.ApplicationFrame;
import de.heinerion.betriebe.view.panels.PanelFactory;
import de.heinerion.betriebe.view.panels.ProgressIndicator;
import de.heinerion.exceptions.HeinerionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;

/**
 * @author heiner
 */
final class InvoiceManager {
  private static final Logger LOGGER = LogManager.getLogger(InvoiceManager.class);

  private static final long ONE_SECOND = 1000L;

  private IO io;

  @Autowired
  private InvoiceManager(IO io) {
    this.io = io;
  }

  @LogMethod
  public static void main(String... args) {
    InvoiceManager application = ConfigurationService.getBean(InvoiceManager.class);

    application.parseArguments(args);
    application.setup();
    application.start();
  }

  private void parseArguments(String... args) {
    Session.isDebugMode(false);

    for (String argument : args) {
      evaluateArgument(argument);
    }

    if (!Session.isDebugMode()) {
      LOGGER.warn("PRODUCTION MODE");
    }
  }

  private void evaluateArgument(String string) {
    switch (string) {
      case "debug":
        Session.isDebugMode(true);
        break;
    }
  }

  @LogBefore
  private void setup() {
    LookAndFeelUtil.setNimbus();
  }

  @LogBefore
  private void start() {
    prepareApplicationFrame(Session.getFrame());
    startDataThread(Session.getApplicationFrame());
  }

  private void prepareApplicationFrame(JFrame frame) {
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  @LogBefore
  private void startDataThread(ApplicationFrame applicationFrame) {
    new Thread(() -> collectData(applicationFrame)).start();
  }

  private void collectData(ApplicationFrame applicationFrame) {
    ProgressIndicator progress = getProgressBarIndicator(applicationFrame);

    // registerListenersAndLoaders needs to be called explicitly here
    // instead of in its constructor to avoid multiple registrations
    io.registerListenersAndLoaders();
    io.load(progress);
    applicationFrame.refresh();

    waitASecond();
    progress.setEnabled(false);
  }

  private ProgressIndicator getProgressBarIndicator(ApplicationFrame applicationFrame) {
    return PanelFactory.getProgressIndicator(applicationFrame.getProgressBar());
  }

  private void waitASecond() {
    try {
      Thread.sleep(ONE_SECOND);
    } catch (final InterruptedException e) {
      HeinerionException.handleException(InvoiceManager.class, e);
    }
  }
}
