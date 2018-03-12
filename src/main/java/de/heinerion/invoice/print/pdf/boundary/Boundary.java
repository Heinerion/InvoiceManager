package de.heinerion.invoice.print.pdf.boundary;

import de.heinerion.invoice.Translator;

public class Boundary {
  private Boundary() {
  }

  public static String translate(String key) {
    return Translator.translate(Boundary.class, key);
  }
}
