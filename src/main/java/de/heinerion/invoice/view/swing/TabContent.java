package de.heinerion.invoice.view.swing;

import de.heinerion.betriebe.models.Letter;

import javax.swing.*;

public interface TabContent {
  Letter getContent();

  String getTitle();

  JPanel getPanel();

  void refresh();
}
