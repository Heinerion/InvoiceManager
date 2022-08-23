package de.heinerion.invoice.view.swing.home.receiver.calculator;

import de.heinerion.invoice.view.swing.home.receiver.money.Money;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculationResultTest {

  @Test
  public void addTaxes() {
    CalculationResult result = CalculationResult.addTaxes(Money.of(100), 15);

    assertEquals("Net", Money.of(100), result.getNet());
    assertEquals("Gross", Money.of(115), result.getGross());
    assertEquals("VAT", Money.of(15), result.getVat());
  }

  @Test
  public void addTaxes_smallAmount() {
    CalculationResult result = CalculationResult.addTaxes(Money.of(1), 10.7);

    assertEquals("Net", Money.of(1), result.getNet());
    assertEquals("Gross", Money.of(1.107), result.getGross());
    assertEquals("VAT", Money.of(.107), result.getVat());
  }

  @Test
  public void subtractTaxes() {
    CalculationResult result = CalculationResult.subtractTaxes(Money.of(115), 15);

    assertEquals("Net", Money.of(100), result.getNet());
    assertEquals("Gross", Money.of(115), result.getGross());
    assertEquals("VAT", Money.of(15), result.getVat());
  }
}