package de.heinerion.betriebe.data;

import de.heinerion.betriebe.data.listable.InvoiceTemplate;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IO.class})
@PowerMockIgnore("javax.management.*")
// Ignore as long powermock is needed but not fixed
@Ignore
public class DataBaseTemplateTest {
  private Company company;
  private InvoiceTemplate template;
  private DataBase dataBase = DataBase.getInstance();

  @Before
  public void setUp() throws Exception {
    mockStatic(IO.class);
    dataBase.clearAllLists();
    company = new CompanyBuilder().build();
    template = new InvoiceTemplate();
  }

  @Test
  public void testAddTemplateWithCompany() {
    dataBase.addTemplate(company, template);

    assertTrue(dataBase.getTemplates(company).contains(template));
  }

  @Test
  public void testAddTemplatesMultipleTimes() {
    int baseSize = dataBase.getTemplates(company).size();
    int expectedGrowth = 1;
    int expectedSize = baseSize + expectedGrowth;

    dataBase.addTemplate(company, template);
    dataBase.addTemplate(company, template);

    assertEquals(expectedSize, dataBase.getTemplates(company).size());
  }
}