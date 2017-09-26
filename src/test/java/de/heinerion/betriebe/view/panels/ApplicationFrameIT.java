package de.heinerion.betriebe.view.panels;

import de.heinerion.betriebe.TestContext;
import de.heinerion.betriebe.builder.AddressBuilder;
import de.heinerion.betriebe.builder.CompanyBuilder;
import de.heinerion.betriebe.builder.InvoiceBuilder;
import de.heinerion.betriebe.builder.SessionPreparer;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.betriebe.util.PathUtil;
import de.heinerion.util.Translator;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationFrameIT {
  private FrameFixture demo;

  @Before
  public void setUp() {
    ApplicationFrame applicationFrame = ConfigurationService.getBean(ApplicationFrame.class);

    JFrame frame = applicationFrame.getFrame();
    frame.setVisible(true);
    demo = new FrameFixture(frame);

    prepareSession();
  }

  private static void prepareSession() {
    Company company = new CompanyBuilder().build();
    Address theReceiver = new AddressBuilder().withRecipient("").build();

    new SessionPreparer()
        .withActiveCompany(company)
        .withActiveAddress(theReceiver)
        .withActiveConveyable(new InvoiceBuilder()
            .withDateOf(2017, Month.MAY, 1)
            .withCompany(company)
            .withReceiver(theReceiver)
        )
        .prepare();
  }

  @After
  public void tearDown() {
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