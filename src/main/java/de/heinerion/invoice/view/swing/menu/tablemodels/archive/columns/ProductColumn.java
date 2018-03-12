package de.heinerion.invoice.view.swing.menu.tablemodels.archive.columns;

import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.invoice.Translator;

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
