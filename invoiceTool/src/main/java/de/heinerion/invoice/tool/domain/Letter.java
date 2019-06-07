package de.heinerion.invoice.tool.domain;

import de.heinerion.invoice.tool.domain.Customer;

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

  public void setCustomer(Customer customer) {
    this.customer=customer;
  }

  public Customer getCustomer() {
    return customer;
  }
}
