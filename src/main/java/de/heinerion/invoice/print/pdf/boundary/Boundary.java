package de.heinerion.invoice.print.pdf.boundary;

import de.heinerion.invoice.Translator;

class Boundary {
  private Boundary() {
  }

  /**
   * delivers the translation of the given key from this packages properties
   *
   * @param key the translation key as found in {@code Boundary.properties}
   * @return the translation of the given key or, if no translation is found, the key in single quotes
   * @see Translator#translate(Class, String)
   */
  public static String translate(String key) {
    return Translator.translate(Boundary.class, key);
  }
}
