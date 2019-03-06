package de.heinerion.invoice.view.swing;

import de.heinerion.invoice.view.common.StatusComponent;

import javax.swing.*;

public interface ApplicationFrame {
  JFrame getFrame();

  void refresh();

  StatusComponent getStatusComponent();
}
