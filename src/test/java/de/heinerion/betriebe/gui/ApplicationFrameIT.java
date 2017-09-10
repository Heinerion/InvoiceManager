package de.heinerion.betriebe.gui;

import de.heinerion.betriebe.TestContext;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.*;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.tools.PathUtil;
import org.fest.swing.fixture.FrameFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.time.LocalDate;
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
    Account bankAccount = new Account("name", "bic", "iban");
    Company company = new Company("companyName", "off", null, "sign", "number", "tax", 10, 11, bankAccount);
    Address theReceiver = new Address("test", "test", "test", "test", "test");

    Letter theActiveConveyable = new Invoice(LocalDate.of(2017, Month.MAY, 1), company, theReceiver);

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
    expectedMessages.add("moveFile(\"" + baseDir + "/null/" + folder + "/companyName/1  01.05.2017.tex\", \"path\")");
    expectedMessages.add("moveFile(\"" + baseDir + "/" + folder + "/companyName/1  01.05.2017.pdf\", \"1  01.05.2017.pdf\")");

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