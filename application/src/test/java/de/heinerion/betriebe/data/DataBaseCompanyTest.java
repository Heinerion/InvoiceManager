package de.heinerion.betriebe.data;

import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.storage.loading.IO;
import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IO.class, Session.class})
@PowerMockIgnore("javax.management.*")
// Ignore as long powermock is needed but not fixed
@Ignore
public class DataBaseCompanyTest {
  private Company company;
  private DataBase dataBase = DataBase.getInstance();

  @Before
  public void setUp() {
    mockStatic(IO.class);
    mockStatic(Session.class);
    dataBase.clearAllLists();
    company = new CompanyBuilder().build();
  }

  @Test
  public void testAddCompanyAsLoadable() {
    dataBase.addLoadable(company);

    assertTrue(dataBase.getCompanies().contains(company));
  }
}