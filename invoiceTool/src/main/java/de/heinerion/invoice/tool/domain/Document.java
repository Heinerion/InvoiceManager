package de.heinerion.invoice.tool.domain;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a printable document and defines minimal requirements for those
 */
public abstract class Document {
  private final Company company;
  private final String subject;
  private final Set<String> keywords;
  private Customer customer;
  private LocalDate date = LocalDate.now();

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

  public LocalDate getDate() {
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
   * <li>company name</li>
   * <li>customer name</li>
   * <li>date</li>
   * </ul>
   *
   * @return
   */
  public Collection<String> getKeywords() {
    Collection<String> result = new HashSet<>(keywords);

    result.add(company.getName());
    result.add(customer.getName());
    result.add(date.toString());

    return result;
  }

  /**
   * creates a fuly detached copy of this entity
   */
  public abstract Document copy();

  protected <T extends Document> void copyDocumentPropertiesTo(T copy) {
    copy.setCustomer(customer);
    // ensure date is another instance
    copy.setDate(date.plusDays(1).minusDays(1));
    keywords.forEach(copy::addKeyword);
  }
}
