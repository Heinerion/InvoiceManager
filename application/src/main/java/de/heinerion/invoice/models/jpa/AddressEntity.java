package de.heinerion.invoice.models.jpa;

import lombok.NoArgsConstructor;

import java.util.*;

// TODO embedded
@NoArgsConstructor
public class AddressEntity {
  /**
   * used as regex to divide address lines
   */
  private static final String DELIMITER = "#-#";

  private String addressLine;

  public List<String> getAddressLines() {
    return addressLine == null
        ? Collections.emptyList()
        : Arrays.asList(addressLine.split(DELIMITER));
  }

  public void setAddressLines(List<String> addressLines) {
    this.addressLine = String.join(DELIMITER, addressLines);
  }

  static AddressEntity of(List<String> addressLines) {
    AddressEntity address = new AddressEntity();
    address.setAddressLines(addressLines);
    return address;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddressEntity that = (AddressEntity) o;
    return Objects.equals(addressLine, that.addressLine);
  }

  @Override
  public int hashCode() {
    return addressLine.hashCode();
  }
}
