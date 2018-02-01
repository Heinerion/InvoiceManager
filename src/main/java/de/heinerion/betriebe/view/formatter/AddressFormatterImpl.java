package de.heinerion.betriebe.view.formatter;

import de.heinerion.betriebe.models.Address;

import java.util.ArrayList;
import java.util.List;

class AddressFormatterImpl implements AddressFormatter {
  private List<String> output;

  @Override
  public final List<String> getOutput() {
    return output;
  }

  public final AddressFormatter format(Address address) {
    output = new ArrayList<>();

    out(address.getRecipient());
    address.getCompany().ifPresent(this::out);
    address.getDistrict().ifPresent(this::out);
    out(address.getStreet() + " " + address.getNumber());
    address.getApartment().ifPresent(this::out);
    out(address.getPostalCode() + " " + address.getLocation());

    return this;
  }

  private void out(String message) {
    output.add(message);
  }
}
