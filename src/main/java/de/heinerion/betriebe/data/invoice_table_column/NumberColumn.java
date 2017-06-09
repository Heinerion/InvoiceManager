package de.heinerion.betriebe.data.invoice_table_column;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.data.RechnungData;

public final class NumberColumn implements ColumnState {
  @Override
  public Class<?> getColumnClass() {
    return Integer.class;
  }

  @Override
  public String getName() {
    return Constants.TABLE_NUMBER;
  }

  @Override
  public Object getValue(RechnungData data) {
    return data.getInvoiceNumber();
  }

  @Override
  public void setValue(RechnungData data, Object value) {
    // Nicht setzbar
  }
}
