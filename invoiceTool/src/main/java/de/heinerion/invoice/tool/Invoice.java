package de.heinerion.invoice.tool;

import java.util.Collection;
import java.util.HashSet;

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

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Customer getCustomer() {
    return customer;
  }
}
