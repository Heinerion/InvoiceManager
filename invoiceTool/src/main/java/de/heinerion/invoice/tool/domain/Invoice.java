package de.heinerion.invoice.tool.domain;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents an invoice with its items
 */
public class Invoice extends Document {
  private final Collection<InvoiceItem> items = new HashSet<>();

  public Invoice(Company company, String id) {
    super(company);
  }

  public void add(InvoiceItem item) {
    items.add(item);
  }

  public Collection<InvoiceItem> getItems() {
    return items;
  }
}
