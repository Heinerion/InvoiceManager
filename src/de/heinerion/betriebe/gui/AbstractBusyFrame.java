package de.heinerion.betriebe.gui;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public abstract class AbstractBusyFrame extends JFrame {
  private Component glassPanel;
  private JMenuBar menuBar;

  /**
   * Aktiviert die Glasspane und "blockiert" den Frame oder revidiert dieses
   *
   * @param busy Ob der Frame beschäftigt sein soll
   */
  public final void setBusyState(BusyState busy) {
    updateComponents();
    setVisibilities(busy);
  }

  private void updateComponents() {
    glassPanel = getGlassPane();
    menuBar = getJMenuBar();
  }

  private void setVisibilities(BusyState busy) {
    glassPanel.setVisible(busy == BusyState.BUSY);
    menuBar.setEnabled(busy == BusyState.IDLE);
  }
}
