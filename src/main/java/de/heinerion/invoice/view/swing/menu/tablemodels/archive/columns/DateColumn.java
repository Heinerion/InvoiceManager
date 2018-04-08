package de.heinerion.invoice.view.swing.menu.tablemodels.archive.columns;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;

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
  public Object getValue(ArchivedInvoice data) {
    return data.getDate();
  }

  @Override
  public void setValue(ArchivedInvoice data, Object value) {
    // Nicht setzbar
  }
}
