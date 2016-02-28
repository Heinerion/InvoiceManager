/**
 * GemeindenMenu.java
 * heiner 30.03.2012
 */
package de.heinerion.betriebe.gui.menu;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import de.heinerion.betriebe.classes.gui.RechnungFrame;
import de.heinerion.betriebe.classes.gui.tableModels.AddressModel;
import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.data.Session;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
public final class AdressbuchMenu extends AbstractMenu {
  private JScrollPane spAddresses;

  public AdressbuchMenu(final RechnungFrame origin) {
    // Macht Modal und setzt busy
    super(origin);
  }

  @Override
  protected void addWidgets() {
    this.setLayout(new BorderLayout());
    this.add(getBtnOk(), BorderLayout.PAGE_END);
    this.add(this.spAddresses, BorderLayout.CENTER);
  }

  @Override
  protected void createWidgets() {
    final AddressModel model = new AddressModel(DataBase.getAddresses(Session
        .getActiveCompany()));

    final JTable tblAddresses = new JTable(model);
    tblAddresses.setAutoCreateRowSorter(true);
    tblAddresses.setRowSelectionAllowed(true);
    tblAddresses.getRowSorter().toggleSortOrder(0);

    this.spAddresses = new JScrollPane(tblAddresses);

    this.spAddresses.setPreferredSize(getUrsprung().getSize());
  }

  @Override
  protected void setTitle() {
    this.setTitle("Adressbuch");
  }

  @Override
  protected void setupInteractions() {
    getBtnOk().addActionListener((event) -> getCloser().windowClosing(null));
  }
}
