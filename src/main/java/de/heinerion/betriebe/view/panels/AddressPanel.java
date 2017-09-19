package de.heinerion.betriebe.view.panels;

import javax.swing.*;

@SuppressWarnings("serial")
class AddressPanel extends SidePanel implements Refreshable {
  private final AddressChooserPanel addressChooser;

  AddressPanel() {
    this.addressChooser = new AddressChooserPanel();
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
