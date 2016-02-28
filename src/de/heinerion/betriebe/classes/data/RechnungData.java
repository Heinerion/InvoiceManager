package de.heinerion.betriebe.classes.data;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import de.heinerion.betriebe.classes.fileOperations.loading.Loadable;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.tools.DateTools;

public final class RechnungData implements Loadable {
  // 14 := dd.mm.yyyy.pdf
  private static final int DATE_LENGTH = 14;
  private static Map<Company, Map<String, Address>> globalAddressCache = new HashMap<>();

  /** Die aus der Quelle ermittelte Rechnungsnummer */
  private final int invoiceNumber;
  /** Das ausgelesene Datum */
  private String dateString;
  private LocalDate date;
  /** Der ausgelesene Empfänger */
  private final Address recipient;
  /** "Artikel" der verkauft wird */
  private String item;
  /** Der Betrieb von dem aus die Rechnung erstellt wurde */
  private Company company;
  /** Der Rechnungsbetrag */
  private double amount;
  /** Pfad zur PDF der Rechnung */
  private final File pdf;

  private Map<String, Address> addressCache;

  // TODO parsing der Daten unbedingt überarbeiten
  public RechnungData(File sourceFile) {
    this.pdf = sourceFile;
    final String name = sourceFile.getName();
    final String companyName = sourceFile.getParentFile().getName();
    this.company = Session.getCompanyByName(companyName);
    Map<String, Address> cache = globalAddressCache.get(this.company);
    if (cache == null) {
      cache = new HashMap<>();
      globalAddressCache.put(this.company, cache);
    }
    this.addressCache = cache;

    int numberStringLen;

    final String[] token = name.split(" ", 2);
    numberStringLen = token[0].length();

    this.invoiceNumber = Integer.parseInt(token[0].trim());
    this.dateString = DateTools.extractDateString(name);
    try {
      this.date = DateTools.parse(this.dateString);
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
    Address result = addressCache.get(recipientsName);
    if (result == null) {
      result = DataBase.getAddress(this.company, recipientsName);
      addressCache.put(recipientsName, result);
    }
    return result;
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
    final String trenner = "\t";
    return this.invoiceNumber + trenner + this.recipient + trenner
        + this.dateString + trenner + this.company + "\n->" + trenner
        + this.pdf.getPath();
  }
}
