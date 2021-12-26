package de.heinerion.invoice.view.swing.menu.tablemodels.archive;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.repositories.AddressRepository;
import de.heinerion.invoice.ParsingUtil;
import de.heinerion.invoice.view.DateUtil;
import lombok.extern.flogger.Flogger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Flogger
public final class PdfArchivedInvoice implements ArchivedInvoice {
  private static final String DIVIDER = "\t";
  // 14 := dd.mm.yyyy.pdf
  private static final int DATE_LENGTH = 14;
  private static final Map<Company, Map<String, Address>> companyAddressCache = new HashMap<>();

  private final int invoiceNumber;
  private final Address recipient;
  private final File pdf;
  private final LocalDate date;
  private String item;
  private Company company;
  private double amount;
  private Map<String, Address> addressCache;
  private final AddressRepository addressRepository;

  // TODO parsing der Daten unbedingt überarbeiten
  // TODO pdf-Properties auslesen?
  public PdfArchivedInvoice(File sourceFile, AddressRepository addressRepository) {
    this.addressRepository = addressRepository;

    pdf = sourceFile;
    final String companyName = sourceFile.getParentFile().getName();
    company = Session.getCompanyByName(companyName);
    addressCache = companyAddressCache.computeIfAbsent(company, k -> new HashMap<>());

    final String name = sourceFile.getName();
    final String[] token = name.split(" ", 2);
    int numberStringLen = token[0].length();

    invoiceNumber = Integer.parseInt(token[0].trim());

    String dateTemp = DateUtil.extractDateString(name);
    date = DateUtil.parseOptional(dateTemp).orElse(null);

    // TODO Empfängerbestimmung zweifelhaft
    final int posDateStart = name.length() - DATE_LENGTH;

    final String recipientsName = name
        .substring(numberStringLen, posDateStart)
        .trim();
    recipient = getAddress(recipientsName);
  }

  private Address getAddress(final String recipientsName) {
    return addressCache
        .computeIfAbsent(recipientsName, n -> addressRepository
            .findByCompanyAndRecipient(company, n)
            .orElse(null));
  }

  @Override
  public void loadFile() {
    File file = getFile();
    try (PDDocument pdDocument = PDDocument.load(file)) {
      PDDocumentInformation info = pdDocument.getDocumentInformation();
      company = Session.getCompanyByName(info.getAuthor());
      item = info.getSubject();
      String keywords = info.getKeywords();
      if (keywords != null && !"".equals(keywords)) {
        amount = ParsingUtil.parseDouble(keywords);
      }
    } catch (IOException e) {
      log
          .atWarning()
          .withCause(e)
          .log("file %s could not be read as PDF", file.getAbsolutePath());
    }
  }

  @Override
  public double getAmount() {
    return amount;
  }

  @Override
  public Company getCompany() {
    return company;
  }

  @Override
  public LocalDate getDate() {
    return date;
  }

  @Override
  public int getInvoiceNumber() {
    return invoiceNumber;
  }

  @Override
  public String getItem() {
    return item;
  }

  @Override
  public File getFile() {
    return pdf;
  }

  @Override
  public Address getRecipient() {
    return recipient;
  }

  @Override
  public String toString() {
    return Stream
        .of(getInvoiceNumber(), getRecipient(), DateUtil.format(getDate()), getCompany(), pdf != null ? pdf.getPath() : null)
        .map(x -> x != null ? x.toString() : "-")
        .reduce((x, y) -> x + DIVIDER + y)
        .orElse("");
  }
}
