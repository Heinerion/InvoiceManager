package de.heinerion.betriebe.loading.jaxb;

import de.heinerion.betriebe.models.Address;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

public class AddressManager {
  private boolean beautify;

  public AddressManager(boolean beautify) {
    this.beautify = beautify;
  }

  public void marshalAddresses(List<Address> addresses, File destination) {
    try {
      JAXBContext context = JAXBContext.newInstance(AddressListWrapper.class);
      Marshaller m = context.createMarshaller();

      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, beautify);

      AddressListWrapper wrapper = new AddressListWrapper();
      wrapper.setAddresses(addresses);

      m.marshal(wrapper, destination);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<Address> unmarshal(File source) {
    try {
      JAXBContext context = JAXBContext.newInstance(AddressListWrapper.class);
      Unmarshaller um = context.createUnmarshaller();

      AddressListWrapper wrapper = (AddressListWrapper) um.unmarshal(source);

      return wrapper.getAddresses();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
