package de.heinerion.latex;

import de.heinerion.betriebe.models.Letter;

public class LatexGeneratorMock extends LatexGenerator {
  @Override
  public String generateSourceContent(Letter letter) {
    return "A Letter";
  }
}
