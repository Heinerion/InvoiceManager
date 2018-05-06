package de.heinerion.invoice.view.formatter;

import de.heinerion.betriebe.models.Address;

import java.util.List;
import java.util.Optional;

class FormatterImpl implements Formatter {
  private AddressFormatter addressFormatter;

  FormatterImpl(AddressFormatter addressFormatter) {
    this.addressFormatter = addressFormatter;
  }

  private Optional<AddressFormatter> getAddressFormatter() {
    return Optional.ofNullable(addressFormatter);
  }

  @Override
  public List<String> formatAddress(Address address) {
    AddressFormatter formatter = getAddressFormatter()
        .orElseThrow(FormatterException::new);
    formatter.format(address);
    return formatter.getOutput();
  }
}
