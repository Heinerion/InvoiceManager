package de.heinerion.invoice.view.formatter;

import de.heinerion.invoice.testsupport.builder.AddressBuilder;
import de.heinerion.betriebe.models.Address;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class AddressFormatterImplTest {
  private AddressFormatter impl;

  @Before
  public void setUp() {
    impl = new AddressFormatterImpl();
  }

  @Test
  public void formatFullBlownAddress() throws Exception {
    Address address = new AddressBuilder().build();

    ArrayList<String> expected = new ArrayList<>();
    expected.add("recipient");
    expected.add("company");
    expected.add("district");
    expected.add("street number");
    expected.add("apartment");
    expected.add("postalCode location");

    assertEquals(expected, impl.format(address).getOutput());
  }

  @Test
  public void formatSimpleAddress() throws Exception {
    Address address = new AddressBuilder()
        .clear()
        .withRecipient("recipient")
        .withStreet("street")
        .withNumber("number")
        .withPostalCode("postalCode")
        .withLocation("location")
        .build();

    ArrayList<String> expected = new ArrayList<>();
    expected.add("recipient");
    expected.add("street number");
    expected.add("postalCode location");

    assertEquals(expected, impl.format(address).getOutput());
  }

}