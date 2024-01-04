package de.heinerion.invoice.view.swing.home.receiver.forms;

import javax.swing.*;
import java.util.Optional;

public interface Form<T> {
  Optional<T> getValue();

  JPanel getPanel();
}