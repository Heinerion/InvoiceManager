package de.heinerion.latex;

import de.heinerion.betriebe.models.Letter;

public interface LatexGenerator {
  String generateSourceContent(Letter letter);
}
