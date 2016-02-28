package de.heinerion.betriebe.classes.data.InvoiceTableColumn;

import de.heinerion.betriebe.classes.data.RechnungData;
import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.models.Company;

public final class SenderColumn implements ColumnState {
  public SenderColumn() {
  }

  @Override
  public Class<?> getColumnClass() {
    return Company.class;
  }

  @Override
  public String getName() {
    return Constants.TABLE_SENDER;
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
