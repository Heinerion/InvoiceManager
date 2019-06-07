package de.heinerion.invoice.tool.domain;

import de.heinerion.invoice.tool.boundary.DataStore;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Unit test based on the following Story:
 * <p>
 * <b>As a</b> salesman <br>
 * <b>I want to</b> add, read, edit and delete customer details like name, address and, optionally,
 * correspondent <br>
 * <b>so that</b> I can easily send letters and invoices to them
 * </p>
 */
public class ManageCustomersTest {

  private DataStore dataStore;

  @Before
  public void setUp() {
    dataStore = new DataStore();
  }

  @Test
  public void addNewCustomer() {
    Customer newCustomer = new Customer("ACME");
    newCustomer.setAddress("some road 75", "some city");
    newCustomer.setCorrespondent("Mr. Anderson");

    dataStore.save(newCustomer);

    Collection<Customer> customers = dataStore.getCustomers();
    assertTrue("the new customer was saved", customers.contains(newCustomer));

    Customer loadedCustomer = dataStore.getCustomer("ACME").orElse(null);
    assertEquals("The loaded customer is equal to the original one", newCustomer, loadedCustomer);
    assertEquals("ACME", loadedCustomer.getName());
    assertEquals(Arrays.asList("some road 75", "some city"), loadedCustomer.getAddress());
    assertEquals(Optional.of("Mr. Anderson"), loadedCustomer.getCorrespondent());
  }

  @Test
  public void editCustomer() {
    Customer newCustomer = new Customer("ACME");
    newCustomer.setAddress("some road 75", "some city");
    newCustomer.setCorrespondent("Mr. Anderson");
    dataStore.save(newCustomer);

    Customer loadedCustomer = dataStore.getCustomer("ACME").orElse(null);
    assertEquals("The original correspondent is set", Optional.of("Mr. Anderson"), loadedCustomer.getCorrespondent());

    newCustomer.setCorrespondent("Mr. Snow");
    dataStore.save(newCustomer);

    loadedCustomer = dataStore.getCustomer("ACME").orElse(null);
    assertEquals("The changed correspondent is set", Optional.of("Mr. Snow"), loadedCustomer.getCorrespondent());
  }

  @Test
  public void deleteCustomer() {
    Customer newCustomer = new Customer("ACME");
    newCustomer.setAddress("some road 75", "some city");
    dataStore.save(newCustomer);

    assertTrue("The customer was saved", dataStore.getCustomers().contains(newCustomer));

    assertTrue("The customer was removed", dataStore.delete(newCustomer));
    assertFalse("No traces are left", dataStore.getCustomers().contains(newCustomer));
  }
}
