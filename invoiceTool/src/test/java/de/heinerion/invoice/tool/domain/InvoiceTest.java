package de.heinerion.invoice.tool.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InvoiceTest {

  private Invoice invoice;

  @Before
  public void setUp() {
    Company company = new Company("Big Business");
    company.setAddress("company drive 1");

    Customer customer = new Customer("ACME");
    customer.setAddress("address line 1", "address line 2");

    invoice = new Invoice(company, "123");
    invoice.setCustomer(customer);
  }

  @Test
  public void getSum_singleItem() {
    Product product = new Product("name");
    product.setPricePerUnit(Euro.of(5));
    InvoiceItem item = new InvoiceItem(product);
    item.setCount(4);
    invoice.add(item);

    assertEquals(Euro.of(20), invoice.getSum());
  }

  @Test
  public void getSum_multipleItems() {
    Product productA = new Product("A");
    productA.setPricePerUnit(Euro.of(5));
    Product productB = new Product("B");
    productB.setPricePerUnit(Euro.of(5,75));
    Product productC = new Product("C");
    productC.setPricePerUnit(Euro.of(9,25));

    invoice.add(new InvoiceItem(productA));
    invoice.add(new InvoiceItem(productB));
    invoice.add(new InvoiceItem(productC));

    assertEquals(Euro.of(20), invoice.getSum());
  }
}