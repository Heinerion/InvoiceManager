package de.heinerion.invoice.print;

import de.heinerion.invoice.models.*;
import de.heinerion.invoice.util.PathUtilNG;
import org.easymock.EasyMock;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class PrintOperationsTest {

  @Test
  public void createDocumentLater_letter() {
    TestPrinter printer = new TestPrinter();
    PathUtilNG pathUtil = mockPathUtil();

    Letter letter = new Letter()
        .setSubject("Subject")
        .setReceiver(new Address().setName("Recipient"))
        .setDate(LocalDate.of(2022, 8, 30));

    new PrintOperations(printer, pathUtil).createDocumentLater(letter);

    assertEquals(letter, printer.conveyable);
    assertEquals("Subject -- Recipient 30.08.2022", printer.title);
    assertEquals("letterDir", printer.parentFolder.toString());
  }

  @Test
  public void createDocumentLater_invoice() {
    TestPrinter printer = new TestPrinter();
    PathUtilNG pathUtil = mockPathUtil();

    Invoice invoice = new Invoice()
        .setNumber(1)
        .setCompany(new Company().setDescriptiveName("descriptiveName"))
        .setReceiver(new Address().setName("Recipient"))
        .setDate(LocalDate.of(2022, 8, 30));

    new PrintOperations(printer, pathUtil).createDocumentLater(invoice);

    assertEquals(invoice, printer.conveyable);
    assertEquals("1 Recipient 30.08.2022", printer.title);
    assertEquals("invoiceDir" + File.separator + "descriptiveName",
        printer.parentFolder.toString());
  }

  private PathUtilNG mockPathUtil() {
    PathUtilNG pathUtil = EasyMock.niceMock(PathUtilNG.class);
    EasyMock
        .expect(pathUtil.determineLetterPath())
        .andReturn(Path.of("letterDir"))
        .anyTimes();
    EasyMock
        .expect(pathUtil.determineInvoicePath(EasyMock.anyObject(Company.class)))
        .andAnswer(() -> Path
            .of("invoiceDir")
            .resolve(((Company) EasyMock.getCurrentArguments()[0]).getDescriptiveName()))
        .anyTimes();
    EasyMock.replay(pathUtil);
    return pathUtil;
  }

  private final class TestPrinter implements Printer {
    private Conveyable conveyable;
    private Path parentFolder;
    private String title;

    @Override
    public void writeFile(Conveyable conveyable, Path parentFolder, String title) {
      System.out.println(conveyable + "::" + title + " @ " + parentFolder);
      this.conveyable = conveyable;
      this.parentFolder = parentFolder;
      this.title = title;
    }
  }
}