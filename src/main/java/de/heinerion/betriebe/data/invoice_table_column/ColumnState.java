package de.heinerion.betriebe.data.invoice_table_column;

import de.heinerion.betriebe.data.RechnungData;

public interface ColumnState {
  Class<?> getColumnClass();

  String getName();

  Object getValue(RechnungData data);

  void setValue(RechnungData data, Object value);
}
