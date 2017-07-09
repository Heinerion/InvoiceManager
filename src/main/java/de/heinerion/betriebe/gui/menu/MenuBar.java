package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.gui.ApplicationFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public final class MenuBar extends JMenuBar {
  private final ApplicationFrame origin;

  private transient List<JMenuItem> menuItems;

  public MenuBar(ApplicationFrame origin) {
    this.origin = origin;
    createWidgets();
    addWidgets();
  }

  private void createWidgets() {
    menuItems = new ArrayList<>();
    menuItems.add(createItem(new AddressBookMenu(origin)));
    menuItems.add(createItem(new ArchiveMenu(origin)));
    menuItems.add(createItem(new InvoiceNumbersMenu(origin)));
    menuItems.add(createItem(new InvoiceDateMenu(origin)));
  }

  private JMenuItem createItem(AbstractMenu menu) {
    JMenuItem item = new JMenuItem(menu.getLinkText());
    item.addActionListener(e -> menu.showDialog());
    return item;
  }

  private void addWidgets() {
    menuItems.forEach(this::add);
  }
}
