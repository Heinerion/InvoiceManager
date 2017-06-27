package de.heinerion.betriebe.gui.tablemodels.archive.columns;

import de.heinerion.betriebe.gui.tablemodels.archive.RechnungData;
import de.heinerion.betriebe.services.Translator;

public final class ProductColumn implements InvoiceColumn {

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
