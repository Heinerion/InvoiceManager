package de.heinerion.betriebe.loader;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Invoice;
import de.heinerion.betriebe.models.Item;
import de.heinerion.betriebe.models.Letter;
import de.heinerion.betriebe.models.interfaces.Addressable;
import de.heinerion.betriebe.models.interfaces.Storable;
import de.heinerion.betriebe.tools.PathTools;

@Deprecated
public abstract class AbstractLoader implements Loader {

  private static final String ADDRESS = "Address";

  private static final String ITEM = "Item.";
  private static final String LINE = "Line.";
  private static final String NAME = ".Name";
  private static final String PPU = ".PricePerUnit";
  private static final String QTTY = ".Quantity";
  private static final String TOTAL = ".Total";
  private static final String UNIT = ".Unit";

  private static final String DOT = ".";

  private static final String CLIENT = "Client";
  private static final String DATE = "Date";
  private static final String DESCRIPTIVE_NAME = "DescriptiveName";
  private static final String NET = "Net";
  private static final String OFFICIAL_NAME = "OfficialName";
  private static final String SIGNER = "Signer";
  private static final String TAX_NUMBER = "TaxNumber";
  private static final String VALUE_ADDED_TAX = "ValueAddedTax";
  private static final String WAGES_PER_HOUR = "WagesPerHour";

  private static final String APARTMENT = "Apartment";
  private static final String COMPANY = "Company";
  private static final String DISTRICT = "District";
  private static final String LOCATION = "Location";
  private static final String NUMBER = "Number";
  private static final String POSTALCODE = "PostalCode";
  private static final String RECIPIENT = "Recipient";
  private static final String STREET = "Street";

  private static Logger logger = LogManager.getLogger(AbstractLoader.class);

  private Writer writer;
  @SuppressWarnings("unused")
  private Reader reader;

  private void addAddress(Map<String, String> attributes,
      Addressable addressable) {
    final Address address = addressable.getAddress();
    attributes.put(ADDRESS + DOT + RECIPIENT, address.getRecipient());
    attributes.put(ADDRESS + DOT + COMPANY, address.getCompany());
    attributes.put(ADDRESS + DOT + DISTRICT, address.getDistrict());
    attributes.put(ADDRESS + DOT + STREET, address.getStreet());
    attributes.put(ADDRESS + DOT + NUMBER, address.getNumber());
    attributes.put(ADDRESS + DOT + APARTMENT, address.getApartment());
    attributes.put(ADDRESS + DOT + POSTALCODE, address.getPostalCode());
    attributes.put(ADDRESS + DOT + LOCATION, address.getLocation());
  }

  private void addAttribute(Map<String, String> attributes, String key,
      LocalDate value) {
    attributes.put(key, value.toString());
  }

  private void addAttribute(Map<String, String> attributes, String key,
      Number value) {
    attributes.put(key, "" + value);
  }

  private void addItem(Map<String, String> attributes, Item item, int number) {
    attributes.put(ITEM + number + NAME, item.getName());
    attributes.put(ITEM + number + UNIT, item.getUnit());
    this.addAttribute(attributes, ITEM + number + PPU, item.getPricePerUnit());
    this.addAttribute(attributes, ITEM + number + QTTY, item.getQuantity());
    this.addAttribute(attributes, ITEM + number + TOTAL, item.getTotal());
  }

  private String generatePath(String rootPath, Storable storable) {
    return PathTools.getPath(storable);
  }

  @Override
  public final List<Address> loadAddresses(String path) {
    return null;
  }

  @Override
  public final List<Company> loadCompanies(String path) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public final List<Invoice> loadInvoices(String path) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public final List<Letter> loadLetters(String path) {
    // TODO Auto-generated method stub
    return null;
  }

  private void prepareAddress(Map<String, String> attributes, Address address) {
    attributes.put(RECIPIENT, address.getRecipient());
    attributes.put(COMPANY, address.getCompany());
    attributes.put(DISTRICT, address.getDistrict());
    attributes.put(STREET, address.getStreet());
    attributes.put(NUMBER, address.getNumber());
    attributes.put(APARTMENT, address.getApartment());
    attributes.put(POSTALCODE, address.getPostalCode());
    attributes.put(LOCATION, address.getLocation());
  }

  private void prepareCompany(Map<String, String> attributes, Company company) {
    attributes.put(DESCRIPTIVE_NAME, company.getDescriptiveName());
    attributes.put(OFFICIAL_NAME, company.getOfficialName());
    attributes.put(SIGNER, company.getSigner());
    attributes.put(TAX_NUMBER, company.getTaxNumber());
    attributes.put(VALUE_ADDED_TAX, "" + company.getValueAddedTax());
    attributes.put(WAGES_PER_HOUR, company.getWagesPerHour() + "");
    this.addAddress(attributes, company);
  }

  private void prepareInvoice(Map<String, String> attributes, Invoice invoice) {
    attributes.put(CLIENT, invoice.getReceiver().getRecipient());
    this.addAttribute(attributes, NUMBER, invoice.getNumber());
    this.addAttribute(attributes, DATE, invoice.getDate());
    final List<Item> items = invoice.getItems();
    for (int i = 0; i < items.size(); i++) {
      this.addItem(attributes, items.get(i), i);
    }
    this.addAttribute(attributes, NET, invoice.getNet());
  }

  private void prepareLetter(Map<String, String> attributes, Letter letter) {
    attributes.put(CLIENT, letter.getReceiver().getRecipient());
    this.addAttribute(attributes, DATE, letter.getDate());
    final List<String> lines = letter.getMessageLines();
    for (int i = 0; i < lines.size(); i++) {
      attributes.put(LINE + i, lines.get(i));
    }
  }

  private void saveAddress(Address address, String path) throws IOException {
    final Map<String, String> attributes = new HashMap<>();
    this.prepareAddress(attributes, address);
    this.writeAttributes(attributes, this.generatePath(path, address));
  }

  @Override
  public final void saveAddresses(List<Address> addresses, String path)
      throws IOException {
    for (final Address address : addresses) {
      if (logger.isDebugEnabled()) {
        logger.debug("save " + address.getRecipient());
      }
      this.saveAddress(address, path);
    }
  }

  @Override
  public final void saveCompanies(List<Company> companies, String path)
      throws IOException {
    for (final Company company : companies) {
      this.saveCompany(company, path);
    }
  }

  private void saveCompany(Company company, String path) throws IOException {
    final Map<String, String> attributes = new HashMap<>();
    this.prepareCompany(attributes, company);
    this.writeAttributes(attributes, this.generatePath(path, company));
  }

  private void saveInvoice(Invoice invoice, String path) throws IOException {
    final Map<String, String> attributes = new HashMap<>();
    this.prepareInvoice(attributes, invoice);
    this.writeAttributes(attributes, this.generatePath(path, invoice));
  }

  @Override
  public final void saveInvoices(List<Invoice> invoices, String path)
      throws IOException {
    for (final Invoice invoice : invoices) {
      this.saveInvoice(invoice, path);
    }
  }

  private void saveLetter(Letter letter, String path) throws IOException {
    final Map<String, String> attributes = new HashMap<>();
    this.prepareLetter(attributes, letter);
    this.writeAttributes(attributes, this.generatePath(path, letter));
  }

  @Override
  public final void saveLetters(List<Letter> letters, String path)
      throws IOException {
    for (final Letter letter : letters) {
      this.saveLetter(letter, path);
    }
  }

  protected final void setReader(Reader aReader) {
    this.reader = aReader;
  }

  protected final void setWriter(Writer aWriter) {
    this.writer = aWriter;
  }

  /**
   * @param key
   * @param value
   */
  private void write(String key, String value) {
    try {
      this.writer.write(key, value);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private void writeAttributes(Map<String, String> attributes, String path)
      throws IOException {
    this.writer.prepareFile(path);
    if (logger.isDebugEnabled()) {
      logger.debug("writing to {}... ", path);
    }
    attributes.forEach((key, value) -> this.write(key, value));

    this.writer.closeFile();
  }
}
