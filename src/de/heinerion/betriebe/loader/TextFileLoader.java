package de.heinerion.betriebe.loader;

import de.heinerion.betriebe.exceptions.HeinerionException;
import de.heinerion.betriebe.models.*;
import de.heinerion.betriebe.models.interfaces.Addressable;
import de.heinerion.betriebe.models.interfaces.Storable;
import de.heinerion.betriebe.tools.PathTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class TextFileLoader {
  private static final String ADDRESS = "Address";
  private static final String ITEM = "Item";
  private static final String LINE = "Line";
  private static final String NAME = "Name";
  private static final String PPU = "PricePerUnit";
  private static final String QTTY = "Quantity";
  private static final String TOTAL = "Total";
  private static final String UNIT = "Unit";
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

  private static final Logger logger = LogManager.getLogger(TextFileLoader.class);

  private Writer writer;
  private Map<String, String> attributes;

  public TextFileLoader() {
    setWriter(new TextFileWriter());
  }

  private void addAddress(Addressable addressable) {
    Address address = addressable.getAddress();

    attributes.put(specify(ADDRESS, RECIPIENT), address.getRecipient());
    attributes.put(specify(ADDRESS, COMPANY), address.getCompany());
    attributes.put(specify(ADDRESS, DISTRICT), address.getDistrict());
    attributes.put(specify(ADDRESS, STREET), address.getStreet());
    attributes.put(specify(ADDRESS, NUMBER), address.getNumber());
    attributes.put(specify(ADDRESS, APARTMENT), address.getApartment());
    attributes.put(specify(ADDRESS, POSTALCODE), address.getPostalCode());
    attributes.put(specify(ADDRESS, LOCATION), address.getLocation());
  }

  private String specify(String... token) {
    String result = "";
    for (int i = 0; i < token.length; i++) {
      result += token[i];
      if (i < token.length - 1) {
        result += DOT;
      }
    }
    return result;
  }

  private void addAttribute(String key, LocalDate value) {
    attributes.put(key, value.toString());
  }

  private void addAttribute(String key, Number value) {
    attributes.put(key, "" + value);
  }

  private void addItem(Item item, int number) {
    attributes.put(specify(ITEM, number + "", NAME), item.getName());
    attributes.put(specify(ITEM, number + "", UNIT), item.getUnit());

    addAttribute(specify(ITEM, number + "", PPU), item.getPricePerUnit());
    addAttribute(specify(ITEM, number + "", QTTY), item.getQuantity());
    addAttribute(specify(ITEM, number + "", TOTAL), item.getTotal());
  }

  private String generatePath(String rootPath, Storable storable) {
    return PathTools.getPath(storable);
  }

  private void prepareAddress(Address address) {
    attributes.put(RECIPIENT, address.getRecipient());
    attributes.put(COMPANY, address.getCompany());
    attributes.put(DISTRICT, address.getDistrict());
    attributes.put(STREET, address.getStreet());
    attributes.put(NUMBER, address.getNumber());
    attributes.put(APARTMENT, address.getApartment());
    attributes.put(POSTALCODE, address.getPostalCode());
    attributes.put(LOCATION, address.getLocation());
  }

  private void prepareCompany(Company company) {
    attributes.put(DESCRIPTIVE_NAME, company.getDescriptiveName());
    attributes.put(OFFICIAL_NAME, company.getOfficialName());
    attributes.put(SIGNER, company.getSigner());
    attributes.put(TAX_NUMBER, company.getTaxNumber());
    attributes.put(VALUE_ADDED_TAX, "" + company.getValueAddedTax());
    attributes.put(WAGES_PER_HOUR, company.getWagesPerHour() + "");

    addAddress(company);
  }

  private void prepareInvoice(Invoice invoice) {
    attributes.put(CLIENT, invoice.getReceiver().getRecipient());

    addAttribute(NUMBER, invoice.getNumber());
    addAttribute(DATE, invoice.getDate());

    List<Item> items = invoice.getItems();
    for (int i = 0; i < items.size(); i++) {
      addItem(items.get(i), i);
    }

    addAttribute(NET, invoice.getNet());
  }

  private void prepareLetter(Letter letter) {
    attributes.put(CLIENT, letter.getReceiver().getRecipient());

    addAttribute(DATE, letter.getDate());

    List<String> lines = letter.getMessageLines();
    for (int i = 0; i < lines.size(); i++) {
      attributes.put(LINE + i, lines.get(i));
    }
  }

  private void saveAddress(Address address, String path) throws IOException {
    attributes = new HashMap<>();

    prepareAddress(address);

    writeAttributes(generatePath(path, address));
  }

  public void saveAddresses(List<Address> addresses, String path)
      throws IOException {
    for (Address address : addresses) {
      debug("save address " + address.getRecipient());
      saveAddress(address, path);
    }
  }

  private void debug(String s, Object... objects) {
    if (logger.isDebugEnabled()) {
      logger.debug(s, objects);
    }
  }

  public void saveCompanies(List<Company> companies, String path)
      throws IOException {
    for (Company company : companies) {
      debug("save company " + company.getDescriptiveName());
      saveCompany(company, path);
    }
  }

  private void saveCompany(Company company, String path) throws IOException {
    attributes = new HashMap<>();
    prepareCompany(company);
    writeAttributes(generatePath(path, company));
  }

  private void saveInvoice(Invoice invoice, String path) throws IOException {
    attributes = new HashMap<>();
    prepareInvoice(invoice);
    writeAttributes(generatePath(path, invoice));
  }

  public void saveInvoices(List<Invoice> invoices, String path)
      throws IOException {
    for (Invoice invoice : invoices) {
      debug("save invoice " + invoice.getEntryName());
      saveInvoice(invoice, path);
    }
  }

  private void saveLetter(Letter letter, String path) throws IOException {
    attributes = new HashMap<>();
    prepareLetter(letter);
    writeAttributes(generatePath(path, letter));
  }

  public void saveLetters(List<Letter> letters, String path)
      throws IOException {
    for (Letter letter : letters) {
      debug("save letter " + letter.getEntryName());
      saveLetter(letter, path);
    }
  }

  protected void setWriter(Writer aWriter) {
    writer = aWriter;
  }

  private void write(String key, String value) {
    try {
      writer.write(key, value);
    } catch (IOException e) {
      logger.error(e);
      HeinerionException.rethrow(e);
    }
  }

  private void writeAttributes(String path)
      throws IOException {
    writer.prepareFile(path);
    debug("writing to {}... ", path);
    attributes.forEach(this::write);

    writer.closeFile();
  }
}
