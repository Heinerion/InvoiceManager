package de.heinerion.invoice.view.swing.menu;

import de.heinerion.invoice.Translator;

/**
 * This class is used to derive the resource bundle of the same name
 * <p>
 * For convenience, this class offers a {@link #translate(String, Object...)} method, preconfigured to use the before
 * mentioned resource bundle
 * </p>
 */
public class Menu {
  private Menu() {
  }

  public static String translate(String key, Object... arguments) {
    return Translator.translate(Menu.class, key, arguments);
  }
}
