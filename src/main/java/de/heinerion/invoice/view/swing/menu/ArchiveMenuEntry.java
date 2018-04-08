package de.heinerion.invoice.view.swing.menu;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.invoice.view.swing.FormatUtil;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoiceTable;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
class ArchiveMenuEntry extends AbstractMenuEntry {
  private static final String NAME = Menu.translate("archive");
  private DataBase dataBase = DataBase.getInstance();

  private ArchivedInvoiceTable model;

  private JTable tblInvoices;
  private JScrollPane spInvoices;

  @Override
  protected void addWidgets(JDialog dialog) {
    dialog.setLayout(new BorderLayout());
    dialog.add(getBtnOk(), BorderLayout.PAGE_END);
    dialog.add(spInvoices, BorderLayout.CENTER);
  }

  @Override
  protected void createWidgets() {
    model = dataBase.getInvoices();
    tblInvoices = new JTable(model);
    tblInvoices.setAutoCreateRowSorter(true);
    tblInvoices.setRowSelectionAllowed(true);
    final TableColumnModel cols = tblInvoices.getColumnModel();
    setColumnWidths(cols);
    // order by number
    tblInvoices.getRowSorter().toggleSortOrder(
        ArchivedInvoiceTable.INDEX_NUMBER);
    // start with highest / latest
    tblInvoices.getRowSorter().toggleSortOrder(
        ArchivedInvoiceTable.INDEX_NUMBER);

    spInvoices = new JScrollPane(tblInvoices);

    spInvoices.setPreferredSize(getBusyFrame().getSize());
  }

  private void setColumnWidths(TableColumnModel cols) {
    cols.getColumn(ArchivedInvoiceTable.INDEX_NUMBER).setMaxWidth(50);
    cols.getColumn(ArchivedInvoiceTable.INDEX_RECEIVER).setPreferredWidth(150);
    cols.getColumn(ArchivedInvoiceTable.INDEX_PRODUCT).setPreferredWidth(150);
    cols.getColumn(ArchivedInvoiceTable.INDEX_DATE).setMinWidth(90);
    cols.getColumn(ArchivedInvoiceTable.INDEX_DATE).setMaxWidth(90);
    cols.getColumn(ArchivedInvoiceTable.INDEX_SENDER).setPreferredWidth(100);
    cols.getColumn(ArchivedInvoiceTable.INDEX_AMOUNT).setMinWidth(80);
    cols.getColumn(ArchivedInvoiceTable.INDEX_AMOUNT).setMaxWidth(80);
  }

  @Override
  protected void setTitle(JDialog dialog) {
    String revenues = FormatUtil.formatLocaleDecimal(model.calculateRevenues());
    String title = format("archiveHeader", NAME, revenues, "€");
    dialog.setTitle(title);
  }

  private static String format(String key, Object... replacements) {
    return MessageFormat.format(Menu.translate(key), replacements);
  }

  @Override
  public String getLinkText() {
    return NAME;
  }

  @Override
  protected void setupInteractions(JDialog dialog) {
    tblInvoices.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        handleDocumentClick(e);
      }
    });
    getBtnOk().addActionListener(arg0 -> getCloser().windowClosing(null));
  }

  private void handleDocumentClick(MouseEvent event) {
    if (event.getClickCount() == 2) {
      final int selectedRow = tblInvoices.getSelectedRow();
      final int rowIndex = tblInvoices.convertRowIndexToModel(selectedRow);
      final File pdf = model.getPdfAt(rowIndex);
      try {
        Desktop.getDesktop().open(pdf);
      } catch (final IOException e) {
        throw new GuiMenuException(e);
      }
    }
  }
}
