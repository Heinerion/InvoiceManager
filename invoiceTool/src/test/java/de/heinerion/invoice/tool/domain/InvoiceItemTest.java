package de.heinerion.invoice.tool.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InvoiceItemTest {
  @Test
  public void getPrice_returnsPriceOfProductMultipliedByCount() {
    Product pen = new Product("pen", new Percent(19));
    pen.setUnit("pc");
    pen.setPricePerUnit(Euro.of(1, 50));
    pen.setTaxes(new Percent(19));

    InvoiceItem penItem = new InvoiceItem(pen);
    assertEquals(Euro.of(1, 50), penItem.getNetPrice());
    assertEquals(new Percent(19), penItem.getTaxPercentage());
    assertEquals(Euro.of(1, 50).multiply(new Percent(19)), penItem.getTaxes());

    penItem.setCount(2);
    assertEquals(Euro.of(3), penItem.getNetPrice());
    assertEquals(new Percent(19), penItem.getTaxPercentage());
    assertEquals(Euro.of(3).multiply(new Percent(19)), penItem.getTaxes());
  }
}