package de.heinerion.betriebe.models;

import de.heinerion.betriebe.classes.fileOperations.loading.Loadable;
import de.heinerion.betriebe.models.interfaces.Storable;

public final class Address implements Storable, Loadable {
  private String apartment;
  private String company;
  private String district;
  private String location;
  private String number;
  private String postalCode;
  private String recipient;
  private String street;

  public Address(String company, String street, String number,
      String postalCode, String location) {
    this(null, company, null, street, number, null, postalCode, location);
  }

  public Address(String recipient, String company, String district,
      String street, String number, String apartment, String postalCode,
      String location) {
    this.apartment = apartment;
    this.company = company;
    this.district = district;
    this.location = location;
    this.number = number;
    this.postalCode = postalCode;
    this.recipient = recipient;
    this.street = street;
  }

  public String getApartment() {
    return this.apartment;
  }

  public String getCompany() {
    return this.company;
  }

  public String getDistrict() {
    return this.district;
  }

  @Override
  public String getEntryName() {
    return this.getRecipient();
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
    this.apartment = apartment;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public void setDistrict(String district) {
    this.district = district;
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

  @Override
  public String toString() {
    return this.recipient;
  }
}
