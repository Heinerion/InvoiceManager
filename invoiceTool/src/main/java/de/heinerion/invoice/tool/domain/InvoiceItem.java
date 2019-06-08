package de.heinerion.invoice.tool.domain;

import de.heinerion.invoice.tool.domain.Euro;

/**
 * Represents a single line in an invoice
 */
public class InvoiceItem {
  private Euro price;

  public void setPrice(Euro price) {
    this.price = price;
  }

  public Euro getPrice() {
    return price;
  }

  @Override
  public String toString() {
    return price.toString();
  }
}
