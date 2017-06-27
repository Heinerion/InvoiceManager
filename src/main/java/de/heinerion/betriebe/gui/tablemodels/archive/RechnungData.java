package de.heinerion.betriebe.gui.tablemodels.archive;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.fileoperations.loading.Loadable;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.tools.DateUtil;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public final class RechnungData implements Loadable {
  private static final String DIVIDER = "\t";
  // 14 := dd.mm.yyyy.pdf
  private static final int DATE_LENGTH = 14;
  private static Map<Company, Map<String, Address>> globalAddressCache = new HashMap<>();

  /**
   * Die aus der Quelle ermittelte Rechnungsnummer
   */
  private final int invoiceNumber;
  /**
   * Das ausgelesene Datum
   */
  private String dateString;
  private LocalDate date;
  /**
   * Der ausgelesene Empfänger
   */
  private final Address recipient;
  /**
   * "Artikel" der verkauft wird
   */
  private String item;
  /**
   * Der Betrieb von dem aus die Rechnung erstellt wurde
   */
  private Company company;
  /**
   * Der Rechnungsbetrag
   */
  private double amount;
  /**
   * Pfad zur PDF der Rechnung
   */
  private final File pdf;

  private Map<String, Address> addressCache;

  // TODO parsing der Daten unbedingt überarbeiten
  public RechnungData(File sourceFile) {
    this.pdf = sourceFile;
    final String name = sourceFile.getName();
    final String companyName = sourceFile.getParentFile().getName();
    this.company = Session.getCompanyByName(companyName);
    this.addressCache = globalAddressCache.computeIfAbsent(this.company, k -> new HashMap<>());

    int numberStringLen;

    final String[] token = name.split(" ", 2);
    numberStringLen = token[0].length();

    this.invoiceNumber = Integer.parseInt(token[0].trim());
    this.dateString = DateUtil.extractDateString(name);
    try {
      this.date = DateUtil.parse(this.dateString);
    } catch (final DateTimeParseException e) {
      this.date = null;
    }
    this.dateString = this.dateString.replace(',', '.');

    // TODO Empfängerbestimmung zweifelhaft
    final int posDateStart = name.length() - DATE_LENGTH;

    final String recipientsName = name.substring(numberStringLen, posDateStart)
        .trim();
    this.recipient = getAddress(recipientsName);
  }

  /**
   * @param recipientsName
   * @return
   */
  private Address getAddress(final String recipientsName) {
    return addressCache.computeIfAbsent(recipientsName, n -> DataBase.getAddress(this.company, n));
  }

  public double getAmount() {
    return this.amount;
  }

  public Company getCompany() {
    return this.company;
  }

  public LocalDate getDate() {
    return this.date;
  }

  public String getDateString() {
    return this.dateString;
  }

  public int getInvoiceNumber() {
    return this.invoiceNumber;
  }

  public String getItem() {
    return this.item;
  }

  public File getPdf() {
    return this.pdf;
  }

  public Address getRecipient() {
    return this.recipient;
  }

  public void setAmount(double anAmount) {
    this.amount = anAmount;
  }

  public void setCompany(Company aCompany) {
    this.company = aCompany;
  }

  public void setCompany(String absender) {
    this.setCompany(Session.getCompanyByName(absender));
  }

  public void setItem(String anItem) {
    this.item = anItem;
  }

  @Override
  public String toString() {

    return this.invoiceNumber + DIVIDER + this.recipient + DIVIDER
        + this.dateString + DIVIDER + this.company + "\n->" + DIVIDER
        + this.pdf.getPath();
  }
}
