package de.heinerion.invoice.tool.domain;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents an invoice with its items
 */
public class Invoice {
  private final Collection<InvoiceItem> items = new HashSet<>();
  private Customer customer;
  private Company company;
  private ChronoLocalDate date = LocalDate.now();

  public Invoice(Company company, String id) {
    this.company = company;
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

  public Company getCompany() {
    return company;
  }

  public ChronoLocalDate getDate() {
    return date;
  }
}
