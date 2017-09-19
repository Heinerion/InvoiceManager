package de.heinerion.betriebe.gui.formatter;

import de.heinerion.betriebe.models.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressFormatterMock implements AddressFormatter {
  private List<String> result;

  @Override
  public List<String> getOutput() {
    result.add("formatted");
    result.add("address");
    return result;
  }

  @Override
  public AddressFormatter format(Address address) {
    result = new ArrayList<>();
    return this;
  }
}
