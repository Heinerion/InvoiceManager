package de.heinerion.betriebe.view.swing;

import de.heinerion.betriebe.view.common.StatusComponent;

import javax.swing.*;

public interface ApplicationFrame {
  JFrame getFrame();

  void refresh();

  StatusComponent<JPanel> getStatusComponent();
}
