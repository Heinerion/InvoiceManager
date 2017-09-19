package de.heinerion.betriebe.view.latex;

import de.heinerion.betriebe.models.Letter;

public class LatexGeneratorMock implements LatexGenerator {
  @Override
  public String generateSourceContent(Letter letter) {
    return "A Letter";
  }
}
