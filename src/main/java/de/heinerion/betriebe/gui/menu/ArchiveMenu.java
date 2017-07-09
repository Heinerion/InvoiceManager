package de.heinerion.betriebe.gui.menu;

import de.heinerion.betriebe.data.DataBase;
import de.heinerion.betriebe.gui.ApplicationFrame;
import de.heinerion.betriebe.gui.tablemodels.archive.ArchivedInvoiceTable;
import de.heinerion.betriebe.services.Translator;
import de.heinerion.betriebe.tools.FormatUtil;
import de.heinerion.exceptions.HeinerionException;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * @author heiner
 */
@SuppressWarnings("serial")
public final class ArchiveMenu extends AbstractMenu {
  static final String NAME = Translator.translate("menu.archive");

  private transient ArchivedInvoiceTable model;

  private JTable tblInvoices;
  private JScrollPane spInvoices;

  /**  */
  ArchiveMenu(final ApplicationFrame origin) {
    super(origin);
  }

  @Override
  protected void addWidgets() {
    dialog.setLayout(new BorderLayout());
    dialog.add(getBtnOk(), BorderLayout.PAGE_END);
    dialog.add(spInvoices, BorderLayout.CENTER);
  }

  @Override
  protected void createWidgets() {
    model = DataBase.getInvoices();
    tblInvoices = new JTable(model);
    tblInvoices.setAutoCreateRowSorter(true);
    tblInvoices.setRowSelectionAllowed(true);
    // Breite der Felder
    final TableColumnModel cols = tblInvoices.getColumnModel();
    cols.getColumn(ArchivedInvoiceTable.INDEX_NUMBER).setMaxWidth(50);
    cols.getColumn(ArchivedInvoiceTable.INDEX_RECEIVER).setPreferredWidth(150);
    cols.getColumn(ArchivedInvoiceTable.INDEX_PRODUCT).setPreferredWidth(150);
    cols.getColumn(ArchivedInvoiceTable.INDEX_DATE).setMinWidth(90);
    cols.getColumn(ArchivedInvoiceTable.INDEX_DATE).setMaxWidth(90);
    cols.getColumn(ArchivedInvoiceTable.INDEX_SENDER).setPreferredWidth(100);
    cols.getColumn(ArchivedInvoiceTable.INDEX_AMOUNT).setMinWidth(80);
    cols.getColumn(ArchivedInvoiceTable.INDEX_AMOUNT).setMaxWidth(80);
    // Nach Nummer sortieren
    tblInvoices.getRowSorter().toggleSortOrder(
        ArchivedInvoiceTable.INDEX_NUMBER);
    // Mit Höchster anfangen
    tblInvoices.getRowSorter().toggleSortOrder(
        ArchivedInvoiceTable.INDEX_NUMBER);

    spInvoices = new JScrollPane(tblInvoices);

    spInvoices.setPreferredSize(getOrigin().getSize());
  }

  @Override
  protected void setTitle() {
    dialog.setTitle(NAME + " - Gesamtsumme: "
        + FormatUtil.formatLocaleDecimal(model.calculateRevenues())
        + " €");
  }

  @Override
  protected void setupInteractions() {
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
        HeinerionException.handleException(getClass(), e);
      }
    }
  }
}
