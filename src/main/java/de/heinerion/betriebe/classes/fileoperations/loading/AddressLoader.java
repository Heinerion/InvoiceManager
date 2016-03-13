package de.heinerion.betriebe.classes.fileoperations.loading;

import de.heinerion.betriebe.models.Address;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

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
    return Pattern.compile(".*\\.address$");
  }

  @Override
  // TODO new class DataStore instead of Map<S, S>
  protected Address parse(Map<String, String> attributes) {
    String recipient = attributes.get(RECIPIENT);
    String company = attributes.get(COMPANY);
    String district = attributes.get(DISTRICT);
    String street = attributes.get(STREET);
    String number = attributes.get(NUMBER);
    String apartment = attributes.get(APARTMENT);
    String postalCode = attributes.get(POSTALCODE);
    String location = attributes.get(LOCATION);

    return new Address(recipient, company, district, street, number, apartment, postalCode, location);
  }
}
