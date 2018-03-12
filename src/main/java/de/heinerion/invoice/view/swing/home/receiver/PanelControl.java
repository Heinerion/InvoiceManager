package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.models.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

class PanelControl {
  private static final Logger logger = LogManager.getLogger(PanelControl.class);

  private static final String DEFAULT_APARTMENT = null;
  private static final String DEFAULT_COMPANY = null;
  private static final String DEFAULT_DISTRICT = null;

  private PanelControl() {
  }

  /**
   * Parst aus einem \n separiertem String eine Adresse
   *
   * @param address Der String mit der Adresse, in einer Form, wie auf Briefen
   *                angegeben
   * @return die geparste Address
   */
  private static Address parseAddress(String address) {
    int token;

    Address parsedAddress = new Address();
    parsedAddress.setApartment(DEFAULT_APARTMENT);
    parsedAddress.setCompany(DEFAULT_COMPANY);
    parsedAddress.setDistrict(DEFAULT_DISTRICT);

    if (address != null) {
      parsedAddress.setRecipient(address.split("\n")[0]);

      String[] stringToken = address.split("\\n");
      token = stringToken.length;

      String streetAndNumber = stringToken[token - 2].trim();
      String[] numberToken = streetAndNumber.split(" ");
      StringBuilder streetTemp = new StringBuilder();
      for (int i = 0; i < numberToken.length - 1; i++) {
        streetTemp.append(numberToken[i].trim())
            .append(" ");
      }
      parsedAddress.setStreet(streetTemp.toString().trim());
      parsedAddress.setNumber(numberToken[numberToken.length - 1].trim());

      String codeAndLocation = stringToken[token - 1].trim();
      String[] locationToken = codeAndLocation.split(" ");
      StringBuilder locationTemp = new StringBuilder();
      for (int i = 1; i < locationToken.length; i++) {
        locationTemp.append(locationToken[i].trim())
            .append(" ");
      }
      parsedAddress.setLocation(locationTemp.toString().trim());
      parsedAddress.setPostalCode(locationToken[0].trim());
    }

    return parsedAddress;
  }

  static void saveAddress(String address) {
    if (logger.isDebugEnabled()) {
      logger.debug("save...");
    }
    if (notEmpty(address)) {
      DataBase.addAddress(parseAddress(address));
    }
  }

  private static boolean notEmpty(String address) {
    return address != null && !"".equals(address.trim());
  }

  static ImageIcon loadImage(String path) {
    ImageIcon image = null;

    // Benutze den Classloader um Bilder einzubinden
    java.net.URL imageURL = Thread.currentThread().getContextClassLoader().getResource(path);
    if (imageURL != null) {
      image = new ImageIcon(imageURL);
    }

    return image;
  }
}
