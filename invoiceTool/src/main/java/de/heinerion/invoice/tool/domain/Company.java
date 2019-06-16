package de.heinerion.invoice.tool.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Company {
  private String name;
  private List<String> address;
  private String correspondent;
  private String phone = "";

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

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = "123-456/789";
  }

  @Override
  public String toString() {
    return getCorrespondent().map(c -> c + "@").orElse("") + getName();
  }
}
