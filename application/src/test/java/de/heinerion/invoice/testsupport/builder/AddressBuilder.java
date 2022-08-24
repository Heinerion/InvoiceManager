package de.heinerion.invoice.testsupport.builder;

import de.heinerion.invoice.models.Address;

import java.util.Optional;

public class AddressBuilder implements TestDataBuilder<Address> {
  private String apartment;
  private String company;
  private String district;
  private String location;
  private String number;
  private String postalCode;
  private String recipient;
  private String street;

  protected Optional<String> getApartment() {
    return Optional.ofNullable(apartment);
  }

  public AddressBuilder withApartment(String apartment) {
    this.apartment = apartment;
    return this;
  }

  protected Optional<String> getCompany() {
    return Optional.ofNullable(company);
  }

  public AddressBuilder withCompany(String company) {
    this.company = company;
    return this;
  }

  protected Optional<String> getDistrict() {
    return Optional.ofNullable(district);
  }

  public AddressBuilder withDistrict(String district) {
    this.district = district;
    return this;
  }

  protected Optional<String> getLocation() {
    return Optional.ofNullable(location);
  }

  public AddressBuilder withLocation(String location) {
    this.location = location;
    return this;
  }

  protected Optional<String> getNumber() {
    return Optional.ofNullable(number);
  }

  public AddressBuilder withNumber(String number) {
    this.number = number;
    return this;
  }

  protected Optional<String> getPostalCode() {
    return Optional.ofNullable(postalCode);
  }

  public AddressBuilder withPostalCode(String postalCode) {
    this.postalCode = postalCode;
    return this;
  }

  protected Optional<String> getRecipient() {
    return Optional.ofNullable(recipient);
  }

  public AddressBuilder withRecipient(String recipient) {
    this.recipient = recipient;
    return this;
  }

  protected Optional<String> getStreet() {
    return Optional.ofNullable(street);
  }

  public AddressBuilder withStreet(String street) {
    this.street = street;
    return this;
  }

  public AddressBuilder clear() {
    apartment = "";
    company = "";
    district = "";
    location = "";
    number = "";
    postalCode = "";
    recipient = "";
    street = "";

    return this;
  }

  @Override
  public Address build() {
    Address address = new Address();
    address.setApartment(getApartment().orElse("apartment"));
    address.setCompany(getCompany().orElse("company"));
    address.setDistrict(getDistrict().orElse("district"));
    address.setLocation(getLocation().orElse("location"));
    address.setNumber(getNumber().orElse("number"));
    address.setPostalCode(getPostalCode().orElse("postalCode"));
    address.setRecipient(getRecipient().orElse("recipient"));
    address.setStreet(getStreet().orElse("street"));

    return address;
  }
}
