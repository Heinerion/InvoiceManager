package de.heinerion.betriebe.repositories.address;

import de.heinerion.betriebe.models.Address;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "addresses")
class AddressListWrapper {
  private List<Address> addresses;

  @XmlElement(name = "address")
  public List<Address> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<Address> addresses) {
    this.addresses = addresses;
  }
}