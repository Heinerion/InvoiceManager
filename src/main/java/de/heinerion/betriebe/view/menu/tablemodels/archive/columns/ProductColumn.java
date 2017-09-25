package de.heinerion.betriebe.view.menu.tablemodels.archive.columns;

import de.heinerion.betriebe.view.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.util.Translator;

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
