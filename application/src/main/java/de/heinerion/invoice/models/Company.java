package de.heinerion.invoice.models;

import lombok.Getter;
import lombok.Setter;

import java.text.Collator;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public final class Company implements Comparable<Company> {
  private UUID id;

  private String descriptiveName;
  private String officialName;
  private Address address;
  private String signer;
  private String taxNumber;
  private String phoneNumber;

  private Account account;

  private double valueAddedTax;
  private double wagesPerHour;

  private int invoiceNumber;

  /**
   * For persistence only
   */
  public Company() {
  }

  public Company(String descriptiveName, String officialName, Address address,
                 String signer, String phoneNumber, String taxNumber,
                 double valueAddedTax, double wagesPerHour, Account bankAccount) {
    this.descriptiveName = descriptiveName;
    this.officialName = officialName;
    this.address = address;
    this.signer = signer;
    this.phoneNumber = phoneNumber;
    this.taxNumber = taxNumber;
    this.valueAddedTax = valueAddedTax;
    this.wagesPerHour = wagesPerHour;
    this.account = bankAccount;
  }

  @Override
  public int compareTo(Company company) {
    return Collator.getInstance().compare(this.getDescriptiveName(),
        company.getDescriptiveName());
  }

  public void increaseInvoiceNumber() {
    this.invoiceNumber++;
  }

  @Override
  public String toString() {
    return this.descriptiveName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Company company = (Company) o;
    return Objects.equals(id, company.id) &&
        Double.compare(company.valueAddedTax, valueAddedTax) == 0 &&
        Double.compare(company.wagesPerHour, wagesPerHour) == 0 &&
        invoiceNumber == company.invoiceNumber &&
        Objects.equals(descriptiveName, company.descriptiveName) &&
        Objects.equals(officialName, company.officialName) &&
        Objects.equals(address, company.address) &&
        Objects.equals(signer, company.signer) &&
        Objects.equals(taxNumber, company.taxNumber) &&
        Objects.equals(phoneNumber, company.phoneNumber) &&
        Objects.equals(account, company.account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, descriptiveName, officialName, address, signer, taxNumber, phoneNumber, account, valueAddedTax, wagesPerHour, invoiceNumber);
  }
}
