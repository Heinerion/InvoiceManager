package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.models.Account;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.Translator;
import lombok.extern.flogger.Flogger;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

@Flogger
class CompanyLoader extends AbstractTextFileLoader {
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
  private static final String POSTAL_CODE = "PostalCode";
  private static final String RECIPIENT = "Recipient";
  private static final String STREET = "Street";

  CompanyLoader(File aLoadDirectory) {
    super(aLoadDirectory);
  }

  @Override
  public String getDescriptiveName() {
    return Translator.translate("company.plural");
  }

  @Override
  protected Pattern getPattern() {
    return Pattern.compile(".*\\.company$");
  }

  @Override
  protected Loadable parse(Map<String, String> attributes) {
    log.atFiner().log("lade %s", attributes.get(DESCRIPTIVE_NAME));
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

  /*
   * TODO almost exact copy of de.heinerion.invoice.storage.loading.AddressLoader.parse
   */
  private Address getAddress(Map<String, String> attributes) {
    Address address = new Address();
    address.setRecipient(getAddressPart(attributes, RECIPIENT));
    address.setCompany(getAddressPart(attributes, COMPANY));
    address.setDistrict(getAddressPart(attributes, DISTRICT));
    address.setStreet(getAddressPart(attributes, STREET));
    address.setNumber(getAddressPart(attributes, NUMBER));
    address.setApartment(getAddressPart(attributes, APARTMENT));
    address.setPostalCode(getAddressPart(attributes, POSTAL_CODE));
    address.setLocation(getAddressPart(attributes, LOCATION));

    return address;
  }

  private String getAddressPart(Map<String, String> attributes, String part) {
    return readAttribute(attributes, ADDRESS + DOT + part);
  }

  private String readAttribute(Map<String, String> attributes, String key) {
    String value = attributes.get(key);
    return value.trim().equals("null") ? null : value;
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
