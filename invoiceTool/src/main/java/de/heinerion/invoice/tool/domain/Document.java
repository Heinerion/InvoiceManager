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
  private String subject;

  public Document(Company company, String subject) {
    this.company = company;
    this.subject = subject;
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

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getSubject(){
    return subject;
  }
}
