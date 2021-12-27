package de.heinerion.invoice.view.swing.menu;

import de.heinerion.betriebe.data.Session;
import de.heinerion.betriebe.repositories.InvoiceRepository;
import de.heinerion.invoice.view.swing.FormatUtil;
import de.heinerion.invoice.view.swing.menu.tablemodels.invoices.InvoiceTable;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * @author heiner
 */
@RequiredArgsConstructor
class InvoicesMenuEntry extends MenuEntry {
  private static final String NAME = Menu.translate("invoices");
  private final InvoiceRepository invoiceRepository;

  private InvoiceTable model;

  private JScrollPane spInvoices;

  public static final int INDEX_NUMBER = 0;
  public static final int INDEX_RECEIVER = 1;
  public static final int INDEX_PRODUCT = 2;
  public static final int INDEX_DATE = 3;
  public static final int INDEX_SENDER = 4;
  public static final int INDEX_AMOUNT = 5;

  @Override
  protected void addWidgets(JDialog dialog) {
    dialog.setLayout(new BorderLayout());
    dialog.add(getBtnOk(), BorderLayout.PAGE_END);
    dialog.add(spInvoices, BorderLayout.CENTER);
  }

  @Override
  protected void createWidgets() {
    model = new InvoiceTable(invoiceRepository.findAllBySender(Session.getActiveCompany().orElse(null)));
    JTable tblInvoices = new JTable(model);
    tblInvoices.setAutoCreateRowSorter(true);
    tblInvoices.setRowSelectionAllowed(true);
    final TableColumnModel cols = tblInvoices.getColumnModel();
    setColumnWidths(cols);
    // order by number
    tblInvoices.getRowSorter().toggleSortOrder(INDEX_NUMBER);
    // start with highest / latest
    tblInvoices.getRowSorter().toggleSortOrder(INDEX_NUMBER);

    spInvoices = new JScrollPane(tblInvoices);

    spInvoices.setPreferredSize(getBusyFrame().getSize());
  }

  private void setColumnWidths(TableColumnModel cols) {
    cols.getColumn(INDEX_NUMBER).setMaxWidth(50);
    cols.getColumn(INDEX_RECEIVER).setPreferredWidth(150);
    cols.getColumn(INDEX_PRODUCT).setPreferredWidth(150);
    cols.getColumn(INDEX_DATE).setMinWidth(90);
    cols.getColumn(INDEX_DATE).setMaxWidth(90);
    cols.getColumn(INDEX_SENDER).setPreferredWidth(100);
    cols.getColumn(INDEX_AMOUNT).setMinWidth(80);
    cols.getColumn(INDEX_AMOUNT).setMaxWidth(80);
  }

  @Override
  protected void setTitle(JDialog dialog) {
    String revenues = FormatUtil.formatLocaleDecimal(model.calculateRevenues());
    String title = Menu.translate("archiveHeader", NAME, revenues, "€");
    dialog.setTitle(title);
  }

  @Override
  public String getLinkText() {
    return NAME;
  }

  @Override
  protected void setupInteractions(JDialog dialog) {
    getBtnOk().addActionListener(arg0 -> getCloser().windowClosing(null));
  }
}
