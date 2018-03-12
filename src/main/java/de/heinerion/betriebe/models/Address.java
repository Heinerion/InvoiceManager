package de.heinerion.betriebe.models;

import de.heinerion.invoice.storage.loading.Loadable;

import java.util.Optional;

public final class Address implements Storable, Loadable {
  private String apartment;
  private String company;
  private String district;
  private String location;
  private String number;
  private String postalCode;
  private String recipient;
  private String street;

  public Optional<String> getApartment() {
    return Optional.ofNullable(apartment);
  }

  public Optional<String> getCompany() {
    return Optional.ofNullable(company);
  }

  public Optional<String> getDistrict() {
    return Optional.ofNullable(district);
  }

  @Override
  public String getEntryName() {
    return getRecipient();
  }

  public String getLocation() {
    return this.location;
  }

  public String getNumber() {
    return this.number;
  }

  public String getPostalCode() {
    return this.postalCode;
  }

  public String getRecipient() {
    return this.recipient;
  }

  public String getStreet() {
    return this.street;
  }

  public void setApartment(String apartment) {
    if (isValidMessage(apartment))
    {
      this.apartment = apartment;
    }
  }

  public void setCompany(String company) {
    if (isValidMessage(company))
    {
      this.company = company;
    }
  }

  public void setDistrict(String district) {
    if (isValidMessage(district))
    {
      this.district = district;
    }
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  private boolean isValidMessage(String message) {
    return null != message && !message.trim().isEmpty();
  }

  @Override
  public String toString() {
    return this.recipient;
  }
}
