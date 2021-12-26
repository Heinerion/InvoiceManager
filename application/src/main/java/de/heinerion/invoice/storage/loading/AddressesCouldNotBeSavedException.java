package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.models.Address;

import java.util.Collection;

class AddressesCouldNotBeSavedException extends RuntimeException {
  AddressesCouldNotBeSavedException(Collection<Address> addresses, Throwable t) {
    super(stringifyAddresses(addresses), t);
  }

  private static String stringifyAddresses(Collection<Address> addresses) {
    StringBuilder message = new StringBuilder("these addresses could not be saved:\n");
    for (Address address : addresses) {
      message.append(address);
    }
    return message.toString();
  }
}
