package de.heinerion.betriebe.gui.tablemodels.archive.columns;

import de.heinerion.betriebe.gui.tablemodels.archive.RechnungData;
import de.heinerion.betriebe.models.Address;
import de.heinerion.betriebe.services.Translator;

public final class ReceiverColumn implements InvoiceColumn {
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
