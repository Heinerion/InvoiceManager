package de.heinerion.invoice.view.swing.menu;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.repositories.AddressRepository;
import de.heinerion.invoice.view.swing.menu.tablemodels.AddressModel;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author heiner
 */
@RequiredArgsConstructor
class AddressBookMenuEntry extends MenuEntry {
  private static final String NAME = Menu.translate("addressBook");
  private JScrollPane spAddresses;
  private final AddressRepository addressRepository;

  @Override
  protected void addWidgets(JDialog dialog) {
    dialog.setLayout(new BorderLayout());
    dialog.add(getBtnOk(), BorderLayout.PAGE_END);
    dialog.add(spAddresses, BorderLayout.CENTER);
  }

  @Override
  protected void createWidgets() {
    Collection<Address> addresses = Session.getActiveCompany()
        .map(addressRepository::findByCompany)
        .orElse(Collections.emptyList());

    AddressModel model = new AddressModel(addresses.stream()
        .sorted(Comparator.comparing(Address::getRecipient))
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
