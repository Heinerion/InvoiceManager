package de.heinerion.invoice.models;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
// public because of AddressForm, AddressLoader & CompanyLoader
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "address")
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private Company owner;

  private String apartment;
  private String company;
  private String district;
  private String location;
  private String number;
  @Column(name = "postal_code")
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

  public Address setApartment(String apartment) {
    if (isValidMessage(apartment)) {
      this.apartment = apartment;
    }
    return this;
  }

  public Address setCompany(String company) {
    if (isValidMessage(company)) {
      this.company = company;
    }
    return this;
  }

  public Address setDistrict(String district) {
    if (isValidMessage(district)) {
      this.district = district;
    }
    return this;
  }

  private boolean isValidMessage(String message) {
    return null != message && !message.trim().isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Address address = (Address) o;
    if (id != null && id.equals(address.id)) {
      return true;
    }
    return Objects.equals(apartment, address.apartment) &&
        Objects.equals(company, address.company) &&
        Objects.equals(district, address.district) &&
        Objects.equals(location, address.location) &&
        Objects.equals(number, address.number) &&
        Objects.equals(postalCode, address.postalCode) &&
        Objects.equals(recipient, address.recipient) &&
        Objects.equals(street, address.street);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return recipient;
  }
}
