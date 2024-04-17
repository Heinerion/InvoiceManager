package de.heinerion.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StringsTest {
  @Test
  void isBlank_null() {
    assertTrue(Strings.isBlank(null));
  }

  @Test
  void isBlank_empty() {
    assertTrue(Strings.isBlank(""));
  }

  @Test
  void isBlank_singleWhitespace() {
    assertTrue(Strings.isBlank(" "));
  }

  @Test
  void isBlank_multipleWhitespaces() {
    assertTrue(Strings.isBlank(" \t"));
  }

  @Test
  void isBlank_lineBreaks() {
    assertTrue(Strings.isBlank("\n"));
    assertTrue(Strings.isBlank("\r"));
    assertTrue(Strings.isBlank("\r\n"));
  }
}