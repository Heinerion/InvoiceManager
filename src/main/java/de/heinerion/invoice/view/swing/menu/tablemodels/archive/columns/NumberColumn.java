package de.heinerion.invoice.view.swing.menu.tablemodels.archive.columns;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;

public final class NumberColumn implements InvoiceColumn {
  @Override
  public Class<?> getColumnClass() {
    return Integer.class;
  }

  @Override
  public String getName() {
    return Translator.translate("table.number");
  }

  @Override
  public Object getValue(ArchivedInvoice data) {
    return data.getInvoiceNumber();
  }
}
