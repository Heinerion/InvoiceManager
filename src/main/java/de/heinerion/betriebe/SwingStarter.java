package de.heinerion.betriebe;

import de.heinerion.aspects.annotations.LogBefore;
import de.heinerion.betriebe.loading.IO;
import de.heinerion.betriebe.view.panels.ApplicationFrame;
import de.heinerion.betriebe.view.panels.PanelFactory;
import de.heinerion.betriebe.view.panels.ProgressIndicator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

class SwingStarter implements GuiStarter {
  private static final Logger LOGGER = LogManager.getLogger(SwingStarter.class);

  private static final long ONE_SECOND = 1000L;

  private final ApplicationFrame applicationFrame;
  private final IO io;

  SwingStarter(ApplicationFrame applicationFrame, IO io) {
    this.io = io;
    this.applicationFrame = applicationFrame;
  }

  @Override
  public void showInterface() {
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
    if (progress != null) {
      progress.setEnabled(false);
    }
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
