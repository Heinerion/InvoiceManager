package de.heinerion.betriebe.loading.jaxb;

import de.heinerion.betriebe.models.Address;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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
  protected JAXBContext getContext() throws JAXBException {
    return JAXBContext.newInstance(AddressListWrapper.class);
  }
}
