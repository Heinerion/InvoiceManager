package de.heinerion.invoice.view.swing.menu.tablemodels.archive.columns;

import de.heinerion.betriebe.models.Address;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;

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
  public Object getValue(ArchivedInvoice data) {
    return data.getRecipient();
  }

  @Override
  public void setValue(ArchivedInvoice data, Object value) {
    // Nicht setzbar
  }
}
