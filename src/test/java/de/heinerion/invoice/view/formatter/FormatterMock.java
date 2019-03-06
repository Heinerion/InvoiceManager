package de.heinerion.invoice.view.formatter;

import de.heinerion.betriebe.models.Address;

import java.util.Arrays;
import java.util.List;

public class FormatterMock extends Formatter {
  @Override
  public List<String> formatAddress(Address address) {
    return Arrays.asList("formatted", "address");
  }
}
