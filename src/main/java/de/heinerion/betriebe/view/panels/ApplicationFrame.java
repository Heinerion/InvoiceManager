package de.heinerion.betriebe.view.panels;

import javax.swing.*;

public interface ApplicationFrame {
  JFrame getFrame();

  void refresh();

  JProgressBar getProgressBar();
}
