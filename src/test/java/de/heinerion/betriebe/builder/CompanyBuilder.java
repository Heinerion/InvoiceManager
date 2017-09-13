package de.heinerion.betriebe.builder;

import de.heinerion.betriebe.models.Account;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;

import java.util.Optional;

public class CompanyBuilder implements TestDataBuilder<Company> {
  private String descriptiveName;
  private String officialName;
  private Address address;
  private String signer;
  private String taxNumber;
  private String phoneNumber;

  private Account account;

  private double valueAddedTax;
  private double wagesPerHour;

  private Optional<String> getDescriptiveName() {
    return Optional.ofNullable(descriptiveName);
  }

  public CompanyBuilder withDescriptiveName(String descriptiveName) {
    this.descriptiveName = descriptiveName;
    return this;
  }

  private Optional<String> getOfficialName() {
    return Optional.ofNullable(officialName);
  }

  public CompanyBuilder withOfficialName(String officialName) {
    this.officialName = officialName;
    return this;
  }

  private Optional<Address> getAddress() {
    return Optional.ofNullable(address);
  }

  public CompanyBuilder withAddress(AddressBuilder builder) {
    withAddress(builder.build());
    return this;
  }

  public CompanyBuilder withAddress(Address address) {
    this.address = address;
    return this;
  }

  private Optional<String> getSigner() {
    return Optional.ofNullable(signer);
  }

  public CompanyBuilder withSigner(String signer) {
    this.signer = signer;
    return this;
  }

  private Optional<String> getTaxNumber() {
    return Optional.ofNullable(taxNumber);
  }

  public CompanyBuilder withTaxNumber(String taxNumber) {
    this.taxNumber = taxNumber;
    return this;
  }

  private Optional<String> getPhoneNumber() {
    return Optional.ofNullable(phoneNumber);
  }

  public CompanyBuilder withPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  private Optional<Account> getAccount() {
    return Optional.ofNullable(account);
  }

  public CompanyBuilder withAccount(AccountBuilder builder) {
    withAccount(builder.build());
    return this;
  }

  public CompanyBuilder withAccount(Account account) {
    this.account = account;
    return this;
  }

  protected Optional<Double> getValueAddedTax() {
    return Optional.ofNullable(valueAddedTax == 0 ? null : valueAddedTax);
  }

  public CompanyBuilder withValueAddedTax(double valueAddedTax) {
    this.valueAddedTax = valueAddedTax;
    return this;
  }

  protected Optional<Double> getWagesPerHour() {
    return Optional.ofNullable(wagesPerHour == 0 ? null : wagesPerHour);
  }

  public CompanyBuilder withWagesPerHour(double wagesPerHour) {
    this.wagesPerHour = wagesPerHour;
    return this;
  }


  @Override
  public Company build() {
    return new Company(
        getDescriptiveName().orElse("descriptiveName"),
        getOfficialName().orElse("officialName"),
        getAddress().orElse(new AddressBuilder().build()),
        getSigner().orElse("signer"),
        getPhoneNumber().orElse("123-456"),
        getTaxNumber().orElse("taxNumber"),
        getValueAddedTax().orElse(10d),
        getWagesPerHour().orElse(50d),
        getAccount().orElse(new AccountBuilder().build()));
  }
}
