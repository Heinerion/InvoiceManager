package de.heinerion.invoice.view;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.invoice.view.common.StatusComponent;
import de.heinerion.invoice.view.swing.ApplicationFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Flogger
@Service
@RequiredArgsConstructor
public class GuiStarter {
  private static final long ONE_SECOND = 1000L;

  private final ApplicationFrame applicationFrame;
  private final DataBase dataBase;

  public void showInterface() {
    prepareApplicationFrame(applicationFrame.getFrame());
    startDataThread(applicationFrame);
  }

  private void prepareApplicationFrame(JFrame frame) {
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private void startDataThread(ApplicationFrame applicationFrame) {
    new Thread(() -> collectData(applicationFrame)).start();
  }

  private void collectData(ApplicationFrame applicationFrame) {
    StatusComponent progress = applicationFrame.getStatusComponent();

    dataBase.load(progress);

    applicationFrame.refresh();

    waitASecond();
    // only needed for testing.
    // In tests the applicationFrame is disposed before {@code waitASecond} returns
    if (progress != null) {
      progress.disposeProgress();
    }
  }

  private void waitASecond() {
    try {
      Thread.sleep(ONE_SECOND);
    } catch (final InterruptedException e) {
      log.atSevere().withCause(e).log("could not wait");
      Thread.currentThread().interrupt();
    }
  }
}
