package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.invoice.models.Address;
import de.heinerion.util.Strings;

import java.util.*;

public class AddressForm extends AbstractForm<Address> {

  private final List<FormLine<Address, ?>> properties = Arrays.asList(
      FormLine.of(Address.class, String.class).name("name").setter(Address::setName).valid(Strings::isNotBlank).build(),
      FormLine.of(Address.class, String.class).name("block").setter(Address::setBlock).valid(Strings::isNotBlank).build()
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
