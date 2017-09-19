package de.heinerion.betriebe.gui.latex;

import de.heinerion.betriebe.models.Letter;

public interface LatexGenerator {
  String generateSourceContent(Letter letter);
}
