package de.heinerion.invoice.view.swing.menu;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.view.swing.menu.info.InfoMenuEntry;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
class MenuBar extends JMenuBar {
  private final JFrame origin;

  private transient List<JMenuItem> menuItems;
  private transient PathUtilNG pathUtil;
  private final DataBase dataBase;

  MenuBar(JFrame origin, PathUtilNG pathUtil, DataBase dataBase) {
    this.origin = origin;
    this.pathUtil = pathUtil;
    this.dataBase = dataBase;
    createWidgets();
    addWidgets();
  }

  private void createWidgets() {
    menuItems = new ArrayList<>();
    menuItems.add(createItem(new AddressBookMenuEntry(dataBase)));
    menuItems.add(createItem(new ArchiveMenuEntry(dataBase)));
    menuItems.add(createItem(new InvoiceNumbersMenuEntry()));
    menuItems.add(createItem(new InvoiceDateMenuEntry()));
    menuItems.add(createItem(new InfoMenuEntry(pathUtil)));
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
