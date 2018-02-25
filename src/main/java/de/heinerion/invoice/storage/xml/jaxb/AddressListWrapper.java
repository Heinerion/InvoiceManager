package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.betriebe.models.Address;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "addresses")
public class AddressListWrapper {
  private List<Address> addresses;

  @XmlElement(name = "address")
  public List<Address> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<Address> addresses) {
    this.addresses = addresses;
  }
}