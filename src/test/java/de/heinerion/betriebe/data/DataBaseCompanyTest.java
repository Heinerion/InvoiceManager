package de.heinerion.betriebe.data;

import de.heinerion.betriebe.classes.file_operations.IO;
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
@PrepareForTest({IO.class, Session.class})
@PowerMockIgnore("javax.management.*")
public class DataBaseCompanyTest {
  private Company company;

  @Before
  public void setUp() throws Exception {
    mockStatic(IO.class);
    mockStatic(Session.class);
    DataBase.clearAllLists();
    company = new Company("desc", "off", null, "sign", "number", "tax", 10, 11, null);
  }

  @Test
  public void testAddCompany() {
    DataBase.addCompany(company);

    assertTrue(DataBase.getCompanies().contains(company));
  }

  @Test
  public void testAddCompanyAsLoadable() {
    DataBase.addLoadable(company);

    assertTrue(DataBase.getCompanies().contains(company));
  }

  @Test
  public void testAddCompanyMultipleTimes() {
    int baseSize = DataBase.getCompanies().size();
    int expectedGrowth = 1;
    int expectedSize = baseSize + expectedGrowth;

    DataBase.addCompany(company);
    DataBase.addCompany(company);

    assertEquals(expectedSize, DataBase.getCompanies().size());
  }

  @Test
  public void testDoNotFindAddressByCompany() {
    Company result = DataBase.getCompany("dummy");
    assertNull(result);
  }

  @Test
  public void testFindAddressByCompany() {
    DataBase.addCompany(company);
    Company result = DataBase.getCompany(company.getDescriptiveName());
    assertEquals(company, result);
  }
}