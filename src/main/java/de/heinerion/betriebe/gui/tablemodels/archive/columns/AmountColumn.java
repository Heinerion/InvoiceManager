package de.heinerion.betriebe.gui.tablemodels.archive.columns;

import de.heinerion.betriebe.gui.tablemodels.archive.ArchivedInvoice;
import de.heinerion.betriebe.services.Translator;

public final class AmountColumn implements InvoiceColumn {
  @Override
  public Class<?> getColumnClass() {
    return Double.class;
  }

  @Override
  public String getName() {
    return Translator.translate("table.amount");
  }

  @Override
  public Object getValue(ArchivedInvoice data) {
    return data.getAmount();
  }

  @Override
  public void setValue(ArchivedInvoice data, Object value) {
    if (value instanceof Double) {
      data.setAmount((double) value);
    }
  }
}
