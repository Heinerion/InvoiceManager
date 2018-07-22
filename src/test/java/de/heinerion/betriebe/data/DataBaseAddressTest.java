package de.heinerion.betriebe.data;

import de.heinerion.betriebe.models.Address;
import de.heinerion.invoice.storage.loading.IO;
import de.heinerion.invoice.testsupport.builder.AddressBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IO.class})
@PowerMockIgnore("javax.management.*")
public class DataBaseAddressTest {
  private Address address;
  private DataBase dataBase = DataBase.getInstance();

  @Mock
  private IO io;

  @Before
  public void setUp() {
    dataBase.setIo(io);
    dataBase.clearAllLists();
    address = new AddressBuilder().build();
  }

  @Test
  public void testAddAddressAsLoadable() {
    dataBase.addLoadable(address);

    assertTrue(dataBase.getAddresses().contains(address));
  }

  @Test
  public void testAddAddressWithoutCompany() {
    dataBase.addAddress(address);

    assertTrue(dataBase.getAddresses().contains(address));
  }
}