package de.heinerion.betriebe.view.swing.home.receiver;

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

    String location = null;
    String number = null;
    String postalCode = null;
    String recipient = null;
    String street = null;

    if (address != null) {
      recipient = address.split("\n")[0];

      String[] stringToken = address.split("\\n");
      token = stringToken.length;

      String streetAndNumber = stringToken[token - 2].trim();
      String[] numberToken = streetAndNumber.split(" ");
      StringBuilder streetTemp = new StringBuilder();
      for (int i = 0; i < numberToken.length - 1; i++) {
        streetTemp.append(numberToken[i].trim())
            .append(" ");
      }
      street = streetTemp.toString().trim();
      number = numberToken[numberToken.length - 1].trim();

      String codeAndLocation = stringToken[token - 1].trim();
      String[] locationToken = codeAndLocation.split(" ");
      StringBuilder locationTemp = new StringBuilder();
      for (int i = 1; i < locationToken.length; i++) {
        locationTemp.append(locationToken[i].trim())
            .append(" ");
      }
      location = locationTemp.toString().trim();
      postalCode = locationToken[0].trim();
    }

    return new Address(recipient, DEFAULT_COMPANY, DEFAULT_DISTRICT, street, number, DEFAULT_APARTMENT,
        postalCode, location);
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