package de.heinerion.betriebe.classes.fileOperations.loading;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

import de.heinerion.betriebe.models.Address;

public final class AddressLoader extends AbstractTextFileLoader<Address> {
  private static final String APARTMENT = "Apartment";
  private static final String COMPANY = "Company";
  private static final String DISTRICT = "District";
  private static final String LOCATION = "Location";
  private static final String NUMBER = "Number";
  private static final String POSTALCODE = "PostalCode";
  private static final String RECIPIENT = "Recipient";
  private static final String STREET = "Street";

  public AddressLoader(File aLoadDirectory) {
    super(aLoadDirectory);
  }

  @Override
  public String getDescriptiveName() {
    return "Adressen";
  }

  /**
   * @return
   */
  @Override
  protected Pattern getPattern() {
    final Pattern addressFileName = Pattern.compile(".*\\.address$");
    return addressFileName;
  }

  @Override
  protected Address parse(Map<String, String> attributes) {
    final Address result = new Address(attributes.get(RECIPIENT),
        attributes.get(COMPANY), attributes.get(DISTRICT),
        attributes.get(STREET), attributes.get(NUMBER),
        attributes.get(APARTMENT), attributes.get(POSTALCODE),
        attributes.get(LOCATION));

    return result;
  }
}
