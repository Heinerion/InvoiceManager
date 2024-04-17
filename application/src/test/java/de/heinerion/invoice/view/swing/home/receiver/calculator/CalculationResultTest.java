package de.heinerion.invoice.view.swing.home.receiver.calculator;

import de.heinerion.invoice.view.swing.home.receiver.money.Money;
import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class CalculationResultTest {

  @Test
  void ofNet() {
    CalculationResult result = CalculationResult.ofNet(Money.of(100), 15);

    assertEquals("Net", Money.of(100), result.net());
    assertEquals("Gross", Money.of(115), result.gross());
    assertEquals("VAT", Money.of(15), result.vat());
  }

  @Test
  void ofNet_smallAmount() {
    CalculationResult result = CalculationResult.ofNet(Money.of(1), 10.7);

    assertEquals("Net", Money.of(1), result.net());
    assertEquals("Gross", Money.of(1.107), result.gross());
    assertEquals("VAT", Money.of(.107), result.vat());
  }

  @Test
  void ofGross() {
    CalculationResult result = CalculationResult.ofGross(Money.of(115), 15);

    assertEquals("Net", Money.of(100), result.net());
    assertEquals("Gross", Money.of(115), result.gross());
    assertEquals("VAT", Money.of(15), result.vat());
  }
}