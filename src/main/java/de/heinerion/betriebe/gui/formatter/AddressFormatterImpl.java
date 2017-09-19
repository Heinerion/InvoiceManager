package de.heinerion.betriebe.gui.formatter;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.util.Constants;

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
    optionalOut(address.getCompany());
    optionalOut(address.getDistrict());
    out(address.getStreet() + Constants.SPACE + address.getNumber());
    optionalOut(address.getApartment());
    out(address.getPostalCode() + Constants.SPACE + address.getLocation());

    return this;
  }

  private void out(String message) {
    output.add(message);
  }

  private void optionalOut(String message) {
    if (isValidMessage(message)) {
      out(message);
    }
  }

  private boolean isValidMessage(String message) {
    return null != message && !message.trim().isEmpty();
  }
}
