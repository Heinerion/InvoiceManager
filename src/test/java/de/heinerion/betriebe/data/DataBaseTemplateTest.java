package de.heinerion.betriebe.data;

import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.invoice.storage.loading.IO;
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
public class DataBaseTemplateTest {
  private Company company;
  private InvoiceTemplate template;

  @Before
  public void setUp() throws Exception {
    mockStatic(IO.class);
    DataBase.clearAllLists();
    company = new CompanyBuilder().build();
    template = new InvoiceTemplate();
  }

  @Test
  public void testAddTemplateWithCompany() {
    DataBase.addTemplate(company, template);

    assertTrue(DataBase.getTemplates(company).contains(template));
  }

  @Test
  public void testAddTemplatesMultipleTimes() {
    int baseSize = DataBase.getTemplates(company).size();
    int expectedGrowth = 1;
    int expectedSize = baseSize + expectedGrowth;

    DataBase.addTemplate(company, template);
    DataBase.addTemplate(company, template);

    assertEquals(expectedSize, DataBase.getTemplates(company).size());
  }
}