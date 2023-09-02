package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.models.Address;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.util.Optional;

@RequiredArgsConstructor
class AddressForm {
  private final JTextArea addressArea;

  public void clear() {
    this.addressArea.setText("");
  }

  public String getText() {
    return this.addressArea.getText();
  }

  public void setAddress(Address address) {
    String addressText = Optional.ofNullable(address)
        .map(Address::getBlock)
        .orElse(null);
    this.addressArea.setText(addressText);
  }
}
