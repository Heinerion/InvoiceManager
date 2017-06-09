package de.heinerion.betriebe.data.invoice_table_column;

import de.heinerion.betriebe.data.RechnungData;
import de.heinerion.betriebe.models.Company;
import de.heinerion.betriebe.services.Translator;

public final class SenderColumn implements ColumnState {
  @Override
  public Class<?> getColumnClass() {
    return Company.class;
  }

  @Override
  public String getName() {
    return Translator.translate("table.sender");
  }

  @Override
  public Object getValue(RechnungData data) {
    return data.getCompany();
  }

  @Override
  public void setValue(RechnungData data, Object value) {
    if (value instanceof Company) {
      data.setCompany((Company) value);
    }
  }
}
