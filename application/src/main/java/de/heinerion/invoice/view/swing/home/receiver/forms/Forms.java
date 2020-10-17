package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.invoice.Translator;

public class Forms {
  private Forms() {
    
  }

  public static String translate(String key, Object... arguments) {
    return Translator.translate(Forms.class, key, arguments);
  }
}
