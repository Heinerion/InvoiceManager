package de.heinerion.invoice.view.swing.menu.info;

import de.heinerion.invoice.Translator;

/**
 * This class is used to derive the resource bundle of the same name
 * <p>
 * For convenience, this class offers a {@link #translate(String, Object...)} method, preconfigured to use the before
 * mentioned resource bundle
 * </p>
 */
class Info {
  private Info(){}

  public static String translate(String key, Object ... arguments) {
    return Translator.translate(Info.class, key, arguments);
  }
}
