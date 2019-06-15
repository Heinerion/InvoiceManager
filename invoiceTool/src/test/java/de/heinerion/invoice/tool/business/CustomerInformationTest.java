package de.heinerion.invoice.tool.business;

import de.heinerion.invoice.tool.boundary.DataStore;
import de.heinerion.invoice.tool.domain.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test based on the following Story:
 * <p>
 * <b>As a</b> manager <br>
 * <b>I want to</b> see all letters and invoices assigned to a certain customer, including a total
 * for all the invoices
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

    Company company = new Company("demo");
    Letter l = new Letter(company);
    l.setCustomer(c);
    dataStore.save(l);

    Invoice i = new Invoice(company,"123");
    i.setCustomer(c);
    Product cheapProduct = new Product("cheap");
    cheapProduct.setPricePerUnit(new Euro(1, 50));
    i.add(new InvoiceItem(cheapProduct));
    Product expensiveProduct = new Product("expensive");
    expensiveProduct.setPricePerUnit(new Euro(3, 75));
    i.add(new InvoiceItem(expensiveProduct));
    dataStore.save(i);

    CustomerInformation ci = dataStore.getCustomerInformation(c);
    assertNotNull("There is Information", ci);
    assertEquals("There is exactly one letter", 1, ci.getLetters().size());
    assertEquals("There is exactly one invoice", 1, ci.getInvoices().size());
    assertEquals("The invoice total is 5.25 €", new Euro(5, 25), ci.getInvoiceTotal());
  }
}
