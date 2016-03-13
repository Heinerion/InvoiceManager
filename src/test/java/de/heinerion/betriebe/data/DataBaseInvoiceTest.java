package de.heinerion.betriebe.data;

import de.heinerion.betriebe.classes.data.RechnungData;
import de.heinerion.betriebe.classes.fileoperations.IO;
import de.heinerion.betriebe.models.Company;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IO.class})
@PowerMockIgnore("javax.management.*")
public class DataBaseInvoiceTest {
  private RechnungData rechnungData;
  private Company company;

  @Mock
  private File file;

  @Mock
  private File parent;

  @Before
  public void setUp() throws Exception {
    mockStatic(IO.class);

    // TODO the RechnungData constructor makes way too much assumptions
    when(file.getName()).thenReturn("1 Test 03.03.2016.pdf");
    when(file.getParentFile()).thenReturn(parent);
    when(parent.getName()).thenReturn("Heinerion");

    DataBase.clearAllLists();
    company = new Company("desc", "off", null, "sign", "number", "tax", 10, 11, null);
    rechnungData = new RechnungData(file);

    DataBase.addCompany(company);
  }

  @Test
  public void testAddInvoice() {
    DataBase.addInvoice(rechnungData);

    assertTrue(DataBase.getInvoices().contains(rechnungData));
  }

  @Test
  public void testAddInvoiceAsLoadable() {
    DataBase.addLoadable(rechnungData);

    assertTrue(DataBase.getInvoices().contains(rechnungData));
  }

  @Test
  public void testAddInvoiceMultipleTimes() {
    int baseSize = DataBase.getInvoices().getAnzahlElemente();
    int expectedGrowth = 1;
    int expectedSize = baseSize + expectedGrowth;

    DataBase.addInvoice(rechnungData);
    DataBase.addInvoice(rechnungData);

    assertEquals(expectedSize, DataBase.getInvoices().getAnzahlElemente());
  }
}