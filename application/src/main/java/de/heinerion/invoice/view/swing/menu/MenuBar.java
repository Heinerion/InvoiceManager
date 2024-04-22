package de.heinerion.invoice.view.swing.menu;

import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.print.PrintOperations;
import de.heinerion.invoice.repositories.*;
import de.heinerion.invoice.util.PathUtilNG;
import de.heinerion.invoice.view.swing.home.ComponentPainter;
import de.heinerion.invoice.view.swing.laf.LookAndFeelUtil;
import de.heinerion.invoice.view.swing.menu.info.InfoMenuEntry;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

class MenuBar extends JMenuBar {
  private final JFrame origin;
  private final transient Session session;

  private transient List<JMenuItem> menuItems;
  private final transient PathUtilNG pathUtil;
  private final transient AddressRepository addressRepository;
  private final transient InvoiceRepository invoiceRepository;
  private final transient LetterRepository letterRepository;
  private final transient CompanyRepository companyRepository;
  private final transient PrintOperations printOperations;
  private final LookAndFeelUtil lookAndFeelUtil;

  MenuBar(JFrame origin, Session session, PathUtilNG pathUtil, AddressRepository addressRepository, InvoiceRepository invoiceRepository, LetterRepository letterRepository, CompanyRepository companyRepository, PrintOperations printOperations, LookAndFeelUtil lookAndFeelUtil) {
    this.origin = origin;
    this.session = session;
    this.pathUtil = pathUtil;
    this.addressRepository = addressRepository;
    this.invoiceRepository = invoiceRepository;
    this.letterRepository = letterRepository;
    this.companyRepository = companyRepository;
    this.printOperations = printOperations;
    this.lookAndFeelUtil = lookAndFeelUtil;
    this.setOpaque(false);
    this.setBorderPainted(false);
    createWidgets();
    addWidgets();
  }

  private void createWidgets() {
    menuItems = Arrays.asList(
        createItem(new AddressBookMenuEntry(addressRepository, session)),
        createItem(new InvoicesMenuEntry(invoiceRepository, printOperations, session)),
        createItem(new LettersMenuEntry(letterRepository, printOperations, session)),
        createItem(new InvoiceNumbersMenuEntry(session, companyRepository, lookAndFeelUtil)),
        createItem(new InvoiceDateMenuEntry(session, lookAndFeelUtil)),
        createItem(new InfoMenuEntry(pathUtil, companyRepository, session))
    );
  }

  private JMenuItem createItem(MenuEntry menuEntry) {
    menuEntry.setBusyFrame(origin);
    JMenuItem item = new JMenuItem(menuEntry.getLinkText());
    item.setOpaque(false);
    item.addActionListener(e -> menuEntry.showDialog());
    return item;
  }

  private void addWidgets() {
    menuItems.forEach(this::add);
  }

  @Override
  protected void paintComponent(Graphics g) {
    ComponentPainter.paintComponent(
        (Graphics2D) g,
        this,
        ComponentPainter.Details
            .builder()
            .decorWidth(10)
            .build());
  }
}
