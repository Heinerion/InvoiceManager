package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Storable;
import de.heinerion.invoice.storage.PathTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @deprecated this class will eventually be replaced
 */
@Deprecated
public class TextFileLoader {

  private static final String EMPTY = "";

  private static final String ADDRESS = "Address";
  private static final String ACCOUNT = "Account";

  private static final String NAME = "Name";
  private static final String BIC = "Bic";
  private static final String IBAN = "Iban";

  private static final String DOT = ".";

  private static final String DESCRIPTIVE_NAME = "DescriptiveName";
  private static final String OFFICIAL_NAME = "OfficialName";
  private static final String SIGNER = "Signer";
  private static final String TAX_NUMBER = "TaxNumber";
  private static final String VALUE_ADDED_TAX = "ValueAddedTax";
  private static final String WAGES_PER_HOUR = "WagesPerHour";
  private static final String PHONE_NUMBER = "PhoneNumber";

  private static final String APARTMENT = "Apartment";
  private static final String COMPANY = "Company";
  private static final String DISTRICT = "District";
  private static final String LOCATION = "Location";
  private static final String NUMBER = "Number";
  private static final String POSTCODE = "PostalCode";
  private static final String RECIPIENT = "Recipient";
  private static final String STREET = "Street";

  private static final Logger logger = LogManager.getLogger(TextFileLoader.class);

  private Writer writer;
  private Map<String, String> attributes;

  TextFileLoader() {
    setWriter(new Writer());
  }

  private String generatePath(Storable storable) {
    return PathTools.getPath(storable);
  }

  private void prepareAddress(Address address) {
    attributes.put(RECIPIENT, address.getRecipient());
    attributes.put(COMPANY, address.getCompany().orElse(""));
    attributes.put(DISTRICT, address.getDistrict().orElse(""));
    attributes.put(STREET, address.getStreet());
    attributes.put(NUMBER, address.getNumber());
    attributes.put(APARTMENT, address.getApartment().orElse(""));
    attributes.put(POSTCODE, address.getPostalCode());
    attributes.put(LOCATION, address.getLocation());
  }

  private void prepareCompany(Company company) {
    attributes.put(ADDRESS + DOT + RECIPIENT, company.getAddress().getRecipient());
    attributes.put(ADDRESS + DOT + COMPANY, company.getAddress().getCompany().orElse(EMPTY));
    attributes.put(ADDRESS + DOT + DISTRICT, company.getAddress().getDistrict().orElse(EMPTY));
    attributes.put(ADDRESS + DOT + STREET, company.getAddress().getStreet());
    attributes.put(ADDRESS + DOT + NUMBER, company.getAddress().getNumber());
    attributes.put(ADDRESS + DOT + APARTMENT, company.getAddress().getApartment().orElse(EMPTY));
    attributes.put(ADDRESS + DOT + POSTCODE, company.getAddress().getPostalCode());
    attributes.put(ADDRESS + DOT + LOCATION, company.getAddress().getLocation());

    attributes.put(VALUE_ADDED_TAX, String.valueOf(company.getValueAddedTax()));
    attributes.put(WAGES_PER_HOUR, String.valueOf(company.getWagesPerHour()));

    attributes.put(ACCOUNT + DOT + NAME, company.getAccount().getName());
    attributes.put(ACCOUNT + DOT + BIC, company.getAccount().getBic());
    attributes.put(ACCOUNT + DOT + IBAN, company.getAccount().getIban());

    attributes.put(DESCRIPTIVE_NAME, company.getDescriptiveName());
    attributes.put(OFFICIAL_NAME, company.getOfficialName());
    attributes.put(SIGNER, company.getSigner());
    attributes.put(PHONE_NUMBER, company.getPhoneNumber());
    attributes.put(TAX_NUMBER, company.getTaxNumber());
  }

  private void saveAddress(Address address) throws IOException {
    attributes = new HashMap<>();

    prepareAddress(address);

    writeAttributes(generatePath(address));
  }

  private void saveCompany(Company company) throws IOException {
    attributes = new HashMap<>();

    prepareCompany(company);

    writeAttributes(generatePath(company));
  }


  void saveAddresses(List<Address> addresses)
      throws IOException {
    for (Address address : addresses) {
      debug("save address " + address.getRecipient());
      saveAddress(address);
    }
  }

  public void saveCompanies(Collection<Company> companies) throws IOException {
    for (Company company : companies) {
      debug("save address " + company.getDescriptiveName());
      saveCompany(company);
    }
  }

  private void debug(String s, Object... objects) {
    if (logger.isDebugEnabled()) {
      logger.debug(s, objects);
    }
  }

  void setWriter(Writer aWriter) {
    writer = aWriter;
  }

  private void write(String key, String value) {
    try {
      writer.write(key, value);
    } catch (IOException e) {
      logger.error(e);
      throw new CouldNotWriteException(e);
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
