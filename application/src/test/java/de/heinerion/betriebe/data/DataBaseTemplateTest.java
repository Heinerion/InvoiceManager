package de.heinerion.betriebe.data;

import de.heinerion.betriebe.data.listable.InvoiceTemplate;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.storage.loading.TextFileLoader;
import de.heinerion.invoice.storage.loading.XmlLoader;
import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
// Ignore as long powermock is needed but not fixed
@Ignore
public class DataBaseTemplateTest {
  private Company company;
  private InvoiceTemplate template;
  private DataBase dataBase;

  @Mock
  private MemoryBank memoryBank;

  @Mock
  private XmlLoader xmlLoader;

  @Mock
  private TextFileLoader fileLoader;

  @Before
  public void setUp() throws Exception {
    dataBase = new DataBase(memoryBank, xmlLoader, fileLoader);
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