package de.heinerion.betriebe.data;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.storage.loading.IO;
import de.heinerion.invoice.testsupport.builder.AddressBuilder;
import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IO.class})
@PowerMockIgnore("javax.management.*")
public class DataBaseAddressTest {
  private Company company;
  private Address address;
  private DataBase dataBase = DataBase.getInstance();

  @Mock
  private IO io;

  @Before
  public void setUp() throws Exception{
    dataBase.setIo(io);
    dataBase.clearAllLists();
    company = new CompanyBuilder().build();
    address = new AddressBuilder().build();
  }

  @Test
  public void testAddAddressWithCompany() {
    dataBase.addAddress(company, address);

    assertTrue(dataBase.getAddresses(company).contains(address));
  }

  @Test
  public void testAddAddressAsLoadable() {
    dataBase.addLoadable(address);

    assertTrue(dataBase.getAddresses(null).contains(address));
  }

  @Test
  public void testAddAddressMultipleTimes() {
    int baseSize = dataBase.getAddresses(company).size();
    int expectedGrowth = 1;
    int expectedSize = baseSize + expectedGrowth;

    dataBase.addAddress(company, address);
    dataBase.addAddress(company, address);

    assertEquals(expectedSize, dataBase.getAddresses(company).size());
  }

  @Test
  public void testAddAdresseWithoutCompany() {
    dataBase.addAddress(address);

    assertTrue(dataBase.getAddresses().contains(address));
  }

  @Test
  public void testDoNotFindAddressByCompany() {
    assertFalse(dataBase.getAddress(company, "Charlie").isPresent());
  }

  @Test
  public void testFindAddressByCompany() {
    String recipient = "Charlie";
    address.setRecipient(recipient);
    dataBase.addAddress(company, address);
    Optional<Address> result = dataBase.getAddress(company, recipient);
    assertTrue(result.isPresent());
    assertEquals(address, result.get());
  }
}