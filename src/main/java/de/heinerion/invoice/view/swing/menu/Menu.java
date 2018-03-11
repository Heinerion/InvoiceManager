package de.heinerion.invoice.view.swing.menu;

import de.heinerion.util.Translator;

public class Menu {
  private Menu(){}

  public static String translate(String key) {
    return Translator.translate(Menu.class, key);
  }
}
