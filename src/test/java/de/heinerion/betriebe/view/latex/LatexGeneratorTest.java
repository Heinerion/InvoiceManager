package de.heinerion.betriebe.view.latex;

import de.heinerion.betriebe.builder.AddressBuilder;
import de.heinerion.betriebe.builder.CompanyBuilder;
import de.heinerion.betriebe.view.formatter.Formatter;
import de.heinerion.betriebe.models.*;
import de.heinerion.betriebe.services.ConfigurationService;
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
      + "{street number, postalCode location}\n"
      + "\\setkomavar{fromphone}{123-456}\n"
      + "\\setkomavar{fromname}{{officialName}\\tiny}\n";
  private static final String DATE_START = "\\date{\n"
      + "\t\\footnotesize \n"
      + "\t\\begin{tabular}{ll}\n"
      + "\t\\textsc Datum & : "
      + String.join(".", DAY + "", (MONTH.getValue() < DOUBLE_DIGIT ? "0" : "")
      + MONTH.getValue(), YEAR + "");
  private static final String DATE_END = "\t\\end{tabular}\n" + "}\n";
  private static final String DOC_START = "\\begin{document}\n";
  private static final String DOC_END = "\\end{document}";
  private static final String EMPTY_LINE = "\\multicolumn{1}{|l}{$\\phantom{sth}$}&&&&\\\\\n"
      + HLINE + Syntax.EOL;

  private static final String LETTER_START = "\\begin{letter}{formatted\\\\\n"
      + "address}\n\n" + "\\opening{}\\vspace{-25pt}\n";
  private static final String LETTER_END = "\\vfill\n" + "\\closing{}\n" + "\n"
      + "\\end{letter}\n";

  private static final String EXPECTATION_LETTER_START = DOCCLASS + PACKAGES
      + "\\hypersetup{pdftitle={Brief}, pdfauthor={officialName}, "
      + "pdfsubject={Test}, pdfkeywords={0.00}}\n" + RENEW + KOMA_SIGNATURE
      + "\\setkomavar{subject}{Test}\n" + KOMA_FROMADDRESS + DATE_START
      + Syntax.EOL + DATE_END + DOC_START + LETTER_START;
  private static final String EXPECTATION_LETTER_END = LETTER_END + DOC_END;

  private static final String BANK_ACCOUNT = DATE_START
      + Syntax.NEWLINE
      + "\t\\textsc Rechnungs-Nr. & : 0\\\\\n"
      + "\t\\textsc Steuernummer & : taxNumber\\\\\n"
      + "\t\\textsc BIC & : bic\\\\\n"
      + "\t\\textsc IBAN & : iban\\\\\n"
      + "\t\\multicolumn{2}{l}{institute}\n"
      + DATE_END;

  private static final String EXPECTATION_INVOICE = DOCCLASS
      + PACKAGES
      + "\\hypersetup{pdftitle={Rechnung}, pdfauthor={officialName}, "
      + "pdfsubject={Artikel 1}, pdfkeywords={3.30}}\n"
      + RENEW
      + KOMA_SIGNATURE
      + "\\setkomavar{subject}{Rechnung}\n"
      + KOMA_FROMADDRESS
      + BANK_ACCOUNT
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
      + "\\multicolumn{1}{|l}{$\\phantom{sth}$}&"
      + "\\multicolumn{1}{r}{\\phantom{(\\hfill000,00}}&&&\\\\\n"
      + HLINE
      + Syntax.EOL
      + "\\multicolumn{1}{|l}{Netto}&&&&\\EUR{3,00}\\\\\n"
      + HLINE
      + Syntax.EOL
      + "&&&\\multicolumn{1}{|c|}{10,00\\% MwSt}&\\EUR{0,30}\\\\\\cline{4-5}\n"
      + "&&&\\multicolumn{1}{|l|}{\\textsc Gesamt}&\\EUR{3,30}\\\\\\cline{4-5}\n"
      + "\\end{tabular}\n" + LETTER_END + DOC_END;

  private static final String EXPECTATION_INVOICE_OF_TWO = DOCCLASS
      + PACKAGES
      + "\\hypersetup{pdftitle={Rechnung}, pdfauthor={officialName}, "
      + "pdfsubject={Artikel 1, Artikel 2}, pdfkeywords={14.65}}\n"
      + RENEW
      + KOMA_SIGNATURE
      + "\\setkomavar{subject}{Rechnung}\n"
      + KOMA_FROMADDRESS
      + BANK_ACCOUNT
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
      + "\\multicolumn{1}{|l}{Artikel 2}&\\multicolumn{1}{r}{3,00}&Stück&"
      + "\\hspace{12pt} á \\EUR{3,44}&\\EUR{10,32}\\\\\n"
      + HLINE
      + Syntax.EOL
      + EMPTY_LINE
      + EMPTY_LINE
      + EMPTY_LINE
      + "\\multicolumn{1}{|l}{$\\phantom{sth}$}&"
      + "\\multicolumn{1}{r}{\\phantom{(\\hfill000,00}}&&&\\\\\n"
      + HLINE
      + Syntax.EOL
      + "\\multicolumn{1}{|l}{Netto}&&&&\\EUR{13,32}\\\\\n"
      + HLINE
      + Syntax.EOL
      + "&&&\\multicolumn{1}{|c|}{10,00\\% MwSt}&\\EUR{1,33}\\\\\\cline{4-5}\n"
      + "&&&\\multicolumn{1}{|l|}{\\textsc Gesamt}&\\EUR{14,65}\\\\\\cline{4-5}\n"
      + "\\end{tabular}\n" + LETTER_END + DOC_END;

  private static Company sender;
  private static Address receiverAddress;
  private static LocalDate date;

  private static Letter letter;
  private static Invoice invoice;

  private LatexGenerator latexGenerator;

  @BeforeClass
  public static void prepare() {
    sender = new CompanyBuilder().build();
    receiverAddress = new AddressBuilder()
        .withDistrict("")
        .withApartment("")
        .build();
    date = LocalDate.of(YEAR, MONTH, DAY);
  }

  @Before
  public final void setUp() {
    letter = new Letter(date, sender, receiverAddress);
    letter.setSubject("Test");

    invoice = new Invoice(date, sender, receiverAddress);
    invoice.add("Artikel 1", "Stück", 1.50, 2);

    Formatter formatter = ConfigurationService.getBean(Formatter.class);
    latexGenerator = new LatexGeneratorImpl(formatter);
  }

  @Test
  public final void generateLatexSourcesTestEmptyLetter() {
    final String result = latexGenerator.generateSourceContent(letter);

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
    String result = latexGenerator.generateSourceContent(letter);

    Assert.assertEquals(
        EXPECTATION_LETTER_START
            + String.join(Syntax.NEWLINE, content, content, content, content)
            + Syntax.EOL + EXPECTATION_LETTER_END, result);
  }

  @Test
  public final void generateLatexSourcesTestInvoice() {
    final String result = latexGenerator.generateSourceContent(invoice);

    Assert.assertEquals(EXPECTATION_INVOICE, result);
  }

  @Test
  public final void buildsCorrectInvoiceForMultipleItems() {
    invoice.add("Artikel 2", "Stück", 3.44, 3);
    final String result = latexGenerator.generateSourceContent(invoice);

    Assert.assertEquals(EXPECTATION_INVOICE_OF_TWO, result);
  }
}