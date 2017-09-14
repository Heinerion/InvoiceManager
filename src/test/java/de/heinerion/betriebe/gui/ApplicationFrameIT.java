package de.heinerion.betriebe.gui;

import de.heinerion.betriebe.TestContext;
import de.heinerion.betriebe.builder.AddressBuilder;
import de.heinerion.betriebe.builder.CompanyBuilder;
import de.heinerion.betriebe.builder.InvoiceBuilder;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.*;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.util.PathUtil;
import org.fest.swing.fixture.FrameFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ApplicationFrameIT {
  private static FrameFixture demo;

  @BeforeClass
  public static void setUp() {
    JFrame frame = ApplicationFrame.getInstance();
    frame.setVisible(true);
    demo = new FrameFixture(frame);

    prepareSession();
  }

  private static void prepareSession() {
    Company company = new CompanyBuilder().build();
    Address theReceiver = new AddressBuilder().withRecipient("").build();

    Letter theActiveConveyable = new InvoiceBuilder()
        .withDateOf(2017, Month.MAY, 1)
        .withCompany(company)
        .withReceiver(theReceiver)
        .build();

    Session.setActiveAddress(theReceiver);
    Session.setActiveCompany(company);

    Session.setActiveConveyable(theActiveConveyable);
  }

  @AfterClass
  public static void tearDown() {
    demo.cleanUp();
  }

  @Test
  public void testPrintButtonText() {
    demo.button("print").requireText("Drucken");
  }

  @Test
  public void testFirePrintButton() {
    demo.button("print").click();

    // check
    List<String> expectedMessages = new ArrayList<>();

    expectedMessages.add("pdflatex \"path\"");

    String baseDir = PathUtil.getBaseDir();
    String folder = Translator.translate("invoice.title");
    expectedMessages.add("moveFile(\"" + baseDir + "/null/" + folder + "/descriptiveName/1  01.05.2017.tex\", \"path\")");
    expectedMessages.add("moveFile(\"" + baseDir + "/" + folder + "/descriptiveName/1  01.05.2017.pdf\", \"1  01.05.2017.pdf\")");

    expectedMessages.add("deleteFile(\"1  01.05.2017.aux\")");
    expectedMessages.add("deleteFile(\"1  01.05.2017.log\")");
    expectedMessages.add("deleteFile(\"1  01.05.2017.out\")");

    TestContext.assertEquals(expectedMessages);
  }

  @Test
  public void testCalculatorPanel() {
    demo.label("calculator.netLabel").requireText(Translator.translate("invoice.net"));
  }
}