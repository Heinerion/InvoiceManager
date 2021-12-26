package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.models.Storable;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.storage.PathTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @deprecated this class will eventually be replaced
 */
@Flogger
@Service
@Deprecated
@RequiredArgsConstructor
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

  private Writer writer = new Writer();
  private Map<String, String> attributes;

  private final PathUtilNG pathUtil;

  private String generatePath(Storable storable) {
    return PathTools.getPath(storable, pathUtil);
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

  public void saveAddresses(Collection<Address> addresses) {
    try {
      for (Address address : addresses) {
        debug("save address %s", address.getRecipient());
        saveAddress(address);
      }
    } catch (final IOException e) {
      log.atSevere().withCause(e).log("could not save the addresses");
      throw new AddressesCouldNotBeSavedException(addresses, e);
    }
  }


  public void saveCompanies(Collection<Company> companies) {
    try {
      for (Company company : companies) {
        debug("save address %s", company.getDescriptiveName());
        saveCompany(company);
      }
    } catch (final IOException e) {
      log.atSevere().withCause(e).log("could not save the companies");

      throw new RuntimeException(companies.stream()
          .map(Company::getDescriptiveName)
          .collect(Collectors.joining(", ")), e);
    }
  }

  private void debug(String s, Object... objects) {
    log.atFine().logVarargs(s, objects);
  }

  void setWriter(Writer aWriter) {
    writer = aWriter;
  }

  private void write(String key, String value) {
    try {
      writer.write(key, value);
    } catch (IOException e) {
      log.atSevere().withCause(e).log();
      throw new CouldNotWriteException(e);
    }
  }

  private void writeAttributes(String path)
      throws IOException {
    writer.prepareFile(path);
    debug("writing to %s... ", path);
    attributes.forEach(this::write);

    writer.closeFile();
  }


}


