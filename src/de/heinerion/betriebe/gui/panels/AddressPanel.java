package de.heinerion.betriebe.gui.panels;


@SuppressWarnings("serial")
public final class AddressPanel extends SidePanel {
  private AddressChooserPanel addressChooser;

  public AddressPanel() {
    this.addressChooser = new AddressChooserPanel();
    add(addressChooser);
  }

  public void refreshBoxes() {
    addressChooser.refreshBoxes();
  }
}
