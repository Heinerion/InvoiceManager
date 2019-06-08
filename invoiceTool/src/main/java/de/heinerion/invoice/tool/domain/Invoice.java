package de.heinerion.invoice.tool.domain;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents an invoice with its items
 */
public class Invoice {
  private Collection<InvoiceItem> items = new HashSet<>();
  private Customer customer;

  public Invoice(String id) {
  }

  public void add(InvoiceItem item) {
    items.add(item);
  }

  public Collection<InvoiceItem> getItems() {
    return items;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
}
