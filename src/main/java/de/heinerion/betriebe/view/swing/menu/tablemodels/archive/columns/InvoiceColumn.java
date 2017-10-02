package de.heinerion.betriebe.view.swing.menu.tablemodels.archive.columns;

import de.heinerion.betriebe.view.swing.menu.tablemodels.archive.ArchivedInvoice;

public interface InvoiceColumn {
  Class<?> getColumnClass();

  String getName();

  Object getValue(ArchivedInvoice data);

  void setValue(ArchivedInvoice data, Object value);
}
