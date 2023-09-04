package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.invoice.models.Address;

import java.util.*;

public class AddressForm extends AbstractForm<Address> {

  private final List<FormLine<Address, ?>> properties = Arrays.asList(
      FormLine.ofString("name", Address::setName),
      FormLine.ofString("block", Address::setBlock, 200)
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
