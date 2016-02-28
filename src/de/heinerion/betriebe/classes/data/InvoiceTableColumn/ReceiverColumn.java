package de.heinerion.betriebe.classes.data.InvoiceTableColumn;

import de.heinerion.betriebe.classes.data.RechnungData;
import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.models.Address;

public final class ReceiverColumn implements ColumnState {
  public ReceiverColumn() {
  }

  @Override
  public Class<?> getColumnClass() {
    return Address.class;
  }

  @Override
  public String getName() {
    return Constants.TABLE_RECEIVER;
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
