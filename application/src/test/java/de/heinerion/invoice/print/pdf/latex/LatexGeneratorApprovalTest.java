package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.testsupport.builder.*;
import org.approvaltests.Approvals;
import org.junit.*;

import java.time.*;

public class LatexGeneratorApprovalTest {
  private static final Month MONTH = Month.JUNE;
  private static final int YEAR = 2010;
  private static final int DAY = 25;

  private static Company sender;
  private static Address receiverAddress;
  private static LocalDate date;

  private static Letter letter;
  private static Invoice invoice;

  @BeforeClass
  public static void prepare() {
    sender = new CompanyBuilder()
        .withAddress(new AddressBuilder()
            .withName("Bob's Address")
            .withBlock("Bob Bubbles\n\nstreet number\npostalCode location"))
        .build();
    receiverAddress = new AddressBuilder()
        .withName("Receiver's Address")
        .withBlock("Receiver\nStreet 7\n\nPC Somewhere©")
        .build();
    date = LocalDate.of(YEAR, MONTH, DAY);
  }

  @Before
  public final void setUp() {
    letter = new Letter(date, sender, receiverAddress);
    letter.setSubject("Test");

    invoice = new Invoice(date, sender, receiverAddress, sender.getInvoiceNumber());
    invoice.addItem(Item.of(invoice.getItems().size(), "Artikel 1", "Stück", 1.50, 2));
  }

  protected LatexGenerator getLatexGenerator() {
    return new LatexGeneratorFreeMarkerImpl();
  }

  @Test
  public final void generateEmptyLetter() {
    final String result = getLatexGenerator().generateSourceContent(letter);

    Approvals.verify(result);
  }

  @Test
  public final void generateNonEmptyLetter() {
    String content = "Hello new World";
    letter.addMessageLine(content);
    letter.addMessageLine(content);
    letter.addMessageLine(content);
    letter.addMessageLine(content);
    String result = getLatexGenerator().generateSourceContent(letter);

    Approvals.verify(result);
  }

  @Test
  public final void generateInvoice() {
    final String result = getLatexGenerator().generateSourceContent(invoice);

    Approvals.verify(result);
  }

  @Test
  public final void generateInvoice_items() {
    invoice.addItem(Item.of(invoice.getItems().size(), "Artikel 2", "Stück", 3.44, 3));
    final String result = getLatexGenerator().generateSourceContent(invoice);

    Approvals.verify(result);
  }

  @Test
  public final void generateInvoice_text() {
    invoice.addItem(Item.of(invoice.getItems().size(), "Message"));

    final String result = getLatexGenerator().generateSourceContent(invoice);

    Approvals.verify(result);
  }
}
