package de.heinerion.invoice.tool.domain;

import de.heinerion.invoice.tool.boundary.FileService;
import de.heinerion.invoice.tool.business.PrintService;
import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Collection;

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
@RunWith(Parameterized.class)
public class PrintDocumentsTest {

  private final Document document;

  private PrintService printer = new PrintService();

  private FileService fileService;

  private Capture<String> textCapture;

  public PrintDocumentsTest(Document doc) {
    this.document = doc.copy();
  }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Document> data() {
    Company company = new Company("Big Business");
    company.setAddress("company drive 1");
    company.setPhone("123-456/789");
    company.setTaxNumber("myTaxNumber");

    company.setIban("myIBAN");
    company.setBic("myBIC");
    company.setBankName("Awesome Bank");

    Customer customer = new Customer("ACME");
    customer.setAddress("address line 1", "address line 2");

    Letter letter = new Letter(company, "special subject");
    letter.setCustomer(customer);
    letter.setText("Foo bar");

    Invoice invoice = new Invoice(company, "123");
    invoice.setCustomer(customer);

    return Arrays.asList(letter, invoice);
  }

  @Before
  public void setUp() {
    fileService = createNiceMock(FileService.class);

    printer.setFileService(fileService);

    textCapture = prepareTextCapture();
  }

  @After
  public void tearDown() {
    verify(fileService);
  }

  @Test
  public void printDocument_containsCustomerAddress() {
    printer.print("Path", "file", document);

    String textArgument = textCapture.getValue();
    assertTrue(textArgument.contains("ACME"));
    assertTrue(textArgument.contains("address line 1"));
    assertTrue(textArgument.contains("address line 2"));
  }

  @Test
  public void printDocument_containsDate() {
    printer.print("Path", "file", document);

    String textArgument = textCapture.getValue();
    String date = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    assertTrue(date + " is contained in \n" + textArgument, textArgument.contains(date));
  }

  @Test
  public void printDocument_containsSpecificDate() {
    document.setDate(LocalDate.now().minusYears(1));
    printer.print("Path", "file", document);

    String textArgument = textCapture.getValue();
    String date = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    assertTrue(textArgument.contains(date));
  }

  @Test
  public void printDocument_containsCompanyInformation() {
    printer.print("Path", "file", document);

    String textArgument = textCapture.getValue();
    assertTrue(textArgument.contains("Big Business"));
    assertTrue(textArgument.contains("company drive 1"));
    assertTrue(textArgument.contains("123-456/789"));
  }

  @Test
  public void printDocument_containsKeywords() {
    document.addKeyword("keyword");
    printer.print("Path", "file", document);

    String textArgument = textCapture.getValue();
    assertTrue(textArgument.contains("keyword"));
  }

  private Capture<String> prepareTextCapture() {
    Capture<String> textCapture = newCapture();
    expect(fileService.writeTex(eq("Path"), eq("file"), capture(textCapture))).andReturn(true);
    expect(fileService.texToPdf("Path", "file")).andReturn(true);
    replay(fileService);
    return textCapture;
  }
}
