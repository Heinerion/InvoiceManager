package de.heinerion.betriebe.data;

import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.PdfArchivedInvoice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataBaseInvoiceTest {
  private ArchivedInvoice archivedInvoice;
  private DataBase dataBase;

  @Mock
  private File file;

  @Mock
  private File parent;

  @Before
  public void setUp() throws Exception {
    dataBase = DataBase.getInstance();
    // TODO the ArchivedInvoice constructor makes way too much assumptions
    when(file.getName()).thenReturn("1 Test 03.03.2016.pdf");
    when(file.getParentFile()).thenReturn(parent);
    when(parent.getName()).thenReturn("officialName");

    dataBase.clearAllLists();
    Company company = new CompanyBuilder().build();

    dataBase.addCompany(company);
    Session.setActiveCompany(company);

    archivedInvoice = new PdfArchivedInvoice(file);
  }

  @Test
  public void testAddInvoice() {
    dataBase.addInvoice(archivedInvoice);

    assertTrue(dataBase.getInvoices().contains(archivedInvoice));
  }

  @Test
  public void testAddInvoiceAsLoadable() {
    dataBase.addLoadable(archivedInvoice);

    assertTrue(dataBase.getInvoices().contains(archivedInvoice));
  }

  @Test
  public void testAddInvoiceMultipleTimes() {
    int baseSize = dataBase.getInvoices().getItemCount();
    int expectedGrowth = 1;
    int expectedSize = baseSize + expectedGrowth;

    dataBase.addInvoice(archivedInvoice);
    dataBase.addInvoice(archivedInvoice);

    assertEquals(expectedSize, dataBase.getInvoices().getItemCount());
  }
}