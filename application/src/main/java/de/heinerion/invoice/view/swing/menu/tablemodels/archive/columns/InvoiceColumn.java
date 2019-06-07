package de.heinerion.invoice.view.swing.menu.tablemodels.archive.columns;

import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;

public interface InvoiceColumn {
  Class<?> getColumnClass();

  String getName();

  Object getValue(ArchivedInvoice data);
}
