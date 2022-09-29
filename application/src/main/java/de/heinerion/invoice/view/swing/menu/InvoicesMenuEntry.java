package de.heinerion.invoice.view.swing.menu;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.data.Session;
import de.heinerion.invoice.models.Invoice;
import de.heinerion.invoice.print.PrintOperations;
import de.heinerion.invoice.repositories.invoice.InvoiceRepository;
import de.heinerion.invoice.view.swing.FormatUtil;
import de.heinerion.invoice.view.swing.menu.tablemodels.NiceTable;
import de.heinerion.invoice.view.swing.menu.tablemodels.invoices.InvoiceTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * @author heiner
 */
@Flogger
@RequiredArgsConstructor
class InvoicesMenuEntry extends MenuEntry {
  private static final String NAME = Menu.translate("invoices");
  private final InvoiceRepository invoiceRepository;
  private final PrintOperations printOperations;
  private final Session session;

  private JButton btnPrint;
  private InvoiceTable model;

  private NiceTable<Invoice> tblInvoices;
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
    dialog.add(getButtonPanel(), BorderLayout.PAGE_END);
    dialog.add(spInvoices, BorderLayout.CENTER);
  }

  private JPanel getButtonPanel() {
    JPanel container = new JPanel(new BorderLayout());
    container.add(btnPrint, BorderLayout.LINE_START);
    container.add(getBtnOk(), BorderLayout.LINE_END);
    return container;
  }

  @Override
  protected void createWidgets() {
    btnPrint = new JButton(Translator.translate("controls.print"));
    btnPrint.setEnabled(false);

    model = new InvoiceTable(invoiceRepository.findAllBySender(session.getActiveCompany().orElse(null)));
    tblInvoices = new NiceTable<>(model);
    setColumnWidths(tblInvoices.getColumnModel());
    tblInvoices.sortBy(INDEX_NUMBER, SortOrder.ASCENDING);
    tblInvoices.addSelectionListener(this::refreshPrintButtonState);

    spInvoices = new JScrollPane(tblInvoices.asJTable());
    spInvoices.setPreferredSize(getBusyFrame().getSize());
  }

  private void refreshPrintButtonState(ListSelectionModel lsm) {
    btnPrint.setEnabled(!lsm.isSelectionEmpty());
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
    getBtnOk().addActionListener(ignored -> getCloser().windowClosing(null));
    btnPrint.addActionListener(ignored -> printSelectedInvoice());
  }

  private void printSelectedInvoice() {
    tblInvoices.getSelectedRowObject().ifPresent(printOperations::createDocument);
  }
}
