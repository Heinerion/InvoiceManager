package de.heinerion.invoice.storage.xml.jaxb;

import de.heinerion.invoice.models.Address;
import de.heinerion.invoice.repositories.address.AddressManager;
import de.heinerion.invoice.testsupport.builder.AddressBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddressManagerTest {
  private AddressManager manager;
  private List<Address> addresses;

  @Before
  public void setUp() throws Exception {
    addresses = new ArrayList<>();
    addresses.add(new AddressBuilder().withRecipient("a").build());
    addresses.add(new AddressBuilder().withRecipient("b").build());
    addresses.add(new AddressBuilder().withRecipient("c").build());
    addresses.add(new AddressBuilder().withRecipient("d").build());
    addresses.add(new AddressBuilder().withRecipient("e").build());
    addresses.add(new AddressBuilder().withRecipient("f").build());
  }

  @Test
  public void roundTripBeauty() throws Exception {
    manager = new AddressManager();
    manager.setBeautify(true);

    Path out = Path.of("addressesBeauty.xml");
    manager.marshal(addresses, out);

    List<Address> result = manager.unmarshal(out);
    Files.delete(out);

    Assert.assertEquals(map(addresses), map(result));
  }

  private List<String> map(List<Address> list) {
    return list.stream()
        .map(Address::toString)
        .collect(Collectors.toList());
  }

  @Test
  public void roundTripUgly() throws Exception {
    manager = new AddressManager();

    Path out = Path.of("addressesUgly.xml");
    manager.marshal(addresses, out);

    List<Address> result = manager.unmarshal(out);
    Files.delete(out);

    Assert.assertEquals(map(addresses), map(result));
  }
}