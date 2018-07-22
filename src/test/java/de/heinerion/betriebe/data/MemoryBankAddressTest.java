package de.heinerion.betriebe.data;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
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
  private Company company;
  private Address address;
  private MemoryBank memoryBank;

  @Before
  public void setUp() {
    memoryBank = new MemoryBank();
    company = new CompanyBuilder().build();
    address = new AddressBuilder().build();
  }

  @Test
  public void testAddAddressWithCompany() {
    memoryBank.addAddress(company, address);

    assertTrue(memoryBank.getAddresses(company).contains(address));
  }

  @Test
  public void testAddAddressMultipleTimes() {
    int baseSize = memoryBank.getAddresses(company).size();
    int expectedGrowth = 1;
    int expectedSize = baseSize + expectedGrowth;

    memoryBank.addAddress(company, address);
    memoryBank.addAddress(company, address);

    assertEquals(expectedSize, memoryBank.getAddresses(company).size());
  }

  @Test
  public void testDoNotFindAddressByCompany() {
    assertFalse(memoryBank.getAddress(company, "Charlie").isPresent());
  }

  @Test
  public void testFindAddressByCompany() {
    String recipient = "Charlie";
    address.setRecipient(recipient);
    memoryBank.addAddress(company, address);
    Optional<Address> result = memoryBank.getAddress(company, recipient);
    assertTrue(result.isPresent());
    assertEquals(address, result.get());
  }
}