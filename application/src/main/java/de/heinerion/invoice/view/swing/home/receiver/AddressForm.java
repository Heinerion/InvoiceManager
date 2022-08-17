package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.models.Address;
import de.heinerion.invoice.view.formatter.Formatter;

import javax.swing.*;
import java.util.List;

class AddressForm {
  private JTextArea addressArea;

  private final Formatter formatter;

  AddressForm(Formatter formatter) {
    this.formatter = formatter;
  }

  public void clear() {
    this.addressArea.setText("");
  }

  public String getText() {
    return this.addressArea.getText();
  }

  public void setAddressArea(JTextArea addressArea) {
    this.addressArea = addressArea;
  }

  public void setAddress(Address address) {
    String result = null;

    if (address != null) {
      List<String> out = formatter.formatAddress(address);
      StringBuilder addressAsText = new StringBuilder();
      for (String line : out) {
        addressAsText.append(line)
            .append("\n");
      }
      result = addressAsText.toString();
    }

    final String addressText = result;
    this.addressArea.setText(addressText);
  }
}
