package de.heinerion.invoice.tool;

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
