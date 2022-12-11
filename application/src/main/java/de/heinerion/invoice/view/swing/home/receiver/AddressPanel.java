package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.repositories.AddressRepository;
import de.heinerion.invoice.view.formatter.Formatter;
import de.heinerion.invoice.view.swing.home.Refreshable;

import javax.swing.*;

class AddressPanel implements Refreshable {
  private final SidePanel sidePanel;
  private final AddressChooserPanel addressChooser;

  AddressPanel(Formatter formatter, AddressRepository addressRepository, Session session) {
    this.addressChooser = new AddressChooserPanel(formatter, addressRepository, session);
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
