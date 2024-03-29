package de.heinerion.invoice.tool.domain;

import java.util.*;

/**
 * Represents a customer and is mainly intended to be used for assignments and addresses
 */
public class Customer {
  private final String name;
  private List<String> address;
  private String correspondent;

  public Customer(String name) {
    this.name = name;
  }

  public Optional<String> getCorrespondent() {
    return Optional.ofNullable(correspondent);
  }

  public void setCorrespondent(String correspondent) {
    this.correspondent = correspondent;
  }

  public String getName() {
    return name;
  }

  public List<String> getAddress() {
    return address;
  }

  public void setAddress(String... addressLines) {
    this.address = Arrays.asList(addressLines);
  }

  @Override
  public String toString() {
    return getCorrespondent().map(c -> c + "@").orElse("") + getName();
  }
}
