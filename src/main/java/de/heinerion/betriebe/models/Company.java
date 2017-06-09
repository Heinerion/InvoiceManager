package de.heinerion.betriebe.models;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.fileoperations.loading.Loadable;
import de.heinerion.betriebe.models.interfaces.Addressable;
import de.heinerion.betriebe.models.interfaces.Storable;
import de.heinerion.betriebe.tools.PathUtil;

import java.io.File;
import java.text.Collator;

public final class Company implements Addressable, Storable, Loadable {
  private final String descriptiveName;
  private final String officialName;
  private final Address address;
  private final String signer;
  private final String taxNumber;
  private final String phoneNumber;

  private final Account account;

  private final double valueAddedTax;
  private final double wagesPerHour;

  private int invoiceNumber;

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

  @Override
  public Address getAddress() {
    return this.address;
  }

  public String getDescriptiveName() {
    return this.descriptiveName;
  }

  @Override
  public String getEntryName() {
    return this.getOfficialName();
  }

  public int getInvoiceNumber() {
    return this.invoiceNumber;
  }

  public String getOfficialName() {
    return this.officialName;
  }

  public File getFolderFile() {
    return new File(PathUtil.determinePath(Invoice.class) + File.separator
        + this.getDescriptiveName());
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public String getSigner() {
    return this.signer;
  }

  public String getTaxNumber() {
    return this.taxNumber;
  }

  public double getValueAddedTax() {
    return this.valueAddedTax;
  }

  public double getWagesPerHour() {
    return this.wagesPerHour;
  }

  public void increaseInvoiceNumber() {
    this.invoiceNumber++;
    Session.notifyCompany();
  }

  public void setInvoiceNumber(int nextInvoiceNumber) {
    this.invoiceNumber = nextInvoiceNumber;
    Session.notifyCompany();
  }

  @Override
  public String toString() {
    return this.descriptiveName;
  }
}
