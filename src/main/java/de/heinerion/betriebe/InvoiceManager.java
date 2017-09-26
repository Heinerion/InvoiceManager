package de.heinerion.betriebe;

import de.heinerion.aspects.annotations.LogBefore;
import de.heinerion.aspects.annotations.LogMethod;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.loading.IO;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.util.LookAndFeelUtil;
import de.heinerion.betriebe.view.panels.ApplicationFrame;
import de.heinerion.betriebe.view.panels.PanelFactory;
import de.heinerion.betriebe.view.panels.ProgressIndicator;
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

  private final ApplicationFrame applicationFrame;
  private IO io;

  @Autowired
  InvoiceManager(IO io, ApplicationFrame applicationFrame) {
    this.io = io;
    this.applicationFrame = applicationFrame;

    DataBase.setIo(io);
  }

  @LogMethod
  public static void main(String... args) {
    LookAndFeelUtil.setNimbus();
    InvoiceManager application = ConfigurationService.getBean(InvoiceManager.class);

    application.invoke(args);
  }

  void invoke(String[] args) {
    parseArguments(args);
    setup();
    start();
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
  }

  @LogBefore
  private void start() {
    prepareApplicationFrame(applicationFrame.getFrame());
    startDataThread(applicationFrame);
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
    ProgressIndicator progress = PanelFactory.getProgressIndicator(applicationFrame.getProgressBar());

    // registerListenersAndLoaders needs to be called explicitly here
    // instead of in its constructor to avoid multiple registrations
    io.registerListenersAndLoaders();
    io.load(progress);
    applicationFrame.refresh();

    waitASecond();
    // only needed for testing.
    // In tests the applicationFrame is disposed before {@code waitASecond} returns
    if (progress != null)
    {
      progress.setEnabled(false);
    }
  }

  private ProgressIndicator getProgressBarIndicator(ApplicationFrame applicationFrame) {
    return PanelFactory.getProgressIndicator(applicationFrame.getProgressBar());
  }

  private void waitASecond() {
    try {
      Thread.sleep(ONE_SECOND);
    } catch (final InterruptedException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(e);
      }
      throw new ThreadWaitException(e);
    }
  }

  private static class ThreadWaitException extends RuntimeException {
    ThreadWaitException(Throwable t) {
      super(t);
    }
  }
}
