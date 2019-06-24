package de.heinerion.invoice.tool.domain;

import de.heinerion.invoice.tool.boundary.FileService;
import de.heinerion.invoice.tool.boundary.Translator;
import de.heinerion.invoice.tool.business.PrintService;
import org.easymock.Capture;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.After;
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

  private Capture<String> textCapture;
  private Letter letter;
  private Invoice invoice;

  @Before
  public void setUp() {
    printer.setFileService(fileService);

    company = new Company("Big Business");
    company.setAddress("company drive 1");

    customer = new Customer("ACME");
    customer.setAddress("address line 1", "address line 2");

    letter = new Letter(company, "special subject");
    letter.setCustomer(customer);
    letter.setText("Foo bar");

    invoice = new Invoice(company, "123");
    invoice.setCustomer(customer);

    textCapture = prepareTextCapture();
  }

  @After
  public void tearDown() {
    verify(fileService);
  }

  @Test
  public void printLetter_containsGivenText() {
    printer.print("Path", "file", letter);

    String textArgument = textCapture.getValue();
    assertTrue(textArgument.contains("Foo bar"));
  }

  @Test
  public void printInvoice_containsItems() {
    Product product = new Product("product", new Percent(19));
    product.setPricePerUnit(Euro.of(1, 50));
    invoice.add(new InvoiceItem(product));

    printer.print("Path", "file", invoice);

    String textArgument = textCapture.getValue();
    assertTrue(textArgument.contains("1,50"));
  }

  @Test
  public void printInvoice_containsItemsSum() {
    Product product = new Product("product", new Percent(19));
    product.setPricePerUnit(Euro.of(1, 50));
    InvoiceItem item = new InvoiceItem(product);
    item.setCount(2);
    invoice.add(item);

    Product productB = new Product("product", new Percent(7));
    productB.setPricePerUnit(Euro.of(29, 99));
    invoice.add(new InvoiceItem(productB));

    printer.print("Path", "file", invoice);

    String textArgument = textCapture.getValue();
    assertTrue(textArgument.contains("3,00"));
    assertTrue(textArgument.contains("29,99"));
    assertTrue(textArgument.contains("32,99"));
  }

  @Test
  public void printLetter_containsSubject() {
    printer.print("Path", "file", letter);

    String textArgument = textCapture.getValue();
    assertTrue(textArgument.contains("special subject"));
  }

  @Test
  public void printInvoice_containsSubjectInvoice() {
    printer.print("Path", "file", invoice);

    String textArgument = textCapture.getValue();
    assertTrue(textArgument.contains(Translator.translate("invoice")));
  }

  private Capture<String> prepareTextCapture() {
    Capture<String> textCapture = newCapture();
    expect(fileService.writeTex(eq("Path"), eq("file"), capture(textCapture))).andReturn(true);
    expect(fileService.texToPdf("Path", "file")).andReturn(true);
    replay(fileService);
    return textCapture;
  }
}
