package de.heinerion.betriebe.data;

import de.heinerion.betriebe.classes.file_operations.IO;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IO.class})
@PowerMockIgnore("javax.management.*")
public class DataBaseAddressTest {
  private Company company;
  private Address address;

  @Before
  public void setUp() throws Exception{
    mockStatic(IO.class);
    DataBase.clearAllLists();
    company = new Company("desc", "off", null, "sign", "number", "tax", 10, 11, null);
    address = new Address("bla", "street", "nr", "12345", "location");
  }

  @Test
  public void testAddAddressWithCompany() {
    DataBase.addAddress(company, address);

    assertTrue(DataBase.getAddresses(company).contains(address));
  }

  @Test
  public void testAddAddressAsLoadable() {
    DataBase.addLoadable(address);

    assertTrue(DataBase.getAddresses(null).contains(address));
  }

  @Test
  public void testAddAddressMultipleTimes() {
    int baseSize = DataBase.getAddresses(company).size();
    int expectedGrowth = 1;
    int expectedSize = baseSize + expectedGrowth;

    DataBase.addAddress(company, address);
    DataBase.addAddress(company, address);

    assertEquals(expectedSize, DataBase.getAddresses(company).size());
  }

  @Test
  public void testAddAdresseWithoutCompany() {
    DataBase.addAdresse(address);

    assertTrue(DataBase.getAddresses().contains(address));
  }

  @Test
  public void testDoNotFindAddressByCompany() {
    Address result = DataBase.getAddress(company, "Charlie");
    assertNull(result);
  }

  @Test
  public void testFindAddressByCompany() {
    String recipient = "Charlie";
    address.setRecipient(recipient);
    DataBase.addAddress(company, address);
    Address result = DataBase.getAddress(company, recipient);
    assertEquals(address, result);
  }
}