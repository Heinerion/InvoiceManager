package de.heinerion.invoice.view.swing.tab;

import de.heinerion.invoice.*;
import de.heinerion.invoice.models.*;
import de.heinerion.util.Strings;
import lombok.extern.flogger.Flogger;

import javax.swing.table.AbstractTableModel;
import java.util.*;

@Flogger
class InvoiceTableModel extends AbstractTableModel {

  private static final int NAME = 0;
  private static final int UNIT = 1;
  private static final int PPU = 2;
  private static final int COUNT = 3;
  private static final int COLS = 4;

  private final transient List<Item> contents;

  InvoiceTableModel(List<Item> items) {
    contents = items;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return true;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return columnIndex == COUNT || columnIndex == PPU
        ? Double.class
        : String.class;
  }

  @Override
  public String getColumnName(int columnIndex) {
    return switch (columnIndex) {
      case NAME -> Translator.translate("item.name");
      case UNIT -> Translator.translate("item.unit");
      case PPU -> Translator.translate("item.ppu");
      case COUNT -> Translator.translate("item.count");
      default -> columnIndex + "";
    };
  }

  @Override
  public int getRowCount() {
    // Always keep two more lines in view
    return contents.size() + 2;
  }

  @Override
  public int getColumnCount() {
    return COLS;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return isValidIndex(rowIndex)
        ? getPropertyValue(rowIndex, columnIndex)
        : null;
  }

  public void clear() {
    contents.clear();
  }

  public void addRow(Item item) {
    contents.add(item.copy());
  }

  private Object getPropertyValue(int rowIndex, int columnIndex) {
    Item item = contents.get(rowIndex);
    return switch (columnIndex) {
      case NAME -> item.getName();
      case UNIT -> item.getUnit();
      case PPU -> item.getPricePerUnit();
      case COUNT -> item.getQuantity();
      default -> null;
    };
  }

  private boolean isValidIndex(int rowIndex) {
    return rowIndex < contents.size();
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    Item item = getItem(rowIndex);

    switch (columnIndex) {
      case NAME -> item.setName((String) aValue);
      case UNIT -> item.setUnit((String) aValue);
      case PPU -> item.setPricePerUnit(parseDouble(aValue));
      case COUNT -> item.setQuantity(parseDouble(aValue));
      default -> {
        // noop
      }
    }

    fireTableCellUpdated(rowIndex, columnIndex);
  }

  private Item getItem(int rowIndex) {
    if (isValidIndex(rowIndex)) {
      return contents.get(rowIndex);
    }

    Item item = Item.empty(rowIndex);
    contents.add(item);
    return item;
  }

  private double parseDouble(Object aValue) {
    if (aValue instanceof String string) {
      return NumberParser.parseDouble(string).orElse(0);
    }

    if (aValue instanceof Double dbl) {
      return dbl;
    }

    return 0;
  }

  InvoiceTemplate createTemplate(Company company) {
    InvoiceTemplate result = new InvoiceTemplate();
    result.setCompany(company);
    result.setName(contents.get(0).getName());
    result.setItems(getItems());
    return result;
  }

  public Set<Item> getItems() {
    List<Item> filtered = contents.stream()
        .filter(i -> Strings.isNotBlank(i.getName()))
        .toList();
    filtered.stream()
        .map(item -> item.setId(null))
        .forEach(c -> c.setPosition(filtered.indexOf(c)));
    log.atInfo().log("gefiltert: %s", filtered);
    return new HashSet<>(filtered);
  }
}