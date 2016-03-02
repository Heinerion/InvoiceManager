package de.heinerion.betriebe.gui;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public abstract class AbstractBusyFrame extends JFrame {

  /**
   * Aktiviert die Glasspane und "blockiert" den Frame oder revidiert dieses
   *
   * @param busy Ob der Frame beschäftigt sein soll
   */
  public final void setBusyState(BusyState busy) {
    getGlassPane().setVisible(busy == BusyState.BUSY);
    getJMenuBar().setEnabled(busy == BusyState.IDLE);
  }
}
