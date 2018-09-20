package de.heinerion.betriebe.models;

import de.heinerion.betriebe.data.Session;
import de.heinerion.invoice.storage.loading.Loadable;

import java.io.File;
import java.text.Collator;
import java.util.Objects;

public final class Company implements Storable, Loadable {
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
  private Company() {
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

  public int compareTo(Company company) {
    return Collator.getInstance().compare(this.getDescriptiveName(),
        company.getDescriptiveName());
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public Address getAddress() {
    return this.address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getDescriptiveName() {
    return this.descriptiveName;
  }

  public void setDescriptiveName(String descriptiveName) {
    this.descriptiveName = descriptiveName;
  }

  @Override
  public String getEntryName() {
    return this.getOfficialName();
  }

  public int getInvoiceNumber() {
    return this.invoiceNumber;
  }

  public void setInvoiceNumber(int nextInvoiceNumber) {
    this.invoiceNumber = nextInvoiceNumber;
  }

  public String getOfficialName() {
    return this.officialName;
  }

  public void setOfficialName(String officialName) {
    this.officialName = officialName;
  }

  public File getFolderFile(String basePath) {
    return new File(basePath + File.separator + this.getDescriptiveName());
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getSigner() {
    return this.signer;
  }

  public void setSigner(String signer) {
    this.signer = signer;
  }

  public String getTaxNumber() {
    return this.taxNumber;
  }

  public void setTaxNumber(String taxNumber) {
    this.taxNumber = taxNumber;
  }

  public double getValueAddedTax() {
    return this.valueAddedTax;
  }

  public void setValueAddedTax(double valueAddedTax) {
    this.valueAddedTax = valueAddedTax;
  }

  public double getWagesPerHour() {
    return this.wagesPerHour;
  }

  public void setWagesPerHour(double wagesPerHour) {
    this.wagesPerHour = wagesPerHour;
  }

  public void increaseInvoiceNumber() {
    this.invoiceNumber++;
    Session.notifyCompany();
  }

  @Override
  public String toString() {
    return this.descriptiveName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Company company = (Company) o;
    return Double.compare(company.valueAddedTax, valueAddedTax) == 0 &&
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
    return Objects.hash(descriptiveName, officialName, address, signer, taxNumber, phoneNumber, account, valueAddedTax, wagesPerHour, invoiceNumber);
  }
}
