package de.heinerion.betriebe.gui.tablemodels.archive.columns;

import de.heinerion.betriebe.gui.tablemodels.archive.RechnungData;
import de.heinerion.betriebe.services.Translator;

import java.time.LocalDate;

public final class DateColumn implements InvoiceColumn {
  @Override
  public Class<?> getColumnClass() {
    return LocalDate.class;
  }

  @Override
  public String getName() {
    return Translator.translate("table.date");
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
