package de.heinerion.invoice.tool.domain;

import de.heinerion.invoice.tool.boundary.FileService;
import de.heinerion.invoice.tool.business.PrintService;
import org.easymock.Capture;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

/**
 * Unit test based on the following Story:
 * <p>
 * <b>As a</b> salesman <br>
 * <b>I want to</b> print previously created invoices and letters as pdf<br>
 * <b>so that</b> I can print or email it
 * </p>
 * <p>
 * This test only assures that something is triggered to be written to the filesystem, not exactly
 * what it is and how it looks like
 * </p>
 */
@RunWith(EasyMockRunner.class)
public class PrintTest {

  private PrintService printer = new PrintService();

  @Mock
  private FileService fileService;

  private Customer customer;
  private Company company;

  @Before
  public void setUp() {
    printer.setFileService(fileService);

    company = new Company("Big Business");
    company.setAddress("company drive 1");

    customer = new Customer("ACME");
    customer.setAddress("address line 1", "address line 2");
  }

  @Test
  public void printLetter() {
    Letter letter = new Letter(company);
    letter.setCustomer(customer);
    letter.setText("Foo bar");

    Capture<String> textCapture = prepareTextCapture();

    printer.print("Path", "file", letter);

    verify(fileService);

    String textArgument = textCapture.getValue();
    assertTrue(textArgument.contains("ACME"));
    assertTrue(textArgument.contains("address line 1"));
    assertTrue(textArgument.contains("address line 2"));
    assertTrue(textArgument.contains("Big Business"));
    assertTrue(textArgument.contains("company drive 1"));
    assertTrue(textArgument.contains("Foo bar"));
  }

  @Test
  public void printInvoice() {
    Invoice invoice = new Invoice(company, "123");
    invoice.setCustomer(customer);

    Product product = new Product("product");
    product.setPricePerUnit(new Euro(1, 50));
    invoice.add(new InvoiceItem(product));

    Capture<String> textCapture = prepareTextCapture();

    printer.print("Path", "file", invoice);

    verify(fileService);

    String textArgument = textCapture.getValue();
    assertTrue(textArgument.contains("ACME"));
    assertTrue(textArgument.contains("address line 1"));
    assertTrue(textArgument.contains("address line 2"));
    assertTrue(textArgument.contains("Big Business"));
    assertTrue(textArgument.contains("company drive 1"));
    assertTrue(textArgument.contains("1,50"));
  }

  private Capture<String> prepareTextCapture() {
    Capture<String> textCapture = newCapture();
    expect(fileService.writeTex(eq("Path"), eq("file"), capture(textCapture))).andReturn(true);
    expect(fileService.texToPdf("Path", "file")).andReturn(true);
    replay(fileService);
    return textCapture;
  }
}
