package de.heinerion.invoice.view.swing.home.receiver;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.repositories.AddressRepository;
import de.heinerion.invoice.view.swing.home.Refreshable;
import de.heinerion.invoice.view.swing.laf.LookAndFeelUtil;

import javax.swing.*;

class AddressPanel implements Refreshable {
  private final SidePanel sidePanel;
  private final AddressChooserPanel addressChooser;

  AddressPanel(AddressRepository addressRepository, Session session, LookAndFeelUtil lookAndFeelUtil) {
    this.addressChooser = new AddressChooserPanel(addressRepository, session, lookAndFeelUtil);
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
