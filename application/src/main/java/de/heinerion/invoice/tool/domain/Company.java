package de.heinerion.invoice.tool.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Company {
  private String name;
  private List<String> address;
  private String correspondent;
  private String phone = "";

  private String iban;
  private String bic;
  private String bankName;
  private String taxNumber;

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
    this.phone = phone;
  }

  public String getTaxNumber() {
    return taxNumber;
  }

  public void setTaxNumber(String taxNumber) {
    this.taxNumber = taxNumber;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public String getBic() {
    return bic;
  }

  public void setBic(String bic) {
    this.bic = bic;
  }

  @Override
  public String toString() {
    return getCorrespondent().map(c -> c + "@").orElse("") + getName();
  }
}
