package de.heinerion.invoice.tool.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Company {
  private String name;
  private List<String> address;
  private String correspondent;

  public Company(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<String> getAddress() {
    return address;
  }

  public void setAddress(String... address) {
    this.address = Arrays.asList(address);
  }

  public Optional<String> getCorrespondent() {
    return Optional.ofNullable(correspondent);
  }

  public void setCorrespondent(String correspondent) {
    this.correspondent = correspondent;
  }
}
