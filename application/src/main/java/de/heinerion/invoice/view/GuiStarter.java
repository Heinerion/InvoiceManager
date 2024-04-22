package de.heinerion.invoice.view;

import de.heinerion.invoice.view.swing.home.ApplicationFrame;
import de.heinerion.invoice.view.swing.laf.LookAndFeelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Flogger
@Service
@RequiredArgsConstructor
public class GuiStarter {
  private final ApplicationFrame applicationFrame;
  private final LookAndFeelUtil lookAndFeel;

  public void showInterface() {
    prepareApplicationFrame(applicationFrame.getFrame());
    lookAndFeel.setLookAndFeel(applicationFrame);
  }

  private void prepareApplicationFrame(JFrame frame) {
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
