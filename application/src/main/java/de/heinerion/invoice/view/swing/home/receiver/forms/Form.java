package de.heinerion.invoice.view.swing.home.receiver.forms;

import javax.swing.*;

public interface Form<T> {
  T getValue();

  JPanel getPanel();
}