package de.heinerion.invoice.tool.domain;

import de.heinerion.invoice.tool.boundary.DataStore;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test based on the following Story:
 * <p>
 * <b>As a</b> salesman <br>
 * <b>I want to</b> add items to an invoice and save it <br>
 * <b>so that</b> I can print and send it later
 * </p>
 */
public class WriteInvoiceTest {
  private DataStore dataStore;

  @Before
  public void setUp() {
    dataStore = new DataStore();
  }

  @Test
  public void writeNewInvoice() {
    Invoice invoice = new Invoice(new Company("demo"),"123");

    InvoiceItem firstItem = new InvoiceItem(new Product("nothing"));
    invoice.add(firstItem);

    dataStore.save(invoice);

    assertTrue("The new invoice was saved", dataStore.getInvoices().contains(invoice));
    Invoice loadedInvoice = dataStore.getInvoice("123").orElse(null);
    assertTrue("The new invoice contains the item", loadedInvoice.getItems().contains(firstItem));
  }
}
