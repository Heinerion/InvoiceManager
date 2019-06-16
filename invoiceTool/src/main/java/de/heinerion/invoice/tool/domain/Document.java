package de.heinerion.invoice.tool.domain;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

/**
 * Represents a printable document and defines minimal requirements for those
 */
public abstract class Document {
  private final Company company;
  private Customer customer;
  private ChronoLocalDate date = LocalDate.now();

  public Document(Company company) {
    this.company = company;
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
