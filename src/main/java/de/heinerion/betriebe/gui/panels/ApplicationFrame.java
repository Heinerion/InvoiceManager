package de.heinerion.betriebe.gui.panels;

import javax.swing.*;

public interface ApplicationFrame {
  JFrame getFrame();

  void refresh();

  JProgressBar getProgressBar();
}
