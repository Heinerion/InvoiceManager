package de.heinerion.betriebe.gui.tablemodels.archive.columns;

import de.heinerion.betriebe.gui.tablemodels.archive.ArchivedInvoice;

public interface InvoiceColumn {
  Class<?> getColumnClass();

  String getName();

  Object getValue(ArchivedInvoice data);

  void setValue(ArchivedInvoice data, Object value);
}
