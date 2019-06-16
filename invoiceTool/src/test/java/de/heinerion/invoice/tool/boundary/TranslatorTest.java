package de.heinerion.invoice.tool.boundary;

import org.junit.Test;

import static org.junit.Assert.*;

public class TranslatorTest {

  @Test
  public void translate() {
    assertEquals("foo", Translator.translate("bar"));
  }

  @Test
  public void translate_returnsUnknownKeys() {
    String baz = Translator.translate("baz");
    assertEquals("'baz'", baz);
  }
}