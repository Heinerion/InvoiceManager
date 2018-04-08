package de.heinerion.betriebe.data;

import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.storage.loading.IO;
import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IO.class, Session.class})
@PowerMockIgnore("javax.management.*")
public class DataBaseCompanyTest {
  private Company company;
  private DataBase dataBase = DataBase.getInstance();

  @Before
  public void setUp() throws Exception {
    mockStatic(IO.class);
    mockStatic(Session.class);
    dataBase.clearAllLists();
    company = new CompanyBuilder().build();
  }

  @Test
  public void testAddCompany() {
    dataBase.addCompany(company);

    assertTrue(dataBase.getCompanies().contains(company));
  }

  @Test
  public void testAddCompanyAsLoadable() {
    dataBase.addLoadable(company);

    assertTrue(dataBase.getCompanies().contains(company));
  }

  @Test
  public void testAddCompanyMultipleTimes() {
    int baseSize = dataBase.getCompanies().size();
    int expectedGrowth = 1;
    int expectedSize = baseSize + expectedGrowth;

    dataBase.addCompany(company);
    dataBase.addCompany(company);

    assertEquals(expectedSize, dataBase.getCompanies().size());
  }

  @Test
  public void testDoNotFindAddressByCompany() {
    assertFalse(dataBase.getCompany("dummy").isPresent());
  }

  @Test
  public void testFindAddressByCompany() {
    dataBase.addCompany(company);
    Optional<Company> result = dataBase.getCompany(company.getDescriptiveName());
    assertTrue(result.isPresent());
    assertEquals(company, result.get());
  }
}