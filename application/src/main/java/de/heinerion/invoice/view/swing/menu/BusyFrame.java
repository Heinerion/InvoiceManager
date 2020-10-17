package de.heinerion.invoice.view.swing.menu;

import javax.swing.*;
import java.awt.*;

public class BusyFrame {
  private JFrame frame;

  private Component glassPanel;
  private JMenuBar menuBar;

  private boolean busy;

  public BusyFrame(JFrame frame) {
    this.frame = frame;
  }

  public JFrame getFrame() {
    return frame;
  }

  private boolean isBusy() {
    return busy;
  }

  public void setBusy(boolean isBusy) {
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
