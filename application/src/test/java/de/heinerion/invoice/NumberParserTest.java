package de.heinerion.invoice;

import org.junit.jupiter.api.*;

import java.util.OptionalDouble;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class NumberParserTest {
  @Test
  void parseDouble_onlyLetters() {
    assertEquals(OptionalDouble.empty(), NumberParser.parseDouble("abc"));
  }

  @Test
  void parseDouble_containingLetters() {
    assertEquals(OptionalDouble.empty(), NumberParser.parseDouble("12 USD"));
  }

  @Test
  void parseDouble_onlySeparator() {
    assertEquals(OptionalDouble.empty(), NumberParser.parseDouble("."));
  }

  @Test
  void parseDouble_containingSpaces() {
    assertEquals(OptionalDouble.of(12_000_000), NumberParser.parseDouble("12 000 000"));
  }

  @Test
  void parseDouble_onlyNumbers() {
    assertEquals(OptionalDouble.of(12), NumberParser.parseDouble("12"));
  }

  @Test
  void parseDouble_numbersUS() {
    assertEquals(OptionalDouble.of(12.00), NumberParser.parseDouble("12.00"));
    assertEquals(OptionalDouble.of(12_000.00), NumberParser.parseDouble("12,000.00"));
  }

  @Test
  void parseDouble_numbersDE() {
    assertEquals(OptionalDouble.of(12.00), NumberParser.parseDouble("12,00"));
    assertEquals(OptionalDouble.of(12_000.00), NumberParser.parseDouble("12.000,00"));
  }

  @Test
  void parseDouble_noNumber() {
    assertEquals(OptionalDouble.empty(), NumberParser.parseDouble(""));
    assertEquals(OptionalDouble.empty(), NumberParser.parseDouble(".,."));
    assertEquals(OptionalDouble.empty(), NumberParser.parseDouble("double"));
    assertEquals(OptionalDouble.empty(), NumberParser.parseDouble("d1"));
    assertEquals(OptionalDouble.empty(), NumberParser.parseDouble("1d"));
  }

  @Test
  void parseDouble_brokenFormat() {
    assertEquals(OptionalDouble.of(1.0), NumberParser.parseDouble("1,.,.,2"));
    assertEquals(OptionalDouble.of(1.0), NumberParser.parseDouble("1.,.,.2"));
  }
}