package de.heinerion.invoice.tool.domain;

/**
 * Represents a single line in an invoice
 */
public class InvoiceItem {
  private Euro price;

  public Euro getPrice() {
    return price;
  }

  public void setPrice(Euro price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return price.toString();
  }
}
