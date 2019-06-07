package de.heinerion.invoice.tool;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Customer {
  private String name;
  private List<String> address;
  private String correspondent;

  public Customer(String name) {
    this.name = name;
  }

  public void setAddress(String... addressLines) {
    this.address = Arrays.asList(addressLines);
  }

  public void setCorrespondent(String correspondent) {
    this.correspondent = correspondent;
  }

  public Optional<String> getCorrespondent() {
    return Optional.ofNullable(correspondent);
  }

  public String getName() {
    return name;
  }

  public List<String> getAddress() {
    return address;
  }
}
