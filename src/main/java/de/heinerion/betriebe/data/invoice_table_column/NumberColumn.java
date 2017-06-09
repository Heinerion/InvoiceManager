package de.heinerion.betriebe.data.invoice_table_column;

import de.heinerion.betriebe.data.RechnungData;
import de.heinerion.betriebe.services.Translator;

public final class NumberColumn implements ColumnState {
  @Override
  public Class<?> getColumnClass() {
    return Integer.class;
  }

  @Override
  public String getName() {
    return Translator.translate("table.number");
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
