package de.heinerion.betriebe.classes.gui.panels;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.formatter.Formatter;
import de.heinerion.betriebe.formatter.PlainFormatter;
import de.heinerion.betriebe.models.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.List;

import static de.heinerion.betriebe.data.Constants.NEWLINE;

public class PanelControl {
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
  public static Address parseAddress(String address) {
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
      String streetTemp = "";
      for (int i = 0; i < numberToken.length - 1; i++) {
        streetTemp += numberToken[i].trim() + Constants.SPACE;
      }
      street = streetTemp.trim();
      number = numberToken[numberToken.length - 1].trim();

      String codeAndLocation = stringToken[token - 1].trim();
      String[] locationToken = codeAndLocation.split(Constants.SPACE);
      String locationTemp = "";
      for (int i = 1; i < locationToken.length; i++) {
        locationTemp += locationToken[i].trim() + Constants.SPACE;
      }
      location = locationTemp.trim();
      postalCode = locationToken[0].trim();
    }

    return new Address(recipient, DEFAULT_COMPANY, DEFAULT_DISTRICT, street, number, DEFAULT_APARTMENT,
        postalCode, location);
  }

  public static void saveAddress(String address) {
    if (logger.isDebugEnabled()) {
      logger.debug("save...");
    }
    if (notEmpty(address)) {
      DataBase.addAdresse(parseAddress(address));
    }
  }

  private static boolean notEmpty(String address) {
    return address != null && !"".equals(address.trim());
  }

  public static String useGivenAddress(Address address) {
    String result = null;

    if (address != null) {
      Formatter formatter = new PlainFormatter();
      formatter.formatAddress(address);
      List<String> out = formatter.getOutput();
      String addressAsText = "";
      for (String line : out) {
        addressAsText += line + NEWLINE;
      }
      result = addressAsText;
    }

    return result;
  }

  public static ImageIcon loadImage(String path) {
    ImageIcon image = null;

    // Benutze den Classloader um Bilder einzubinden
    java.net.URL imageURL = Thread.currentThread().getContextClassLoader().getResource(path);
    if (imageURL != null) {
      image = new ImageIcon(imageURL);
    }

    return image;
  }
}
