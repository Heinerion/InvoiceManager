package de.heinerion.betriebe.classes.fileOperations.loading;

import de.heinerion.betriebe.models.Account;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Company;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

public final class CompanyLoader extends AbstractTextFileLoader<Company> {
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
    final Pattern addressFileName = Pattern.compile(".*\\.company$");
    return addressFileName;
  }

  @Override
  protected Loadable parse(Map<String, String> attributes) {
    // TODO String rausziehen?

    final Address address = new Address(attributes.get(ADDRESS + DOT
        + RECIPIENT), attributes.get(ADDRESS + DOT + COMPANY),
        attributes.get(ADDRESS + DOT + DISTRICT), attributes.get(ADDRESS + DOT
            + STREET), attributes.get(ADDRESS + DOT + NUMBER),
            attributes.get(ADDRESS + DOT + APARTMENT), attributes.get(ADDRESS + DOT
                + POSTALCODE), attributes.get(ADDRESS + DOT + LOCATION));
    final double valueAddedTax = Double.parseDouble(attributes
        .get(VALUE_ADDED_TAX));
    final double wagesPerHour = Double.parseDouble(attributes
        .get(WAGES_PER_HOUR));
    final Account bankAccount = new Account(
        attributes.get(ACCOUNT + DOT + NAME), attributes.get(ACCOUNT + DOT
            + BIC), attributes.get(ACCOUNT + DOT + IBAN));
    final Company result = new Company(attributes.get(DESCRIPTIVE_NAME),
        attributes.get(OFFICIAL_NAME), address, attributes.get(SIGNER),
        attributes.get(PHONE_NUMBER), attributes.get(TAX_NUMBER),
        valueAddedTax, wagesPerHour, bankAccount);

    return result;
  }
}
