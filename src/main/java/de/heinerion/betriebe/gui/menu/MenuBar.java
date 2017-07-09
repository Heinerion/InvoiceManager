package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.gui.ApplicationFrame;

import javax.swing.*;

@SuppressWarnings("serial")
public final class MenuBar extends JMenuBar {
  private static MenuBar instance;

  private JMenuItem addresses;
  private JMenuItem invoices;
  private JMenuItem numbers;
  private JMenuItem date;

  private MenuBar() {
    createWidgets();
    addWidgets();
    setupInteractions();
  }

  private void createWidgets() {
    addresses = createItem(AddressBookMenu.NAME);
    invoices = createItem(ArchiveMenu.NAME);
    numbers = createItem(InvoiceNumbersMenu.NAME);
    date = createItem(InvoiceDateMenu.NAME);
  }

  private JMenuItem createItem(String linkText) {
    return new JMenuItem(linkText);
  }

  private void addWidgets() {
    add(addresses);
    add(invoices);
    add(numbers);
    add(date);
  }

  private void setupInteractions() {
    addresses.addActionListener(e -> new AddressBookMenu(getFrame()));
    invoices.addActionListener(e -> new ArchiveMenu(getFrame()));
    numbers.addActionListener(e -> new InvoiceNumbersMenu(getFrame()));
    date.addActionListener(e -> new InvoiceDateMenu(getFrame()));
  }

  public static MenuBar getInstance() {
    if (instance == null) {
      instance = new MenuBar();
    }
    return instance;
  }

  private ApplicationFrame getFrame() {
    return Session.getActiveFrame();
  }
}
