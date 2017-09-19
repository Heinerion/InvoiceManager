package de.heinerion.betriebe.view.menu;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
class BusyFrame {
  private JFrame frame;

  private Component glassPanel;
  private JMenuBar menuBar;

  private boolean busy;

  BusyFrame(JFrame frame) {
    this.frame = frame;
  }

  JFrame getFrame() {
    return frame;
  }

  private boolean isBusy() {
    return busy;
  }

  void setBusy(boolean isBusy) {
    busy = isBusy;

    updateComponents();
    setVisibilities();
  }

  private void updateComponents() {
    glassPanel = frame.getGlassPane();
    menuBar = frame.getJMenuBar();
  }

  private void setVisibilities() {
    glassPanel.setVisible(isBusy());
    menuBar.setEnabled(!isBusy());
  }
}
