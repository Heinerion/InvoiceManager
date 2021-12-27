package de.heinerion.invoice.view;

import de.heinerion.invoice.view.swing.ApplicationFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Flogger
@Service
@RequiredArgsConstructor
public class GuiStarter {
  private final ApplicationFrame applicationFrame;

  public void showInterface() {
    prepareApplicationFrame(applicationFrame.getFrame());
    applicationFrame.refresh();
  }

  private void prepareApplicationFrame(JFrame frame) {
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
