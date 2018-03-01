package de.heinerion.betriebe.view.swing.menu.tablemodels.archive;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.invoice.storage.loading.Loadable;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.util.DateUtil;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ArchivedInvoice implements Loadable {
  private static final String DIVIDER = "\t";
  // 14 := dd.mm.yyyy.pdf
  private static final int DATE_LENGTH = 14;
  private static Map<Company, Map<String, Address>> companyAddressCache = new HashMap<>();

  private final int invoiceNumber;

  private String dateString;

  private LocalDate date;

  private final Address recipient;

  private String item;

  private Company company;

  private double amount;

  private final File pdf;

  private Map<String, Address> addressCache;

  // TODO parsing der Daten unbedingt überarbeiten
  // TODO pdf-Properties auslesen?
  public ArchivedInvoice(File sourceFile) {
    pdf = sourceFile;
    final String companyName = sourceFile.getParentFile().getName();
    company = Session.getCompanyByName(companyName);
    addressCache = companyAddressCache.computeIfAbsent(company, k -> new HashMap<>());

    final String name = sourceFile.getName();
    final String[] token = name.split(" ", 2);
    int numberStringLen = token[0].length();

    invoiceNumber = Integer.parseInt(token[0].trim());
    dateString = DateUtil.extractDateString(name);
    try {
      date = DateUtil.parse(dateString);
    } catch (final DateTimeParseException e) {
      date = null;
    }
    dateString = dateString.replace(',', '.');

    // TODO Empfängerbestimmung zweifelhaft
    final int posDateStart = name.length() - DATE_LENGTH;

    final String recipientsName = name
        .substring(numberStringLen, posDateStart)
        .trim();
    recipient = getAddress(recipientsName);
  }

  private Address getAddress(final String recipientsName) {
    return addressCache.computeIfAbsent(recipientsName, n -> DataBase.getAddress(company, n).orElse(null));
  }

  public double getAmount() {
    return amount;
  }

  public Company getCompany() {
    return company;
  }

  public LocalDate getDate() {
    return date;
  }

  public int getInvoiceNumber() {
    return invoiceNumber;
  }

  public String getItem() {
    return item;
  }

  File getPdf() {
    return pdf;
  }

  public Address getRecipient() {
    return recipient;
  }

  public void setAmount(double anAmount) {
    amount = anAmount;
  }

  public void setCompany(Company aCompany) {
    company = aCompany;
  }

  public void setCompany(String absender) {
    setCompany(Session.getCompanyByName(absender));
  }

  public void setItem(String anItem) {
    item = anItem;
  }

  @Override
  public String toString() {
    List<String> properties = Stream
        .of(invoiceNumber, recipient, dateString, company, pdf != null ? pdf.getPath() : null)
        .map(x -> x != null ? x.toString() : "-")
        .collect(Collectors.toList());

    return String.join(DIVIDER, properties);
  }
}
