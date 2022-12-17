package de.heinerion.invoice.view.swing.menu;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.Address;
import de.heinerion.invoice.repositories.AddressRepository;
import de.heinerion.invoice.view.swing.menu.tablemodels.AddressModel;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * @author heiner
 */
@RequiredArgsConstructor
class AddressBookMenuEntry extends MenuEntry {
  private static final String NAME = Menu.translate("addressBook");
  private final AddressRepository addressRepository;
  private final Session session;

  private JScrollPane spAddresses;

  @Override
  protected void addWidgets(JDialog dialog) {
    dialog.setLayout(new BorderLayout());
    dialog.add(getBtnOk(), BorderLayout.PAGE_END);
    dialog.add(spAddresses, BorderLayout.CENTER);
  }

  @Override
  protected void createWidgets() {
    Collection<Address> addresses = session.getActiveCompany()
        .map(addressRepository::findByOwner)
        .orElse(Collections.emptyList());

    AddressModel model = new AddressModel(addresses.stream()
        .sorted()
        .toList());

    JTable tblAddresses = new JTable(model);
    tblAddresses.setAutoCreateRowSorter(true);
    tblAddresses.setRowSelectionAllowed(true);
    tblAddresses.getRowSorter().toggleSortOrder(0);

    spAddresses = new JScrollPane(tblAddresses);

    spAddresses.setPreferredSize(getBusyFrame().getSize());
  }

  @Override
  protected void setTitle(JDialog dialog) {
    dialog.setTitle(NAME);
  }

  @Override
  public String getLinkText() {
    return NAME;
  }

  @Override
  protected void setupInteractions(JDialog dialog) {
    getBtnOk().addActionListener(event -> getCloser().windowClosing(null));
  }
}
