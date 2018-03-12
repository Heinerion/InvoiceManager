package de.heinerion.invoice.view.swing.menu.info;

import de.heinerion.invoice.Translator;

class Info {
  private Info(){}

  public static String translate(String key) {
    return Translator.translate(Info.class, key);
  }
}
