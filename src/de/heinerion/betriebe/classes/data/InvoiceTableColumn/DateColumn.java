package de.heinerion.betriebe.classes.data.InvoiceTableColumn;

import java.time.LocalDate;

import de.heinerion.betriebe.classes.data.RechnungData;
import de.heinerion.betriebe.data.Constants;

public final class DateColumn implements ColumnState {
  public DateColumn() {
  }

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
