package de.heinerion.invoice.tool.domain;

/**
 * Represents an informal letter
 */
public class Letter {
  private final Company company;
  private String text;
  private Customer customer;

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
}
