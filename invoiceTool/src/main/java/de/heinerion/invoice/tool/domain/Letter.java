package de.heinerion.invoice.tool.domain;

/**
 * Represents an informal letter
 */
public class Letter {
  private String text;
  private Customer customer;

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
}
