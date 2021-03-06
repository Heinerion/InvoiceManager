package de.heinerion.invoice.view.swing.menu;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.models.Address;
import de.heinerion.invoice.view.swing.menu.tablemodels.AddressModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author heiner
 */
class AddressBookMenuEntry extends MenuEntry {
  private static final String NAME = Menu.translate("addressBook");
  private JScrollPane spAddresses;
  private DataBase dataBase = DataBase.getInstance();

  @Override
  protected void addWidgets(JDialog dialog) {
    dialog.setLayout(new BorderLayout());
    dialog.add(getBtnOk(), BorderLayout.PAGE_END);
    dialog.add(spAddresses, BorderLayout.CENTER);
  }

  @Override
  protected void createWidgets() {
    List<Address> addresses = dataBase.getAddresses(Session.getActiveCompany());
    final AddressModel model = new AddressModel(addresses);

    final JTable tblAddresses = new JTable(model);
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
