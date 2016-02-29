package de.heinerion.betriebe.classes.gui.panels;

import de.heinerion.betriebe.classes.gui.RechnungFrame;
import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.formatter.Formatter;
import de.heinerion.betriebe.formatter.PlainFormatter;
import de.heinerion.betriebe.models.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.List;

public final class PanelControl {
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
      recipient = address.split(Constants.NEWLINE)[0];

      final String[] stringToken = address.split("\\n");
      token = stringToken.length;

      final String streetAndNumber = stringToken[token - 2].trim();
      final String[] numberToken = streetAndNumber.split(Constants.SPACE);
      String streetTemp = "";
      for (int i = 0; i < numberToken.length - 1; i++) {
        streetTemp += numberToken[i].trim() + Constants.SPACE;
      }
      street = streetTemp.trim();
      number = numberToken[numberToken.length - 1].trim();

      final String codeAndLocation = stringToken[token - 1].trim();
      final String[] locationToken = codeAndLocation.split(Constants.SPACE);
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

  /**
   *
   */
  public static void saveAddress(String address) {
    if (logger.isDebugEnabled()) {
      logger.debug("save...");
    }
    if (address != null && !"".equals(address)) {
      DataBase.addAdresse(parseAddress(address));
    }
  }

  /**
   *
   */
  public static String useGivenAddress(Address address) {
    final String result;

    if (address != null) {
      final Formatter formatter = new PlainFormatter();
      formatter.formatAddress(address);
      final List<String> out = formatter.getOutput();
      String addressAsText = "";
      for (final String line : out) {
        addressAsText += line + Constants.NEWLINE;
      }
      result = addressAsText;
    } else {
      result = null;
    }

    return result;
  }

  /**
   * @return
   */
  public static ImageIcon loadImage(String path) {
    ImageIcon image = null;

    // Benutze den Classloader um Bilder einzubinden
    final java.net.URL imageURL = RechnungFrame.class.getClassLoader()
        .getResource(path);
    if (imageURL != null) {
      image = new ImageIcon(imageURL);
    } else {
      image = null;
    }
    return image;
  }
}
