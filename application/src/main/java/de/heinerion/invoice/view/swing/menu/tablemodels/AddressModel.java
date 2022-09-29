package de.heinerion.invoice.view.swing.menu.tablemodels;

import de.heinerion.invoice.Translator;
import de.heinerion.invoice.models.Address;

import javax.swing.table.AbstractTableModel;
import java.util.List;

@SuppressWarnings("serial")
public final class AddressModel extends AbstractTableModel {
  enum ColumnId {
    RECIPIENT, COMPANY, DISTRICT, STREET, NUMBER, APARTMENT, POSTCODE, LOCATION
  }

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
      case APARTMENT -> "address.apartment";
      case COMPANY -> "address.company";
      case DISTRICT -> "address.district";
      case LOCATION -> "address.location";
      case NUMBER -> "address.number";
      case POSTCODE -> "address.postcode";
      case RECIPIENT -> "address.recipient";
      case STREET -> "address.street";
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
      case APARTMENT -> address.getApartment().orElse("");
      case COMPANY -> address.getCompany().orElse("");
      case DISTRICT -> address.getDistrict().orElse("");
      case LOCATION -> address.getLocation();
      case NUMBER -> address.getNumber();
      case POSTCODE -> address.getPostalCode();
      case RECIPIENT -> address.getRecipient();
      case STREET -> address.getStreet();
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
      case APARTMENT -> address.setApartment(value);
      case COMPANY -> address.setCompany(value);
      case DISTRICT -> address.setDistrict(value);
      case LOCATION -> address.setLocation(value);
      case NUMBER -> address.setNumber(value);
      case POSTCODE -> address.setPostalCode(value);
      case RECIPIENT -> address.setRecipient(value);
      case STREET -> address.setStreet(value);
    }
  }
}
