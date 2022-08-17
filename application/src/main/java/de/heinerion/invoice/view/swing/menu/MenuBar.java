package de.heinerion.invoice.view.swing.menu;

import de.heinerion.betriebe.repositories.AddressRepository;
import de.heinerion.betriebe.repositories.CompanyRepository;
import de.heinerion.betriebe.repositories.InvoiceRepository;
import de.heinerion.betriebe.util.PathUtilNG;
import de.heinerion.invoice.view.swing.home.ComponentPainter;
import de.heinerion.invoice.view.swing.menu.info.InfoMenuEntry;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

class MenuBar extends JMenuBar {
  private final JFrame origin;

  private transient List<JMenuItem> menuItems;
  private final transient PathUtilNG pathUtil;
  private final transient AddressRepository addressRepository;
  private final transient InvoiceRepository invoiceRepository;
  private final transient CompanyRepository companyRepository;

  MenuBar(JFrame origin, PathUtilNG pathUtil, AddressRepository addressRepository, InvoiceRepository invoiceRepository, CompanyRepository companyRepository) {
    this.origin = origin;
    this.pathUtil = pathUtil;
    this.addressRepository = addressRepository;
    this.invoiceRepository = invoiceRepository;
    this.companyRepository = companyRepository;
    this.setOpaque(false);
    this.setBorderPainted(false);
    createWidgets();
    addWidgets();
  }

  private void createWidgets() {
    menuItems = Arrays.asList(
        createItem(new AddressBookMenuEntry(addressRepository)),
        createItem(new InvoicesMenuEntry(invoiceRepository)),
        createItem(new InvoiceNumbersMenuEntry(companyRepository)),
        createItem(new InvoiceDateMenuEntry()),
        createItem(new InfoMenuEntry(pathUtil))
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
