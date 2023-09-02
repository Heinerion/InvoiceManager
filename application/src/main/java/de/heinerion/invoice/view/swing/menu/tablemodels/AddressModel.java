package de.heinerion.invoice.view.swing.menu.tablemodels;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.models.Address;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public final class AddressModel extends AbstractTableModel {
  private enum ColumnId {NAME, BLOCK}

  private final transient List<Address> addresses;

  public AddressModel(List<Address> someAddresses) {
    this.addresses = someAddresses;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  @Override
  public int getColumnCount() {
    return ColumnId.values().length;
  }

  @Override
  public String getColumnName(int columnIndex) {
    return this.getColumnNameById(ColumnId.values()[columnIndex]);
  }

  private String getColumnNameById(ColumnId columnId) {
    return Translator.translate(switch (columnId) {
      case NAME -> "address.name";
      case BLOCK -> "address.block";
    });
  }

  @Override
  public int getRowCount() {
    return this.addresses.size();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return this.getValueById(this.addresses.get(rowIndex),
        ColumnId.values()[columnIndex]);
  }

  private String getValueById(Address address, ColumnId columnId) {
    return switch (columnId) {
      case NAME -> address.getName();
      case BLOCK -> String.join(" / ", address.getLines());
    };
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    // no saving means no reason to edit
    return false;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    this.setValueById(this.addresses.get(rowIndex),
        ColumnId.values()[columnIndex], (String) aValue);
    this.fireTableCellUpdated(rowIndex, columnIndex);
  }

  private void setValueById(Address address, ColumnId columnId, String value) {
    switch (columnId) {
      case NAME -> address.setName(value);
      case BLOCK -> address.setBlock(value);
    }
  }
}
