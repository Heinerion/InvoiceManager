package de.heinerion.betriebe.view.menu.tablemodels.archive.columns;

import de.heinerion.betriebe.view.menu.tablemodels.archive.ArchivedInvoice;
import de.heinerion.betriebe.services.Translator;

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

  @Override
  public void setValue(ArchivedInvoice data, Object value) {
    // Nicht setzbar
  }
}
