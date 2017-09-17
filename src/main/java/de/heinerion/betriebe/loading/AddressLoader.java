package de.heinerion.betriebe.loading;

import de.heinerion.betriebe.models.Address;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

class AddressLoader extends AbstractTextFileLoader {
  private static final String APARTMENT = "Apartment";
  private static final String COMPANY = "Company";
  private static final String DISTRICT = "District";
  private static final String LOCATION = "Location";
  private static final String NUMBER = "Number";
  private static final String POSTALCODE = "PostalCode";
  private static final String RECIPIENT = "Recipient";
  private static final String STREET = "Street";

  AddressLoader(File aLoadDirectory) {
    super(aLoadDirectory);
  }

  @Override
  public String getDescriptiveName() {
    return "Adressen";
  }

  @Override
  protected Pattern getPattern() {
    return Pattern.compile(".*\\.address$");
  }

  @Override
  // TODO new class DataStore instead of Map<S, S>
  protected Address parse(Map<String, String> attributes) {
    String recipient = readAttribute(attributes, RECIPIENT);
    String company = readAttribute(attributes, COMPANY);
    String district = readAttribute(attributes, DISTRICT);
    String street = readAttribute(attributes, STREET);
    String number = readAttribute(attributes, NUMBER);
    String apartment = readAttribute(attributes, APARTMENT);
    String postalCode = readAttribute(attributes, POSTALCODE);
    String location = readAttribute(attributes, LOCATION);

    return new Address(recipient, company, district, street, number, apartment, postalCode, location);
  }

  private String readAttribute(Map<String, String> attributes, String key) {
    String value = attributes.get(key);
    return value.trim().equals("null") ? null : value;
  }
}
