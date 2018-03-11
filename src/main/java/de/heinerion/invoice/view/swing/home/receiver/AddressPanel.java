package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.view.formatter.Formatter;
import de.heinerion.invoice.view.swing.home.Refreshable;

import javax.swing.*;

@SuppressWarnings("serial")
class AddressPanel implements Refreshable {
  private SidePanel sidePanel;
  private final AddressChooserPanel addressChooser;

  AddressPanel(Formatter formatter) {
    this.addressChooser = new AddressChooserPanel(formatter);
    sidePanel = new SidePanel();

    sidePanel.add(addressChooser);
  }

  @Override
  public void refresh() {
    addressChooser.refreshBoxes();
  }

  @Override
  public JPanel getPanel() {
    return sidePanel;
  }
}
