package de.heinerion.betriebe.data.invoice_table_column;

import de.heinerion.betriebe.data.RechnungData;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.services.Translator;

public final class ReceiverColumn implements ColumnState {
  @Override
  public Class<?> getColumnClass() {
    return Address.class;
  }

  @Override
  public String getName() {
    return Translator.translate("table.receiver");
  }

  @Override
  public Object getValue(RechnungData data) {
    return data.getRecipient();
  }

  @Override
  public void setValue(RechnungData data, Object value) {
    // Nicht setzbar
  }
}
