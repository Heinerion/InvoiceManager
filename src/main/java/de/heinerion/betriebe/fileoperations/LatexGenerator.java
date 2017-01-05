package de.heinerion.betriebe.fileoperations;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.enums.Utilities;
import de.heinerion.betriebe.formatter.Formatter;
import de.heinerion.betriebe.formatter.PlainFormatter;
import de.heinerion.betriebe.models.*;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.tools.DateUtil;
import de.heinerion.betriebe.tools.FormatUtil;
import de.heinerion.latex.KomaKey;
import de.heinerion.latex.LatexScrLetter;
import de.heinerion.latex.LatexTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class LatexGenerator {
  private static final String INVOICE_NUMBER = "Rechnungs-Nr.";
  private static final String TAX_NUMBER = "Steuernummer";
  private static final String BIC = "BIC";
  private static final String IBAN = "IBAN";

  private static final String COL_SPACE = ", ";
  private static final String COL_SEP = "|";
  private static final String CENTER = "c";
  private static final String RIGHT = "r";
  private static final String LEFT = "l";

  private static final String PHANTOM = "$\\phantom{iwas}$";
  private static final String PHANTOM_EURO = "\\phantom{(\\hfill000,00}";

  private static final int SPAN_PRICES = 1;
  private static final int SPAN_ARTICLE = 3;

  private static final String TABLE_FORMAT = "p{5cm}p{1cm}p{3cm}|r|r|";

  private static final String SIGNATURE = "\\underline{Unterschrift:\\hspace{10cm}}";

  private LatexGenerator() {
  }

  private static String convertAddress(Address address) {
    String convertedAddress = "null";

    if (address != null) {
      convertedAddress = address.getStreet() + Constants.SPACE + address.getNumber()
          + COL_SPACE + address.getPostalCode() + Constants.SPACE + address.getLocation();
    }

    return convertedAddress;
  }

  private static String createRecipientsAddress(Address address) {
    final Formatter formatter = new PlainFormatter();
    formatter.formatAddress(address);
    final List<String> out = formatter.getOutput();

    return String.join(Syntax.NEWLINE, out);
  }

  private static void generateTableContent(LatexTable table, Invoice invoice) {
    final int itemCount = invoice.getItems().size();

    final String style = CENTER + COL_SEP;
    final String first = COL_SEP + style;

    table.addColumnHeader(SPAN_ARTICLE, first,
        Syntax.sc(Constants.INVOICE_NAME));
    table.addColumnHeader(SPAN_PRICES, style, Syntax.sc(Constants.INVOICE_PPU));
    table.addColumnHeader(SPAN_PRICES, style, Syntax.sc(Constants.INVOICE_SUM));

    for (Item item : invoice.getItems()) {
      table.addLine();
      table.add(itemToLatex(item));
    }

    // TODO warum -1?
    // Weil die Letzte Reihe aus optischen Gründen frei bleibt.
    // (in generateTableSum sichergestellt)
    for (int i = itemCount; i < Constants.INVOICE_LINECOUNT - 1; i++) {
      table.addLine();
      table.addEmptyRow();
    }
  }

  public static String generateLatexSource(Conveyable letter) {
    final LatexScrLetter latexLetter = new LatexScrLetter(
        createRecipientsAddress(letter.getReceiver()));
    final String subject = letter.getSubject();
    final boolean isInvoice = letter instanceof Invoice;

    addTypeArguments(latexLetter);
    addPackages(latexLetter);
    renewCommands(latexLetter);
    addKomaVars(latexLetter, letter.getCompany(), subject);
    addHyperSetupArguments(letter, latexLetter, subject, isInvoice);
    setDate(letter, latexLetter, isInvoice);
    setContent(letter, latexLetter, isInvoice);

    return latexLetter.toString();
  }

  /**
   * @param letter
   * @param latexLetter
   * @param isInvoice
   */
  private static void setContent(Conveyable letter,
                                 final LatexScrLetter latexLetter, final boolean isInvoice) {
    if (isInvoice) {
      final LatexTable table = new LatexTable(TABLE_FORMAT);
      generateTableContent(table, (Invoice) letter);
      generateTableSum(table, (Invoice) letter);
      latexLetter.setContent(table);
    } else {
      final String content = String.join(Syntax.NEWLINE,
          ((Letter) letter).getMessageLines());
      latexLetter.setContent(content);
    }
  }

  /**
   * @param letter
   * @param latexLetter
   * @param isInvoice
   */
  private static void setDate(Conveyable letter,
                              final LatexScrLetter latexLetter, final boolean isInvoice) {
    String tab = " & : ";
    List<String> fields = new ArrayList<>();
    fields.add(Syntax.sc("Datum") + tab + DateUtil.format(letter.getDate()));

    if (isInvoice) {
      Company company = letter.getCompany();
      fields.add(Syntax.sc(INVOICE_NUMBER) + tab + company.getInvoiceNumber());
      fields.add(Syntax.sc(TAX_NUMBER) + tab + company.getTaxNumber());

      Account bankAccount = company.getAccount();
      fields.add(Syntax.sc(BIC) + tab + bankAccount.getBic());
      fields.add(Syntax.sc(IBAN) + tab + bankAccount.getIban());
      fields.add(Syntax.multicol(2, LEFT, bankAccount.getName()));
    }

    LatexTable dateTable = new LatexTable("ll");
    dateTable.setContent(String.join("\\\\" + Syntax.EOL, fields));

    String dateString = String.join(Syntax.EOL, "\\footnotesize ", dateTable.toString());

    latexLetter.setDate(dateString);
  }

  private static void addHyperSetupArguments(Conveyable letter,
                                             final LatexScrLetter latexLetter, final String subject,
                                             final boolean isInvoice) {
    final Company company = letter.getCompany();
    final String title = isInvoice ? Utilities.RECHNUNG.getText()
        : Utilities.BRIEF.getText();

    List<Item> items = new ArrayList<>();
    double total = 0.0;
    if (isInvoice) {
      items = ((Invoice) letter).getItems();
      total = ((Invoice) letter).getGross();
    }

    String pdfSubject;
    if (isInvoice) {
      List<String> itemNames = items.stream()
          .map(item -> item.toString())
          .collect(Collectors.toList());
      pdfSubject = String.join(COL_SPACE, itemNames);
    } else {
      pdfSubject = subject;
    }

    latexLetter.addHyperSetupArgument("pdftitle", title);
    latexLetter.addHyperSetupArgument("pdfauthor", company.getOfficialName());
    latexLetter.addHyperSetupArgument("pdfsubject", pdfSubject);
    latexLetter.addHyperSetupArgument("pdfkeywords",
        FormatUtil.formatAmericanDecimal(total));
  }

  /**
   * @param letter
   * @param company
   * @param subject
   */
  private static void addKomaVars(final LatexScrLetter letter,
                                  final Company company, final String subject) {
    letter.addKomaVar(KomaKey.SIGNATURE, SIGNATURE);
    letter.addKomaVar(KomaKey.SUBJECT, subject);
    letter
        .addKomaVar(KomaKey.FROMADDRESS, convertAddress(company.getAddress()));
    letter.addKomaVar(KomaKey.FROMPHONE, company.getPhoneNumber());
    letter.addKomaVar(KomaKey.FROMNAME,
        Syntax.START + company.getOfficialName() + Syntax.END
            + Syntax.TEXT_TINY);
  }

  /**
   * @param latexLetter
   */
  private static void renewCommands(final LatexScrLetter latexLetter) {
    latexLetter.renewCommand("\\raggedsignature", "\\raggedright");
  }

  /**
   * @param letter
   */
  private static void addPackages(final LatexScrLetter letter) {
    letter.addPackage("inputenc", "utf8");
    letter.addPackage("babel", "ngermanb");
    letter.addPackage("eurosym", "right");
    letter.addPackage("hyperref");
  }

  /**
   * @param letter
   */
  private static void addTypeArguments(final LatexScrLetter letter) {
    letter.addTypeArgument("fontsize", "12pt");
    letter.addTypeArgument("paper", "a4");
    letter.addTypeArgument("fromalign", "center");
    letter.addTypeArgument(KomaKey.FROMPHONE + "", "true");
  }

  private static void generateTableSum(LatexTable table, Invoice letter) {
    final String vat = FormatUtil.formatLocaleDecimal(letter.getVat());

    table.addLine();

    table.fillEnd(Syntax.multicol(1, COL_SEP + LEFT, PHANTOM),
        Syntax.multicol(1, RIGHT, PHANTOM_EURO));
    table.finishRow();
    table.addLine();

    table.fillMid(Syntax.multicol(1, COL_SEP + LEFT, "Netto"),
        Syntax.euro(letter.getNet()));
    table.finishRow();
    table.addLine();

    table.fillStart(
        Syntax.multicol(1, COL_SEP + CENTER + COL_SEP, vat + "\\% MwSt"),
        Syntax.euro(letter.getTax()) + Syntax.BR);
    table.addLine(-1);

    table.fillStart(
        Syntax.multicol(1, COL_SEP + LEFT + COL_SEP,
            Syntax.sc(Constants.INVOICE_SUM)), Syntax.euro(letter.getGross())
            + Syntax.BR);
    table.addLine(-1);
  }

  private static String itemToLatex(Item item) {
    String itemLine;

    if (item.getPricePerUnit() > 0 && item.getQuantity() > 0) {
      final String quantity = FormatUtil.formatLocaleDecimal(item
          .getQuantity());

      itemLine = Syntax.multicol(1, COL_SEP + LEFT, item.getName())
          + Syntax.tab() + Syntax.multicol(1, RIGHT, quantity) + Syntax.tab()
          + item.getUnit() + Syntax.tab() + "\\hspace{12pt} á "
          + Syntax.euro(item.getPricePerUnit()) + Syntax.tab()
          + Syntax.euro(item.getTotal());
    } else {
      itemLine = Syntax.multicol(SPAN_ARTICLE, COL_SEP + LEFT + COL_SEP,
          item.getName())
          + Syntax.tab(2);
    }

    return itemLine + Syntax.NEWLINE;
  }
}
