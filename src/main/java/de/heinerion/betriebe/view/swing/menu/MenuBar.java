package de.heinerion.betriebe.view.swing.menu;

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
    menuItems.add(createItem(new AddressBookMenu()));
    menuItems.add(createItem(new ArchiveMenu()));
    menuItems.add(createItem(new InvoiceNumbersMenu()));
    menuItems.add(createItem(new InvoiceDateMenu()));
    menuItems.add(createItem(new InfoMenu()));
  }

  private JMenuItem createItem(Menu menu) {
    menu.setBusyFrame(origin);
    JMenuItem item = new JMenuItem(menu.getLinkText());
    item.addActionListener(e -> menu.showDialog());
    return item;
  }

  private void addWidgets() {
    menuItems.forEach(this::add);
  }
}
