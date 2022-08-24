package de.heinerion.invoice.repositories.address;

import de.heinerion.invoice.models.Address;
import de.heinerion.invoice.repositories.JaxbManager;

import java.util.List;

public class AddressManager extends JaxbManager<Address> {
  @Override
  protected Object getRootObject() {
    return new AddressListWrapper();
  }

  @Override
  protected void setContent(Object wrapper, List<Address> items) {
    ((AddressListWrapper) wrapper).setAddresses(items);
  }

  @Override
  protected List<Address> getContent(Object rootObject) {
    return ((AddressListWrapper) rootObject).getAddresses();
  }

  @Override
  protected Class<?> getWrapper() {
    return AddressListWrapper.class;
  }
}
