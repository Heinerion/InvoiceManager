package de.heinerion.invoice.tool.domain;

/**
 * Represents a single line in an invoice
 */
public class InvoiceItem {
  private final Product product;
  private int count = 1;

  public InvoiceItem(Product product) {
    this.product = product;
  }

  public Euro getPrice() {
    return product.getPricePerUnit().multiply(count);
  }

  public Percent getTaxPercentage() {
    return product.getTaxes();
  }

  public Euro getTaxes() {
    return getPrice().multiply(getTaxPercentage());
  }

  public void setCount(int count) {
    this.count = count;
  }

  @Override
  public String toString() {
    return String.format("%s %d %s", product.toString(), count, getPrice());
  }
}
