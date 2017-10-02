package de.heinerion.betriebe.view.swing;

import javax.swing.*;

public interface ApplicationFrame {
  JFrame getFrame();

  void refresh();

  JProgressBar getProgressBar();
}
