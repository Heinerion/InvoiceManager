package de.heinerion.invoice.view.swing.menu.tablemodels.archive.columns;

import de.heinerion.betriebe.models.Company;
import de.heinerion.invoice.Translator;
import de.heinerion.invoice.view.swing.menu.tablemodels.archive.ArchivedInvoice;

public final class SenderColumn implements InvoiceColumn {
  @Override
  public Class<?> getColumnClass() {
    return Company.class;
  }

  @Override
  public String getName() {
    return Translator.translate("table.sender");
  }

  @Override
  public Object getValue(ArchivedInvoice data) {
    return data.getCompany();
  }

  @Override
  public void setValue(ArchivedInvoice data, Object value) {
    if (value instanceof Company) {
      data.setCompany((Company) value);
    }
  }
}
