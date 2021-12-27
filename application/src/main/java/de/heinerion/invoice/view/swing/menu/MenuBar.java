package de.heinerion.invoice.view.swing.menu;

import de.heinerion.betriebe.repositories.AddressRepository;
import de.heinerion.betriebe.repositories.InvoiceRepository;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.view.swing.menu.info.InfoMenuEntry;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

class MenuBar extends JMenuBar {
  private final JFrame origin;

  private transient List<JMenuItem> menuItems;
  private final transient PathUtilNG pathUtil;
  private final transient AddressRepository addressRepository;
  private final transient InvoiceRepository invoiceRepository;

  MenuBar(JFrame origin, PathUtilNG pathUtil, AddressRepository addressRepository, InvoiceRepository invoiceRepository) {
    this.origin = origin;
    this.pathUtil = pathUtil;
    this.addressRepository = addressRepository;
    this.invoiceRepository = invoiceRepository;
    createWidgets();
    addWidgets();
  }

  private void createWidgets() {
    menuItems = Arrays.asList(
        createItem(new AddressBookMenuEntry(addressRepository)),
        createItem(new InvoicesMenuEntry(invoiceRepository)),
        createItem(new InvoiceNumbersMenuEntry()),
        createItem(new InvoiceDateMenuEntry()),
        createItem(new InfoMenuEntry(pathUtil))
    );
  }

  private JMenuItem createItem(MenuEntry menuEntry) {
    menuEntry.setBusyFrame(origin);
    JMenuItem item = new JMenuItem(menuEntry.getLinkText());
    item.addActionListener(e -> menuEntry.showDialog());
    return item;
  }

  private void addWidgets() {
    menuItems.forEach(this::add);
  }
}
