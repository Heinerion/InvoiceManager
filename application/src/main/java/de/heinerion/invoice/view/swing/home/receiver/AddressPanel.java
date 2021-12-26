package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.invoice.view.formatter.Formatter;
import de.heinerion.invoice.view.swing.home.Refreshable;

import javax.swing.*;

class AddressPanel implements Refreshable {
  private final SidePanel sidePanel;
  private final AddressChooserPanel addressChooser;

  AddressPanel(Formatter formatter, DataBase dataBase) {
    this.addressChooser = new AddressChooserPanel(formatter, dataBase);
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
