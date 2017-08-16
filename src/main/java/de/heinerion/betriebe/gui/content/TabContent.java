package de.heinerion.betriebe.gui.content;

import de.heinerion.betriebe.models.Letter;

public interface TabContent {
  Letter getContent();

  String getTitle();

  void refresh();
}
