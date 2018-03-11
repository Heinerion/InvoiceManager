package de.heinerion.invoice.view.swing.menu;

import de.heinerion.invoice.view.swing.menu.info.InfoMenuEntry;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
class MenuBar extends JMenuBar {
  private final JFrame origin;

  private transient List<JMenuItem> menuItems;

  MenuBar(JFrame origin) {
    this.origin = origin;
    createWidgets();
    addWidgets();
  }

  private void createWidgets() {
    menuItems = new ArrayList<>();
    menuItems.add(createItem(new AddressBookMenuEntry()));
    menuItems.add(createItem(new ArchiveMenuEntry()));
    menuItems.add(createItem(new InvoiceNumbersMenuEntry()));
    menuItems.add(createItem(new InvoiceDateMenuEntry()));
    menuItems.add(createItem(new InfoMenuEntry()));
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
