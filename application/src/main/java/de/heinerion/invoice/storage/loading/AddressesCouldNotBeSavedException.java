package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.models.Address;

import java.util.List;

class AddressesCouldNotBeSavedException extends RuntimeException {
  AddressesCouldNotBeSavedException(List<Address> addresses, Throwable t) {
    super(stringifyAddresses(addresses), t);
  }

  private static String stringifyAddresses(List<Address> addresses) {
    StringBuilder message = new StringBuilder("these addresses could not be saved:\n");
    for (Address address : addresses) {
      message.append(address);
    }
    return message.toString();
  }
}
