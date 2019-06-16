package de.heinerion.invoice.tool.domain;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a printable document and defines minimal requirements for those
 */
public abstract class Document {
  private final Company company;
  private Customer customer;
  private ChronoLocalDate date = LocalDate.now();
  private final String subject;
  private final Set<String> keywords;

  public Document(Company company, String subject) {
    this.company = company;
    this.subject = subject;
    this.keywords = new HashSet<>();
    keywords.add(subject);
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

  public String getSubject() {
    return subject;
  }

  public void addKeyword(String keyword) {
    Objects.requireNonNull(keyword);
    keywords.add(keyword);
  }

  /**
   * Returns the added Keywords extended by fixed keywords such as
   * <ul>
   *   <li>company name</li>
   *   <li>customer name</li>
   *   <li>date</li>
   * </ul>
   * @return
   */
  public Collection<String> getKeywords() {
    Collection<String> result = new HashSet<>(keywords);

    result.add(company.getName());
    result.add(customer.getName());
    result.add(date.toString());

    return result;
  }
}
