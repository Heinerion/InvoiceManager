package de.heinerion.betriebe.view.menu;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.view.menu.tablemodels.AddressModel;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.services.Translator;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
class AddressBookMenu extends AbstractMenu {
  private static final String NAME = Translator.translate("menu.addressBook");
  private JScrollPane spAddresses;

  @Override
  protected void addWidgets(JDialog dialog) {
    dialog.setLayout(new BorderLayout());
    dialog.add(getBtnOk(), BorderLayout.PAGE_END);
    dialog.add(spAddresses, BorderLayout.CENTER);
  }

  @Override
  protected void createWidgets() {
    List<Address> addresses = DataBase.getAddresses(Session.getActiveCompany());
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
