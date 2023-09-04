package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.invoice.models.Address;
import de.heinerion.util.Strings;

import java.util.*;

public class AddressForm extends AbstractForm<Address> {

  private final List<FormLine<Address, ?>> properties = Arrays.asList(
      FormLine.of("name", String.class, Address::setName, Strings::isNotBlank),
      FormLine.of("block", String.class, Address::setBlock, Strings::isNotBlank)
  );

  @Override
  protected List<FormLine<Address, ?>> getProperties() {
    return properties;
  }

  @Override
  protected String getTitle() {
    return "Address";
  }

  @Override
  protected Address createInstance() {
    return new Address();
  }
}
