package de.heinerion.invoice.tool.domain;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

/**
 * Represents an informal letter
 */
public class Letter {
  private final Company company;
  private String text;
  private Customer customer;
  private ChronoLocalDate date = LocalDate.now();

  public Letter(Company company) {
    this.company = company;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
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
