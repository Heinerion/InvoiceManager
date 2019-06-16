package de.heinerion.invoice.tool.domain;

import de.heinerion.invoice.tool.boundary.DataStore;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

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
public class ManageProductsTest {

  private DataStore dataStore;

  @Before
  public void setUp() {
    dataStore = new DataStore();
  }

  @Test
  public void addNewProduct() {
    Product newProduct = createDefaultProduct();
    dataStore.save(newProduct);

    Collection<Product> products = dataStore.getProducts();
    assertTrue("the new product was saved", products.contains(newProduct));

    Product loadedProduct = dataStore.getProduct("Bread").orElse(null);
    assertEquals("The loaded product is equal to the original one", newProduct, loadedProduct);
    assertNotNull(loadedProduct);
    assertEquals("Bread", loadedProduct.getName());
    assertEquals("pc", loadedProduct.getUnit());
    assertEquals(Euro.of(1, 10), loadedProduct.getPricePerUnit());
    assertEquals(new Percent(7), loadedProduct.getTaxes());
  }

  @Test
  public void editProduct() {
    Product newProduct = createDefaultProduct();
    dataStore.save(newProduct);

    Product loadedProduct = dataStore.getProduct("Bread").orElse(null);
    assertEquals("The original unit is set", "pc", loadedProduct.getUnit());

    newProduct.setUnit("kg");
    dataStore.save(newProduct);

    loadedProduct = dataStore.getProduct("Bread").orElse(null);
    assertEquals("The changed unit is set", "kg", loadedProduct.getUnit());
  }

  @Test
  public void deleteProduct() {
    Product newProduct = createDefaultProduct();
    dataStore.save(newProduct);

    assertTrue("The customer was saved", dataStore.getProducts().contains(newProduct));

    assertTrue("The customer was removed", dataStore.delete(newProduct));
    assertFalse("No traces are left", dataStore.getProducts().contains(newProduct));
  }

  private Product createDefaultProduct() {
    Product product = new Product("Bread");
    product.setUnit("pc");
    product.setPricePerUnit(Euro.of(1, 10));
    product.setTaxes(new Percent(7));
    return product;
  }
}
