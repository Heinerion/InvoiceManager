package de.heinerion.betriebe.view.panels;

import de.heinerion.betriebe.models.Letter;

import javax.swing.*;

public interface TabContent {
  Letter getContent();

  String getTitle();

  JPanel getPanel();

  void refresh();
}
