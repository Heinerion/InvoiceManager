package de.heinerion.betriebe.view.panels;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.view.formatter.Formatter;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.services.ConfigurationService;
import de.heinerion.betriebe.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.List;

import static de.heinerion.betriebe.util.Constants.NEWLINE;

class PanelControl {
  private static final Logger logger = LogManager.getLogger(PanelControl.class);

  private static final String DEFAULT_APARTMENT = null;
  private static final String DEFAULT_COMPANY = null;
  private static final String DEFAULT_DISTRICT = null;

  private static final Formatter formatter;

  private PanelControl() {
  }

  static {
    formatter = ConfigurationService.getBean(Formatter.class);
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
      recipient = address.split(NEWLINE)[0];

      String[] stringToken = address.split("\\n");
      token = stringToken.length;

      String streetAndNumber = stringToken[token - 2].trim();
      String[] numberToken = streetAndNumber.split(Constants.SPACE);
      StringBuilder streetTemp = new StringBuilder();
      for (int i = 0; i < numberToken.length - 1; i++) {
        streetTemp.append(numberToken[i].trim())
            .append(Constants.SPACE);
      }
      street = streetTemp.toString().trim();
      number = numberToken[numberToken.length - 1].trim();

      String codeAndLocation = stringToken[token - 1].trim();
      String[] locationToken = codeAndLocation.split(Constants.SPACE);
      StringBuilder locationTemp = new StringBuilder();
      for (int i = 1; i < locationToken.length; i++) {
        locationTemp.append(locationToken[i].trim())
            .append(Constants.SPACE);
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

  static String useGivenAddress(Address address) {
    String result = null;

    if (address != null) {
      List<String> out = formatter.formatAddress(address);
      StringBuilder addressAsText = new StringBuilder();
      for (String line : out) {
        addressAsText.append(line)
            .append(NEWLINE);
      }
      result = addressAsText.toString();
    }

    return result;
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