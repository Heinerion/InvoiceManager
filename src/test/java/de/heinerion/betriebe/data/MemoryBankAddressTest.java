package de.heinerion.betriebe.data;

import de.heinerion.betriebe.models.Address;
import de.heinerion.invoice.testsupport.builder.AddressBuilder;
import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MemoryBankAddressTest {
  private Address address;
  private MemoryBank memoryBank;

  @Before
  public void setUp() {
    memoryBank = new MemoryBank();
    address = new AddressBuilder().build();
  }

  @Test
  public void testAddAddressWithCompany() {
    memoryBank.addAddress(address);

    assertTrue(memoryBank.getAllAddresses().contains(address));
  }

  @Test
  public void testAddAddressMultipleTimes() {
    int baseSize = memoryBank.getAllAddresses().size();
    int expectedGrowth = 1;
    int expectedSize = baseSize + expectedGrowth;

    memoryBank.addAddress(address);
    memoryBank.addAddress(address);

    assertEquals(expectedSize, memoryBank.getAllAddresses().size());
  }

  @Test
  public void testDoNotFindAddressByCompany() {
    assertFalse(memoryBank.getAddress(new CompanyBuilder().build(), "Charlie").isPresent());
  }

  @Test
  public void testFindAddressByCompany() {
    String recipient = "Charlie";
    address.setRecipient(recipient);
    memoryBank.addAddress(address);
    Optional<Address> result = memoryBank.getAddress(null, recipient);
    assertTrue(result.isPresent());
    assertEquals(address, result.get());
  }
}