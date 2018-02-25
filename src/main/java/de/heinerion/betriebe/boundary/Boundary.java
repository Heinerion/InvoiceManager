package de.heinerion.betriebe.boundary;

import de.heinerion.util.Translator;

public class Boundary {
  private Boundary() {
  }

  public static String translate(String key) {
    return Translator.translate(Boundary.class, key);
  }
}
