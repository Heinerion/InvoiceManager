package de.heinerion.betriebe.classes.fileoperations.loading;

import de.heinerion.betriebe.models.Account;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

public final class CompanyLoader extends AbstractTextFileLoader<Company> {
  private static final Logger logger = LogManager.getLogger(CompanyLoader.class);

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
  private static final String POSTALCODE = "PostalCode";
  private static final String RECIPIENT = "Recipient";
  private static final String STREET = "Street";

  public CompanyLoader(File aLoadDirectory) {
    super(aLoadDirectory);
  }

  @Override
  public String getDescriptiveName() {
    return "Betriebe";
  }

  /**
   * @return
   */
  @Override
  protected Pattern getPattern() {
    return Pattern.compile(".*\\.company$");
  }

  @Override
  protected Loadable parse(Map<String, String> attributes) {
    if (logger.isDebugEnabled()) {
      logger.debug("lade {}", attributes.get(DESCRIPTIVE_NAME));
    }
    final Address address = getAddress(attributes);
    final double valueAddedTax = getValueAddedTax(attributes);
    final double wagesPerHour = getWagesPerHour(attributes);
    final Account bankAccount = getAccount(attributes);

    final String descriptiveName = attributes.get(DESCRIPTIVE_NAME);
    final String officialName = attributes.get(OFFICIAL_NAME);
    final String signer = attributes.get(SIGNER);
    final String phoneNumber = attributes.get(PHONE_NUMBER);
    final String taxNumber = attributes.get(TAX_NUMBER);

    return new Company(descriptiveName, officialName, address, signer, phoneNumber, taxNumber, valueAddedTax,
        wagesPerHour, bankAccount);
  }

  private Address getAddress(Map<String, String> attributes) {
    String recipient = getAddressPart(attributes, RECIPIENT);
    String company = getAddressPart(attributes, COMPANY);
    String district = getAddressPart(attributes, DISTRICT);
    String street = getAddressPart(attributes, STREET);
    String number = getAddressPart(attributes, NUMBER);
    String apartment = getAddressPart(attributes, APARTMENT);
    String postalCode = getAddressPart(attributes, POSTALCODE);
    String location = getAddressPart(attributes, LOCATION);

    return new Address(recipient, company, district, street, number, apartment, postalCode, location);
  }

  private String getAddressPart(Map<String, String> attributes, String part) {
    return attributes.get(ADDRESS + DOT + part);
  }

  private double getValueAddedTax(Map<String, String> attributes) {
    return Double.parseDouble(attributes
        .get(VALUE_ADDED_TAX));
  }

  private double getWagesPerHour(Map<String, String> attributes) {
    return Double.parseDouble(attributes
        .get(WAGES_PER_HOUR));
  }

  private Account getAccount(Map<String, String> attributes) {
    return new Account(
        attributes.get(ACCOUNT + DOT + NAME), attributes.get(ACCOUNT + DOT
        + BIC), attributes.get(ACCOUNT + DOT + IBAN));
  }
}
