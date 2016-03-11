package de.heinerion.betriebe.classes.gui.table_models;

import de.heinerion.betriebe.models.Address;

import javax.swing.table.AbstractTableModel;
import java.util.List;

@SuppressWarnings("serial")
public final class AddressModel extends AbstractTableModel {
  enum ColumnId {
    RECIPIENT, COMPANY, DISTRICT, STREET, NUMBER, APARTMENT, POSTCODE, LOCATION
  }

  private final List<Address> addresses;

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
    String result;

    switch (columnId) {
      case APARTMENT:
        result = "Apartment";
        break;
      case COMPANY:
        result = "Firma";
        break;
      case DISTRICT:
        result = "Ortsteil";
        break;
      case LOCATION:
        result = "Ort";
        break;
      case NUMBER:
        result = "Hausnummer";
        break;
      case POSTCODE:
        result = "Postleitzahl";
        break;
      case RECIPIENT:
        result = "Name";
        break;
      case STREET:
        result = "Straße";
        break;
      default:
        result = columnId.toString();
        break;
    }

    return result;
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
    String result;

    switch (columnId) {
      case APARTMENT:
        result = address.getApartment();
        break;
      case COMPANY:
        result = address.getCompany();
        break;
      case DISTRICT:
        result = address.getDistrict();
        break;
      case LOCATION:
        result = address.getLocation();
        break;
      case NUMBER:
        result = address.getNumber();
        break;
      case POSTCODE:
        result = address.getPostalCode();
        break;
      case RECIPIENT:
        result = address.getRecipient();
        break;
      case STREET:
        result = address.getStreet();
        break;
      default:
        result = columnId.toString();
        break;
    }

    return result;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return true;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    this.setValueById(this.addresses.get(rowIndex),
        ColumnId.values()[columnIndex], (String) aValue);
    this.fireTableCellUpdated(rowIndex, columnIndex);
  }

  private void setValueById(Address address, ColumnId columnId, String value) {
    switch (columnId) {
      case APARTMENT:
        address.setApartment(value);
        break;
      case COMPANY:
        address.setCompany(value);
        break;
      case DISTRICT:
        address.setDistrict(value);
        break;
      case LOCATION:
        address.setLocation(value);
        break;
      case NUMBER:
        address.setNumber(value);
        break;
      case POSTCODE:
        address.setPostalCode(value);
        break;
      case RECIPIENT:
        address.setRecipient(value);
        break;
      case STREET:
        address.setStreet(value);
        break;
      default:
        break;
    }
  }
}
