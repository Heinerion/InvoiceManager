package de.heinerion.betriebe.data.invoice_table_column;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.data.RechnungData;

public final class AmountColumn implements ColumnState {
  @Override
  public Class<?> getColumnClass() {
    return Double.class;
  }

  @Override
  public String getName() {
    return Constants.TABLE_AMOUNT;
  }

  @Override
  public Object getValue(RechnungData data) {
    return data.getAmount();
  }

  @Override
  public void setValue(RechnungData data, Object value) {
    if (value instanceof Double) {
      data.setAmount((double) value);
    }
  }
}
