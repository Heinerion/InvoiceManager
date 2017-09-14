package de.heinerion.formatter;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.models.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressFormatter {
  private final List<String> output;

  public AddressFormatter() {
    output = new ArrayList<>();
  }

  public final List<String> getOutput() {
    return output;
  }

  public final void format(Address address) {
    out(address.getRecipient());
    optionalOut(address.getCompany());
    optionalOut(address.getDistrict());
    out(address.getStreet() + Constants.SPACE + address.getNumber());
    optionalOut(address.getApartment());
    out(address.getPostalCode() + Constants.SPACE + address.getLocation());
  }

  protected final void out(String message) {
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
