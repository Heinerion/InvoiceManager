package de.heinerion.invoice.storage.xml.jaxb.migration;

import de.heinerion.betriebe.models.Address;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.storage.loading.AbstractTextFileLoader;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

public class AddressLoader extends AbstractTextFileLoader {
  private static final String APARTMENT = "Apartment";
  private static final String COMPANY = "Company";
  private static final String DISTRICT = "District";
  private static final String LOCATION = "Location";
  private static final String NUMBER = "Number";
  private static final String POSTAL_CODE = "PostalCode";
  private static final String RECIPIENT = "Recipient";
  private static final String STREET = "Street";

  public AddressLoader(File aLoadDirectory) {
    super(aLoadDirectory);
  }

  @Override
  public String getDescriptiveName() {
    return Translator.translate("address.plural");
  }

  @Override
  protected Pattern getPattern() {
    return Pattern.compile(".*\\.address$");
  }

  @Override
  // TODO new class DataStore instead of Map<S, S>
  protected Address parse(Map<String, String> attributes) {
    Address address = new Address();
    address.setRecipient(readAttribute(attributes, RECIPIENT));
    address.setCompany(readAttribute(attributes, COMPANY));
    address.setDistrict(readAttribute(attributes, DISTRICT));
    address.setStreet(readAttribute(attributes, STREET));
    address.setNumber(readAttribute(attributes, NUMBER));
    address.setApartment(readAttribute(attributes, APARTMENT));
    address.setPostalCode(readAttribute(attributes, POSTAL_CODE));
    address.setLocation(readAttribute(attributes, LOCATION));

    return address;
  }

  private String readAttribute(Map<String, String> attributes, String key) {
    String value = attributes.get(key);
    return value.trim().equals("null") ? null : value;
  }
}
