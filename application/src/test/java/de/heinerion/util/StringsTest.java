package de.heinerion.util;

import org.junit.jupiter.api.*;

import static de.heinerion.util.Strings.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StringsTest {
  @Test
  @DisplayName("isBlank: Should accept null")
  public void isBlankShouldAcceptNull() {
    assertTrue(isBlank(null));
  }

  @Test
  @DisplayName("isBlank: Should accept the empty String")
  public void isBlankShouldAcceptTheEmptyString() {
    assertTrue(isBlank(""));
  }

  @Test
  @DisplayName("isBlank: Should accept whitespaces")
  public void isBlankShouldAcceptWhitespaces() {
    assertTrue(isBlank(" "));
    assertTrue(isBlank(" \t"));
  }

  @Test
  @DisplayName("isBlank: Should accept line endings")
  public void isBlankShouldAcceptLineEndings() {
    assertTrue(isBlank("\n"));
    assertTrue(isBlank("\r"));
    assertTrue(isBlank("\r\n"));
  }

  @Test
  @DisplayName("isBlank: Should reject any alphanumerics")
  public void isBlankShouldRejectAnyAlphanumerics() {
    assertFalse(isBlank("0"));
    assertFalse(isBlank("42"));
    assertFalse(isBlank("foo"));
  }

  @Test
  @DisplayName("isNotBlank: Should reject null")
  public void isNotBlankShouldRejectNull() {
    assertFalse(isNotBlank(null));
  }

  @Test
  @DisplayName("isNotBlank: Should reject the empty String")
  public void isNotBlankShouldRejectTheEmptyString() {
    assertFalse(isNotBlank(""));
  }

  @Test
  @DisplayName("isNotBlank: Should reject whitespaces")
  public void isNotBlankShouldRejectWhitespaces() {
    assertFalse(isNotBlank(" "));
    assertFalse(isNotBlank(" \t"));
  }

  @Test
  @DisplayName("isNotBlank: Should reject line endings")
  public void isNotBlankShouldRejectLineEndings() {
    assertFalse(isNotBlank("\n"));
    assertFalse(isNotBlank("\r"));
    assertFalse(isNotBlank("\r\n"));
  }

  @Test
  @DisplayName("isNotBlank: should accept alphanumerics")
  public void isNotBlankShouldAcceptAlphanumerics() {
    assertTrue(isNotBlank("0"));
    assertTrue(isNotBlank("42"));
    assertTrue(isNotBlank("foo"));
  }
}