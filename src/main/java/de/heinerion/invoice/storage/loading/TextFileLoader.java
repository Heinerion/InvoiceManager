package de.heinerion.invoice.storage.loading;

import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.models.Storable;
import de.heinerion.invoice.storage.PathTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @deprecated this class will eventually be replaced
 */
@Deprecated
public class TextFileLoader {
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
    setWriter(new TextFileWriter());
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

  private void saveAddress(Address address) throws IOException {
    attributes = new HashMap<>();

    prepareAddress(address);

    writeAttributes(generatePath(address));
  }

  void saveAddresses(List<Address> addresses)
      throws IOException {
    for (Address address : addresses) {
      debug("save address " + address.getRecipient());
      saveAddress(address);
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
