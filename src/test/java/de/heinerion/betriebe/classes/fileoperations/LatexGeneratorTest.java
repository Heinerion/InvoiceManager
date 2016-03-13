package de.heinerion.betriebe.classes.fileoperations;

import de.heinerion.betriebe.models.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

public class LatexGeneratorTest {
  private static final Month MONTH = Month.JUNE;
  private static final int YEAR = 2010;
  private static final int DAY = 25;

  private static final int DOUBLE_DIGIT = 10;

  private static final double TAX_PERCENT = 10;
  private static final double WAGES_PER_H = 20;

  private static final String HLINE = "\\hline";

  private static final String DOCCLASS = "\\documentclass"
      + "[fontsize=12pt, fromalign=center, fromphone=true, paper=a4]"
      + "{scrlttr2}\n";
  private static final String PACKAGES = "\\usepackage[utf8]{inputenc}\n"
      + "\\usepackage[ngermanb]{babel}\n" + "\\usepackage[right]{eurosym}\n"
      + "\\usepackage{hyperref}\n";
  private static final String RENEW = "\\renewcommand{\\raggedsignature}"
      + "{\\raggedright}\n";
  private static final String KOMA_SIGNATURE = "\\setkomavar{signature}"
      + "{\\underline{Unterschrift:\\hspace{10cm}}}\n";
  private static final String KOMA_FROMADDRESS = "\\setkomavar{fromaddress}"
      + "{Senderstraße 1, 12345 Senderort}\n"
      + "\\setkomavar{fromphone}{038355}\n"
      + "\\setkomavar{fromname}{{Offizieller Name GmbH}\\tiny}\n";
  private static final String DATE_START = "\\date{\n"
      + "\t\\footnotesize \n"
      + "\t\\begin{tabular}{ll}\n"
      + "\t\\textsc Datum & : "
      + String.join(".", DAY + "", (MONTH.getValue() < DOUBLE_DIGIT ? "0" : "")
      + MONTH.getValue(), YEAR + "");
  private static final String DATE_END = "\t\\end{tabular}\n" + "}\n";
  private static final String DOC_START = "\\begin{document}\n";
  private static final String DOC_END = "\\end{document}";
  private static final String EMPTY_LINE = "\\multicolumn{1}{|l}{$\\phantom{iwas}$}&&&&\\\\\n"
      + HLINE + Syntax.EOL;

  private static final String LETTER_START = "\\begin{letter}{Empfänger\\\\\n"
      + "Empfängerfirma\\\\\n" + "Empfängerstraße 2\\\\\n"
      + "14785 Empfängerort}\n\n" + "\\opening{}\\vspace{-25pt}\n";
  private static final String LETTER_END = "\\vfill\n" + "\\closing{}\n" + "\n"
      + "\\end{letter}\n";

  private static final String EXPECTATION_LETTER_START = DOCCLASS + PACKAGES
      + "\\hypersetup{pdftitle={Brief}, pdfauthor={Offizieller Name GmbH}, "
      + "pdfsubject={Test}, pdfkeywords={0.00}}\n" + RENEW + KOMA_SIGNATURE
      + "\\setkomavar{subject}{Test}\n" + KOMA_FROMADDRESS + DATE_START
      + Syntax.EOL + DATE_END + DOC_START + LETTER_START;
  private static final String EXPECTATION_LETTER_END = LETTER_END + DOC_END;

  private static final String EXPECTATION_INVOICE = DOCCLASS
      + PACKAGES
      + "\\hypersetup{pdftitle={Rechnung}, pdfauthor={Offizieller Name GmbH}, "
      + "pdfsubject={Artikel 1}, pdfkeywords={3.30}}\n"
      + RENEW
      + KOMA_SIGNATURE
      + "\\setkomavar{subject}{Rechnung}\n"
      + KOMA_FROMADDRESS
      + DATE_START
      + Syntax.NEWLINE
      + "\t\\textsc Rechnungs-Nr. & : 0\\\\\n"
      + "\t\\textsc Steuernummer & : 123456789\\\\\n"
      + "\t\\textsc BIC & : GENODEF1ANK\\\\\n"
      + "\t\\textsc IBAN & : DE83 1506 1638 0001 1009 47\\\\\n"
      + "\t\\multicolumn{2}{l}{Volksbank Raiffeisenbank eG}\n"
      + DATE_END
      + DOC_START
      + LETTER_START
      + "\\begin{tabular}{p{5cm}p{1cm}p{3cm}|r|r|}\n"
      + HLINE
      + Syntax.EOL
      + "\\multicolumn{3}{|c|}{\\textsc Bezeichnung}&"
      + "\\multicolumn{1}{c|}{\\textsc Einzelpreis}&"
      + "\\multicolumn{1}{c|}{\\textsc Gesamt}\\\\\n"
      + HLINE
      + Syntax.EOL
      + HLINE
      + Syntax.EOL
      + "\\multicolumn{1}{|l}{Artikel 1}&\\multicolumn{1}{r}{2,00}&Stück&"
      + "\\hspace{12pt} á \\EUR{1,50}&\\EUR{3,00}\\\\\n"
      + HLINE
      + Syntax.EOL
      + EMPTY_LINE
      + EMPTY_LINE
      + EMPTY_LINE
      + EMPTY_LINE
      + "\\multicolumn{1}{|l}{$\\phantom{iwas}$}&"
      + "\\multicolumn{1}{r}{\\phantom{(\\hfill000,00}}&&&\\\\\n"
      + HLINE
      + Syntax.EOL
      + "\\multicolumn{1}{|l}{Netto}&&&&\\EUR{3,00}\\\\\n"
      + HLINE
      + Syntax.EOL
      + "&&&\\multicolumn{1}{|c|}{10,00\\% MwSt}&\\EUR{0,30}\\\\\\cline{4-5}\n"
      + "&&&\\multicolumn{1}{|l|}{\\textsc Gesamt}&\\EUR{3,30}\\\\\\cline{4-5}\n"
      + "\\end{tabular}\n" + LETTER_END + DOC_END;

  private static Address senderAddress;
  private static Company sender;
  private static Address receiverAddress;
  private static LocalDate date;

  private static Letter letter;
  private static Invoice invoice;

  @BeforeClass
  public static void prepare() {
    senderAddress = new Address("Senderfirma", "Senderstraße", "1", "12345",
        "Senderort");
    final Account anAccount = new Account("Volksbank Raiffeisenbank eG",
        "GENODEF1ANK", "DE83 1506 1638 0001 1009 47");
    sender = new Company("Kurzname", "Offizieller Name GmbH", senderAddress,
        "Prokurist", "038355", "123456789", TAX_PERCENT, WAGES_PER_H, anAccount);
    receiverAddress = new Address("Empfänger", "Empfängerfirma", null,
        "Empfängerstraße", "2", null, "14785", "Empfängerort");
    date = LocalDate.of(YEAR, MONTH, DAY);
  }

  @Before
  public final void setUp() {
    letter = new Letter(date, sender, receiverAddress);
    letter.setSubject("Test");

    invoice = new Invoice(date, sender, receiverAddress);
    invoice.add("Artikel 1", "Stück", 1.50, 2);
  }

  @Test
  public final void generateLatexSourcesTestEmptyLetter() {
    final String result = LatexGenerator.generateLatexSource(letter);

    Assert.assertEquals(EXPECTATION_LETTER_START + EXPECTATION_LETTER_END,
        result);
  }

  @Test
  public final void generateLatexSourcesTestNonEmptyLetter() {
    String content = "Hello new World";
    letter.addMessageLine(content);
    letter.addMessageLine(content);
    letter.addMessageLine(content);
    letter.addMessageLine(content);
    String result = LatexGenerator.generateLatexSource(letter);

    Assert.assertEquals(
        EXPECTATION_LETTER_START
            + String.join(Syntax.NEWLINE, content, content, content, content)
            + Syntax.EOL + EXPECTATION_LETTER_END, result);
  }

  @Test
  public final void generateLatexSourcesTestInvoice() {
    final String result = LatexGenerator.generateLatexSource(invoice);

    Assert.assertEquals(EXPECTATION_INVOICE, result);
  }
}
