package de.heinerion.invoice.view.swing.menu.tablemodels;

import de.heinerion.betriebe.models.Address;
import de.heinerion.invoice.Translator;

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
    String result;

    switch (columnId) {
      case APARTMENT:
        result = Translator.translate("address.apartment");
        break;
      case COMPANY:
        result = Translator.translate("address.company");
        break;
      case DISTRICT:
        result = Translator.translate("address.district");
        break;
      case LOCATION:
        result = Translator.translate("address.location");
        break;
      case NUMBER:
        result = Translator.translate("address.number");
        break;
      case POSTCODE:
        result = Translator.translate("address.postcode");
        break;
      case RECIPIENT:
        result = Translator.translate("address.recipient");
        break;
      case STREET:
        result = Translator.translate("address.street");
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
        result = address.getApartment().orElse("");
        break;
      case COMPANY:
        result = address.getCompany().orElse("");
        break;
      case DISTRICT:
        result = address.getDistrict().orElse("");
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
