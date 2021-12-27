package de.heinerion.invoice.view.swing.menu;

import javax.swing.*;

public record BusyFrame(JFrame frame) {
  public void setBusy(boolean isBusy) {
    frame.getGlassPane().setVisible(isBusy);
    frame.getJMenuBar().setEnabled(!isBusy);
  }
}
