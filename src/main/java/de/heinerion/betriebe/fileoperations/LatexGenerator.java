package de.heinerion.betriebe.fileoperations;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.formatter.Formatter;
import de.heinerion.formatter.PlainFormatter;
import de.heinerion.betriebe.models.*;
import de.heinerion.betriebe.models.interfaces.Conveyable;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.tools.DateUtil;
import de.heinerion.betriebe.tools.FormatUtil;
import de.heinerion.latex.KomaKey;
import de.heinerion.latex.LatexScrLetter;
import de.heinerion.latex.LatexTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class LatexGenerator {
  private static final String BIC = "BIC";
  private static final String IBAN = "IBAN";

  private static final String COL_SPACE = ", ";
  private static final String COL_SEP = "|";
  private static final String CENTER = "c";
  private static final String RIGHT = "r";
  private static final String LEFT = "l";

  private static final String PHANTOM = "$\\phantom{sth}$";
  private static final String PHANTOM_EURO = "\\phantom{(\\hfill000,00}";

  private static final int SPAN_PRICES = 1;
  private static final int SPAN_ARTICLE = 3;

  private static final String TABLE_FORMAT = "p{5cm}p{1cm}p{3cm}|r|r|";

  private LatexGenerator() {
  }

  private static String convertAddress(Address address) {
    String convertedAddress = "null";

    if (address != null) {
      convertedAddress = joinWithSpaces(address.getStreet(), address.getNumber()) + ", " + joinWithSpaces(address.getPostalCode(), address.getLocation());
    }

    return convertedAddress;
  }

  private static String joinWithSpaces(String... strings) {
    return String.join(Constants.SPACE, strings);
  }

  private static String createRecipientsAddress(Address address) {
    return String.join(Syntax.NEWLINE, formatAddress(address));
  }

  private static List<String> formatAddress(Address address) {
    final Formatter formatter = new PlainFormatter();
    formatter.formatAddress(address);
    return formatter.getOutput();
  }

  private static void generateTableContent(LatexTable table, Invoice invoice) {
    fillHeader(table);
    fillItemLines(table, invoice);
    fillWithEmptyLines(table, getItemCount(invoice));
  }

  private static void fillHeader(LatexTable table) {
    final String style = CENTER + COL_SEP;
    final String first = COL_SEP + style;

    table.addColumnHeader(SPAN_ARTICLE, first, smallCaps("invoice.description"));
    table.addColumnHeader(SPAN_PRICES, style, smallCaps("invoice.pricePerUnit"));
    table.addColumnHeader(SPAN_PRICES, style, smallCaps("invoice.sum"));
  }

  private static String smallCaps(String key) {
    return Syntax.sc(Translator.translate(key));
  }

  private static void fillItemLines(LatexTable table, Invoice invoice) {
    for (Item item : invoice.getItems()) {
      addItemLine(table, item);
    }
  }

  private static void addItemLine(LatexTable table, Item item) {
    table.underLine();
    table.add(itemToLatex(item));
  }

  private static int getItemCount(Invoice invoice) {
    return invoice.getItems().size();
  }

  private static void fillWithEmptyLines(LatexTable table, int itemCount) {
    /* Why -1? */
    /*
    The last line of the invoice should be empty for optical reasons. generateTableSum ensures that.
    Thus we do not need to take care of the last line here.
     */
    for (int i = itemCount; i < Constants.INVOICE_LINE_COUNT - 1; i++) {
      addEmptyLine(table);
    }
  }

  private static void addEmptyLine(LatexTable table) {
    table.underLine();
    table.addEmptyRow();
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

  private static void setContent(Conveyable letter,
                                 final LatexScrLetter latexLetter, final boolean isInvoice) {
    if (isInvoice) {
      setInvoiceContent((Invoice) letter, latexLetter);
    } else {
      setLetterContent((Letter) letter, latexLetter);
    }
  }

  private static void setInvoiceContent(Invoice letter, LatexScrLetter latexLetter) {
    final LatexTable table = new LatexTable(TABLE_FORMAT);
    generateTableContent(table, letter);
    generateTableSum(table, letter);
    latexLetter.setContent(table);
  }

  private static void setLetterContent(Letter letter, LatexScrLetter latexLetter) {
    final String content = String.join(Syntax.NEWLINE, letter.getMessageLines());
    latexLetter.setContent(content);
  }

  private static void setDate(Conveyable letter,
                              final LatexScrLetter latexLetter, final boolean isInvoice) {
    String tab = " & : ";
    List<String> fields = new ArrayList<>();
    fields.add(smallCaps("invoice.date") + tab + DateUtil.format(letter.getDate()));

    if (isInvoice) {
      Company company = letter.getCompany();
      fields.add(smallCaps("invoice.number.short") + tab + company.getInvoiceNumber());
      fields.add(smallCaps("invoice.taxNumber") + tab + company.getTaxNumber());

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


    String key = isInvoice ? "invoice.title" : "letter.title";
    final String title = Translator.translate(key);

    List<Item> items = new ArrayList<>();
    double total = 0.0;
    if (isInvoice) {
      items = ((Invoice) letter).getItems();
      total = ((Invoice) letter).getGross();
    }

    String pdfSubject;
    if (isInvoice) {
      List<String> itemNames = items.stream()
          .map(Item::toString)
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

  private static void addKomaVars(final LatexScrLetter letter,
                                  final Company company, final String subject) {
    letter.addKomaVar(KomaKey.SIGNATURE, "\\underline{" + Translator.translate("invoice.signature") + ":\\hspace{10cm}}");
    letter.addKomaVar(KomaKey.SUBJECT, subject);
    letter
        .addKomaVar(KomaKey.FROMADDRESS, convertAddress(company.getAddress()));
    letter.addKomaVar(KomaKey.FROMPHONE, company.getPhoneNumber());
    letter.addKomaVar(KomaKey.FROMNAME,
        Syntax.START + company.getOfficialName() + Syntax.END
            + Syntax.TEXT_TINY);
  }

  private static void renewCommands(final LatexScrLetter latexLetter) {
    latexLetter.renewCommand("\\raggedsignature", "\\raggedright");
  }

  private static void addPackages(final LatexScrLetter letter) {
    letter.addPackage("inputenc", "utf8");
    letter.addPackage("babel", "ngermanb");
    letter.addPackage("eurosym", "right");
    letter.addPackage("hyperref");
  }

  private static void addTypeArguments(final LatexScrLetter letter) {
    letter.addTypeArgument("fontsize", "12pt");
    letter.addTypeArgument("paper", "a4");
    letter.addTypeArgument("fromalign", "center");
    letter.addTypeArgument(KomaKey.FROMPHONE + "", "true");
  }

  private static void generateTableSum(LatexTable table, Invoice letter) {
    startTableFooter(table);
    addEmptyRow(table);
    addNetRow(table, letter);
    addVatRow(table, letter);
    addGrossRow(table, letter);
  }

  private static void startTableFooter(LatexTable table) {
    table.underLine();
  }

  private static void addEmptyRow(LatexTable table) {
    table.fillEnd(Syntax.multicol(COL_SEP + LEFT, PHANTOM),
        Syntax.multicol(RIGHT, PHANTOM_EURO));
    table.finishRow();
    table.underLine();
  }

  private static void addNetRow(LatexTable table, Invoice letter) {
    table.fillMid(Syntax.multicol(COL_SEP + LEFT, Translator.translate("invoice.net")),
        Syntax.euro(letter.getNet()));
    table.finishRow();
    table.underLine();
  }

  private static void addVatRow(LatexTable table, Invoice letter) {
    final String vat = FormatUtil.formatLocaleDecimal(letter.getVat());
    String label = Syntax.multicol(COL_SEP + CENTER + COL_SEP, vat + "\\% " + Translator.translate("invoice.vat"));
    String value = Syntax.euro(letter.getTax());
    table.fillStart(label, value + Syntax.BR);
    table.underLine(-1);
  }

  private static void addGrossRow(LatexTable table, Invoice letter) {
    String label = Syntax.multicol(COL_SEP + LEFT + COL_SEP, smallCaps("invoice.sum"));
    String value = Syntax.euro(letter.getGross());
    table.fillStart(label, value + Syntax.BR);
    table.underLine(-1);
  }

  private static String itemToLatex(Item item) {
    StringBuilder itemLine = new StringBuilder();

    if (isItem(item)) {
      appendItem(item, itemLine);
    } else {
      appendLabel(item, itemLine);
    }

    return itemLine
        .append(Syntax.NEWLINE)
        .toString();
  }

  private static boolean isItem(Item item) {
    return item.getPricePerUnit() > 0 && item.getQuantity() > 0;
  }

  private static void appendLabel(Item item, StringBuilder itemLine) {
    itemLine
        .append(Syntax.multicol(SPAN_ARTICLE, COL_SEP + LEFT + COL_SEP, item.getName()))
        .append(Syntax.tab(2));
  }

  private static void appendItem(Item item, StringBuilder itemLine) {
    List<String> columns = new ArrayList<>();
    columns.add(Syntax.multicol(COL_SEP + LEFT, item.getName()));
    columns.add(Syntax.multicol(RIGHT, FormatUtil.formatLocaleDecimal(item.getQuantity())));
    columns.add(item.getUnit());
    columns.add("\\hspace{12pt} á " + Syntax.euro(item.getPricePerUnit()));
    columns.add(Syntax.euro(item.getTotal()));

    itemLine.append(String.join(Syntax.tab(), columns));
  }
}
