package de.heinerion.betriebe.loading.jaxb;

import de.heinerion.betriebe.builder.AddressBuilder;
import de.heinerion.betriebe.models.Address;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddressManagerTest {
  AddressManager manager;
  List<Address> addresses;

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
    manager = new AddressManager(true);

    File out = new File("addressesBeauty.xml");
    manager.marshalAddresses(addresses, out);

    List<Address> result = manager.unmarshal(out);
    out.delete();

    Assert.assertEquals(map(addresses), map(result));
  }

  private List<String> map(List<Address> list) {
    return list.stream()
        .map(Address::toString)
        .collect(Collectors.toList());
  }

  @Test
  public void roundTripUgly() throws Exception {
    manager = new AddressManager(false);

    File out = new File("addressesUgly.xml");
    manager.marshalAddresses(addresses, out);

    List<Address> result = manager.unmarshal(out);
    out.delete();

    Assert.assertEquals(map(addresses), map(result));
  }
}