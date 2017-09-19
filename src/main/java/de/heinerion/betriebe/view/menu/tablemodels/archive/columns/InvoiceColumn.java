package de.heinerion.betriebe.view.menu.tablemodels.archive.columns;

import de.heinerion.betriebe.view.menu.tablemodels.archive.ArchivedInvoice;

public interface InvoiceColumn {
  Class<?> getColumnClass();

  String getName();

  Object getValue(ArchivedInvoice data);

  void setValue(ArchivedInvoice data, Object value);
}
