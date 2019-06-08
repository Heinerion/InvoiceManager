package de.heinerion.invoice.tool.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represents a customer and is mainly intended to be used for assignments and addresses
 */
public class Customer {
  private String name;
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
}
