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
    addresses = createItem("Adressen");
    invoices = createItem("Verwaltung");
    numbers = createItem("Nummern");
    date = createItem("Datum");
  }

  private JMenuItem createItem(String adressen) {
    return new JMenuItem(adressen);
  }

  private void addWidgets() {
    add(addresses);
    add(invoices);
    add(numbers);
    add(date);
  }

  private void setupInteractions() {
    addresses.addActionListener(e -> new AdressbuchMenu(getFrame()));
    invoices.addActionListener(e -> new VerwaltungMenu(getFrame()));
    numbers.addActionListener(e -> new RechnungsnummernMenu(getFrame()));
    date.addActionListener(e -> new RechnungsdatumMenu(getFrame()));
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
