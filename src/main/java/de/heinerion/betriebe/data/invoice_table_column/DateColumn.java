package de.heinerion.betriebe.data.invoice_table_column;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.data.RechnungData;

import java.time.LocalDate;

public final class DateColumn implements ColumnState {
  @Override
  public Class<?> getColumnClass() {
    return LocalDate.class;
  }

  @Override
  public String getName() {
    return Constants.TABLE_DATE;
  }

  @Override
  public Object getValue(RechnungData data) {
    return data.getDate();
  }

  @Override
  public void setValue(RechnungData data, Object value) {
    // Nicht setzbar
  }
}
