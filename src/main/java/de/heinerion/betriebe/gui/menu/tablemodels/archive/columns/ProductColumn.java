package de.heinerion.betriebe.gui.menu.tablemodels.archive.columns;

import de.heinerion.betriebe.gui.menu.tablemodels.archive.ArchivedInvoice;
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
  public Object getValue(ArchivedInvoice data) {
    return data.getItem();
  }

  @Override
  public void setValue(ArchivedInvoice data, Object value) {
    if (value instanceof String) {
      data.setItem((String) value);
    }
  }
}
