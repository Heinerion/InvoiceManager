package de.heinerion.invoice.view.swing.home.receiver.forms;

import de.heinerion.invoice.models.Address;

import java.util.*;

public class AddressForm extends AbstractForm<Address> {

  private final List<FormLine<Address, ?>> properties = Arrays.asList(
      FormLine.builder(Address.class, String.class).name("recipient").setter(Address::setRecipient).valid(s -> !s.isEmpty()).build(),
      FormLine.builder(Address.class, String.class).name("company").setter(Address::setCompany).valid(s -> !s.isEmpty()).build(),
      FormLine.builder(Address.class, String.class).name("street").setter(Address::setStreet).valid(s -> !s.isEmpty()).build(),
      FormLine.builder(Address.class, String.class).name("number").setter(Address::setNumber).valid(s -> !s.isEmpty()).build(),
      FormLine.builder(Address.class, String.class).name("postalCode").setter(Address::setPostalCode).valid(s -> !s.isEmpty()).build(),
      FormLine.builder(Address.class, String.class).name("location").setter(Address::setLocation).valid(s -> !s.isEmpty()).build(),
      FormLine.builder(Address.class, String.class).name("district").setter(Address::setDistrict).valid(s -> true).build(),
      FormLine.builder(Address.class, String.class).name("apartment").setter(Address::setApartment).valid(s -> true).build()
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
