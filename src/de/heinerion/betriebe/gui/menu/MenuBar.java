package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.classes.gui.RechnungFrame;
import de.heinerion.betriebe.data.Session;

import javax.swing.*;

@SuppressWarnings("serial")
public final class MenuBar extends JMenuBar {
  private static MenuBar instance;

  private MenuBar() {
    final JMenuItem addresses = new JMenuItem("Adressen");
    addresses.addActionListener(e -> new AdressbuchMenu(getFrame()));

    final JMenuItem invoices = new JMenuItem("Verwaltung");
    invoices.addActionListener(e -> new VerwaltungMenu(getFrame()));

    final JMenuItem numbers = new JMenuItem("Nummern");
    numbers.addActionListener(e -> new RechnungsnummernMenu(getFrame()));

    final JMenuItem date = new JMenuItem("Datum");
    date.addActionListener(e -> new RechnungsdatumMenu(getFrame()));

    add(addresses);
    add(invoices);
    add(numbers);
    add(date);
  }

  public static MenuBar getInstance() {
    if (instance == null) {
      instance = new MenuBar();
    }
    return instance;
  }

  private RechnungFrame getFrame() {
    return Session.getActiveFrame();
  }
}
