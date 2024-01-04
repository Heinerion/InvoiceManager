package de.heinerion.invoice.boundary;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TranslatorTest {

  @Test
  void translate() {
    assertEquals("foo", Translator.translate("bar"));
  }

  @Test
  void translate_returnsUnknownKeys() {
    String baz = Translator.translate("baz");
    assertEquals("'baz'", baz);
  }
}