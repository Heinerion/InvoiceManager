package de.heinerion.invoice.tool.domain;


import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class InvoiceTest {

  private Invoice invoice;

  @BeforeEach
  public void setUp() {
    Company company = new Company("Big Business");
    company.setAddress("company drive 1");

    Customer customer = new Customer("ACME");
    customer.setAddress("address line 1", "address line 2");

    invoice = new Invoice(company, "123");
    invoice.setCustomer(customer);
  }

  @Test
  void getSums_singleItem() {
    Product product = new Product("name", new Percent(19));
    product.setPricePerUnit(Euro.of(5));
    InvoiceItem item = new InvoiceItem(product);
    item.setCount(4);
    invoice.add(item);

    assertEquals(Euro.of(20), invoice.getNetSum());
  }

  @Test
  void getSums_multipleItems() {
    Product productA = new Product("A", new Percent(19));
    productA.setPricePerUnit(Euro.of(5));
    Product productB = new Product("B", new Percent(19));
    productB.setPricePerUnit(Euro.of(5, 75));
    Product productC = new Product("C", new Percent(7));
    productC.setPricePerUnit(Euro.of(9, 25));

    invoice.add(new InvoiceItem(productA));
    invoice.add(new InvoiceItem(productB));
    invoice.add(new InvoiceItem(productC));

    assertEquals(Euro.of(20), invoice.getNetSum());
    assertEquals(Euro.of(22, 68), invoice.getGrossSum());

    Map<Percent, Euro> taxSums = new HashMap<>();
    taxSums.put(new Percent(7), Euro.fromCents(64));
    taxSums.put(new Percent(19), Euro.of(2, 4));

    assertEquals(taxSums, invoice.getTaxes());
  }
}