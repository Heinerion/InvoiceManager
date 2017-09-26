package de.heinerion.betriebe.view.panels;

import de.heinerion.betriebe.view.formatter.Formatter;

import javax.swing.*;

@SuppressWarnings("serial")
class AddressPanel extends SidePanel implements Refreshable {
  private final AddressChooserPanel addressChooser;

  AddressPanel(Formatter formatter) {
    this.addressChooser = new AddressChooserPanel(formatter);
    add(addressChooser);
  }

  @Override
  public void refresh() {
    addressChooser.refreshBoxes();
  }

  @Override
  public JPanel getPanel() {
    return this;
  }
}
