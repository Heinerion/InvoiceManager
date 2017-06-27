package de.heinerion.betriebe.gui.tablemodels.archive.columns;

import de.heinerion.betriebe.gui.tablemodels.archive.RechnungData;

public interface InvoiceColumn {
  Class<?> getColumnClass();

  String getName();

  Object getValue(RechnungData data);

  void setValue(RechnungData data, Object value);
}
