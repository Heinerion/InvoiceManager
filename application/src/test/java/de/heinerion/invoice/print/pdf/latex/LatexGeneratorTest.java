package de.heinerion.invoice.print.pdf.latex;

import de.heinerion.invoice.models.Address;
import de.heinerion.invoice.models.Company;
import de.heinerion.invoice.models.Invoice;
import de.heinerion.invoice.models.Letter;
import de.heinerion.invoice.testsupport.builder.AddressBuilder;
import de.heinerion.invoice.testsupport.builder.CompanyBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

public abstract class LatexGeneratorTest {
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
      + "\\usepackage{longtable}\n"
      + "\\usepackage{lastpage}             % determine last page\n"
      + "\\usepackage{scrlayer-scrpage}     % scr-Styling\n"
      + "\\usepackage[hidelinks]{hyperref}  % use hyperref without link highlighting\n";
  private static final String RENEW = "\\renewcommand{\\raggedsignature}"
      + "{\\raggedright}\n";
  private static final String KOMA_SIGNATURE = "\\setkomavar{signature}"
      + "{\\underline{Unterschrift:\\hspace{10cm}}}\n";
  private static final String KOMA_FROMADDRESS = "\\setkomavar{fromaddress}"
      + "{street number, postalCode location}\n"
      + "\\setkomavar{fromphone}{123-456}\n"
      + "\\setkomavar{fromname}{{officialName}\\tiny}\n";
  private static final String DATE_START = "\\date{}\n"
      + "\\setkomavar{location}{\n"
      + "  \\vfill\n"
      + "  \\raggedright\n"
      + "  \\footnotesize\n"
      + "  \\begin{tabular}{ll}\n"
      + "  \\textsc Datum & : "
      + String.join(".", DAY + "", (MONTH.getValue() < DOUBLE_DIGIT ? "0" : "")
      + MONTH.getValue(), YEAR + "");
  private static final String DATE_END = "  \\end{tabular}\n" + "}\n";
  private static final String FOOTER_DECLARATION_START = "\n"
      + "\\setkomavar{firstfoot}{\n"
      + "  \\usekomafont{pageheadfoot}\n"
      + "  \\parbox{\\useplength{firstfootwidth}}{\n"
      + "    \\rule{\\linewidth}{.4pt}\\\\\n"
      + "    \\parbox[t]{0.8\\linewidth}{\n";

  private static final String FOOTER_DECLARATION_END = "    }\n"
      + "    \\hfill\n"
      + "    \\parbox[t]{0.1\\linewidth}{\n"
      + "      Seite \\thepage \\hspace{1pt} von \\pageref{LastPage}\n"
      + "    }\n"
      + "  }\n"
      + "}\n"
      + "\\setkomafont{pageheadfoot}{\\scriptsize}\n"
      + "\n"
      + "% Use the footer-style of the first page in following pages\n"
      + "\\DeclareNewLayer[\n"
      + "  foreground,\n"
      + "  textarea,\n"
      + "  voffset=\\useplength{firstfootvpos},\n"
      + "  hoffset=\\dimexpr.5\\paperwidth-.5\\useplength{firstfootwidth}\\relax,\n"
      + "  width=\\useplength{firstfootwidth},\n"
      + "  mode=picture,\n"
      + "  contents=\\putUL{\\raisebox{\\dimexpr-\\height}{\\usekomavar{firstfoot}}}\n"
      + "]{likefirstpage.foot}\n"
      + "\n"
      + "\\AddLayersToPageStyle{scrheadings}{likefirstpage.foot}\n"
      + "\\clearpairofpagestyles\n"
      + "\n";
  private static final String TWEAKS = "\\usepackage{etoolbox}% http://ctan.org/pkg/etoolbox\n"
      + "\\makeatletter\n"
      + "\n"
      + "% allows for more horizontal space in the location\n"
      + "\\@setplength{lochpos}{\\oddsidemargin}\n"
      + "\\@addtoplength{lochpos}{6cm}\n"
      + "\n"
      + "% pull signature closer to the table\n"
      + "\\patchcmd{\\closing}% <cmd>\n"
      + "  {\\parbox}% <search>\n"
      + "  {\\parbox{\\linewidth}{\\raggedsignature\\strut\\ignorespaces\\let\\\\\\relax%\n"
      + "      #1 \\usekomavar{signature}}%\n"
      + "   \\@gobbletwo}%< <replace>\n"
      + "  {}{}% <success><failure>\n"
      + "\\makeatother"
      + "\n"
      + "\n";
  private static final String DOC_START = "\\begin{document}\n";
  private static final String DOC_END = "\\end{document}";
  private static final String EMPTY_LINE = "\\multicolumn{1}{|l}{$\\phantom{sth}$}&&&&\\\\\n"
      + HLINE + "\n";

  private static final String LETTER_START = "\\begin{letter}{formatted\\\\\n"
      + "address}\n\n" + "\\opening{}\\vspace{-25pt}\n";
  private static final String LETTER_END = "\\vfill\n" + "\\closing{}\n" + "\n"
      + "\\end{letter}\n";

  private static final String EXPECTATION_LETTER_START = DOCCLASS + PACKAGES
      + "\\hypersetup{\n  pdftitle={Brief},\n  pdfauthor={officialName},\n  "
      + "pdfsubject={Test},\n  pdfkeywords={0.00}\n}\n" + RENEW + KOMA_SIGNATURE
      + "\\setkomavar{subject}{Test}\n" + KOMA_FROMADDRESS + DATE_START
      + "\n"
      + DATE_END
      + FOOTER_DECLARATION_START
      + "      officialName, Test, 25.06.2010\n"
      + FOOTER_DECLARATION_END
      + DOC_START
      + LETTER_START;
  private static final String EXPECTATION_LETTER_END = LETTER_END + DOC_END;

  private static final String BANK_ACCOUNT = DATE_START
      + "\\\\\n"
      + "  \\textsc Rechnungs-Nr. & : 0\\\\\n"
      + "  \\textsc Steuernummer & : taxNumber\\\\\n"
      + "  \\textsc BIC & : bic\\\\\n"
      + "  \\textsc IBAN & : iban\\\\\n"
      + "  \\multicolumn{2}{l}{institute}\n"
      + DATE_END;

  private static final String EXPECTATION_INVOICE = LatexTestBuilder.builder()
      .withArticles("Artikel 1")
      .withNet("3,00")
      .withTax("0,30")
      .withGross("3,30")
      .withSum("3.30")
      .withLine("\\multicolumn{1}{|l}{Artikel 1}&\\multicolumn{1}{r}{2,00}&Stück&"
          + "\\hspace{12pt} á \\EUR{1,50}&\\EUR{3,00}\\\\\n")
      .build();

  private static final String EXPECTATION_INVOICE_OF_TWO = LatexTestBuilder.builder()
      .withArticles("Artikel 1, Artikel 2")
      .withNet("13,32")
      .withTax("1,33")
      .withGross("14,65")
      .withSum("14.65")
      .withLine("\\multicolumn{1}{|l}{Artikel 1}&\\multicolumn{1}{r}{2,00}&Stück&"
          + "\\hspace{12pt} á \\EUR{1,50}&\\EUR{3,00}\\\\\n")
      .withLine("\\multicolumn{1}{|l}{Artikel 2}&\\multicolumn{1}{r}{3,00}&Stück&"
          + "\\hspace{12pt} á \\EUR{3,44}&\\EUR{10,32}\\\\\n")
      .build();

  private static Company sender;
  private static Address receiverAddress;
  private static LocalDate date;

  private static Letter letter;
  private static Invoice invoice;

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
  }

  protected abstract LatexGenerator getLatexGenerator();

  @Test
  public final void generateLatexSourcesTestEmptyLetter() {
    final String result = getLatexGenerator().generateSourceContent(letter);

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
    String result = getLatexGenerator().generateSourceContent(letter);

    Assert.assertEquals(
        EXPECTATION_LETTER_START
            + String.join("\\\\\n", content, content, content, content)
            + "\n" + EXPECTATION_LETTER_END, result);
  }

  @Test
  public final void generateLatexSourcesTestInvoice() {
    final String result = getLatexGenerator().generateSourceContent(invoice);

    Assert.assertEquals(EXPECTATION_INVOICE, result);
  }

  @Test
  public final void buildsCorrectInvoiceForMultipleItems() {
    invoice.add("Artikel 2", "Stück", 3.44, 3);
    final String result = getLatexGenerator().generateSourceContent(invoice);

    Assert.assertEquals(EXPECTATION_INVOICE_OF_TWO, result);
  }

  @Test
  public final void invoiceWithTextLine() {
    invoice.addMessageLine("Message");

    final String result = getLatexGenerator().generateSourceContent(invoice);

    Assert.assertEquals(LatexTestBuilder.builder()
        .withArticles("Artikel 1")
        .withNet("3,00")
        .withTax("0,30")
        .withGross("3,30")
        .withSum("3.30")
        .withLine("\\multicolumn{1}{|l}{Artikel 1}&\\multicolumn{1}{r}{2,00}&Stück&"
            + "\\hspace{12pt} á \\EUR{1,50}&\\EUR{3,00}\\\\\n")
        .withLine("\\multicolumn{2}{|l}{Message}&&&\\\\\n")
        .build(), result);
  }
}
