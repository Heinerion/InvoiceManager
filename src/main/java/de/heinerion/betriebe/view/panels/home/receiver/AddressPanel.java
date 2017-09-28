package de.heinerion.betriebe.view.panels.home.receiver;

import de.heinerion.betriebe.view.formatter.Formatter;
import de.heinerion.betriebe.view.panels.home.Refreshable;

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
