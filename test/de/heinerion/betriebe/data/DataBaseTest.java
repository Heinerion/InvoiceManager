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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IO.class})
@PowerMockIgnore("javax.management.*")
public class DataBaseTest {
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
}