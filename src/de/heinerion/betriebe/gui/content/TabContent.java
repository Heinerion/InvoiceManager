package de.heinerion.betriebe.gui.content;

import de.heinerion.betriebe.models.interfaces.Conveyable;

public interface TabContent {
  Conveyable getContent();

  String getTitle();

  void refresh();
}
