package de.heinerion.invoice.tool;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test based on the following Story:
 * <p>
 * <b>As a</b> manager <br>
 * <b>I want to</b> see all letters and invoices assigned to a certain customer, including a total for all the invoices
 * <br>
 * <b>so that</b> I can see all transactions with this customer in one place
 * </p>
 */
public class CustomerInformationTest {

  private DataStore dataStore;

  @Before
  public void setUp() {
    dataStore = new DataStore();
  }

  @Test
  public void retrieveCustomerInformation() {
    Customer c = new Customer("ACME");
    dataStore.save(c);

    Letter l = new Letter();
    l.setCustomer(c);
    dataStore.save(l);

    Invoice i = new Invoice("123");
    i.setCustomer(c);
    InvoiceItem item = new InvoiceItem();
    item.setPrice(new Euro(1, 50));
    i.add(item);
    item = new InvoiceItem();
    item.setPrice(new Euro(3, 75));
    i.add(item);
    dataStore.save(i);

    CustomerInformation ci = dataStore.getCustomerInformation(c);
    assertNotNull("There is Information", ci);
    assertEquals("There is exactly one letter", 1, ci.getLetters().size());
    assertEquals("There is exactly one invoice", 1, ci.getInvoices().size());
    assertEquals("The invoice total is 5.25 €", new Euro(5, 25), ci.getInvoiceTotal());
  }
}
