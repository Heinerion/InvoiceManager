package de.heinerion.invoice.tool.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InvoiceItemTest {
  @Test
  public void getPrice_returnsPriceOfProductMultipliedByCount() {
    Product pen = new Product("pen");
    pen.setUnit("pc");
    pen.setPricePerUnit(new Euro(1, 50));
    pen.setTaxes(new Percent(19));

    InvoiceItem penItem = new InvoiceItem(pen);
    assertEquals(new Euro(1, 50), penItem.getPrice());
    assertEquals(new Percent(19), penItem.getTaxPercentage());
    assertEquals(new Euro(1, 50).multiply(new Percent(19)), penItem.getTaxes());

    penItem.setCount(2);
    assertEquals(new Euro(3), penItem.getPrice());
    assertEquals(new Percent(19), penItem.getTaxPercentage());
    assertEquals(new Euro(3).multiply(new Percent(19)), penItem.getTaxes());
  }
}