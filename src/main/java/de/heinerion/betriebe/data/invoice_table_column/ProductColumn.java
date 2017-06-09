package de.heinerion.betriebe.data.invoice_table_column;

import de.heinerion.betriebe.data.RechnungData;
import de.heinerion.betriebe.services.Translator;

public final class ProductColumn implements ColumnState {

  @Override
  public Class<?> getColumnClass() {
    return String.class;
  }

  @Override
  public String getName() {
    return Translator.translate("table.product");
  }

  @Override
  public Object getValue(RechnungData data) {
    return data.getItem();
  }

  @Override
  public void setValue(RechnungData data, Object value) {
    if (value instanceof String) {
      data.setItem((String) value);
    }
  }
}
