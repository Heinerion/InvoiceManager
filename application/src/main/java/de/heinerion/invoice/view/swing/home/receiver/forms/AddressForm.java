package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.invoice.models.Address;

import java.util.*;

public class AddressForm extends AbstractForm<Address> {

  private final List<FormLine<Address, ?>> properties = Arrays.asList(
      FormLine.of(Address.class, String.class).name("name").setter(Address::setName).build(),
      FormLine.of(Address.class, String.class).name("block").setter(Address::setBlock).build()
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
