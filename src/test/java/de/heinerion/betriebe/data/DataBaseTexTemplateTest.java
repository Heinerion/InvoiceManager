package de.heinerion.betriebe.data;

import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import de.heinerion.betriebe.data.listable.TexTemplate;
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
@PowerMockIgnore({"javax.management.*", "javax.swing.*"})
public class DataBaseTexTemplateTest {
  private Company company;
  private TexTemplate template;

  @Before
  public void setUp() throws Exception {
    mockStatic(IO.class);
    DataBase.clearAllLists();
    company = new CompanyBuilder().build();
    template = new TexTemplate("basePath","template");
  }

  @Test
  public void testAddTemplateWithCompany() {
    DataBase.addTexTemplate(company, template);

    assertTrue(DataBase.getTexTemplates(company).contains(template));
  }

  @Test
  public void testAddTemplatesMultipleTimes() {
    int baseSize = DataBase.getTexTemplates(company).size();
    int expectedGrowth = 1;
    int expectedSize = baseSize + expectedGrowth;

    DataBase.addTexTemplate(company, template);
    DataBase.addTexTemplate(company, template);

    assertEquals(expectedSize, DataBase.getTexTemplates(company).size());
  }
}